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
import com.darden.dash.common.enums.AuditActionValues;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.model.DeleteResponseBodyFormat;
import com.darden.dash.common.service.AppParameterService;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.GlobalDataCall;
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
	
	private GlobalDataCall globalDataCall;
	
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
			AuditService auditService,GlobalDataCall globalDataCall) {
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
		this.globalDataCall = globalDataCall;
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
		BigInteger concepId = new BigInteger(conceptId);

		// fetching the list of capacity template entities within the concept.
		List<CapacityTemplateEntity> capacityTemplateEntities = capacityTemplateRepo.findByConceptId(concepId);
		List<CapacityTemplate> capacityTemplates = new ArrayList<>();
		capacityTemplateEntities.stream().filter(Objects::nonNull).forEach(capacityTemplateEntity -> {
			
			//mapping the channels related to capacityTemplateEntity to model class channels. 
			List<Channel> channels = capacityTemplateMapper.getCapacityTemplateChannels(capacityTemplateEntity);
			
			//fetching all the capacity slots related to capacity template entity. 
			List<CapacitySlotEntity> capacitySlots = capacityTemplateEntity.getCapacitySlots();
			MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
			Set<String> channelIds = new HashSet<>();
			Map<String, String> channelNames = new HashMap<>();
			
			//mapping capacity channel and slot details related to capacityTemplateEntity to channelSlotDetails and group it based on 
			//channel id.
			capacityTemplateMapper.mapCapacitySlots(capacitySlots, channelSlotDetails, channelIds, channelNames);
			
			//mapping all capacity channels and slots related to capacityTemplateEntity to list of slotChannels model class.
			List<SlotChannel> slotChannels = capacityTemplateMapper.mapSlotChannels(channelSlotDetails, channelIds, channelNames);
			
			//mapping the capacityTemplateEntity data to CapacityTemplate model class and adding it to list.
			mapCapacityTemplateModel(capacityTemplates, capacityTemplateEntity, channels, slotChannels);
		});
		//fetching all the capacityChannels based on the isRefDataReq flag.
		List<ReferenceDatum> referenceData = getReferenceDataBasedOnIsRefDataReq(isRefDataReq);
		
		//Setting the list of capacityTemplate and reference data to CapacityResponse model class.
		return new CapacityResponse(capacityTemplates, referenceData);
	}
	/**
	 * Method is used to get the CapacityTemplate buy id for the respective
	 * CapacityTemplate Info id.Used Builder Design pattern to create and set the
	 * CapacityTemplate object.**
	 *
	 * @param capacityTemplateId, Id of capacityTemplate
	 * @return capacityTemplate {@code capacityTemplate}
	 */
	@Override
	public CapacityTemplate getCapacityTemplateById(BigInteger capacityTemplateId) {
		ApplicationErrors applicationErrors = new ApplicationErrors();

		// fetching the capacity template entity by Id within the concept.
		Optional<CapacityTemplateEntity> findByCapacityTemplateIdAndConceptId = capacityTemplateRepo
				.findByCapacityTemplateIdAndConceptId(capacityTemplateId, new BigInteger(RequestContext.getConcept()));
		if (findByCapacityTemplateIdAndConceptId.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), "capacityTemplateId");
			applicationErrors.raiseExceptionIfHasErrors();
		}

		// mapping the channels related to capacityTemplateEntity to model class
		// channels.
		List<Channel> capacityTemplateChannels = capacityTemplateMapper
				.getCapacityTemplateChannels(findByCapacityTemplateIdAndConceptId.get());

		// fetching all the capacity slots related to capacity template entity.
		List<CapacitySlotEntity> capacitySlots = findByCapacityTemplateIdAndConceptId.get().getCapacitySlots();
		MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
		Set<String> channelIds = new HashSet<>();
		Map<String, String> channelNames = new HashMap<>();

		// mapping capacity channel and slot details related to capacityTemplateEntity
		// to channelSlotDetails and group it based on
		// channel id.
		capacityTemplateMapper.mapCapacitySlots(capacitySlots, channelSlotDetails, channelIds, channelNames);

		// mapping all capacity channels and slots related to capacityTemplateEntity to
		// list of slotChannels model class.
		List<SlotChannel> slotChannels = capacityTemplateMapper.mapSlotChannels(channelSlotDetails, channelIds,
				channelNames);

		// mapping the capacityTemplateEntity data to CapacityTemplate model class and
		// adding it to list.

		return mapTemplateModel(findByCapacityTemplateIdAndConceptId.get(), capacityTemplateChannels, slotChannels);

	}
	/**
	 * 
	 * This method is used to map slot details ,slot channels to capacity template
	 * model Here by using mapstruct capacity template entities are mapped to
	 * capacity template
	 * 
	 * @param capacityTemplateModels list of model class containing the detail of
	 *                               Capacity Template models.
	 * 
	 * @param capacityTemplateEntity entity class containing the detail of capacity
	 *                               template.
	 * 
	 * @param channels               list of model class containing the detail of
	 *                               channel.
	 * 
	 * @param slotChannels           list of model class containing the detail of
	 *                               slot channels.
	 */

	private CapacityTemplate mapTemplateModel(CapacityTemplateEntity capacityTemplateEntity, List<Channel> channels,
			List<SlotChannel> slotChannels) {

		// mapping capacityTemplateEntity to capacityTemplate model class.
		CapacityTemplate capacityTemplateModel = capacityTemplateMapper.map(capacityTemplateEntity);
		capacityTemplateModel
				.setCapacityTemplateType(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm());
		capacityTemplateModel.setSlotStartTime(String.valueOf(capacityTemplateEntity.getStartTime()));
		capacityTemplateModel.setSlotEndTime(String.valueOf(capacityTemplateEntity.getEndTime()));

		// mapping days related fields if templateTypeName is DAYS.
		if (capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm()
				.equals(CapacityConstants.DAYS)) {
			capacityTemplateMapper.mapToCapacityTemplateFromEntity(capacityTemplateEntity, capacityTemplateModel);
		}

		// mapping days related fields if templateTypeName is DATES.
		else if (capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm()
				.equals(CapacityConstants.DATES)) {
			List<BusinessDate> datesAssigned = new ArrayList<>();

			// multiple dates can be assigned to capacityTemplateEntity mapping all dates
			// related to capacityTemplateEntity.
			capacityTemplateEntity.getCapacityTemplateAndBusinessDates().stream().filter(Objects::nonNull)
					.forEach(d -> {
						BusinessDate date = new BusinessDate();
						date.setDate(DateUtil.dateToString(d.getId().getBusinessDate()));
						datesAssigned.add(date);
					});
			capacityTemplateModel.setBusinessDate(datesAssigned);
		}
		capacityTemplateModel.setSlotChannels(slotChannels);
		capacityTemplateModel.setChannels(channels);

		return capacityTemplateModel;
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
		
		//Fetching referenceData if isRefDataReq value is true.
		if(null == isRefDataReq || isRefDataReq) {
			
			//Calling the channel service to get all the channels within the concept.
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
		
		//mapping capacityTemplateEntity to capacityTemplate model class.
		CapacityTemplate capacityTemplateModel = capacityTemplateMapper.map(capacityTemplateEntity);
		capacityTemplateModel.setCapacityTemplateType(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm());
		capacityTemplateModel.setSlotStartTime(String.valueOf(capacityTemplateEntity.getStartTime()));
		capacityTemplateModel.setSlotEndTime(String.valueOf(capacityTemplateEntity.getEndTime()));
		
		//mapping days related fields if templateTypeName is DAYS.
		if(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS)) {
			capacityTemplateMapper.mapToCapacityTemplateFromEntity(capacityTemplateEntity, capacityTemplateModel);
		}
		
		//mapping days related fields if templateTypeName is DATES.
		else if(capacityTemplateEntity.getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES)) {
			List<BusinessDate> datesAssigned = new ArrayList<>();
			
			//multiple dates can be assigned to capacityTemplateEntity mapping all dates related to capacityTemplateEntity.
			capacityTemplateEntity.getCapacityTemplateAndBusinessDates().stream().filter(Objects::nonNull).forEach(d -> {
				BusinessDate date = new BusinessDate();
				date.setDate(DateUtil.dateToString(d.getId().getBusinessDate()));
				datesAssigned.add(date);
			});
			capacityTemplateModel.setBusinessDate(datesAssigned);
		}
		capacityTemplateModel.setSlotChannels(slotChannels);
		capacityTemplateModel.setChannels(channels);
		
		//adding mapped capacityTemplate to list.
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
		//fetching the user detail.
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		
		//fetching the capacityTemplateType.
		CapacityTemplateTypeEntity templateType = capacityTemplateTypeRepository
				.findByCapacityTemplateTypeNm(templateRequest.getTemplateTypeName());
		
		//mapping the requested data to CapacityTemplateEntity.
		CapacityTemplateEntity templateEntity = capacityTemplateMapper.mapToTemplate(templateRequest, templateType,
				createdBy, dateTime);
		
		//saving the CapacityTemplateEntity.
		CapacityTemplateEntity createdTemplateEntity = capacityTemplateRepo.save(templateEntity);
		List<BusinessDate> responseDate = new ArrayList<>();
		
		//if templateType is DATES creating and saving CapacityTemplateAndBusinessDateEntity. 
		if (CapacityConstants.DATES.equals(templateType.getCapacityTemplateTypeNm())) {
			
			//fetching all the business dates from request.
			List<BusinessDate> dateList = templateRequest.getBusinessDates();
			List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDateEntityList = new ArrayList<>();
			dateList.stream().forEach(t -> {
				//mapping to CapacityTemplateAndBusinessDateEntity for each business date.
				CapacityTemplateAndBusinessDateEntity templateDate = capacityTemplateMapper
						.mapToBusinessDate(createdTemplateEntity, t, createdBy, dateTime);
				
				//Adding CapacityTemplateAndBusinessDateEntity to list.
				capacityTemplateAndBusinessDateEntityList.add(templateDate);
				BusinessDate resDate = new BusinessDate();
				resDate.setDate(t.getDate());
				responseDate.add(resDate);
			});
			//Saving all the list of CapacityTemplateAndBusinessDateEntities.
			capacityTemplateAndBusinessDateRepository.saveAll(capacityTemplateAndBusinessDateEntityList);
		}
		List<SlotChannel> responseChannelList = new ArrayList<>();
		
		//fetching all the slots to be created from request.
		List<SlotChannel> slotChannelList = templateRequest.getSlotChannels();
		
		//fetching the reference entity.
		Optional<ReferenceEntity> reference = referenceRepository.findById(BigInteger.TEN);
		
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntityList = new ArrayList<>();
		
		//For all the requested channel and slots creating entities.
		slotChannelList.stream().forEach(t -> {
			
			//fetching the channel entity based on channel id passed for the slot in the request.
			Optional<CapacityChannelEntity> channelEntity = capacityChannelRepo.findById(t.getChannelId());
			
			//mapping request data to CapacityTemplateAndCapacityChannelEntity.
			CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = capacityTemplateMapper
					.mapToTemplateAndChannelEntity(createdTemplateEntity, channelEntity, t, createdBy, dateTime);
			
			//adding CapacityTemplateAndCapacityChannelEntity to list.
			capacityTemplateAndCapacityChannelEntityList.add(capacityTemplateAndCapacityChannelEntity);

			SlotChannel responseChannel = new SlotChannel();
			responseChannel.setChannelId(t.getChannelId());
			responseChannel.setIsSelectedFlag(CapacityConstants.Y);
			
			List<SlotDetail> responseDetail = new ArrayList<>();
			List<SlotDetail> slotDetailList = t.getSlotDetails();
			
			Map<String, Optional<CapacitySlotTypeEntity>> capacitySlotTypeMap = new HashMap<>();
			
			List<CapacitySlotEntity> capacitySlotEntityList = new ArrayList<>();
			
			//For each slot creating the entity.
			slotDetailList.stream().forEach(s -> {
				
				//Adding capacitySlotTypeEntity to map if not present.
				if(!capacitySlotTypeMap.containsKey(s.getSlotTypeId())) {
					Optional<CapacitySlotTypeEntity> slotTypeEntity = capacitySlotTypeRepository
							.findById(new BigInteger(s.getSlotTypeId()));
					capacitySlotTypeMap.put(s.getSlotTypeId(), slotTypeEntity);
				}
				
				//mapping the requested data to capacity slot entity.
				CapacitySlotEntity slotEntity = capacityTemplateMapper.mapToSlot(createdTemplateEntity, reference,
						capacitySlotTypeMap.get(s.getSlotTypeId()), channelEntity, t, s, createdBy);
				
				//Adding CapacitySlotEntity to list.
				capacitySlotEntityList.add(slotEntity);
				
				//mapping data to response.
				SlotDetail responseSlot = capacityTemplateMapper.mapToResponseSlot(slotEntity, s);
				responseDetail.add(responseSlot);
			});
			
			//Saving all the slot entities.
			capacitySlotRepository.saveAll(capacitySlotEntityList);
			
			responseChannel.setSlotDetails(responseDetail);
			responseChannelList.add(responseChannel);
		});
		
		//Saving the capacityTemplateAndCapacityChannelEntity List.
		capacityTemplateAndCapacityChannelRepository.saveAll(capacityTemplateAndCapacityChannelEntityList);
		
		//Adding operation performed to audit table using audit service.
		if(null != createdTemplateEntity.getCapacityTemplateNm()) {
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.INSERT, null, createdTemplateEntity, createdBy);
		}
		
		//Mapping the data and return.
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
		// fetching the any duplicates for capacity template name within the concept
		CapacityTemplateEntity capacityTemplateEntity = capacityTemplateRepo
				.findByCapacityTemplateNmAndConceptId(capacityTemplateNm, new BigInteger(RequestContext.getConcept()));
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
	public String deleteByTemplateId(String templateId, String deleteConfirmed, String userDetail)
			throws JsonProcessingException {
		
		//Fetching capacity template entity based on the template id.
		CapacityTemplateEntity capacityTemplateEntity = getByCapacityTemplateIdAndIsDeletedFlag(new BigInteger(templateId));
		
		//Performing delete operation if the value of deletedConfirmed is Y.
		if (deleteConfirmed.equals(CapacityConstants.Y)) {
			// performing hard delete if parameter value is N in appParameter entity.
			if (capacityTemplateEntity.getCapacityTemplateId() != null) {

				// deleting all the relational data related to capacity template entity to be
				// deleted.
				capacityTemplateAndBusinessDateRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
				capacityTemplateAndCapacityChannelRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
				capacitySlotRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);

				// deleting the capacity template entity by id for hard delete.
				capacityTemplateRepo.deleteById(capacityTemplateEntity.getCapacityTemplateId());
			}
			// adding the action performed to audit table using audit service.
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.DELETE_HARD, null,
					capacityTemplateEntity, userDetail);
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
		
		//fetching the capacity template entity by template id within the concept.
		Optional<CapacityTemplateEntity> dbTemplateEntityOptional = capacityTemplateRepo
				.findByCapacityTemplateIdAndConceptId(templateId, new BigInteger(RequestContext.getConcept()));
		if(dbTemplateEntityOptional.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		else{
			capacityTemplateEntity = dbTemplateEntityOptional.get();
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
		
		//Fetching the CapacityTemplateEntity by template id within the concept.
        Optional<CapacityTemplateEntity> dbTemplateValue = capacityTemplateRepo
        		.findByCapacityTemplateIdAndConceptId(new BigInteger(templateId),  new BigInteger(RequestContext.getConcept()));
        
        //Raising exception if no entity found.
         if(dbTemplateValue.isEmpty()) {
             applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
                     CapacityConstants.CAPACITY_TEMPLATE_ID);
             applicationErrors.raiseExceptionIfHasErrors();
         }
         List<String> headerFooterList = new ArrayList<>();
         List<DeleteResponseBodyFormat> deleteResponseBodyFormatList = new ArrayList<>();
         List<CapacityModelAndCapacityTemplateEntity> dbModelAndTemplate = null;
         
         if(dbTemplateValue.isPresent()) {
        	 
        	 //Fetching the list of CapacityModelAndCapacityTemplateEntities based on CapacityTemplateEntity value.
             dbModelAndTemplate = capacityModelAndCapacityTemplateRepository.findByCapacityTemplate(dbTemplateValue.get());
             
             //If dbModelAndTemplate is not empty mapping the model names to DeleteResponseBodyFormat model class.
	         if(!dbModelAndTemplate.isEmpty()) {
	              List<String> capacityNames = new ArrayList<>();
	              
	              //Adding capacity names to list.
	              dbModelAndTemplate.stream().forEach(dbModelAndTemplate1->
	                     capacityNames.add(dbModelAndTemplate1.getCapacityModel().getCapacityModelNm()));
	              DeleteResponseBodyFormat capacityModel = DeleteResponseBodyFormat.builder().build();
	              if (CollectionUtils.isNotEmpty(capacityNames)) {
	                  capacityModel.setTitle(CapacityConstants.MODEL);
	                  capacityModel.setListOfData(capacityNames);
	                  headerFooterList.add(CapacityConstants.MODEL);
	                  deleteResponseBodyFormatList.add(capacityModel);
	              }
	         }
	         
	         //Raising exception for the dependencies present.
	         globalDataCall.raiseException(headerFooterList, deleteResponseBodyFormatList, dbTemplateValue.get().getCapacityTemplateNm(), applicationErrors);
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
		ApplicationErrors applicationErrors = new ApplicationErrors();
		boolean isCapacityTemplateNameExists = true;
		if (StringUtils.isNotBlank(templateId)) {
			
			//Fetching the capacityTemplateEntity within the concept
			Optional<CapacityTemplateEntity> template = capacityTemplateRepo
					.findByCapacityTemplateIdAndConceptId(new BigInteger(templateId), new BigInteger(RequestContext.getConcept()));
			
			if (template.isPresent()) {
				// Here checking the templateName against the templateId it means no
				// modification done from UI
				if (template.filter(t -> t.getCapacityTemplateNm().equalsIgnoreCase(capacityTemplateNm)).isPresent()) {
					isCapacityTemplateNameExists = false;
				} else {
					isCapacityTemplateNameExists = validateCapacityTemplateNm(capacityTemplateNm);
				}
			}
			else {
				//Raising exceptions if entity not found.
				applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_TEMPLATE_ID);
				applicationErrors.raiseExceptionIfHasErrors();
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
	 * @throws JsonProcessingException 
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) }, put = {
            @CachePut(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key = CapacityConstants.CAPACITY_TEMPLATE_CACHE_KEY) })
	public CreateTemplateResponse updateCapacityTemplate(@Valid CreateCapacityTemplateRequest templateRequest,
			String accessToken, BigInteger templateId) throws JsonProcessingException {
		CreateTemplateResponse response = new CreateTemplateResponse();
		
		//Fetching string from access token.
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		
		//Fetching CapacityTemplateEntity by capacity template Id.
		Optional<CapacityTemplateEntity> capacityTemplateEntity = capacityTemplateRepo.findById(templateId);
		capacityTemplateEntity.ifPresent(existingTemplate ->{
			
			//Deleting all the relational data to capacityTemplate entity.
			capacityTemplateAndBusinessDateRepository.deleteAllBycapacityTemplate(existingTemplate);
			capacitySlotRepository.deleteAllBycapacityTemplate(existingTemplate);
			capacityTemplateAndCapacityChannelRepository.deleteAllBycapacityTemplate(existingTemplate);
			
			//Mapping the request data to be updated to capacity template entity.
			mapExistingTemplateAndUpdate(templateRequest, createdBy,dateTime, existingTemplate);
		});
		//fetching capacityTemplateEntity by templateId.
		Optional<CapacityTemplateEntity> templateData = capacityTemplateRepo.findById(templateId);
		CapacityTemplateEntity responseCapacityTemplateEntity = null;
		if (templateData.isPresent()) {
		     responseCapacityTemplateEntity = templateData.get();
		     
		     //Mapping the updated entity to model class for response.
			response = mapUpdateResponse(templateRequest, responseCapacityTemplateEntity);
		}
		
		//Adding the operation performed in audit table using audit service.
		if (responseCapacityTemplateEntity != null && responseCapacityTemplateEntity.getCapacityTemplateNm() != null) {
            auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.UPDATE, null, responseCapacityTemplateEntity,
                    createdBy);
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
		
		//Mapping the updated capacity template entity response model class.
		CreateTemplateResponse response = capacityTemplateMapper.mapCreateResponse(responseCapacityTemplateEntity);
		
		//Fetching the capacity slot data related to template entity.
		List<CapacitySlotEntity> capacitySlots = responseCapacityTemplateEntity.getCapacitySlots();
		
		MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
		Set<String> channelIds = new HashSet<>();
		Map<String, String> isSelectedFlags = new HashMap<>();
		
		//Fetching the list of CapacityTemplateAndCapacityChannelEntities related to template entity
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntites = responseCapacityTemplateEntity
				.getCapacityTemplateAndCapacityChannels();
		
		capacityTemplateAndCapacityChannelEntites.stream().filter(Objects::nonNull).forEach(ctc -> {
			CapacityTemplateAndCapacityChannelPK pk = ctc.getId();
			isSelectedFlags.put(String.valueOf(pk.getCapacityChannelId()), CapacityConstants.Y);
		});
		
		//Mapping the channel and slot data to response model class. 
		mapCapacitySlotsResponseForUpdate(capacitySlots, channelSlotDetails, channelIds);
		List<SlotChannel> slotChannels = mapSlotChannelsResponseForUpdate(channelSlotDetails, channelIds,isSelectedFlags);
		
		//If template type is DATES mapping business dates.
		if (CapacityConstants.DATES.equals(templateTypeName)) {
			response.setBusinessDates(templateRequest.getBusinessDates());
		}
		response.setSlotChannels(slotChannels);
		
		//Mapping template type to reponse model class.
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
		
		//Mapping all capacity slot entity data to response model class.
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
		
		//Fetching template type from request.
		String templateTypeName = templateRequest.getTemplateTypeName();
		
		//Mapping for template type with string DAYS.
		if (CapacityConstants.DAYS.equals(templateTypeName)) {
			existingTemplate.setEffectiveDate(DateUtil.stringToDate(templateRequest.getEffectiveDate()));
			
			//Mapping based on the value of expiry date passed in the request.
			if(templateRequest.getExpiryDate() != null)
				existingTemplate.setExpiryDate(DateUtil.stringToDate(templateRequest.getExpiryDate()));
			else
				existingTemplate.setExpiryDate(DateUtil.stringToDate(CapacityConstants.BLANK));
			
			//Mapping all the days flag from the requested data.
			capacityTemplateMapper.mapTemplateDaysFromTemplateCreateUpdateRequest(templateRequest, existingTemplate);
		}
		//Mapping for template type with string DATES.
		else {
			existingTemplate.setEffectiveDate(DateUtil.stringToDate(CapacityConstants.BLANK));
			existingTemplate.setExpiryDate(DateUtil.stringToDate(CapacityConstants.BLANK));
		}
		existingTemplate.setStartTime(LocalTime.parse(templateRequest.getSlotStartTime()));
		existingTemplate.setEndTime(LocalTime.parse(templateRequest.getSlotEndTime()));
		
		//Fetching the CapacityTemplateTypeEntity based on template name passed in the request.
		CapacityTemplateTypeEntity capacityTemplateTypeEntity = capacityTemplateTypeRepository
				.findByCapacityTemplateTypeNm(templateRequest.getTemplateTypeName());
		if (capacityTemplateTypeEntity != null) {
			existingTemplate.setCapacityTemplateType(capacityTemplateTypeEntity);
		}
		
		//Saving the updated capacity template entity.
		capacityTemplateRepo.save(existingTemplate);
		
		//updating the related data to capacity template entity.
		updateCapacitySlots(templateRequest, createdBy, dateTime, existingTemplate);
		
		//If template type string is DATES updating the business date.
		if (CapacityConstants.DATES.equals(templateTypeName)) {
			
			//Setting the days flag to null.
			capacityTemplateMapper.setTemplateDaysToNullValue(existingTemplate);
			
			//Creating the entities for business date and saving.
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
			
			//Creating the capacity slot entities and saving.
			templateRequest.getSlotChannels().forEach(slotChannel -> {
				
				//fetching the channel id.
				BigInteger channelId = slotChannel.getChannelId();
				
				//Filtering the slot detail based on channel id.
				slotChannel.getSlotDetails()
						.forEach(slotDetail -> channelSlotDetails.put(channelId, slotDetail));
				
				//Fetching capacity channel based on the channel id.
				Optional<CapacityChannelEntity> channelEntity = capacityChannelRepo.findById(channelId);
				
				if (channelEntity.isPresent()) {
					
					//Mapping the CapacityTemplateAndCapacityChannelEntity based on the request data.
					CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = capacityTemplateMapper
							.mapToTemplateAndChannelEntity(existingTemplate, channelEntity, slotChannel, createdBy, dateTime);
					
					//Adding CapacityTemplateAndCapacityChannelEntity to list.
					capacityTemplateAndCapacityChannelEntityList.add(capacityTemplateAndCapacityChannelEntity);
					List<CapacitySlotEntity> newSlotEntities = new ArrayList<>();
					
					//Fetching all the slot details based on channel id.
					Collection<SlotDetail> slotDetailsReq = channelSlotDetails.get(channelId);
					
					//Creating List of entities for capacity slot data from request.
					slotDetailsReq.stream().filter(Objects::nonNull).forEach(slotDetailReq -> {
						
						//Mapping data to capacity slot entity.
						CapacitySlotEntity capacitySlotEntity = mapSlotEntity(createdBy, dateTime, existingTemplate,
								channelEntity, slotDetailReq);
						
						//Adding to list.
						newSlotEntities.add(capacitySlotEntity);
					});
					
					//Saving the list of updated entities.
					capacitySlotRepository.saveAll(newSlotEntities);
				}
			});
		}
		//Saving capacityTemplateAndCapacityChannelEntity List.
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
		//Fetching CapacitySlotTypeEntity by slot type id.
		Optional<CapacitySlotTypeEntity> capacitySlotTypeEntity = capacitySlotTypeRepository
				.findById(new BigInteger(slotDetailReq.getSlotTypeId()));
		
		//Fetching ReferenceEntity by reference id.
		Optional<ReferenceEntity> reference = referenceRepository
				.findById(CapacityConstants.BIG_INT_CONSTANT);
		
		//Mapping data to capacitySlotEntity and return.
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
		
		//Getting business date from request.
		List<BusinessDate> bList = templateRequest.getBusinessDates();
		List<CapacityTemplateAndBusinessDateEntity> list = new ArrayList<>();
		
		//Creating entities for List of business dates.
		bList.stream().filter(Objects::nonNull).forEach(t -> {
			
			//Mapping data to CapacityTemplateAndBusinessDateEntity.
			CapacityTemplateAndBusinessDateEntity templateDate = capacityTemplateMapper
					.mapToBusinessDate(existingTemplate, t, createdBy, dateTime);
			
			//Adding to list.
			list.add(templateDate);
		});
		
		//Saving all the list of CapacityTemplateAndBusinessDateEntities.
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
		
		//Fetching all the CapacityModelAndCapacityTemplateEntity data.
		List<CapacityModelAndCapacityTemplateEntity> list = capacityModelAndCapacityTemplateRepository.findAll();
		
		//Fetching all the capacity template id.
		List<BigInteger> assignedTemplateId = extractingAllAssignedTemplateId(list);
		if(assignedTemplateId.contains(new BigInteger(templateId))) {
			
			//Extracting other templates to be compared with.
			list = extractingAssignedTemplatesToBeComapred(templateId, list);
			
			//Fetching template type name.
			String templateTypeName = createCapacityTemplateRequest.getTemplateTypeName();
			
			//If template type name is DAYS validate day flags with other days template flag.
			if (CapacityConstants.DAYS.equalsIgnoreCase(templateTypeName)) {
				validateEffectiveDateAndExpiryDate(createCapacityTemplateRequest, applicationErrors);
				return validationForTemplateDays(createCapacityTemplateRequest, list);
			}
			
			//If template type name is DATES validate day flags with other days template flag.
			else if (CapacityConstants.DATES.equalsIgnoreCase(templateTypeName)) {
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
		
		//validating effective date in request if null raising exception.
		if(createCapacityTemplateRequest.getEffectiveDate() == null) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4001),CapacityConstants.EFFECTIVE_DATE);
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
				//filtering templates with template types string as DATES
				.filter(tempType -> tempType.getCapacityTemplate().getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DATES))
				.anyMatch(t -> t.getCapacityTemplate()
				.getCapacityTemplateAndBusinessDates()
				.stream()
				.filter(Objects::nonNull)
				.anyMatch(s -> {
					
					//Converting string format to localDate.
					LocalDate businessDate = DateUtil.convertDatetoLocalDate(s.getId().getBusinessDate());
					
					//Validating if there is any conflicting date and returning the value.
					return createCapacityTemplateRequest.getBusinessDates().stream().filter(Objects::nonNull)
							.anyMatch(bDate -> {
								LocalDate reqBusinessDate = DateUtil.convertStringtoLocalDate(bDate.getDate());
								boolean isSameBusinessDate = false;
								
								//if date matches setting isSameBusinessDate value to true.
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
		
		//Collecting list where the value is template id.
		List<CapacityModelAndCapacityTemplateEntity> assignedModel = list.stream()
				//filtering list for matching value of template id.
				.filter(templateAssigned -> templateAssigned.getCapacityTemplate().getCapacityTemplateId().equals(new BigInteger(templateId)))
				.collect(Collectors.toList());
		
		//Collecting all the model ids that has been assigned.
		List<BigInteger> allModelId = new ArrayList<>();
		assignedModel.stream()
				//filtering list if allModel doen't contains the model id.
				.filter(model -> !allModelId.contains(model.getCapacityModel().getCapacityModelId()))
				.forEach(model -> allModelId.add(model.getCapacityModel().getCapacityModelId()));
		
		//Collecting templates for all other template assigned to same model.
		list = list.stream()
				//filtering list if allModelId contains the model id.
				.filter(modelAssignedTemplate -> allModelId.contains(modelAssignedTemplate.getCapacityModel().getCapacityModelId()))
				//filtering list to avoid self check for given template id.
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
		list.stream()
			.forEach( assignedId -> assignedTemplateId.add(assignedId.getId().getCapacityTemplateId()));
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
		return list.stream()
				.filter(Objects::nonNull)
				//filtering templates with template type name string with DAYS.
				.filter(templateType -> templateType.getCapacityTemplate().getCapacityTemplateType().getCapacityTemplateTypeNm().equals(CapacityConstants.DAYS))
				.anyMatch(t -> {
			LocalDate dbTemplateExpDate = null;
			LocalDate expReq = null;
			
			//converting other dates format to localdate.
			LocalDate dbTemplateEffectiveDate = DateUtil
					.convertDatetoLocalDate(t.getCapacityTemplate().getEffectiveDate());
			if(t.getCapacityTemplate().getExpiryDate() != null)
				dbTemplateExpDate = DateUtil.convertDatetoLocalDate(t.getCapacityTemplate().getExpiryDate());
			LocalDate effectiveDateReq = DateUtil
					.convertStringtoLocalDate(createCapacityTemplateRequest.getEffectiveDate());
			if(createCapacityTemplateRequest.getExpiryDate() != null)
				expReq = DateUtil.convertStringtoLocalDate(createCapacityTemplateRequest.getExpiryDate());
			
			//validating if both expiry date is null
			if ((dbTemplateExpDate == null && expReq == null) 
					//if comparing expiry date is null and to be compared effective is before the other expiry date.
					|| (expReq == null && dbTemplateExpDate != null && effectiveDateReq.isBefore(dbTemplateExpDate)) 
					//if to be compared expiry date is null and comparing expiry date is after the other effective date.
					|| (dbTemplateExpDate == null && expReq != null && expReq.isAfter(dbTemplateEffectiveDate))
					//if no null value for both expiry date and to be compared effective is before the other expiry date and comparing expiry date is after the other effective date.
					|| (dbTemplateExpDate != null && expReq != null && effectiveDateReq.isBefore(dbTemplateExpDate) && expReq.isAfter(dbTemplateEffectiveDate))) {
				CapacityTemplateEntity template=t.getCapacityTemplate();
				return validateDays(createCapacityTemplateRequest,template);
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
		
		//Fetching all the requested template days flags.
		String sunDay = createCapacityTemplateRequest.getSunDay();
		String monDay = createCapacityTemplateRequest.getMonDay();
		String tueDay = createCapacityTemplateRequest.getTueDay();
		String wedDay = createCapacityTemplateRequest.getWedDay();
		String thuDay = createCapacityTemplateRequest.getThuDay();
		String friDay = createCapacityTemplateRequest.getFriDay();
		String satDay = createCapacityTemplateRequest.getSatDay();

		//checks if any days are matching and returns value.
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
		
		//checks if both strings contains Y.
		return StringUtils.equalsIgnoreCase(CapacityConstants.Y, resFlg)
				&& StringUtils.equalsIgnoreCase(resFlg, reqFlg);
	}

	/**
	 * Fetches all the model data for the list of templated passed in the
	 * parameter.
	 * 
	 * @param templateIds list of big integer containing the data of template ids.
	 * 
	 * @return List<CapacityModel> list of models containing the data of capacity model.
	 */
	@Override
	public List<CapacityModel> getAllModelsRelatingToTemplateIdList(Set<BigInteger> templateIds) {
		Set<BigInteger> modelIdList = new HashSet<>();
		List<CapacityModel> capacityModelList = new ArrayList<>();
		
		//Fetching all the capacity templates for the list of ids.
		List<CapacityTemplateEntity> templates = capacityTemplateRepo.findAllById(templateIds);
		templates.stream()
				 .forEach(template -> template.getCapacityModelAndCapacityTemplates().stream()
						 .forEach(model -> {
				//If condition to avoid duplicate model ids.
				if(!modelIdList.contains(model.getCapacityModel().getCapacityModelId())) {
					modelIdList.add(model.getCapacityModel().getCapacityModelId());
					
					//Mapping data to model class
					CapacityModel capModel = new CapacityModel();
					capModel.setCapacityModelId(model.getCapacityModel().getCapacityModelId());
					capModel.setCapacityModelName(model.getCapacityModel().getCapacityModelNm());
					
					//adding to list.
					capacityModelList.add(capModel);
				}
			}));
		return capacityModelList;
	}
}
