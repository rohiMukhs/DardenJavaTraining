package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.darden.dash.capacity.client.OrderClient;
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
import com.darden.dash.capacity.model.OrderTemplate;
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
import com.darden.dash.common.enums.AuditActionValues;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.model.DeleteResponseBodyFormat;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.GlobalDataCall;
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
	
    private OrderClient orderClient;
	
	private GlobalDataCall globalDataCall;
	
	private AuditService auditService;

	
	
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
	 * @param orderClient
	 * @param globalDataCall
	 * @param auditService
	 */
	@Autowired
	public CapacityTemplateModelServiceImpl(CapacityModelRepository capacityModelRepository,
			LocationClient locationClient, JwtUtils jwtUtils,
			CapacityModelAndLocationRepository capacityModelAndLocationRepo, CapacityTemplateRepo capacityTemplateRepo,
			CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo,
			ConceptClient conceptClient,OrderClient orderClient,GlobalDataCall globalDataCall,AuditService auditService) {
		this.jwtUtils = jwtUtils;
		this.capacityModelRepository = capacityModelRepository;
		this.locationClient = locationClient;
		this.capacityModelAndCapacityTemplateRepo = capacityModelAndCapacityTemplateRepo;
		this.capacityModelAndLocationRepo = capacityModelAndLocationRepo;
		this.capacityTemplateRepo = capacityTemplateRepo;
		this.conceptClient = conceptClient;
		this.orderClient = orderClient;
		this.globalDataCall = globalDataCall;
		this.auditService = auditService;
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
		
		//Fetching the list of capacity model entity within the concept.
		List<CapacityModelEntity> modelEntityList = capacityModelRepository
				.findByConceptIdAndIsDeletedFlg(new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
		
		//validating location call response.
		List<Locations> restaurantList = validatingLocationRestCall();
		List<CapacityModel> modelResponseList = new ArrayList<>();
		
		List<OrderTemplate> orderTemplates = orderClient.getAllOrderTemplates();
		
		//Iterating list of entity class.
		modelEntityList.stream()
			.forEach(mel -> {
				
			List<BigInteger> restaurantNumberViaOrderTemplate = new ArrayList<>();
			//iterating through the order templates.
			orderTemplates
			.stream()
			.filter(orderTemplate->
				orderTemplate.getOrderLists()
				.stream()
				//Checks for list type string with capacity model list and with matching capacity template id.
				.anyMatch(list->list.getListType().equalsIgnoreCase(CapacityConstants.ORDER_TEMPLATE_TYPE)
						&& list.getListId().equals(mel.getCapacityModelId()))
				)
			.forEach(loc -> loc.getLocations()
					.stream()
					.filter(restNbr -> !restaurantNumberViaOrderTemplate.contains(restNbr.getRestaurantNumber()))
					.forEach(restNbr -> restaurantNumberViaOrderTemplate.add(restNbr.getRestaurantNumber())));
				
			//Mapping entity data to model class.
			CapacityModel model = capacityModelMapper.mapToCapacityModel(mel, restaurantList, restaurantNumberViaOrderTemplate);
			
			//adding to list.
			modelResponseList.add(model);
		});
		return modelResponseList;
	}

	/**
	 * This service method is to get all location details.
	 * 
	 * @return List<Locations> list of model class containing the data of locations.
	 */
	private List<Locations> validatingLocationRestCall() {
		List<Locations> restaurantList =  null;
		ApplicationErrors applicationErrors = new ApplicationErrors();
		int i = 0;
		do {
			//Calling location micro service to get all restaurant detail.
			restaurantList = locationClient.getAllRestaurants();
			i++;
		}
		while(restaurantList.isEmpty() && i < 3);
		
		//If restaurant list is empty raising exception.
		if(restaurantList.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), CapacityConstants.LOCATION_CONNECTION);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		return restaurantList;
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
	public CapacityTemplateModel createCapacityModel(CapacityModelRequest capacityModelRequest, String accessToken) throws JsonProcessingException {
		CapacityTemplateModel capacityTemplateModel = new CapacityTemplateModel();
		
		//Fetching user detail from access token.
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		
		//Mapping request data to entity class.
		CapacityModelEntity capacityModelEntity = capacityModelMapper.mapToCapacityModelEntity(capacityModelRequest, createdBy, dateTime);
		
		//Fetching all the template ids to be assigned from request data.
		List<BigInteger> templateIds = extractingAllTemplateIdFromRequest(capacityModelRequest);
		
		//Saving the entity.
		CapacityModelEntity capacityModelEntityRes = capacityModelRepository.save(capacityModelEntity);
		
		//Adding operation performed to audit table using audit service.
		if (null != capacityModelEntityRes.getCapacityModelNm()) {
            auditService.addAuditData(CapacityConstants.CAPACITY_MODEL, AuditActionValues.INSERT, null,
                    capacityModelEntityRes, createdBy);
        }
		
		//Fetching capacity model id.
		BigInteger capacityModelId = capacityModelEntityRes.getCapacityModelId();
		
		//Creating entities for capacityModelAndCapacityTemplate for requested data and saving it.
		mapCapacityModelAndTemplates(createdBy, dateTime, capacityModelEntityRes, templateIds);
		
		//Creating entities for capacityModelAndLocation for requested data and saving it.
		mapCapacityModelAndLocations(capacityModelRequest, createdBy, dateTime, capacityModelEntityRes);
		
		//Fetching capacity model entity for capacity model id.
		Optional<CapacityModelEntity> capacityModelEntityResponse = capacityModelRepository.findById(capacityModelId);
		
		//If entity present mapping data to response class and returning it.
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
		
		//Mapping entity data to model class.
		capacityModelMapper.mapToCapacityTemplateModel(capacityTemplateModel, responseEntity);
		
		//Fetching list of CapacityModelAndCapacityTemplateEntity from entity.
		List<CapacityModelAndCapacityTemplateEntity> modelTemplatesList = responseEntity
				.getCapacityModelAndCapacityTemplates();
		
		//if modelTemplatesList not empty.
		if (CollectionUtils.isNotEmpty(modelTemplatesList)) {
			List<TemplatesAssigned> templatesAssignedList = new ArrayList<>();
			
			modelTemplatesList.stream()
				.filter(Objects::nonNull)
				.forEach(templatesAssignedEntity -> {
					
				//Fetching CapacityTemplateEntity for template id from entity id.
				Optional<CapacityTemplateEntity> capacityTemplate = capacityTemplateRepo
						.findById(templatesAssignedEntity.getId().getCapacityTemplateId());
				
				//if capacityTemplate present.
				capacityTemplate.ifPresent(t -> {
					
					//Mapping entity data to model class.
					TemplatesAssigned templatesAssigned = new TemplatesAssigned();
					templatesAssigned.setTemplateId(String.valueOf(t.getCapacityTemplateId()));
					templatesAssigned.setTemplateName(t.getCapacityTemplateNm());
					
					//Adding to list.
					templatesAssignedList.add(templatesAssigned);
				});

			});
			//Setting template data to model class.
			capacityTemplateModel.setTemplatesAssigned(templatesAssignedList);
		}
		
		//Fetching list of CapacityModelAndLocationEntity from entity class.
		List<CapacityModelAndLocationEntity> modelLocationsList = responseEntity.getCapacityModelAndLocations();
		
		//if modelLocationsList not empty.
		if (CollectionUtils.isNotEmpty(modelLocationsList)) {
			
			//Mapping to list of data from entity to model class.
			List<RestaurantsAssigned> restaurantsAssignedList = capacityModelMapper.mapToRestaurantAssignedList(modelLocationsList);
			
			//Setting restaurant data to model class.
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
		
		//Calling location micro service to get all restaurant detail.
		List<Locations> restaurantInDB = locationClient.getAllRestaurants();
		
		//Iterating list of locations for requested data.
		capacityModelRequest.getRestaurantsAssigned()
							.stream()
							.filter(Objects::nonNull)
							//filtering out if any blank values.
							.filter(t -> StringUtils.isNotBlank(t.getLocationId()))
							.forEach(restaurantsAssigned -> {
					
					//If location is present in location service get call.
					if(isLocationIdValid(restaurantsAssigned.getLocationId(), restaurantInDB)) {
						
						//if location is not present raising exception.
						applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), CapacityConstants.LOCATION_ID);
						applicationErrors.raiseExceptionIfHasErrors();
					}
					
					//Mapping requested data to CapacityModelAndLocationEntity.
					CapacityModelAndLocationEntity capacityModelAndLocationEntity = capacityModelMapper.mapToCapacityModelAndLocationEntity(
							createdBy, dateTime, capacityModelEntity, restaurantsAssigned);
					
					//Adding to list.
					capacityModelAndLocationEntites.add(capacityModelAndLocationEntity);
				});
		
		//If capacityModelAndLocationEntites not empty.
		if (CollectionUtils.isNotEmpty(capacityModelAndLocationEntites)) {
			
			//Setting location entity values to entity class.
			capacityModelEntity.setCapacityModelAndLocations(capacityModelAndLocationEntites);
			
			//Saving list of capacityModelAndLocationEntites.
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
		
		//Fetching List of CapacityTemplateEntity for requested template ids.
		List<CapacityTemplateEntity> capacityTemplateEntites = capacityTemplateRepo.findAllById(templateIds);
		
		//Mapping to list of CapacityModelAndCapacityTemplateEntity from requested data.
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplateEntites = capacityModelMapper.mapToCapacityModelAndCapacityTemplateEntityList(
				createdBy, dateTime, capacityModelEntity, capacityTemplateEntites);
		
		//if capacityModelAndCapacityTemplateEntites not empty.
		if (CollectionUtils.isNotEmpty(capacityModelAndCapacityTemplateEntites)) {
			
			//Setting value to capacityMoodel entity.
			capacityModelEntity.setCapacityModelAndCapacityTemplates(capacityModelAndCapacityTemplateEntites);
			
			//Saving list of capacityModelAndCapacityTemplateEntites.
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
		
		//Fetching the CapacityModelEntity by name within the concept id.
		List<CapacityModelEntity> capacityModelEntity = capacityModelRepository
				.findByCapacityModelNmAndConceptIdAndIsDeletedFlg(capacityModelNm, new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
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
	public boolean validateCapacityModelTemplateBusinessDates(CapacityTemplateEntity capacityTemplateEntityRequest, List<BigInteger> otherTemplateId
			,List<String> matchingTemplatesId) { 
		
		//Fetching the list of CapacityTemplateEntity for otherTemplateIds.
		List<CapacityTemplateEntity> list = capacityTemplateRepo.findAllById(otherTemplateId);
		
		//Fetching the templateType name string from the requested template entity.
		String templateTypeNm = capacityTemplateEntityRequest.getCapacityTemplateType().getCapacityTemplateTypeNm();
		
		//If template type name equals DAYS.
		if (CapacityConstants.DAYS.equalsIgnoreCase(templateTypeNm)) {
			
			//validates if any conflicting days with other DAYS templates within the model
			return validateAssignedTemplateDays(capacityTemplateEntityRequest, list, matchingTemplatesId);
		} 
		
		//If template type name equals DAYS
		else if (CapacityConstants.DATES.equalsIgnoreCase(templateTypeNm)) {
			
			//validates if any conflicting dates with other DATES templates within the model
			return validateAssignedTemplateDates(capacityTemplateEntityRequest, list, matchingTemplatesId);
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
			List<CapacityTemplateEntity> list, List<String> matchingTemplatesId) {
		
		//Iterating list of entities and validating if any conflicting dates.
		return list.stream()
				.filter(Objects::nonNull)
				//Filtering by template type name if String is DATES.
				.filter(templateType -> templateType.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES))
				.anyMatch(t -> t.getCapacityTemplateAndBusinessDates()
					.stream()
					.filter(Objects::nonNull)
					.anyMatch(s -> {
					
					//Converting Date datatype to localdate.
					LocalDate businessDate = DateUtil.convertDatetoLocalDate(s.getId().getBusinessDate());
					
					//Iterating to list of dates from request and validating conflict dates.
					return capacityTemplateEntityRequest.getCapacityTemplateAndBusinessDates()
							.stream()
							.filter(Objects::nonNull)
							.anyMatch(bDate -> {
								
								//Converting Date datatype to localdate.
								LocalDate reqBusinessDate = DateUtil
										.convertDatetoLocalDate(bDate.getId().getBusinessDate());
								
								//Setting value for isSameBusinessDate to false.
								boolean isSameBusinessDate = false;
								
								//if dates match.
								if (businessDate.equals(reqBusinessDate)) {
									
									//Setting value for isSameBusinessDate to true.
									isSameBusinessDate = true;
									
									//Adding conflict templates to list.
									if(!matchingTemplatesId.contains(bDate.getCapacityTemplate().getCapacityTemplateNm())) {
										matchingTemplatesId.add(bDate.getCapacityTemplate().getCapacityTemplateNm());
									}
									if(!matchingTemplatesId.contains(t.getCapacityTemplateNm())) {
										matchingTemplatesId.add(t.getCapacityTemplateNm());
									}
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
			List<CapacityTemplateEntity> list, List<String> matchingTemplatesId) {
		
		//Iterating through list and validating is any templates with conflicting days.
		return list.stream()
				.filter(Objects::nonNull)
				//Filtering out templates with template type name ad DAYS.
				.filter(templateType -> templateType.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS))
				.anyMatch(t -> {
			LocalDate dbTemplateExpDate = null;
			LocalDate expModelReq = null;
			
			//Converting other date formats to localDate datatype.
			LocalDate dbTemplateEffectiveDate = DateUtil
					.convertDatetoLocalDate(t.getEffectiveDate());
			if(t.getExpiryDate() != null)
				dbTemplateExpDate = DateUtil.convertDatetoLocalDate(t.getExpiryDate());
			LocalDate effectiveDateModelReq = DateUtil
					.convertDatetoLocalDate(capacityTemplateEntityRequest.getEffectiveDate());
			if(capacityTemplateEntityRequest.getExpiryDate() != null)
				expModelReq = DateUtil.convertDatetoLocalDate(capacityTemplateEntityRequest.getExpiryDate());
			
			//validating if both expiry date is null
			if ((expModelReq == null && dbTemplateExpDate == null) 
					//if comparing expiry date is null and to be compared effective is before the other expiry date.
					|| (expModelReq == null && dbTemplateExpDate != null && effectiveDateModelReq.isBefore(dbTemplateExpDate))
					//if to be compared expiry date is null and comparing expiry date is after the other effective date.
					|| (dbTemplateExpDate == null && expModelReq != null && expModelReq.isAfter(dbTemplateEffectiveDate))
					//if no null value for both expiry date and to be compared effective is before the other expiry date and comparing expiry date is after the other effective date.
					|| (expModelReq != null && dbTemplateExpDate != null && effectiveDateModelReq.isBefore(dbTemplateExpDate) && expModelReq.isAfter(dbTemplateEffectiveDate))) {
				CapacityTemplateEntity template = t;
				
				//validating dates for overlapping days templates.
				validateDays(capacityTemplateEntityRequest, template, matchingTemplatesId);
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
	public void validateDays(CapacityTemplateEntity capacityTemplateEntityRequest, CapacityTemplateEntity template, List<String> matchingTemplatesId) {
		
		//Fetching all the days flags from request entity.
		String sunDayFlg = capacityTemplateEntityRequest.getSunFlg();
		String monDayFlg = capacityTemplateEntityRequest.getMonFlg();
		String tueDayFlg = capacityTemplateEntityRequest.getTueFlg();
		String wedDayFlg = capacityTemplateEntityRequest.getWedFlg();
		String thuDayFlg = capacityTemplateEntityRequest.getThuFlg();
		String friDayFlg = capacityTemplateEntityRequest.getFriFlg();
		String satDayFlg = capacityTemplateEntityRequest.getSatFlg();
		
		//if any days flag validations checks.
		if(validateDay(template.getSunFlg(), sunDayFlg) || validateDay(template.getMonFlg(), monDayFlg)
				|| validateDay(template.getTueFlg(), tueDayFlg) || validateDay(template.getWedFlg(), wedDayFlg)
				|| validateDay(template.getThuFlg(), thuDayFlg) || validateDay(template.getFriFlg(), friDayFlg)
				|| validateDay(template.getSatFlg(), satDayFlg)) {
			
			//Adding conflicting templates to list.
			if(!matchingTemplatesId.contains(capacityTemplateEntityRequest.getCapacityTemplateNm())) {
				matchingTemplatesId.add(capacityTemplateEntityRequest.getCapacityTemplateNm());
			}
			if(!matchingTemplatesId.contains(template.getCapacityTemplateNm())) {
				matchingTemplatesId.add(template.getCapacityTemplateNm());
			}
		}
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
		
		//Checks if both strings contain values as Y.
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
	 * @throws JsonProcessingException 
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_MODEL_CACHE, allEntries = true) }, put = {
            @CachePut(value = CapacityConstants.CAPACITY_MODEL_CACHE, key = CapacityConstants.CAPACITY_MODEL_CACHE_KEY) })
	public CapacityTemplateModel updateCapacityModel(String modelId, CapacityModelRequest capacityModelRequest,
			String user) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		CapacityTemplateModel capacityTemplateModel = new CapacityTemplateModel();
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		
		//Fetching CapacityModelEntity for the model id within the concept.
		Optional<CapacityModelEntity> dbModelEntityOptional = capacityModelRepository
				.findByCapacityModelIdAndIsDeletedFlgAndConceptId(new BigInteger(modelId), CapacityConstants.N, new BigInteger(RequestContext.getConcept()));
		CapacityModelEntity dbModelEntity = new CapacityModelEntity();
		
		//if dbModelEntityOptional is present.
		if(dbModelEntityOptional.isPresent()) {
			dbModelEntity = dbModelEntityOptional.get();
		}
		
		//if dbModelEntityOptional is empty raising exception.
		else {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), CapacityConstants.CAPACITY_TEMPLATE_MODEL_ID);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		//Mapping entity with requested data.
		dbModelEntity.setCapacityModelNm(capacityModelRequest.getTemplateModelName());
		dbModelEntity.setLastModifiedBy(user);
		dbModelEntity.setLastModifiedDatetime(dateTime);
		
		//Saving entity.
		CapacityModelEntity savedEntity = capacityModelRepository.save(dbModelEntity);
				
		//deleting all the relational data for entity.
		capacityModelAndCapacityTemplateRepo.deleteByCapacityModel(savedEntity);
		capacityModelAndLocationRepo.deleteAllByCapacityModel(savedEntity);
		
		//extracting template ids from request.
		List<BigInteger> templateIds = extractingAllTemplateIdFromRequest(capacityModelRequest);
		
		//creating entities for modelandlocation and saving all.
		mapCapacityModelAndLocations(capacityModelRequest, user, dateTime, savedEntity);
		
		//creating entities for modelandtemplate and saving all.
		mapCapacityModelAndTemplates(user, dateTime, savedEntity, templateIds);
		
		//Mapping entity data to model class.
		mapCapacityModelResponse(capacityTemplateModel, savedEntity);
		
		//Adding action performed to audit table using audit service.
		if (null != savedEntity.getCapacityModelNm()) {
			auditService.addAuditData(CapacityConstants.CAPACITY_MODEL, AuditActionValues.UPDATE, null, savedEntity,
	            user);
		   	}
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
		
		//if templates assigned in the request is not empty.
		if (CollectionUtils.isNotEmpty(capacityModelRequest.getTemplatesAssigned())) {
			
			//Iterating through template ids in request.
			capacityModelRequest.getTemplatesAssigned()
				.stream()
				.filter(Objects::nonNull)
				//Adding id to templateIds.
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
		
		//Fetching CapacityModelEntity for capacity name within the concept.
		Optional<CapacityModelEntity> capacityModelEntity = capacityModelRepository
				.findByCapacityModelNmAndConceptId(capacityModelNm, new BigInteger(RequestContext.getConcept()));
		
		//condition to avoid self check.
		if(capacityModelEntity.isPresent() && capacityModelEntity.get().getIsDeletedFlg().equals(CapacityConstants.N) && capacityModelEntity.get().getCapacityModelId().equals(new BigInteger(id))) {
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
		
		//Iterating through list of locations.
		return restaurantInDB
				.stream()
				.filter(Objects::nonNull)
				//checks if location id matches.
				.noneMatch(l -> l.getLocationId().equals(new BigInteger(locationId)));
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
		
		//Iterating through list of concepts.
		conceptClient.getAllConcepts()
			.stream()
			.forEach(concept ->{
				
			//Mapping concept data to model class.
			ConceptForCache cacheConcept = new ConceptForCache();
			cacheConcept.setConceptId(concept.getConceptId());
			cacheConcept.setConceptName(concept.getConceptName());
			
			//Adding to list.
			cachedConceptList.add(cacheConcept);
		});
		return cachedConceptList;
	}
	
	/**
	 * This method is used for DELETE operationIn this the Capacity Model Data is fetch by 
	 * Capacity model id and concept id.Then we rest call the location data and orderTemplate
	 * data for dependency check.If the confirm Flag is N the model data is checked with 
	 * the dependency present or not and If the confirm flag is Y then it confirms if 
	 * there is no dependency present and if its not present it hard deletes the model data.
	 * 
	 * @param ModelId Department List Id of department List to be 
	 * 							deleted.
	 * 
	 * @param confirm Flag containing confirmation regarding deleting flag or checking dependency.
	 * 
	 * @param userDetail information of user operating on the delete action.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_MODEL_CACHE, key = CapacityConstants.CAPACITY_TEMPLATE_CACHE_KEY),
            @CacheEvict(value = CapacityConstants.CAPACITY_MODEL_CACHE, allEntries = true) })
	public void deleteTemplateModel(String templateId, String userDetail,String deletedConfirm) throws JsonProcessingException {
	    ApplicationErrors applicationErrors = new ApplicationErrors();
		CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
		
		//Fetching capacity model entity for template id within the concept.
		Optional<CapacityModelEntity> dbTemplateModelEntityOptional = capacityModelRepository
				.findByCapacityModelIdAndConceptId(new BigInteger(templateId),new BigInteger(RequestContext.getConcept()));
		
		//Fetching the list of locations list from location micro service.
		List<Locations> restaurantInDB = locationClient.getAllRestaurants();
		
		//Fetching the list of operation templates list from location micro service.
		List<OrderTemplate> orderTemplates = orderClient.getAllOrderTemplates();
		
		//if dbTemplateModelEntityOptional is empty raisinf exception.
		if(dbTemplateModelEntityOptional.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_MODEL_ID);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		else{
			capacityModelEntity = dbTemplateModelEntityOptional.get();
			}
		
		//Checks validation for model entity.
	  	validationForPresentOrderTemplateAndLocationList(restaurantInDB,capacityModelEntity,orderTemplates);
		
		//if deleted confirm flag contains value as Y.
		if(CapacityConstants.Y.equals(deletedConfirm)){
			
			//performs hard delete.
			hardDeleteCapacityModel(capacityModelEntity);
			}
		
		//adding operation performed to audit trail table using audit service.
		if (null != capacityModelEntity.getCapacityModelNm()) { 
			auditService.addAuditData(CapacityConstants.CAPACITY_MODEL, AuditActionValues.DELETE_HARD, null, capacityModelEntity, userDetail); }
		}		
	
	
	
	/**
	 * This method Deletes the model Data.
	 * 
	 * @param capacityModelEntity contains capacityModelEntity modelLIstData.
	 * @param userDetail contains details of the user
	 @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */

	private void hardDeleteCapacityModel(CapacityModelEntity capacityModelEntity) {
		
		//Deleting all the relational data.
		capacityModelAndCapacityTemplateRepo.deleteAllByCapacityModel(capacityModelEntity);
		capacityModelAndLocationRepo.deleteAllByCapacityModel(capacityModelEntity);
		
		//Deleting capacity model.
		capacityModelRepository.deleteById(capacityModelEntity.getCapacityModelId());	
	}
	
	/**
	 * This method contains validation of dependency present for model delete.
	 * Its checks if modelTemplate consist of restaurant asssigned and orderTemplate assigned 
	 * if the dependency is not present it set isDeleted flag to Y so that it can be deleted 
	 * when user sends delete confirm request. 
	 * @param restaurantInDB contains data of restaurant list.
	 * @param capacityModelEntity contains capacityModelEntity modelLIstData.
	 * @param orderTemplates contains data of orderTemplate list.
	 */

	private void validationForPresentOrderTemplateAndLocationList(List<Locations> restaurantInDB,
			CapacityModelEntity capacityModelEntity, List<OrderTemplate> orderTemplates) {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		List<DeleteResponseBodyFormat> deleteResponseBodyFormatList = new ArrayList<>();
		List<String> headerFooterList = new ArrayList<>();
		List<String> restaurantNumberList = new ArrayList<>();
		
		//iterating through list of locations.
		restaurantInDB.stream()
			.forEach(restaurantInDB1 -> 
			//iterating through capacityModelAndLocations list
			capacityModelEntity.getCapacityModelAndLocations().stream()
					.forEach(capacityModelEntity1 -> {
						
						//if locations if matches.
						if(restaurantInDB1.getLocationId().equals(capacityModelEntity1.getId().getLocationId()))
							
							//adding restaurant number to list.
							restaurantNumberList.add(restaurantInDB1.getRestaurantNumber().toString());
					})
		);		
		
		//iterating through the order templates.
		List<OrderTemplate> orderTemplates1 = orderTemplates
				.stream()
				.filter(orderTemplate->
					orderTemplate.getOrderLists()
					.stream()
					//Checks for list type string with capacity model list.
					.anyMatch(list->list.getListType().equalsIgnoreCase(CapacityConstants.ORDER_TEMPLATE_TYPE)))
					.filter(orderTemplate->
							orderTemplate.getOrderLists()
							.stream()
							//Checks if list id equals capacity model id.
							.anyMatch(list->list.getListId().equals(capacityModelEntity.getCapacityModelId())))
					.collect(Collectors.toList());
		
		//if both dependency list not empty.
		if(!orderTemplates1.isEmpty()||!restaurantNumberList.isEmpty()) {
			
			//Mapping model class with dependency data.
			restaurantMapper(restaurantNumberList,headerFooterList,deleteResponseBodyFormatList);
			orderTemplateMapper(orderTemplates1,headerFooterList,deleteResponseBodyFormatList);
			
			//raising exceptions with the dependency data.
			globalDataCall.raiseException(headerFooterList, deleteResponseBodyFormatList, capacityModelEntity.getCapacityModelNm(), applicationErrors);			
		}
		
	}
   
	/**
	 * Model Template which is present in orderTemplate is checked and set the DeleteResponseBody
	 * @param orderTemplates1 - order templates dependency present in modelTemplate
	 * @param headerFooterList - header FooterList to be sent in response to set in DeleteResponseBody
	 * @param deleteResponseBodyFormatList Response te be send while model template containing any dependency
	 */
	private void orderTemplateMapper(List<OrderTemplate> orderTemplates1, List<String> headerFooterList,
			List<DeleteResponseBodyFormat> deleteResponseBodyFormatList) {
		
		//if orderTemplates1 is not empty.
		if(!orderTemplates1.isEmpty()) {
			List<String> orderTemplateName = new ArrayList<>();
			
			//iterating through the order template.
			orderTemplates1
			.stream()
			//fetching all the template names.
			.forEach(orderTemplates->orderTemplateName.add(orderTemplates.getOrderTemplateName()));
			
		//Mapping dependency data to model class.
		DeleteResponseBodyFormat orderTemplate = DeleteResponseBodyFormat.builder().build();
		orderTemplate.setTitle(CapacityConstants.ORDER_TEMPLATE);
		orderTemplate.setListOfData(orderTemplateName);
		
		//Adding to list.
		headerFooterList.add(CapacityConstants.ORDER_TEMPLATE);
		deleteResponseBodyFormatList.add(orderTemplate);
		
		}
	}

	/**
	 * 
	 * @param restaurantNumberList - restaurantNumber List dependency present in model Template
	 * @param headerFooterList -header FooterList to be sent in response to set in DeleteResponseBody
	 * @param deleteResponseBodyFormatList Response te be send while model template containing any dependency
	 */
	
	private void restaurantMapper(List<String> restaurantNumberList, List<String> headerFooterList,
			List<DeleteResponseBodyFormat> deleteResponseBodyFormatList) {
		
		//if restaurantNumberList is not empty.
		if(!restaurantNumberList.isEmpty()) {
			
			//Mapping to model class.
			DeleteResponseBodyFormat restaurantList = DeleteResponseBodyFormat.builder().build();
			restaurantList.setTitle(CapacityConstants.RESTAURANT_LIST);
			restaurantList.setListOfData(restaurantNumberList);
			
			//Adding to list.
			headerFooterList.add(CapacityConstants.RESTAURANT_LIST);
			deleteResponseBodyFormatList.add(restaurantList);
		}
	}

	

}
