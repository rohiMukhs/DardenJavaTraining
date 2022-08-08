package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndCapacityChannelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.CapacityTemplateTypeRepository;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.entity.AppParameterEntity;
import com.darden.dash.common.enums.AuditActionValues;
import com.darden.dash.common.enums.CharacterConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.service.AppParameterService;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author skashala
 * @date 16-May-2022
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity template or any business logic related to Capacity
 *       Template
 */
@Service
public class CapacityManagementServiceImpl implements CapacityManagementService {

	private final JwtUtils jwtUtils;
	private CapacityTemplateRepo capacityTemplateRepo;
	private CapacityChannelRepo capacityChannelRepo;
	private CapacityTemplateTypeRepository capacityTemplateTypeRepository;

	private CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository;

	private CapacitySlotTypeRepository capacitySlotTypeRepository;

	private ReferenceRepository referenceRepository;

	private CapacitySlotRepository capacitySlotRepository;

	private CapacityTemplateAndCapacityChannelRepository capacityTemplateAndCapacityChannelRepository;
	
	private CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepository;
	
	private CapacityChannelService capacityChannelService;
	
	private AppParameterService appParameterService;
	
	private AuditService auditService;

	private CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);

	/**
	 * Autowiring required properties
	 * 
	 * @param jwtUtils
	 * @param capacityTemplateRepo
	 * @param capacityChannelRepo
	 * @param capacityTemplateTypeRepository
	 * @param capacityTemplateAndBusinessDateRepository
	 * @param capacitySlotTypeRepository
	 * @param referenceRepository
	 * @param capacitySlotRepository
	 * @param capacityTemplateAndCapacityChannelRepository
	 * @param capacityModelAndCapacityTemplateRepository
	 * @param capacityChannelService
	 * @param appParameterService
	 * @param auditService
	 */
	@Autowired
	public CapacityManagementServiceImpl(JwtUtils jwtUtils, CapacityTemplateRepo capacityTemplateRepo,
			CapacityChannelRepo capacityChannelRepo, CapacityTemplateTypeRepository capacityTemplateTypeRepository,
			CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository,
			CapacitySlotTypeRepository capacitySlotTypeRepository, ReferenceRepository referenceRepository,
			CapacitySlotRepository capacitySlotRepository,
			CapacityTemplateAndCapacityChannelRepository capacityTemplateAndCapacityChannelRepository,
			CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepository,
			CapacityChannelService capacityChannelService,
			@Qualifier(CapacityConstants.APP_PARAMETER_SERVICE) AppParameterService appParameterService,
			AuditService auditService) {
		super();
		this.jwtUtils = jwtUtils;
		this.capacityTemplateRepo = capacityTemplateRepo;
		this.capacityChannelRepo = capacityChannelRepo;
		this.capacityTemplateTypeRepository = capacityTemplateTypeRepository;
		this.capacityTemplateAndBusinessDateRepository = capacityTemplateAndBusinessDateRepository;
		this.capacitySlotTypeRepository = capacitySlotTypeRepository;
		this.referenceRepository = referenceRepository;
		this.capacitySlotRepository = capacitySlotRepository;
		this.capacityTemplateAndCapacityChannelRepository = capacityTemplateAndCapacityChannelRepository;
		this.capacityModelAndCapacityTemplateRepository = capacityModelAndCapacityTemplateRepository;
		this.capacityChannelService = capacityChannelService;
		this.appParameterService = appParameterService;
		this.auditService = auditService;
	}

	/**
	 * This method is to get list of Capacity Templates from database along with
	 * list of channel data from CapacityTemplate,
	 * CapacityTemplateAndCapacityChannel , CapacitySlot ,CapacityChannel entities
	 * and mapping the capacity channel mapper for reference data Here validating
	 * the concept Id ,if concept Id is empty then error message is thrown.
	 * 
	 * @return ResponseEntity response containing all list of 
	 * 							capacity Template.
	 * 
	 */
	@Override
	@Cacheable(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key= CapacityConstants.COMBINE_CAPACITY_TEMPLATE_CACHE_KEY)
	public CapacityResponse getAllCapacityTemplates(Boolean isRefDataReq, String conceptId ) {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		if (StringUtils.isBlank(RequestContext.getConcept())) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4421));
			applicationErrors.raiseExceptionIfHasErrors();
		}
		BigInteger concepId = new BigInteger(conceptId);
		List<CapacityTemplateEntity> capacityTemplateEntities = capacityTemplateRepo.findByConceptId(concepId);
		List<CapacityTemplate> capacityTemplates = new ArrayList<>();
		capacityTemplateEntities.stream().filter(Objects::nonNull).forEach(capacityTemplateEntity -> {
			List<Channel> channels = capacityTemplateMapper.getCapacityTemplateChannels(capacityTemplateEntity);
			List<CapacitySlotEntity> capacitySlots = capacityTemplateEntity.getCapacitySlots();
			MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
			Set<String> channelIds = new HashSet<>();
			Map<String, String> channelNames = new HashMap<>();
			capacityTemplateMapper.mapCapacitySlots(capacitySlots, channelSlotDetails, channelIds, channelNames);
			List<SlotChannel> slotChannels = capacityTemplateMapper.mapSlotChannels(channelSlotDetails, channelIds, channelNames);
			mapCapacityTemplateModel(capacityTemplates, capacityTemplateEntity, channels, slotChannels);
		});
		List<ReferenceDatum> referenceData = getReferenceDataBasedOnIsRefDataReq(isRefDataReq);
		return new CapacityResponse(capacityTemplates, referenceData);
	}

	/**
	 * This method is used to retrieve reference data based on the boolean
	 * value of isRefDataReq passed in the parameters.
	 * 
	 * @param isRefDataReq boolean class containing the value of 
	 * 							if reference data is required.
	 * 
	 * @param capacityTemplateEntities list of entity class containing the
	 * 						value of Capacity Template.
	 * 
	 * @return List<ReferenceDatum> list of model class containing the 
	 * 						value of reference data.
	 */
	private List<ReferenceDatum> getReferenceDataBasedOnIsRefDataReq(Boolean isRefDataReq) {
		List<ReferenceDatum> referenceData = new ArrayList<>();
		if(null == isRefDataReq || isRefDataReq) {
			ReferenceDatum refData = capacityChannelService.getReferenceData();
			referenceData = Collections.singletonList(refData);
		}
		return referenceData;
	}

	/**
	 * 
	 * This method is used to map slot details ,slot channels to capacity template
	 * model Here by using mapstruct capacity template entities are mapped to
	 * capacity template
	 * 
	 * @param capacityTemplateModels list of model class containing the detail of
	 * 						Capacity Template models.
	 * 
	 * @param capacityTemplateEntity entity class containing the detail of capacity 
	 * 						template.
	 * 
	 * @param channels list of model class containing the detail of channel.
	 * 
	 * @param slotChannels list of model class containing the detail of slot
	 * 							channels.
	 */

	private void mapCapacityTemplateModel(List<CapacityTemplate> capacityTemplateModels,
			CapacityTemplateEntity capacityTemplateEntity, List<Channel> channels, List<SlotChannel> slotChannels) {
		CapacityTemplate capacityTemplateModel = capacityTemplateMapper.map(capacityTemplateEntity);
		capacityTemplateModel.setCapacityTemplateType(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm());
		capacityTemplateModel.setSlotStartTime(String.valueOf(capacityTemplateEntity.getStartTime()));
		capacityTemplateModel.setSlotEndTime(String.valueOf(capacityTemplateEntity.getEndTime()));
		if(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS)) {
			capacityTemplateMapper.mapToCapacityTemplateFromEntity(capacityTemplateEntity, capacityTemplateModel);
		}
		else if(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES)) {
			List<BusinessDate> datesAssigned = new ArrayList<>();
			capacityTemplateEntity.getCapacityTemplateAndBusinessDates().stream().filter(Objects::nonNull).forEach(d -> {
				BusinessDate date = new BusinessDate();
				date.setDate(DateUtil.dateToString(d.getId().getBusinessDate()));
				datesAssigned.add(date);
			});
			capacityTemplateModel.setBusinessDate(datesAssigned);
		}
		capacityTemplateModel.setSlotChannels(slotChannels);
		capacityTemplateModel.setChannels(channels);
		capacityTemplateModels.add(capacityTemplateModel);
	}

	/**
	 * Method is used for CREATE operation.First it stores the
	 * CapacityTemplateEntity based on the value of CapacityTypeId the data of days
	 * or dates is mapped for dates table need to create
	 * CapacityTemplateAndBussinessEntity and store its value ,the channelId is
	 * required to create CapacityTemplateAndCapacityChannel along with the id of
	 * created CapacityTemplateEntity based on number of ChannelId is passed number
	 * of entities are created and stored ,the list of slots is passed with respect
	 * to channelId, based on the slotList the CapacitySlotEntities are created for
	 * respective channelTemplateId, ChannelId, CapacitySlotTypeId is created
	 * 
	 * @param createCapacityTemplateRequest request class containing detail of
	 * 								Capacity Template to be created.
	 * 
	 * @param accessToken  Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 * 
	 * @return CreateTemplateResponse response containing value of Capacity
	 * 									Template created.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
	public CreateTemplateResponse createTemplate(@Valid CreateCapacityTemplateRequest templateRequest,
			String accessToken) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityTemplateTypeEntity templateType = capacityTemplateTypeRepository
				.findByCapacityTemplateTypeNm(templateRequest.getTemplateTypeName());
		CapacityTemplateEntity templateEntity = capacityTemplateMapper.mapToTemplate(templateRequest, templateType,
				createdBy, dateTime);
		if(templateRequest.getTemplateTypeName().equals(CapacityConstants.DAYS) && templateEntity.getExpiryDate() ==  null) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4001),CapacityConstants.EXPIRY_DATE);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		CapacityTemplateEntity createdTemplateEntity = capacityTemplateRepo.save(templateEntity);
		List<BusinessDate> responseDate = new ArrayList<>();
		if (CapacityConstants.DATES.equals(templateType.getCapacityTemplateTypeNm())) {
			List<BusinessDate> dateList = templateRequest.getBusinessDates();
			dateList.stream().forEach(t -> {
				CapacityTemplateAndBusinessDateEntity templateDate = capacityTemplateMapper
						.mapToBusinessDate(createdTemplateEntity, t, createdBy, dateTime);
				CapacityTemplateAndBusinessDateEntity savedBusinessDateEntity = capacityTemplateAndBusinessDateRepository
						.save(templateDate);
				BusinessDate resDate = new BusinessDate();
				resDate.setDate(savedBusinessDateEntity.getId().getBusinessDate().toString());
				responseDate.add(resDate);
			});
		}
		List<SlotChannel> responseChannelList = new ArrayList<>();
		List<SlotChannel> slotChannelList = templateRequest.getSlotChannels();
		slotChannelList.stream().forEach(t -> {
			Optional<ReferenceEntity> reference = referenceRepository.findById(CapacityConstants.BIG_INT_CONSTANT);
			Optional<CapacityChannelEntity> channelEntity = capacityChannelRepo.findById(t.getChannelId());
			CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = capacityTemplateMapper
					.mapToTemplateAndChannelEntity(createdTemplateEntity, channelEntity, t, createdBy, dateTime);
			CapacityTemplateAndCapacityChannelEntity savedChannelEntity = capacityTemplateAndCapacityChannelRepository
					.save(capacityTemplateAndCapacityChannelEntity);
			SlotChannel responseChannel = new SlotChannel();
			responseChannel.setChannelId(savedChannelEntity.getId().getCapacityChannelId());
			responseChannel.setIsSelectedFlag(CapacityConstants.Y);
			List<SlotDetail> responseDetail = new ArrayList<>();
			List<SlotDetail> slotDetailList = t.getSlotDetails();
			slotDetailList.stream().forEach(s -> {
				Optional<CapacitySlotTypeEntity> slotTypeEntity = capacitySlotTypeRepository
						.findById(new BigInteger(s.getSlotTypeId()));
				CapacitySlotEntity slotEntity = capacityTemplateMapper.mapToSlot(createdTemplateEntity, reference,
						slotTypeEntity, channelEntity, t, s, createdBy);
				CapacitySlotEntity savedSlot = capacitySlotRepository.save(slotEntity);
				SlotDetail responseSlot = capacityTemplateMapper.mapToResponseSlot(savedSlot, s);
				responseDetail.add(responseSlot);
			});
			responseChannel.setSlotDetails(responseDetail);
			responseChannelList.add(responseChannel);
		});
		if(null != createdTemplateEntity.getCapacityTemplateNm()) {
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.INSERT, null, createdTemplateEntity, createdBy);
		}
		return capacityTemplateMapper.mapToCreateTemplateResponse(createdTemplateEntity, responseDate,
				responseChannelList, templateRequest);
	}

	/**
	 * Method is used to validate the CapacityTemplate name based the presence of
	 * name it returns boolean value
	 * 
	 * @param CapacityTemplateNm Capacity Template Name to be validated
	 * 					in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */

	@Override
	public boolean validateCapacityTemplateNm(String capacityTemplateNm) {
		CapacityTemplateEntity capacityTemplateEntity = capacityTemplateRepo
				.findByCapacityTemplateNm(capacityTemplateNm);
		return capacityTemplateEntity != null;
	}
	
	/**
	 * This method is used for DELETE operation.In this method the parameter 
	 * value is retrieved using the appParameter Service based on the value
	 * of parameter value soft delete or hard delete is performed.If the 
	 * appParameter entity value is null applicationError is raised.Then
	 * the capacityTemplate entity value to be deleted is fetched using the
	 * template Id passed in the parameter then the capacityTemaplate entity
	 * certain required values are modified.If the value of parameter value 
	 * of appParameter entity is Y then soft delete is performed the modified 
	 * capacityTemplate value with isDeletedFlag as Y is saved to database.If 
	 * the value of parameter of appParameter entity is N then hard delete is 
	 * performed the CapacityTemplate with related data is deleted from the 
	 * database.
	 * 
	 * @param departmentListId Department List Id of department List to be 
	 * 							deleted.
	 * 
	 * @param deletedFlag Deleted flag detail for the department list entity.
	 * 
	 * @param userDetail information of user operating on the delete action.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@Override
	@Transactional
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key = CapacityConstants.CAPACITY_TEMPLATE_CACHE_KEY),
            @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
	public String deleteByTemplateId(String templateId, String deletedFlag, String userDetail)
			throws JsonProcessingException {
		
		ApplicationErrors applicationErrors = new ApplicationErrors();
		
		AppParameterEntity appParameterEntity = appParameterService
				.findByParameterName(CapacityConstants.CAPACITY_SOFT_DELETE);
		if (appParameterEntity == null) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_5000),
					CapacityConstants.CAPACITY_CHANNEL_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		CapacityTemplateEntity capacityTemplateEntity = getByCapacityTemplateIdAndIsDeletedFlag(new BigInteger(templateId));
		capacityTemplateEntity.setIsDeletedFlg(CapacityConstants.Y);
		capacityTemplateEntity.setLastModifiedBy(userDetail);
		capacityTemplateEntity.setLastModifiedDatetime(Instant.now());
		if (appParameterEntity != null
				&& CharacterConstants.Y.getCode().toString().equals(appParameterEntity.getParameterValue())) {
			capacityTemplateRepo.save(capacityTemplateEntity);
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.DELETE_SOFT, null, capacityTemplateEntity, userDetail);
		}
		else if (appParameterEntity != null
				&& CharacterConstants.N.getCode().toString().equals(appParameterEntity.getParameterValue())) {
			if(capacityTemplateEntity.getCapacityTemplateId() != null) {
				capacityTemplateAndBusinessDateRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
				capacityTemplateAndCapacityChannelRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
				capacitySlotRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
				capacityTemplateRepo.deleteById(capacityTemplateEntity.getCapacityTemplateId());
			}
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.DELETE_HARD, null, capacityTemplateEntity, userDetail);
		}
		return capacityTemplateEntity.getCapacityTemplateNm();
	}
	
	/**
	 * This method is used to retrieve CapacityTemplate value using the Capacity
	 * template Id passed in the delete operation parameter for the value to be deleted
	 * and concept Id passed in the request header.if the value is not present for the 
	 * given CapacityTemplateId applicationErrors is raised with certain error code. If
	 * the retrieved CapacityTempalate value has isDeletedFlg value as Y applicationErrors 
	 * is raised with certain error code.
	 * 
	 * 
	 * @param templateId BigInteger containing the value of template Id of the Capacity 
	 * 									Template Entity.
	 * 
	 * @return CapacityTemplateEntity returning entity class containing the detail of
	 * 						capacity template entity retrieved by template id.
	 */
	private CapacityTemplateEntity getByCapacityTemplateIdAndIsDeletedFlag(BigInteger templateId) {
		
		ApplicationErrors applicationErrors = new ApplicationErrors();
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		Optional<CapacityTemplateEntity> dbTemplateEntityOptional = capacityTemplateRepo.findByCapacityTemplateIdAndConceptId(templateId, new BigInteger(RequestContext.getConcept()));
		if(dbTemplateEntityOptional.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		if(dbTemplateEntityOptional.isPresent()) {
			capacityTemplateEntity = dbTemplateEntityOptional.get();
		}
		
		if (!(CapacityConstants.N.equals(capacityTemplateEntity.getIsDeletedFlg()))) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		return capacityTemplateEntity;
		
	}

	/**
	 * This method is validate if the CapacityTemplate is assigned to the CapacityTemplate Model 
	 * in the database it checks if there is any templateId is present in capacityModelAndCapacityTemplate
	 * table for the validation.
	 * 
	 * @param templateId Template Id of Capacity template to be validated 
	 * 						in database.
	 * 
	 * @param applicationErrors error class to raise error if validation
	 * 						fails.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateCapacityTemplateId(String templateId, ApplicationErrors applicationErrors) {

		Optional<CapacityTemplateEntity> dbTemplateValue = capacityTemplateRepo.findById(new BigInteger(templateId));
		if(dbTemplateValue.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_TEMPLATE_ID);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		List<CapacityModelAndCapacityTemplateEntity> dbModelAndTemplate = new ArrayList<>();
		if(dbTemplateValue.isPresent()) {
			dbModelAndTemplate = capacityModelAndCapacityTemplateRepository.findByCapacityTemplate(dbTemplateValue.get());
		}
		if(!dbModelAndTemplate.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4501), dbModelAndTemplate.get(0).getCapacityTemplate().getCapacityTemplateNm(),
					dbModelAndTemplate.get(0).getCapacityModel().getCapacityModelNm());
		}

		return true;
	}
	
	/**
	* This method is validate existing CapacityTemplate name in the database for the
	* validation based on the value of templateNm and template Id.
	*
	*
	* @param capacityTemplateNm capacity Template Name of capacity template to be
	* 							validated.
	* 
	* @param templateId Template Id of capacity template.
	* 
	* @return boolean returns the boolean value based on the condition.
	*/
	@Override
	public boolean validateCapacityTemplateNmForCreate(String capacityTemplateNm, String templateId) {
		boolean isCapacityTemplateNameExists = true;
		if (StringUtils.isNotBlank(templateId)) {
			Optional<CapacityTemplateEntity> template = capacityTemplateRepo.findById(new BigInteger(templateId));
			if (template.isPresent()) {
				// Here checking the templateName against the templateId it means no
				// modification done from UI
				if (template.filter(t -> t.getCapacityTemplateNm().equalsIgnoreCase(capacityTemplateNm)).isPresent()) {
					isCapacityTemplateNameExists = false;
				} else {
					isCapacityTemplateNameExists = validateCapacityTemplateNm(capacityTemplateNm);
				}
			}
		} else {
			isCapacityTemplateNameExists = validateCapacityTemplateNm(capacityTemplateNm);
		}
		return isCapacityTemplateNameExists;
	}

	/**
	 * Method is for UPDATE operation.First it stores the
	 * CapacityTemplateEntity based on the value of CapacityTypeId the data of days
	 * or dates is mapped for dates table need to update
	 * CapacityTemplateAndBussinessEntity and store its value ,the list of slots is passed with respect
	 * to channelId, based on the slotList the CapacitySlotEntities are updated for
	 * respective channelTemplateId, ChannelId, CapacitySlotTypeId.
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be updated.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API.
	 *                      
	 * @param templateId Template Id of Capacity template to be updated.
	 * 
	 * @return CreateTemplateResponse response class containing detail of 
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) }, put = {
            @CachePut(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key = CapacityConstants.CAPACITY_TEMPLATE_CACHE_KEY) })
	public CreateTemplateResponse updateCapacityTemplate(@Valid CreateCapacityTemplateRequest templateRequest,
			String accessToken, BigInteger templateId) {
		CreateTemplateResponse response = new CreateTemplateResponse();
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		Optional<CapacityTemplateEntity> capacityTemplateEntity = capacityTemplateRepo.findById(templateId);
		capacityTemplateEntity.ifPresent(existingTemplate ->{
			capacityTemplateAndBusinessDateRepository.deleteAllBycapacityTemplate(existingTemplate);
			capacitySlotRepository.deleteAllBycapacityTemplate(existingTemplate);
			capacityTemplateAndCapacityChannelRepository.deleteAllBycapacityTemplate(existingTemplate);
			mapExistingTemplateAndUpdate(templateRequest, createdBy,dateTime, existingTemplate);
		});
		Optional<CapacityTemplateEntity> templateData = capacityTemplateRepo.findById(templateId);
		if (templateData.isPresent()) {
			CapacityTemplateEntity responseCapacityTemplateEntity = templateData.get();
			response = mapUpdateResponse(templateRequest, responseCapacityTemplateEntity);
		}
		return response;
	}

	/**
	 * This method is for mapping the response for updated template and retrieving the template details
	 * based on template Id, mapping business dates,slot details,capacity slots,channel slot details 
	 * based on channel Id
	 * 
	 * @param templateRequest request class containing the value of capacity template 
	 * 									to be created.
	 * 
	 * @param responseCapacityTemplateEntity entity class containing the value of
	 * 									capacity template.
	 * 
	 * @return CreateTemplateResponse returning response class containing the 
	 * 									value of created capacity template.
	 */
	public CreateTemplateResponse mapUpdateResponse(CreateCapacityTemplateRequest templateRequest,
			CapacityTemplateEntity responseCapacityTemplateEntity) {
		String templateTypeName = templateRequest.getTemplateTypeName();
		CreateTemplateResponse response = capacityTemplateMapper.mapCreateResponse(responseCapacityTemplateEntity);
		List<CapacitySlotEntity> capacitySlots = responseCapacityTemplateEntity.getCapacitySlots();
		MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
		Set<String> channelIds = new HashSet<>();
		Map<String, String> isSelectedFlags = new HashMap<>();
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntites = responseCapacityTemplateEntity
				.getCapacityTemplateAndCapacityChannels();
		capacityTemplateAndCapacityChannelEntites.stream().filter(Objects::nonNull).forEach(ctc -> {
			CapacityTemplateAndCapacityChannelPK pk = ctc.getId();
			isSelectedFlags.put(String.valueOf(pk.getCapacityChannelId()), CapacityConstants.Y);
		});
		mapCapacitySlotsResponseForUpdate(capacitySlots, channelSlotDetails, channelIds);
		List<SlotChannel> slotChannels = mapSlotChannelsResponseForUpdate(channelSlotDetails, channelIds,isSelectedFlags);
		if (CapacityConstants.DATES.equals(templateTypeName)) {
			response.setBusinessDates(templateRequest.getBusinessDates());
		}
		response.setSlotChannels(slotChannels);
		capacityTemplateMapper.mapTemplateTypeResponse(response, responseCapacityTemplateEntity);
		return response;
	}
	
	/**
     * This method is to  retrieve the slot details based on channel Id and constructing the response 
     * based on channel slot details
	 * 
	 * @param capacitySlots list of entity class containing the value of capacity
	 * 							slot details.
	 * 
	 * @param channelSlotDetails model class containing the value of channel
	 * 							slot details.
	 * 
	 * @param channelIds set of strings containing the value of channelIds.
	 */
	private void mapCapacitySlotsResponseForUpdate(List<CapacitySlotEntity> capacitySlots,
			MultiValuedMap<String, SlotDetail> channelSlotDetails, Set<String> channelIds) {
		capacitySlots.stream().filter(Objects::nonNull).forEach(cs -> {
			String channelId = String.valueOf(cs.getCapacityChannel().getCapacityChannelId());
			channelIds.add(channelId);
			SlotDetail slotDetail = capacityTemplateMapper.mapToUpdateCapacityTemplateSlots(cs);
			channelSlotDetails.put(String.valueOf(cs.getCapacityChannel().getCapacityChannelId()), slotDetail);
		});
	}
	
	/**
	 * This method is to  retrieve the channel slot details and constructing the response based on
	 * channel slot details 
	 * 
	 * @param channelSlotDetails model class containing the value of channel
	 * 							slot details.
	 * 
	 * @param channelIds set of strings containing the value of channelIds.
	 * 
	 * @param isSeletedFlags Map of strings containing the value of isSelected
	 * 							flags.
	 * 
	 * @return List<SlotChannel> returning the list of model class containing the
	 * 							value of slot channel mapped.
	 */
	private List<SlotChannel> mapSlotChannelsResponseForUpdate(MultiValuedMap<String, SlotDetail> channelSlotDetails,
			Set<String> channelIds, Map<String, String> isSeletedFlags) {
		return capacityTemplateMapper.mapToUpdateSlotChannelResponse(channelSlotDetails, channelIds, isSeletedFlags);
	}

	/**
	 * This method is to update data based on the given template Id ,first retrieve the template details 
	 * and setting the values to the same and saving into the database, if template type name is DATES then updating 
	 * the business dates else updating the days
	 * 
	 * @param templateRequest model class containing the value of Capacity Template
	 * 								to be updated.
	 * 
	 * @param createdBy information of user operating on the update action.
	 * 
	 * @param dateTime Instant value containing the value of dateTime.
	 * 
	 * @param existingTemplate entity class containing the value of entity
	 * 
	 */
	private void mapExistingTemplateAndUpdate(CreateCapacityTemplateRequest templateRequest, String createdBy,
			Instant dateTime, CapacityTemplateEntity existingTemplate) {
		existingTemplate.setCapacityTemplateNm(templateRequest.getCapacityTemplateName());
		existingTemplate.setLastModifiedBy(createdBy);
		existingTemplate.setLastModifiedDatetime(dateTime);
		String templateTypeName = templateRequest.getTemplateTypeName();
		if (CapacityConstants.DAYS.equals(templateTypeName)) {
			existingTemplate.setEffectiveDate(DateUtil.stringToDate(templateRequest.getEffectiveDate()));
			existingTemplate.setExpiryDate(DateUtil.stringToDate(templateRequest.getExpiryDate()));
			capacityTemplateMapper.mapTemplateDaysFromTemplateCreateUpdateRequest(templateRequest, existingTemplate);
		}
		else {
			existingTemplate.setEffectiveDate(DateUtil.stringToDate(CapacityConstants.BLANK));
			existingTemplate.setExpiryDate(DateUtil.stringToDate(CapacityConstants.BLANK));
		}
		existingTemplate.setStartTime(LocalTime.parse(templateRequest.getSlotStartTime()));
		existingTemplate.setEndTime(LocalTime.parse(templateRequest.getSlotEndTime()));
		CapacityTemplateTypeEntity capacityTemplateTypeEntity = capacityTemplateTypeRepository
				.findByCapacityTemplateTypeNm(templateRequest.getTemplateTypeName());
		if (capacityTemplateTypeEntity != null) {
			existingTemplate.setCapacityTemplateType(capacityTemplateTypeEntity);
		}
		capacityTemplateRepo.save(existingTemplate);
		updateCapacitySlots(templateRequest, createdBy, dateTime, existingTemplate);
		if (CapacityConstants.DATES.equals(templateTypeName)) {
			capacityTemplateMapper.setTemplateDaysToNullValue(existingTemplate);
			updateBusinessDates(templateRequest, createdBy, dateTime, existingTemplate);
		}
	}

	/**
	 * This method is to update the capacity slots details based on capacity channel Ids
	 * 
	 * @param templateRequest model class containing the value of Capacity Template
	 * 								to be updated.
	 * 
	 * @param createdBy information of user operating on the update action.
	 * 
	 * @param dateTime Instant value containing the value of dateTime.
	 * 
	 * @param existingTemplate entity class containing the value of entity
	 */
	private void updateCapacitySlots(CreateCapacityTemplateRequest templateRequest, String createdBy, Instant dateTime,
			CapacityTemplateEntity existingTemplate) {
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntityList = new ArrayList<>();
		MultiValuedMap<BigInteger, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
		if (CollectionUtils.isNotEmpty(templateRequest.getSlotChannels())) {
			templateRequest.getSlotChannels().forEach(slotChannel -> {
				BigInteger channelId = slotChannel.getChannelId();
				slotChannel.getSlotDetails()
						.forEach(slotDetail -> channelSlotDetails.put(channelId, slotDetail));
				Optional<CapacityChannelEntity> channelEntity = capacityChannelRepo.findById(channelId);
				if (channelEntity.isPresent()) {
					CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = capacityTemplateMapper
							.mapToTemplateAndChannelEntity(existingTemplate, channelEntity, slotChannel, createdBy, dateTime);
					capacityTemplateAndCapacityChannelEntityList.add(capacityTemplateAndCapacityChannelEntity);
					List<CapacitySlotEntity> newSlotEntities = new ArrayList<>();
					Collection<SlotDetail> slotDetailsReq = channelSlotDetails.get(channelId);
					slotDetailsReq.stream().filter(Objects::nonNull).forEach(slotDetailReq -> {
						CapacitySlotEntity capacitySlotEntity = mapSlotEntity(createdBy, dateTime, existingTemplate,
								channelEntity, slotDetailReq);
						newSlotEntities.add(capacitySlotEntity);
					});
					capacitySlotRepository.saveAll(newSlotEntities);
				}
			});
		}
		capacityTemplateAndCapacityChannelRepository.saveAll(capacityTemplateAndCapacityChannelEntityList);
	}

	/**
	 * This method is to map the slot entity 
	 * 
	 * @param createdBy information of user operating on the update action.
	 * 
	 * @param dateTime Instant value containing the value of dateTime.
	 * 
	 * @param existingTemplate entity class containing the value of entity.
	 * 
	 * @param channelEntity entity class containing the value of capacity
	 * 								channel entity.
	 * 
	 * @param slotDetailReq model class containing the value of slot detail.
	 * 
	 * @return CapacitySlotEntity returning the value of capacity slot
	 * 					entity.
	 */
	private CapacitySlotEntity mapSlotEntity(String createdBy, Instant dateTime,
			CapacityTemplateEntity existingTemplate, Optional<CapacityChannelEntity> channelEntity,
			SlotDetail slotDetailReq) {
		Optional<CapacitySlotTypeEntity> capacitySlotTypeEntity = capacitySlotTypeRepository
				.findById(new BigInteger(slotDetailReq.getSlotTypeId()));
		Optional<ReferenceEntity> reference = referenceRepository
				.findById(CapacityConstants.BIG_INT_CONSTANT);
		return capacityTemplateMapper.mapToCapacitySlotEntity(createdBy, dateTime, existingTemplate,
				channelEntity, slotDetailReq, capacitySlotTypeEntity, reference);
	}
	
	/**
	 * This method is to update the existing business dates based on capacityTemplateAndBusinessDate Id
	 * and saving new dates
	 * 
	 * @param templateRequest Request class with value of capacity template to be updated.
	 * 
	 * @param createdBy information of user operating on the update action.
	 * 
	 * @param dateTime Instant value containing the value of dateTime.
	 * 
	 * @param existingTemplate entity class with value of existing capacity
	 * 							template.
	 */
	private void updateBusinessDates(CreateCapacityTemplateRequest templateRequest, String createdBy, Instant dateTime,
			CapacityTemplateEntity existingTemplate) {
		List<BusinessDate> bList = templateRequest.getBusinessDates();
		List<CapacityTemplateAndBusinessDateEntity> list = new ArrayList<>();
		bList.stream().filter(Objects::nonNull).forEach(t -> {
			CapacityTemplateAndBusinessDateEntity templateDate = capacityTemplateMapper
					.mapToBusinessDate(existingTemplate, t, createdBy, dateTime);
			list.add(templateDate);
		});
		capacityTemplateAndBusinessDateRepository.saveAll(list); 
	}
	
	/**
	 * This method is to validate days if same days are given for updating then it will throw error it will
	 * first check the expiry and effective dates if it is present it will throw error
	 * otherwise it will add the template in capacity model.
	 * If dates are selected validation is made for dates if value is already existing in other 
	 * Capacity Template under the same Capacity Model.If present will application Error will 
	 * be raised.
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateCapacityModelBusinessDates(CreateCapacityTemplateRequest createCapacityTemplateRequest, String templateId) {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		List<CapacityModelAndCapacityTemplateEntity> list = capacityModelAndCapacityTemplateRepository.findAll();
		List<BigInteger> assignedTemplateId = extractingAllAssignedTemplateId(list);
		if(assignedTemplateId.contains(new BigInteger(templateId))) {
			list = extractingAssignedTemplatesToBeComapred(templateId, list);
			String templateTypeName = createCapacityTemplateRequest.getTemplateTypeName();
			if (CapacityConstants.DAYS.equalsIgnoreCase(templateTypeName)) {
				validateEffectiveDateAndExpiryDate(createCapacityTemplateRequest, applicationErrors);
				return validationForTemplateDays(createCapacityTemplateRequest, list);
			} else if (CapacityConstants.DATES.equalsIgnoreCase(templateTypeName)) {
				return validateForTemplateDates(createCapacityTemplateRequest, list);
			}
		}
		return false;
	}

	/**
	 * This method is to validate effective date and expiry date for days
	 * capacity template.
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @param applicationErrors error class used to throw errors if validation fails.
	 */
	private void validateEffectiveDateAndExpiryDate(CreateCapacityTemplateRequest createCapacityTemplateRequest,
			ApplicationErrors applicationErrors) {
		if(createCapacityTemplateRequest.getEffectiveDate() == null) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4001),CapacityConstants.EFFECTIVE_DATE);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		if(createCapacityTemplateRequest.getExpiryDate() == null) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4001),CapacityConstants.EXPIRY_DATE);
			applicationErrors.raiseExceptionIfHasErrors();
		}
	}

	/**
	 * This method is used to validate days if any similar day format template
	 * is present in same capacity model.
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @param list of entity class containing the value of Capacity Model And Capacity 
	 * 							Template.
	 * 
	 * @return boolean value is returned based on condition.
	 */
	private boolean validateForTemplateDates(CreateCapacityTemplateRequest createCapacityTemplateRequest,
			List<CapacityModelAndCapacityTemplateEntity> list) {
		return list.stream().filter(Objects::nonNull)
				.filter(tempType -> tempType.getCapacityTemplate().getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES))
				.anyMatch(t -> t.getCapacityTemplate()
				.getCapacityTemplateAndBusinessDates()
				.stream()
				.filter(Objects::nonNull)
				.anyMatch(s -> {
					LocalDate businessDate = DateUtil.convertDatetoLocalDate(s.getId().getBusinessDate());
					return createCapacityTemplateRequest.getBusinessDates().stream().filter(Objects::nonNull)
							.anyMatch(bDate -> {
								LocalDate reqBusinessDate = DateUtil.convertStringtoLocalDate(bDate.getDate());
								boolean isSameBusinessDate = false;
								if (businessDate.equals(reqBusinessDate)) {
									isSameBusinessDate = true;
								}
								return isSameBusinessDate;
							});
				}));
	}

	/**
	 * This method is used to get all other templates assigned to capacity model.
	 * 
	 * @param templateId String containing value of template Id to be updated.
	 * 
	 * @param list of entity class containing the value of Capacity Model And Capacity 
	 * 							Template.
	 * 
	 * @return List<CapacityModelAndCapacityTemplateEntity> list of entity class containing 
	 * 						the value of Capacity Model And Capacity Template.
	 */
	private List<CapacityModelAndCapacityTemplateEntity> extractingAssignedTemplatesToBeComapred(String templateId,
			List<CapacityModelAndCapacityTemplateEntity> list) {
		List<CapacityModelAndCapacityTemplateEntity> assignedModel = list.stream()
				.filter(templateAssigned -> templateAssigned.getCapacityTemplate().getCapacityTemplateId().equals(new BigInteger(templateId)))
				.collect(Collectors.toList());
		list = list.stream()
				.filter(modelAssignedTemplate -> modelAssignedTemplate.getCapacityModel().getCapacityModelId().equals(assignedModel.get(0).getCapacityModel().getCapacityModelId()))
				.filter(template -> !template.getCapacityTemplate().getCapacityTemplateId().equals(new BigInteger(templateId)))
				.collect(Collectors.toList());
		return list;
	}

	/**
	 * This method is used get all assigned template Id.
	 * 
	 * @param list of entity class containing the value of Capacity Model And Capacity 
	 * 							Template.
	 * 
	 * @return List<BigInteger> List of BigInteger containing the data of assigned
	 * 							template Id.
	 */
	private List<BigInteger> extractingAllAssignedTemplateId(List<CapacityModelAndCapacityTemplateEntity> list) {
		List<BigInteger> assignedTemplateId = new ArrayList<>();
		list.stream().forEach( assignedId -> assignedTemplateId.add(assignedId.getId().getCapacityTemplateId()));
		return assignedTemplateId;
	}

	/**
	 * This method is to validate if capacity templates assigned to capacity
	 * template model are overlapping over the same days.
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @param list of entity class containing the value of capacity model and capacity
	 * 								template relation.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validationForTemplateDays(CreateCapacityTemplateRequest createCapacityTemplateRequest, List<CapacityModelAndCapacityTemplateEntity> list) {
		return list.stream().filter(Objects::nonNull)
				.filter(templateType -> templateType.getCapacityTemplate().getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS))
				.anyMatch(t -> {
			LocalDate dbTemplateEffectiveDate = DateUtil
					.convertDatetoLocalDate(t.getCapacityTemplate().getEffectiveDate());
			LocalDate dbTemplateExpDate = DateUtil.convertDatetoLocalDate(t.getCapacityTemplate().getExpiryDate());
			LocalDate effectiveDateReq = DateUtil
					.convertStringtoLocalDate(createCapacityTemplateRequest.getEffectiveDate());
			LocalDate expReq = DateUtil.convertStringtoLocalDate(createCapacityTemplateRequest.getExpiryDate());
			if (effectiveDateReq.isBefore(dbTemplateExpDate) && expReq.isAfter(dbTemplateEffectiveDate)) {
				CapacityTemplateEntity template=t.getCapacityTemplate();
				if (CapacityConstants.DAYS.equalsIgnoreCase(template.getCapacityTemplateType().getCapacityTemplateTypeNm())) {
				 return validateDays(createCapacityTemplateRequest,template);
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
	 * @param createCapacityTemplateRequest request class with value of 
	 * 				capacity template to be updated contains all day flags 
	 * 				to be validated.
	 * 
	 * @param template entity class with value of other capacity template 
	 * 				assigned to same model for comparison.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateDays(CreateCapacityTemplateRequest createCapacityTemplateRequest,
			CapacityTemplateEntity template) {
		String sunDay = createCapacityTemplateRequest.getSunDay();
		String monDay = createCapacityTemplateRequest.getMonDay();
		String tueDay = createCapacityTemplateRequest.getTueDay();
		String wedDay = createCapacityTemplateRequest.getWedDay();
		String thuDay = createCapacityTemplateRequest.getThuDay();
		String friDay = createCapacityTemplateRequest.getFriDay();
		String satDay = createCapacityTemplateRequest.getSatDay();

		return validateBusinessDay(template.getSunFlg(), sunDay) || validateBusinessDay(template.getMonFlg(), monDay)
				|| validateBusinessDay(template.getTueFlg(), tueDay)
				|| validateBusinessDay(template.getWedFlg(), wedDay)
				|| validateBusinessDay(template.getThuFlg(), thuDay)
				|| validateBusinessDay(template.getFriFlg(), friDay)
				|| validateBusinessDay(template.getSatFlg(), satDay);
	}
	
	/**
	 * @param template
	 * @param sunDayFlg
	 * @return
	 */
	private boolean validateBusinessDay(String reqFlg, String resFlg) {
		return StringUtils.equalsIgnoreCase(CapacityConstants.Y, resFlg)
				&& StringUtils.equalsIgnoreCase(resFlg, reqFlg);
	}

	@Override
	public List<CapacityModel> getAllModelsRelatingToTemplateIdList(Set<BigInteger> templateIds) {
		Set<BigInteger> modelIdList = new HashSet<>();
		List<CapacityModel> capacityModelList = new ArrayList<>();
		List<CapacityTemplateEntity> templates = capacityTemplateRepo.findAllById(templateIds);
		templates.stream().forEach(template -> template.getCapacityModelAndCapacityTemplates().stream().forEach(model -> {
				if(!modelIdList.contains(model.getCapacityModel().getCapacityModelId())) {
					modelIdList.add(model.getCapacityModel().getCapacityModelId());
					CapacityModel capModel = new CapacityModel();
					capModel.setCapacityModelId(model.getCapacityModel().getCapacityModelId());
					capModel.setCapacityModelName(model.getCapacityModel().getCapacityModelNm());
					capacityModelList.add(capModel);
				}
			}));
		return capacityModelList;
	}
}
