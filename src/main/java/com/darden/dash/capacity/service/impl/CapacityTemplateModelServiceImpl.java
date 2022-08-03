package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.mapper.CapacityModelMapper;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.ConceptForCache;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacityModelAndLocationRepository;
import com.darden.dash.capacity.repository.CapacityModelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.client.service.ConceptClient;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity Model or any business logic related to Capacity Model.
 *
 */
@Service
public class CapacityTemplateModelServiceImpl implements CapacityTemplateModelService {

	private CapacityModelRepository capacityModelRepository;

	private LocationClient locationClient;

	private CapacityModelMapper capacityModelMapper = Mappers.getMapper(CapacityModelMapper.class);

	private final JwtUtils jwtUtils;

	private CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo;

	private CapacityTemplateRepo capacityTemplateRepo;

	private CapacityModelAndLocationRepository capacityModelAndLocationRepo;
	
	private ConceptClient conceptClient;
	/**
	 * Autowiring required properties
	 * 
	 * @param capacityModelRepository
	 * @param locationClient
	 * @param jwtUtils
	 * @param capacityModelAndLocationRepo
	 * @param capacityTemplateRepo
	 * @param capacityModelAndCapacityTemplateRepo
	 * @param conceptClient
	 */
	@Autowired
	public CapacityTemplateModelServiceImpl(CapacityModelRepository capacityModelRepository,
			LocationClient locationClient, JwtUtils jwtUtils,
			CapacityModelAndLocationRepository capacityModelAndLocationRepo, CapacityTemplateRepo capacityTemplateRepo,
			CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo,
			ConceptClient conceptClient) {
		this.jwtUtils = jwtUtils;
		this.capacityModelRepository = capacityModelRepository;
		this.locationClient = locationClient;
		this.capacityModelAndCapacityTemplateRepo = capacityModelAndCapacityTemplateRepo;
		this.capacityModelAndLocationRepo = capacityModelAndLocationRepo;
		this.capacityTemplateRepo = capacityTemplateRepo;
		this.conceptClient = conceptClient;
	}

	/**
	 * This service method is written for the purpose of retrieving the list of
	 * capacity model based on concept Id along with the valye of capacity template
	 * and restaurant assigned to the model.
	 * 
	 * @return List<CapacityModel> list of model class containing the value of
	 *         capacity models.
	 */
	@Override
	@Cacheable(value = CapacityConstants.CAPACITY_MODEL_CACHE)
	public List<CapacityModel> getAllCapacityModels() {
		List<CapacityModelEntity> modelEntityList = capacityModelRepository
				.findByConceptId(new BigInteger(RequestContext.getConcept()));
		List<Locations> restaurantList = locationClient.getAllRestaurants();
		List<CapacityModel> modelResponseList = new ArrayList<>();
		modelEntityList.stream().forEach(mel -> {
			CapacityModel model = capacityModelMapper.mapToCapacityModel(mel, restaurantList);
			modelResponseList.add(model);
		});
		return modelResponseList;
	}

	/**
	 * Method is used for CREATE operation.First it stores the capacity model name
	 * in the CapacityModelEntity based on the value of CapacityModelId ,all the
	 * details are mapped to capacitModel entity then assigned restaurants are
	 * mapped based on the capacityandlocation Id and then assigned template are
	 * mapped based on the capacity templates assigned to the capacity model all
	 * deatils get saved in the database and capacity model is craeted.
	 * 
	 * @param createCapacityModel request class containing detail of Capacity
	 *                            Template to be created.
	 * 
	 * @param accessToken         Token used to authenticate the user and extract
	 *                            the userDetails for this API
	 * 
	 * @return capacityTemplateModel response containing value of Capacity Template
	 *         Model created.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */

	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_MODEL_CACHE, allEntries = true) })
	public CapacityTemplateModel createCapacityModel(CapacityModelRequest capacityModelRequest, String accessToken) {
		CapacityTemplateModel capacityTemplateModel = new CapacityTemplateModel();
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityModelEntity capacityModelEntity = capacityModelMapper.mapToCapacityModelEntity(capacityModelRequest, createdBy, dateTime);
		List<BigInteger> templateIds = extractingAllTemplateIdFromRequest(capacityModelRequest);
		CapacityModelEntity capacityModelEntityRes = capacityModelRepository.save(capacityModelEntity);
		BigInteger capacityModelId = capacityModelEntityRes.getCapacityModelId();
		mapCapacityModelAndTemplates(createdBy, dateTime, capacityModelEntityRes, templateIds);
		mapCapacityModelAndLocations(capacityModelRequest, createdBy, dateTime, capacityModelEntityRes);
		Optional<CapacityModelEntity> capacityModelEntityResponse = capacityModelRepository.findById(capacityModelId);
		capacityModelEntityResponse
				.ifPresent(responseEntity -> mapCapacityModelResponse(capacityTemplateModel, responseEntity));
		return capacityTemplateModel;
	}

	/**
	 * This method is to retrieve data from capacity Model entity and mapping it to
	 * the capacity template model and retrieving the data from capacity model and
	 * capacity template entity also retrieving all the template assigned based on
	 * capacity template Id from capacity model and capacity template entity and
	 * mapping it to the capacity template model
	 * 
	 * @param capacityTemplateModel response containing value of Capacity Template
	 *                              Model created.
	 * @param responseEntity        Response class with information of the Object
	 *                              containing created/modified Capacity Model
	 *                              detail
	 */
	private void mapCapacityModelResponse(CapacityTemplateModel capacityTemplateModel,
			CapacityModelEntity responseEntity) {
		capacityModelMapper.mapToCapacityTemplateModel(capacityTemplateModel, responseEntity);
		List<CapacityModelAndCapacityTemplateEntity> modelTemplatesList = responseEntity
				.getCapacityModelAndCapacityTemplates();
		if (CollectionUtils.isNotEmpty(modelTemplatesList)) {
			List<TemplatesAssigned> templatesAssignedList = new ArrayList<>();
			modelTemplatesList.stream().filter(Objects::nonNull).forEach(templatesAssignedEntity -> {
				Optional<CapacityTemplateEntity> capacityTemplate = capacityTemplateRepo
						.findById(templatesAssignedEntity.getId().getCapacityTemplateId());
				capacityTemplate.ifPresent(t -> {
					TemplatesAssigned templatesAssigned = new TemplatesAssigned();
					templatesAssigned.setTemplateId(String.valueOf(t.getCapacityTemplateId()));
					templatesAssigned.setTemplateName(t.getCapacityTemplateNm());
					templatesAssignedList.add(templatesAssigned);
				});

			});
			capacityTemplateModel.setTemplatesAssigned(templatesAssignedList);
		}
		List<CapacityModelAndLocationEntity> modelLocationsList = responseEntity.getCapacityModelAndLocations();
		if (CollectionUtils.isNotEmpty(modelLocationsList)) {
			List<RestaurantsAssigned> restaurantsAssignedList = capacityModelMapper.mapToRestaurantAssignedList(modelLocationsList);
			capacityTemplateModel.setRestaurantsAssigned(restaurantsAssignedList);
		}
	}

	/**
	 * This method is to map the restaurants assigned request data to
	 * capacityModelAndLocation entity with respective to the model id and saving
	 * into the CapacityModelAndLocation table
	 * 
	 * 
	 * @param capacityModelRequest Request class with information to create Model
	 *                             Template.
	 * 
	 * @param createdBy            information of user operating on the update
	 *                             action.
	 * 
	 * @param dateTime             Instant value containing the value of dateTime.
	 * 
	 * @param capacityModelEntity  entity class with information of capacity model
	 *                             template
	 */
	private void mapCapacityModelAndLocations(CapacityModelRequest capacityModelRequest, String createdBy,
			Instant dateTime, CapacityModelEntity capacityModelEntity) {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		List<CapacityModelAndLocationEntity> capacityModelAndLocationEntites = new ArrayList<>();
		List<Locations> restaurantInDB = locationClient.getAllRestaurants();
		capacityModelRequest.getRestaurantsAssigned().stream().filter(Objects::nonNull)
				.filter(t -> StringUtils.isNotBlank(t.getLocationId())).forEach(restaurantsAssigned -> {
					if(isLocationIdValid(restaurantsAssigned.getLocationId(), restaurantInDB)) {
						applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), CapacityConstants.LOCATION_ID);
						applicationErrors.raiseExceptionIfHasErrors();
					}
					CapacityModelAndLocationEntity capacityModelAndLocationEntity = capacityModelMapper.mapToCapacityModelAndLocationEntity(
							createdBy, dateTime, capacityModelEntity, restaurantsAssigned);
					capacityModelAndLocationEntites.add(capacityModelAndLocationEntity);
				});
		if (CollectionUtils.isNotEmpty(capacityModelAndLocationEntites)) {
			capacityModelEntity.setCapacityModelAndLocations(capacityModelAndLocationEntites);
			capacityModelAndLocationRepo.saveAll(capacityModelAndLocationEntites);
		}
	}

	/**
	 * This method is to map the template assigned request data to
	 * capacityModelAndTemplate entity with respective to the model id and saving
	 * into the capacityModelAndTemplate table.Here retrieving template data based
	 * on the template Id mapping the respective fields and saving
	 * capacityModelAndCapacityTemplate table
	 * 
	 * @param createdBy           information of user operating on the update
	 *                            action.
	 * @param dateTime            Instant value containing the value of dateTime.
	 * @param capacityModelEntity entity class with information of capacity model
	 *                            template
	 * @param templateIds         To get all template Ids from capacity template
	 *                            table
	 */
	private void mapCapacityModelAndTemplates(String createdBy, Instant dateTime,
			CapacityModelEntity capacityModelEntity, List<BigInteger> templateIds) {
		List<CapacityTemplateEntity> capacityTemplateEntites = capacityTemplateRepo.findAllById(templateIds);
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplateEntites = capacityModelMapper.mapToCapacityModelAndCapacityTemplateEntityList(
				createdBy, dateTime, capacityModelEntity, capacityTemplateEntites);
		if (CollectionUtils.isNotEmpty(capacityModelAndCapacityTemplateEntites)) {
			capacityModelEntity.setCapacityModelAndCapacityTemplates(capacityModelAndCapacityTemplateEntites);
			capacityModelAndCapacityTemplateRepo.saveAll(capacityModelAndCapacityTemplateEntites);
		}
	}

	/**
	 * This method is to check whether the same capacity model name template is
	 * already present or not if it is present it will throw error if not the
	 * template gets created.
	 * 
	 * @param capacityModelNm capacityModel name present in the capacity model
	 *                        table.
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateModelTemplateNm(String capacityModelNm) {
		List<CapacityModelEntity> capacityModelEntity = capacityModelRepository.findByCapacityModelNm(capacityModelNm);
		return CollectionUtils.isNotEmpty(capacityModelEntity);

	}

	/**
	 * This method is to validate days if same days are given for updating then it
	 * will throw error it will first check the expiry and effective dates if it is
	 * present it will throw error otherwise it will add the template in capacity
	 * model. If dates are selected validation is made for dates if value is already
	 * existing in other Capacity Template under the same Capacity Model.If present
	 * will application Error will be raised.
	 * 
	 * @param capacityTemplateEntityRequest Request class containing the detail of
	 *                                      Capacity Template Model to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateCapacityModelTemplateBusinessDates(CapacityTemplateEntity capacityTemplateEntityRequest, List<BigInteger> otherTemplateId) {
		List<CapacityTemplateEntity> list = capacityTemplateRepo.findAllById(otherTemplateId);
		String templateTypeNm = capacityTemplateEntityRequest.getCapacityTemplateType().getCapacityTemplateTypeNm();
		if (CapacityConstants.DAYS.equalsIgnoreCase(templateTypeNm)) {
			return validateAssignedTemplateDays(capacityTemplateEntityRequest, list);
		} else if (CapacityConstants.DATES.equalsIgnoreCase(templateTypeNm)) {
			return validateAssignedTemplateDates(capacityTemplateEntityRequest, list);
		}
		return false;
	}

	/**
	 * This method is to validate assigned template dates between the assigned 
	 * template to capacity model.
	 * 
	 * @param capacityTemplateEntityRequest entity class containing the detail of
	 *                                      Capacity Template Model to be created.
	 *                                      
	 * @param list of entity class containing the detail of Capacity Template Entity 
	 * 								to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validateAssignedTemplateDates(CapacityTemplateEntity capacityTemplateEntityRequest,
			List<CapacityTemplateEntity> list) {
		return list.stream().filter(Objects::nonNull)
				.filter(templateType -> templateType.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES))
				.anyMatch(t -> t
				.getCapacityTemplateAndBusinessDates().stream().filter(Objects::nonNull).anyMatch(s -> {
					LocalDate businessDate = DateUtil.convertDatetoLocalDate(s.getId().getBusinessDate());
					return capacityTemplateEntityRequest.getCapacityTemplateAndBusinessDates().stream()
							.filter(Objects::nonNull).anyMatch(bDate -> {
								LocalDate reqBusinessDate = DateUtil
										.convertDatetoLocalDate(bDate.getId().getBusinessDate());
								boolean isSameBusinessDate = false;
								if (businessDate.equals(reqBusinessDate)) {
									isSameBusinessDate = true;
								}
								return isSameBusinessDate;
							});
				}));
	}

	/**
	 * This method is to validate assigned template days between the assigned 
	 * template to capacity model.
	 * 
	 * @param capacityTemplateEntityRequest entity class containing the detail of
	 *                                      Capacity Template Model to be created.
	 *                                      
	 * @param list of entity class containing the detail of Capacity Template Entity 
	 * 								to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validateAssignedTemplateDays(CapacityTemplateEntity capacityTemplateEntityRequest,
			List<CapacityTemplateEntity> list) {
		return list.stream().filter(Objects::nonNull)
				.filter(templateType -> templateType.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS))
				.anyMatch(t -> {
			LocalDate dbTemplateEffectiveDate = DateUtil
					.convertDatetoLocalDate(t.getEffectiveDate());
			LocalDate dbTemplateExpDate = DateUtil.convertDatetoLocalDate(t.getExpiryDate());
			LocalDate effectiveDateModelReq = DateUtil
					.convertDatetoLocalDate(capacityTemplateEntityRequest.getEffectiveDate());
			LocalDate expModelReq = DateUtil.convertDatetoLocalDate(capacityTemplateEntityRequest.getExpiryDate());
			if (effectiveDateModelReq.isBefore(dbTemplateExpDate) && expModelReq.isAfter(dbTemplateEffectiveDate)) {
				CapacityTemplateEntity template = t;
				if (CapacityConstants.DAYS
						.equalsIgnoreCase(template.getCapacityTemplateType().getCapacityTemplateTypeNm())) {
					return validateDays(capacityTemplateEntityRequest, template);
				}
				return false;
			}
			return false;
		});
	}

	/**
	 * This method is to validate days if same days are present in other templates
	 * under the same capacity Model.If same days is present it will raise
	 * application Error.
	 * 
	 * 
	 * @param sunDay
	 * @param monDay
	 * @param tueDay
	 * @param wedDay
	 * @param thuDay
	 * @param friDay
	 * @param satDay
	 * @param template
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateDays(CapacityTemplateEntity capacityTemplateEntityRequest, CapacityTemplateEntity template) {
		String sunDayFlg = capacityTemplateEntityRequest.getSunFlg();
		String monDayFlg = capacityTemplateEntityRequest.getMonFlg();
		String tueDayFlg = capacityTemplateEntityRequest.getTueFlg();
		String wedDayFlg = capacityTemplateEntityRequest.getWedFlg();
		String thuDayFlg = capacityTemplateEntityRequest.getThuFlg();
		String friDayFlg = capacityTemplateEntityRequest.getFriFlg();
		String satDayFlg = capacityTemplateEntityRequest.getSatFlg();
		return validateDay(template.getSunFlg(), sunDayFlg) || validateDay(template.getMonFlg(), monDayFlg)
				|| validateDay(template.getTueFlg(), tueDayFlg) || validateDay(template.getWedFlg(), wedDayFlg)
				|| validateDay(template.getThuFlg(), thuDayFlg) || validateDay(template.getFriFlg(), friDayFlg)
				|| validateDay(template.getSatFlg(), satDayFlg);
	}

	/**
	 * This method is to validate the day flags here comparing value of flag coming
	 * from database and value of flag coming from request if it matches it will not
	 * add that template to the capacity model.
	 * 
	 * @param reqFlg string containing value of resFlg.
	 * 
	 * @param resFlg string containing value of resFlg.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validateDay(String reqFlg, String resFlg) {
		return StringUtils.equalsIgnoreCase(CapacityConstants.Y, resFlg)
				&& StringUtils.equalsIgnoreCase(resFlg, reqFlg);
	}

	/**
	 * This service method is used to update capacity model based on the 
	 * data RestaurantAssigned and TemplateAssigned which is assigned to the capacity Model
	 * provided in capacityModel request and user detail passed in
	 * the parameter of method.The data to be updated is fetched based 
	 * upon the value of model id, is deleted flag and concept passed in the 
	 * header.The fields needed to be updated is changed and then save operation
	 * is performed.Then capacity templates are assigned by adding the capacity
	 * model and capacity template combination values in 
	 * 
	 * @param modelId string contains the value of capacity model 
	 * 					to be updated.
	 * 
	 * @param capacityModelRequest request class containing detail of
	 * 								Capacity Model Template to be created.
	 * 
	 * @param user String contains the detail of user extracted from the
	 * 								access token.
	 * 
	 * @return CapacityTemplateModel model class containing the value of
	 * 						data of updated capacity template model.
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_MODEL_CACHE, allEntries = true) }, put = {
            @CachePut(value = CapacityConstants.CAPACITY_MODEL_CACHE, key = CapacityConstants.CAPACITY_MODEL_CACHE_KEY) })
	public CapacityTemplateModel updateCapacityModel(String modelId, CapacityModelRequest capacityModelRequest,
			String user) {
		CapacityTemplateModel capacityTemplateModel = new CapacityTemplateModel();
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		Optional<CapacityModelEntity> dbModelEntityOptional = capacityModelRepository
				.findByCapacityModelIdAndIsDeletedFlgAndConceptId(new BigInteger(modelId), CapacityConstants.N, new BigInteger(RequestContext.getConcept()));
		CapacityModelEntity dbModelEntity = new CapacityModelEntity();
		if(dbModelEntityOptional.isPresent()) {
			dbModelEntity = dbModelEntityOptional.get();
		}
		dbModelEntity.setCapacityModelNm(capacityModelRequest.getTemplateModelName());
		dbModelEntity.setLastModifiedBy(user);
		dbModelEntity.setLastModifiedDatetime(dateTime);
		CapacityModelEntity savedEntity = capacityModelRepository.save(dbModelEntity);
		capacityModelAndCapacityTemplateRepo.deleteByCapacityModel(savedEntity);
		capacityModelAndLocationRepo.deleteAllByCapacityModel(savedEntity);
		List<BigInteger> templateIds = extractingAllTemplateIdFromRequest(capacityModelRequest);
		mapCapacityModelAndLocations(capacityModelRequest, user, dateTime, savedEntity);
		mapCapacityModelAndTemplates(user, dateTime, savedEntity, templateIds);
		mapCapacityModelResponse(capacityTemplateModel, savedEntity);
		return capacityTemplateModel;
	}

	
	/**
	 * This method is used to get all template id from request.
	 * 
	 * @param capacityModelRequest request class containing detail of
	 * 								Capacity Model Template to be created.
	 * 
	 * @return List<BigInteger> list of bigInteger containing all the 
	 * 				value of template id from request.
	 */
	private List<BigInteger> extractingAllTemplateIdFromRequest(CapacityModelRequest capacityModelRequest) {
		List<BigInteger> templateIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(capacityModelRequest.getTemplatesAssigned())) {
			capacityModelRequest.getTemplatesAssigned().stream().filter(Objects::nonNull)
					.forEach(templatesAssigned -> templateIds.add(new BigInteger(templatesAssigned.getTemplateId())));
		}
		return templateIds;
	}

	/**
	 * This service method is written for purpose of validating capacity
	 * template model name in database to avoid duplicates.
	 * 
	 * @param capacityModelNm String contains the value capacity model 
	 * 					name to be validated in database.
	 * 
	 * @param id String contains the value of capacity template model id.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateModelTemplateNmForUpdate(String capacityModelNm, String id) {
		Optional<CapacityModelEntity> capacityModelEntity = capacityModelRepository
				.findByCapacityModelNmAndConceptId(capacityModelNm, new BigInteger(RequestContext.getConcept()));
		if(capacityModelEntity.isPresent() && capacityModelEntity.get().getCapacityModelId().equals(new BigInteger(id))) {
			return CapacityConstants.FALSE;
		}
		return !capacityModelEntity.isEmpty();
	}
	
	/**
	 * This service method is written to validate if location Id exists in
	 * location table through location micro service.
	 * 
	 * @param locationId String contains the value of location to be validated.
	 * 
	 * @param restaurantInDB list of model class containing the value of location
	 *                from location micro service.
	 *                
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean isLocationIdValid(String locationId, List<Locations> restaurantInDB) {
		return restaurantInDB.stream().filter(Objects::nonNull)
				.noneMatch(l -> l.getLocationId().equals(new BigInteger(locationId)));
	}

	/**
	 * This service method is to validate if capacity template is already assigned to other
	 * capacity model for update operation.
	 * 
	 * @param capacityModelRequest request class containing the value of template model
	 * 				to be updated.
	 * 
	 * @param applicationErrors error class used to raise exception in case any validation
	 * 				is failed
	 * 
	 * @param id string containing the value of template id to be updated.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateTemplateAssignedforUpdate(CapacityModelRequest capacityModelRequest, ApplicationErrors applicationErrors, String id) {
		capacityModelRequest.getTemplatesAssigned().stream().filter(Objects::nonNull).forEach(t -> {
			Optional<CapacityTemplateEntity> dbTemplate = capacityTemplateRepo
					.findById(new BigInteger(t.getTemplateId()));
			if (dbTemplate.isEmpty()) {
				applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4503), t.getTemplateId());
			}
		});
		applicationErrors.raiseExceptionIfHasErrors();
		return true;
	}

	/**
	 * This service method is used to cache concept data from restCall.
	 * 
	 * @return List<ConceptForCache> list of model class containing the value of
	 * 				concept data.
	 */
	@Override
	@Cacheable(value = CapacityConstants.CONCEPTS_CACHE)
	public List<ConceptForCache> getCacheConceptData() {
		List<ConceptForCache> cachedConceptList = new ArrayList<>();
		conceptClient.getAllConcepts().stream().forEach(concept ->{
			ConceptForCache cacheConcept = new ConceptForCache();
			cacheConcept.setConceptId(concept.getConceptId());
			cacheConcept.setConceptName(concept.getConceptName());
			cachedConceptList.add(cacheConcept);
		});
		return cachedConceptList;
	}

}
