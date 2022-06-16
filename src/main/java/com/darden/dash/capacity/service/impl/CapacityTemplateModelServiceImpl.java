package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.mapper.CapacityModelMapper;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacityModelAndLocationRepository;
import com.darden.dash.capacity.repository.CapacityModelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.util.DateUtil;
import com.darden.dash.common.RequestContext;
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

	/**
	 * Autowiring required properties
	 * 
	 * @param capacityModelRepository
	 * @param locationClient
	 */
	@Autowired
	public CapacityTemplateModelServiceImpl(CapacityModelRepository capacityModelRepository,
			LocationClient locationClient, JwtUtils jwtUtils,
			CapacityModelAndLocationRepository capacityModelAndLocationRepo, CapacityTemplateRepo capacityTemplateRepo,
			CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo) {
		this.jwtUtils = jwtUtils;
		this.capacityModelRepository = capacityModelRepository;
		this.locationClient = locationClient;
		this.capacityModelAndCapacityTemplateRepo = capacityModelAndCapacityTemplateRepo;
		this.capacityModelAndLocationRepo = capacityModelAndLocationRepo;
		this.capacityTemplateRepo = capacityTemplateRepo;
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
	public CapacityTemplateModel createCapacityModel(CapacityModelRequest capacityModelRequest, String accessToken) {
		CapacityTemplateModel capacityTemplateModel = new CapacityTemplateModel();
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
		capacityModelEntity.setCapacityModelNm(capacityModelRequest.getTemplateModelName());
		capacityModelEntity.setCreatedBy(createdBy);
		capacityModelEntity.setCreatedDatetime(dateTime);
		capacityModelEntity.setLastModifiedBy(createdBy);
		capacityModelEntity.setLastModifiedDatetime(dateTime);
		capacityModelEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		capacityModelEntity.setIsDeletedFlg(CapacityConstants.N);
		List<BigInteger> templateIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(capacityModelRequest.getTemplatesAssigned())) {
			capacityModelRequest.getTemplatesAssigned().stream().filter(Objects::nonNull)
					.forEach(templatesAssigned -> templateIds.add(new BigInteger(templatesAssigned.getTemplateId())));
		}
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
		capacityTemplateModel.setTemplateModelName(responseEntity.getCapacityModelNm());
		capacityTemplateModel.setCreatedBy(responseEntity.getCreatedBy());
		capacityTemplateModel.setCreatedDateTime(responseEntity.getCreatedDatetime());
		capacityTemplateModel.setLastModifiedBy(responseEntity.getLastModifiedBy());
		capacityTemplateModel.setLastModifiedDateTime(responseEntity.getLastModifiedDatetime());
		capacityTemplateModel.setIsDeletedFlg(responseEntity.getIsDeletedFlg());
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
			List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
			modelLocationsList.stream().filter(Objects::nonNull).forEach(restaurantsAssignedEntity -> {
				RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
				restaurantsAssigned.setLocationId(String.valueOf(restaurantsAssignedEntity.getId().getLocationId()));
				restaurantsAssignedList.add(restaurantsAssigned);
			});
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
		List<CapacityModelAndLocationEntity> capacityModelAndLocationEntites = new ArrayList<>();
		capacityModelRequest.getRestaurantsAssigned().stream().filter(Objects::nonNull)
				.filter(t -> StringUtils.isNotBlank(t.getLocationId())).forEach(restaurantsAssigned -> {
					CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
					CapacityModelAndLocationPK capacityModelAndLocationPK = new CapacityModelAndLocationPK();
					capacityModelAndLocationPK.setLocationId(new BigInteger(restaurantsAssigned.getLocationId()));
					capacityModelAndLocationPK.setCapacityModelId(capacityModelEntity.getCapacityModelId());
					capacityModelAndLocationEntity.setId(capacityModelAndLocationPK);
					capacityModelAndLocationEntity.setCreatedBy(createdBy);
					capacityModelAndLocationEntity.setCreatedDatetime(dateTime);
					capacityModelAndLocationEntity.setLastModifiedBy(createdBy);
					capacityModelAndLocationEntity.setLastModifiedDatetime(dateTime);
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
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplateEntites = new ArrayList<>();
		List<CapacityTemplateEntity> capacityTemplateEntites = capacityTemplateRepo.findAllById(templateIds);
		capacityTemplateEntites.stream().forEach(capactityTemplateEntity -> {
			CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
			CapacityModelAndCapacityTemplatePK capacityModelAndCapacityTemplatePK = new CapacityModelAndCapacityTemplatePK();
			capacityModelAndCapacityTemplatePK.setCapacityModelId(capacityModelEntity.getCapacityModelId());
			capacityModelAndCapacityTemplatePK.setCapacityTemplateId(capactityTemplateEntity.getCapacityTemplateId());
			capacityModelAndCapacityTemplateEntity.setId(capacityModelAndCapacityTemplatePK);
			capacityModelAndCapacityTemplateEntity.setCreatedBy(createdBy);
			capacityModelAndCapacityTemplateEntity.setCreatedDatetime(dateTime);
			capacityModelAndCapacityTemplateEntity.setLastModifiedBy(createdBy);
			capacityModelAndCapacityTemplateEntity.setLastModifiedDatetime(dateTime);
			capacityModelAndCapacityTemplateEntites.add(capacityModelAndCapacityTemplateEntity);
		});
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
	public boolean validateCapacityModelTemplateBusinessDates(CapacityTemplateEntity capacityTemplateEntityRequest) {
		List<CapacityModelAndCapacityTemplateEntity> list = capacityModelAndCapacityTemplateRepo.findAll();
		String templateTypeNm = capacityTemplateEntityRequest.getCapacityTemplateType().getCapacityTemplateTypeNm();
		if (CapacityConstants.DAYS.equalsIgnoreCase(templateTypeNm)) {
			return list.stream().filter(Objects::nonNull).anyMatch(t -> {
				LocalDate dbTemplateEffectiveDate = DateUtil
						.convertDatetoLocalDate(t.getCapacityTemplate().getEffectiveDate());
				LocalDate dbTemplateExpDate = DateUtil.convertDatetoLocalDate(t.getCapacityTemplate().getExpiryDate());
				LocalDate effectiveDateModelReq = DateUtil
						.convertDatetoLocalDate(capacityTemplateEntityRequest.getEffectiveDate());
				LocalDate expModelReq = DateUtil.convertDatetoLocalDate(capacityTemplateEntityRequest.getExpiryDate());
				if (effectiveDateModelReq.isBefore(dbTemplateExpDate) && expModelReq.isAfter(dbTemplateEffectiveDate)) {
					CapacityTemplateEntity template = t.getCapacityTemplate();
					if (CapacityConstants.DAYS
							.equalsIgnoreCase(template.getCapacityTemplateType().getCapacityTemplateTypeNm())) {
						return validateDays(capacityTemplateEntityRequest, template);
					}
					return false;
				}
				return false;
			});
		} else if (CapacityConstants.DATES.equalsIgnoreCase(templateTypeNm)) {
			return list.stream().filter(Objects::nonNull).anyMatch(t -> t.getCapacityTemplate()
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
		return false;
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
	 * @param template
	 * @param sunDayFlg
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validateDay(String reqFlg, String resFlg) {
		return StringUtils.equalsIgnoreCase(CapacityConstants.Y, resFlg)
				&& StringUtils.equalsIgnoreCase(resFlg, reqFlg);
	}

}
