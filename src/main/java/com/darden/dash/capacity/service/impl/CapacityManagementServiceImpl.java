package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateResponseSlot;
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
	
	private AppParameterService appParameterService;
	
	private AuditService auditService;

	private CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);

	private CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);

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
	 * @return ResponseEntity<Object>
	 * 
	 */
	@Override
	public ResponseEntity<Object> getAllCapacityTemplates() {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		if (StringUtils.isBlank(RequestContext.getConcept())) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4421));
			applicationErrors.raiseExceptionIfHasErrors();
		}
		BigInteger concepId = new BigInteger(RequestContext.getConcept());
		List<CapacityTemplateEntity> capacityTemplateEntities = capacityTemplateRepo.findByConceptId(concepId);
		List<CapacityChannelEntity> channelEntities = capacityChannelRepo.findAll();
		List<CapacityTemplate> capacityTemplates = new ArrayList<>();
		capacityTemplateEntities.stream().filter(Objects::nonNull).forEach(capacityTemplateEntity -> {
			List<Channel> channels = getCapacityTemplateChannels(capacityTemplateEntity);
			List<CapacitySlotEntity> capacitySlots = capacityTemplateEntity.getCapacitySlots();
			MultiValuedMap<String, SlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
			Set<String> channelIds = new HashSet<>();
			Map<String, String> channelNames = new HashMap<>();
			mapCapacitySlots(capacitySlots, channelSlotDetails, channelIds, channelNames);
			List<SlotChannel> slotChannels = mapSlotChannels(channelSlotDetails, channelIds, channelNames);
			mapCapacityTemplateModel(capacityTemplates, capacityTemplateEntity, channels, slotChannels);
		});
		CapacityResponse response = mapReferenceData(channelEntities, capacityTemplates);
		return response.build(CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY,
				CapacityConstants.STATUS_CODE_200);
	}

	/**
	 * This method is to map the reference data to capacity Response Here by using
	 * mapstruct channel entities are mapped to capacity channel
	 * 
	 * @param channelEntities
	 * @param capacityTemplates
	 * @return CapacityResponse
	 */
	private CapacityResponse mapReferenceData(List<CapacityChannelEntity> channelEntities,
			List<CapacityTemplate> capacityTemplates) {
		CapacityResponse response = new CapacityResponse();
		response.setCapacityTemplates(capacityTemplates);
		ReferenceDatum referenceDatum = new ReferenceDatum();
		referenceDatum.setCapacityChannel(capacityChannelMapper.mapChannels(channelEntities));
		response.setReferenceData(Collections.singletonList(referenceDatum));
		return response;
	}

	/**
	 * 
	 * This method is used to map slot details ,slot channels to capacity template
	 * model Here by using mapstruct capacity template entities are mapped to
	 * capacity template
	 * 
	 * @param capacityTemplateModels
	 * @param capacityTemplateEntity
	 * @param channels
	 * @param slotChannels
	 */

	private void mapCapacityTemplateModel(List<CapacityTemplate> capacityTemplateModels,
			CapacityTemplateEntity capacityTemplateEntity, List<Channel> channels, List<SlotChannel> slotChannels) {
		CapacityTemplate capacityTemplateModel = capacityTemplateMapper.map(capacityTemplateEntity);
		capacityTemplateModel.setSlotStartTime(String.valueOf(capacityTemplateEntity.getStartTime()));
		capacityTemplateModel.setSlotEndTime(String.valueOf(capacityTemplateEntity.getEndTime()));
		capacityTemplateModel.setSlotChannels(slotChannels);
		capacityTemplateModel.setChannels(channels);
		capacityTemplateModels.add(capacityTemplateModel);
	}

	/**
	 * 
	 * This method is used to map capacity channel from capacity template entity
	 * 
	 * @param capacityTemplateEntity
	 * @return List<Channel>
	 */

	private List<Channel> getCapacityTemplateChannels(CapacityTemplateEntity capacityTemplateEntity) {
		List<Channel> channels = new ArrayList<>();
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntity = capacityTemplateEntity
				.getCapacityTemplateAndCapacityChannels();
		capacityTemplateAndCapacityChannelEntity.stream().filter(Objects::nonNull).forEach(ctc -> {
			Channel channel = new Channel();
			channel.setCapacityChannelId(ctc.getCapacityChannel().getCapacityChannelId());
			channel.setCapacityChannelName(ctc.getCapacityChannel().getCapacityChannelNm());
			channel.setIsSelectedFlag(ctc.getIsSelectedFlag());
			channels.add(channel);
		});
		return channels;
	}

	/**
	 * 
	 * This method is used to map the slots corresponding to this template Id and
	 * channel Id
	 * 
	 * @param capacitySlots
	 * @param channelSlotDetails
	 * @param channelIds
	 * @param channelNames
	 */
	private void mapCapacitySlots(List<CapacitySlotEntity> capacitySlots,
			MultiValuedMap<String, SlotDetail> channelSlotDetails, Set<String> channelIds,
			Map<String, String> channelNames) {
		capacitySlots.stream().filter(Objects::nonNull).forEach(cs -> {
			String channelId = String.valueOf(cs.getCapacityChannel().getCapacityChannelId());
			channelIds.add(channelId);
			channelNames.put(channelId, cs.getCapacityChannel().getCapacityChannelNm());
			SlotDetail slotDetail = new SlotDetail();
			slotDetail.setSlotId(cs.getCapacitySlotId());
			slotDetail.setSlotTypeId(String.valueOf(cs.getCapacitySlotType().getCapacitySlotTypeId()));
			slotDetail.setStartTime(String.valueOf(cs.getStartTime()));
			slotDetail.setEndTime(String.valueOf(cs.getEndTime()));
			slotDetail.setIsDeletedFlg(cs.getIsDeletedFlg());
			slotDetail.setCapacityCount(String.valueOf(cs.getCapacityCnt()));
			channelSlotDetails.put(String.valueOf(cs.getCapacityChannel().getCapacityChannelId()), slotDetail);
		});
	}

	/**
	 * This method is used to group slot channels using channel id
	 * 
	 * @param channelSlotDetails
	 * @param channelIds
	 * @param channelNames
	 * @return List<SlotChannel>
	 */
	private List<SlotChannel> mapSlotChannels(MultiValuedMap<String, SlotDetail> channelSlotDetails,
			Set<String> channelIds, Map<String, String> channelNames) {
		List<SlotChannel> slotChannels = new ArrayList<>();
		channelIds.stream().filter(StringUtils::isNotBlank).forEach(channelId -> {
			List<SlotDetail> slotDetails = new ArrayList<>();
			SlotChannel slotChannel = new SlotChannel();
			slotChannel.setChannelId(channelId);
			slotChannel.setChannelName(channelNames.get(channelId));
			slotDetails.addAll(channelSlotDetails.get(channelId));
			slotChannel.setSlotDetails(slotDetails);
			slotChannels.add(slotChannel);
		});
		return slotChannels;
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
	 * @param templateRequest
	 * @param accessToken
	 * @return CreateTemplateResponse
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	public CreateTemplateResponse createTemplate(@Valid CreateCapacityTemplateRequest templateRequest,
			String accessToken) throws JsonProcessingException {
		String createdBy = jwtUtils.findUserDetail(accessToken);
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		Optional<CapacityTemplateTypeEntity> templateType = capacityTemplateTypeRepository
				.findById(templateRequest.getTemplateTypeId());
		CapacityTemplateEntity templateEntity = capacityTemplateMapper.mapToTemplate(templateRequest, templateType,
				createdBy, dateTime);
		CapacityTemplateEntity createdTemplateEntity = capacityTemplateRepo.save(templateEntity);
		List<BusinessDate> responseDate = new ArrayList<>();
		String templateName = templateType.map(CapacityTemplateTypeEntity::getCapacityTemplateTypeNm)
				.orElse(StringUtils.EMPTY);
		if (CapacityConstants.DATES.equals(templateName)) {
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
		List<CreateResponseSlot> responseChannelList = new ArrayList<>();
		List<CreateResponseSlot> slotChannelList = templateRequest.getSlotChannels();
		slotChannelList.stream().forEach(t -> {
			Optional<ReferenceEntity> reference = referenceRepository.findById(CapacityConstants.BIG_INT_CONSTANT);
			Optional<CapacityChannelEntity> channelEntity = capacityChannelRepo.findById(t.getChannelId());
			CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = capacityTemplateMapper
					.mapToTemplateAndChannelEntity(createdTemplateEntity, channelEntity, t, createdBy, dateTime);
			CapacityTemplateAndCapacityChannelEntity savedChannelEntity = capacityTemplateAndCapacityChannelRepository
					.save(capacityTemplateAndCapacityChannelEntity);
			CreateResponseSlot responseChannel = new CreateResponseSlot();
			responseChannel.setChannelId(savedChannelEntity.getId().getCapacityChannelId());
			responseChannel.setIsSelectedFlag(savedChannelEntity.getIsSelectedFlag());
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
		if(null != createdTemplateEntity && null != createdTemplateEntity.getCapacityTemplateNm()) {
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.INSERT, null, createdTemplateEntity, createdBy);
		}
		return capacityTemplateMapper.mapToCreateTemplateResponse(createdTemplateEntity, responseDate,
				responseChannelList, templateRequest);
	}

	/**
	 * Method is used to validate the CapacityTemplate name based the presence of
	 * name it returns boolean value
	 * 
	 * @param CapacityTemplateNm
	 * @return boolean
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
	 * @param templateId
	 * @param deletedFlag
	 * @param userDetail
	 */
	@Override
	@Transactional
	public void deleteByTemplateId(String templateId, String deletedFlag, String userDetail)
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
			capacityTemplateAndBusinessDateRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
			capacityTemplateAndCapacityChannelRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
			capacitySlotRepository.deleteAllBycapacityTemplate(capacityTemplateEntity);
			capacityTemplateRepo.delete(capacityTemplateEntity);
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.DELETE_HARD, null, capacityTemplateEntity, userDetail);
		}
		
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
	 * @param templateId
	 * @return CapacityTemplateEntity
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
	 * @param templateId
	 * @return boolean
	 */
	@Override
	public boolean validateCapacityTemplateId(String templateId) {

		Optional<CapacityTemplateEntity> dbTemplateValue = capacityTemplateRepo.findById(new BigInteger(templateId));
		CapacityModelAndCapacityTemplateEntity dbModelAndTemplate = new CapacityModelAndCapacityTemplateEntity();
		if(dbTemplateValue.isPresent()) {
			dbModelAndTemplate = capacityModelAndCapacityTemplateRepository.findByCapacityTemplate(dbTemplateValue.get());
		}

		return dbModelAndTemplate != null;
	}
}
