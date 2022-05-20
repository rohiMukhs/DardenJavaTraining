package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.error.ApplicationErrors;

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

	private CapacityTemplateRepo capacityTemplateRepo;
	private CapacityChannelRepo capacityChannelRepo;
	private CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);
	private CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);

	/**
	 * Autowiring required properties
	 * 
	 * @param capacityTemplateRepo
	 * @param capacitychannelRepo
	 */
	@Autowired
	public CapacityManagementServiceImpl(CapacityTemplateRepo capacityTemplateRepo,
			CapacityChannelRepo capacityChannelRepo) {
		super();
		this.capacityTemplateRepo = capacityTemplateRepo;
		this.capacityChannelRepo = capacityChannelRepo;
	}

	/**
	 * This method is to get list of Capacity Templates from database along with
	 * list of channel data from CapacityTemplate,
	 * CapacityTemplateAndCapacityChannel , CapacitySlot ,CapacityChannel entities
	 * and mapping the capacity channel mapper to get the reference data 
	 * Here validating the concept Id ,if concept Id is empty then error message is 
	 * thrown.
	 * 
	 * @return ResponseEntity<Object>
	 * 
	 */
	@Override
	public ResponseEntity<Object> getAllCapacityTemplates() {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		if(StringUtils.isBlank(RequestContext.getConcept())) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4421));
			applicationErrors.raiseExceptionIfHasErrors();
		}
		BigInteger concepId=new BigInteger(RequestContext.getConcept());
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
	 * This method is to map the reference data to capacity Response
	 * Here by using mapstruct channel entities are mapped to capacity channel
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
	 * This method is used to map slot details ,slot channels to capacity template model
	 * Here by using mapstruct capacity template entities are mapped to capacity template
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
	 * This method is used to map the slots corresponding to this template Id
	 * and channel Id 
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

}
