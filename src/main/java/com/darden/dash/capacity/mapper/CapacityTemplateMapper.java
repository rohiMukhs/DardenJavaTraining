package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDatePK;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.util.DateUtil;

/**
 * @author skashala
 * @date 16-May-2022
 * 
 *       This class is used to map capacity template entity to capacity template
 *       model
 */
@Mapper
public interface CapacityTemplateMapper {

	/**
	 * This method is used to mapping the objects from CapacityTemplateEntity to
	 * CapacityTemplate
	 * 
	 * @param CapacityTemplateEntity entity class with information of
	 *                         Capacity template.
	 * 
	 * @return CapacityTemplate mapped model class of Capacity Template.
	 */

	@Mapping(target = CapacityConstants.CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_END_TIME, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_START_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAP_EFFECTIVE_DATE, ignore = true)
	@Mapping(target = CapacityConstants.MAP_EXPIRY_DATE, ignore = true)
	@Mapping(target = CapacityConstants.CAPACITYTEMPLATETYPE, ignore = true)
	@Mapping(target = CapacityConstants.BUSINESSDATE, ignore = true)
	@Mapping(target = CapacityConstants.SUN_DAY, ignore = true)
	@Mapping(target = CapacityConstants.MON_DAY, ignore = true)
	@Mapping(target = CapacityConstants.TUE_DAY, ignore = true)
	@Mapping(target = CapacityConstants.WED_DAY, ignore = true)
	@Mapping(target = CapacityConstants.THU_DAY, ignore = true)
	@Mapping(target = CapacityConstants.FRI_DAY, ignore = true)
	@Mapping(target = CapacityConstants.SAT_DAY, ignore = true)
	@Mapping(source = CapacityConstants.MAP_CAPACITY_TEMPLATE_NM, target = CapacityConstants.TEMPLATE_NAME)
	CapacityTemplate map(CapacityTemplateEntity capacityTemplate);

	/**
	 * This method is used to mapping the CapacityTemplateEntity to CapacityTemplate
	 * and converting the date formatter to String format for the specific fields
	 * 
	 * @param CapacityTemplateEntity entity class with information of
	 *                         Capacity template.
	 *                         
	 * @param CapacityTemplate mapped model class of Capacity Template.
	 */
	@AfterMapping
	default void map(CapacityTemplateEntity capacityEntity, @MappingTarget CapacityTemplate capacityModel) {
		if(capacityEntity.getExpiryDate() != null) 
			capacityModel.setExpiryDate(DateUtil.dateToString(capacityEntity.getExpiryDate()));
		if(capacityEntity.getEffectiveDate() != null)
			capacityModel.setEffectiveDate(DateUtil.dateToString(capacityEntity.getEffectiveDate()));
	}

	/**
	 * Method to map data to capacityTemplateEntity
	 * 
	 * @param templateRequest request class with information of
	 *                       Capacity template.
	 * 
	 * @param capacityTemplateTypeEntity entity class with information of
	 *                       Capacity template type.
	 *                       
	 * @param createdBy String with information of user.
	 * 
	 * @param dateTime Instant with information of dateTime.
	 * 
	 * @return CapacityTemplateEntity mapped entity class of Capacity Template.
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_ENTITY)
	default CapacityTemplateEntity mapToTemplate(CreateCapacityTemplateRequest templateRequest,
			CapacityTemplateTypeEntity templateType, String createdBy, Instant dateTime) {
		CapacityTemplateEntity templateEntity = new CapacityTemplateEntity();
		templateEntity.setCapacityTemplateNm(templateRequest.getCapacityTemplateName());
		templateEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		templateEntity.setIsDeletedFlg(CapacityConstants.N);
		templateEntity.setCreatedBy(createdBy);
		templateEntity.setCreatedDatetime(dateTime);
		templateEntity.setLastModifiedBy(createdBy);
		templateEntity.setLastModifiedDatetime(dateTime);
		templateEntity.setCapacityTemplateType(templateType);
		templateEntity.setStartTime(LocalTime.parse(templateRequest.getSlotStartTime()));
		templateEntity.setEndTime(LocalTime.parse(templateRequest.getSlotEndTime()));
		if (null != templateRequest.getTemplateTypeName() && CapacityConstants.DAYS.equals(templateRequest.getTemplateTypeName())) {
			templateEntity.setEffectiveDate(DateUtil.stringToDate(templateRequest.getEffectiveDate()));
			if(templateRequest.getExpiryDate() != null)
				templateEntity.setExpiryDate(DateUtil.stringToDate(templateRequest.getExpiryDate()));
			templateEntity.setSunFlg(templateRequest.getSunDay());
			templateEntity.setMonFlg(templateRequest.getMonDay());
			templateEntity.setTueFlg(templateRequest.getTueDay());
			templateEntity.setWedFlg(templateRequest.getWedDay());
			templateEntity.setThuFlg(templateRequest.getThuDay());
			templateEntity.setFriFlg(templateRequest.getFriDay());
			templateEntity.setSatFlg(templateRequest.getSatDay());
		}
		return templateEntity;
	}

	/**
	 * Method to map data to CapacityTemplateAndBusinessDateEntity
	 * 
	 * @param createdTemplateEntity entity class with information of
	 *                       		Capacity template.
	 *                       
	 * @param businessDate model class with information of
	 *                      Capacity template business date.
	 *                      
	 * @param createdBy String with information of user.
	 * 
	 * @param dateTime Instant with information of dateTime.
	 * 
	 * @return CapacityTemplateAndBusinessDateEntity mapped entity class of Capacity  
	 * 								and business date.
	 */
	@Named(CapacityConstants.MAP_TO_BUSSINESS_DATE)
	default CapacityTemplateAndBusinessDateEntity mapToBusinessDate(CapacityTemplateEntity createdTemplateEntity,
			BusinessDate t, String createdBy, Instant dateTime) {
		CapacityTemplateAndBusinessDateEntity templateDate = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(createdTemplateEntity.getCapacityTemplateId());
		id.setBusinessDate(DateUtil.stringToDate(t.getDate()));
		templateDate.setId(id);
		templateDate.setCreatedBy(createdBy);
		templateDate.setCreatedDatetime(dateTime);
		templateDate.setLastModifiedBy(createdBy);
		templateDate.setLastModifiedDatetime(dateTime);
		return templateDate;
	}

	/**
	 * Method to map data to CapacitySlotEntity
	 * 
	 * @param createdTemplateEntity entity class with information of
	 *                     Capacity template type detail.
	 * 
	 * @param reference entity class with information of
	 *                     reference type detail.
	 * 
	 * @param slotTypeEntity entity class with information of
	 *                     Capacity slot type detail.
	 * 
	 * @param channelEntity entity class with information of
	 *                     Capacity channel detail.
	 * 
	 * @param slotChannel model class with information of
	 *                     Capacity slot channel.
	 * 
	 * @param slotDetail model class with information of
	 *                     Capacity slot detail.
	 * 
	 * @param createdBy String with information of user.
	 * 
	 * @param dateTime Instant with information of dateTime.
	 * 
	 * @return CapacitySlotEntity mapped entity class of Capacity Slot.
	 */
	@Named(CapacityConstants.MAP_TO_SLOT_ENTITY)
	default CapacitySlotEntity mapToSlot(CapacityTemplateEntity createdTemplateEntity,
			Optional<ReferenceEntity> reference, Optional<CapacitySlotTypeEntity> slotTypeEntity,
			Optional<CapacityChannelEntity> channelEntity, SlotChannel t, SlotDetail s, String createdBy) {
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacitySlotEntity slotEntity = new CapacitySlotEntity();
		slotEntity.setCapacityTemplate(createdTemplateEntity);
		slotEntity.setIsDeletedFlg(CapacityConstants.N);
		slotEntity.setCreatedBy(createdBy);
		slotEntity.setCreatedDatetime(dateTime);
		slotEntity.setLastModifiedBy(createdBy);
		slotEntity.setLastModifiedDatetime(dateTime);
		reference.ifPresent(slotEntity::setReference);
		channelEntity.ifPresent(slotEntity::setCapacityChannel);
		slotEntity.setStartTime(LocalTime.parse(s.getStartTime()));
		slotEntity.setEndTime(LocalTime.parse(s.getEndTime()));
		slotEntity.setCapacityCnt(s.getCapacityCount());
		slotTypeEntity.ifPresent(slotEntity::setCapacitySlotType);
		return slotEntity;
	}

	/**
	 * Method to map data to CapacityTemplateAndCapacityChannelEntity
	 * 
	 * @param createdTemplateEntity entity class with information of
	 *                     Capacity template detail.
	 *                     
	 * @param channelEntity entity class with information of
	 *                     Capacity channel detail.
	 *                     
	 * @param slotChannel  model class with information of
	 *                     Capacity slot detail.
	 *                     
	 * @param createdBy String with information of user.
	 * 
	 * @param dateTime Instant with information of dateTime.
	 * 
	 * @return CapacityTemplateAndCapacityChannelEntity mapped entity class of 
	 * 							Capacity Template And Capacity Channel.
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_CHANNEL_ENTITY)
	default CapacityTemplateAndCapacityChannelEntity mapToTemplateAndChannelEntity(
			CapacityTemplateEntity createdTemplateEntity, Optional<CapacityChannelEntity> channelEntity,
			SlotChannel t, String createdBy, Instant dateTime) {
		CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new CapacityTemplateAndCapacityChannelEntity();
		CapacityTemplateAndCapacityChannelPK id = new CapacityTemplateAndCapacityChannelPK();
		id.setCapacityTemplateId(createdTemplateEntity.getCapacityTemplateId());
		id.setCapacityChannelId(t.getChannelId());
		capacityTemplateAndCapacityChannelEntity.setId(id);
		capacityTemplateAndCapacityChannelEntity.setCreatedBy(createdBy);
		capacityTemplateAndCapacityChannelEntity.setCreatedDatetime(dateTime);
		capacityTemplateAndCapacityChannelEntity.setLastModifiedBy(createdBy);
		capacityTemplateAndCapacityChannelEntity.setLastModifiedDatetime(dateTime);
		return capacityTemplateAndCapacityChannelEntity;
	}

	/**
	 * Method to map data to CreateTemplateResponse
	 * 
	 * @param createdTemplateEntity entity class with information of
	 *                     Capacity template detail.
	 *                     
	 * @param responseDate list of model class with information of
	 *                     Capacity template detail.
	 * 
	 * @param responseChannelList list of model class with information of
	 *                     Capacity slot and channel detail.
	 * 
	 * @return CreateTemplateResponse  mapped model class of 
	 * 							Capacity Template.
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_RESPONSE)
	default CreateTemplateResponse mapToCreateTemplateResponse(CapacityTemplateEntity createdTemplateEntity,
			List<BusinessDate> responseDate, List<SlotChannel> responseChannelList,
			CreateCapacityTemplateRequest templateRequest) {
		CreateTemplateResponse createTemplateResponse = mapCreateResponse(createdTemplateEntity);
		createTemplateResponse.setTemplateTypeId(templateRequest.getTemplateTypeId());
		createTemplateResponse.setTemplateTypeName(templateRequest.getTemplateTypeName());
		createTemplateResponse.setBusinessDates(responseDate);
		createTemplateResponse.setSlotChannels(responseChannelList);
		return createTemplateResponse;
	}

	/**
	 * Method to map data to SlotDetail
	 * 
	 * @param savedSlot model class with information of
	 *            saved Capacity slot detail.
	 * 
	 * @param s model class with information of
	 *                Capacity slot detail.
	 * 
	 * @return SlotDetail  mapped model class of 
	 * 							Capacity slot detail.
	 */
	@Named(CapacityConstants.MAP_TO_SLOT)
	default SlotDetail mapToResponseSlot(CapacitySlotEntity savedSlot, SlotDetail s) {
		SlotDetail responseSlot = new SlotDetail();
		responseSlot.setCapacityCount(savedSlot.getCapacityCnt());
		responseSlot.setEndTime(savedSlot.getEndTime().toString());
		responseSlot.setStartTime(savedSlot.getStartTime().toString());
		responseSlot.setSlotId(savedSlot.getCapacitySlotId());
		responseSlot.setSlotTypeId(s.getSlotTypeId());
		responseSlot.setIsDeletedFlg(savedSlot.getIsDeletedFlg());
		return responseSlot;
	}
	
	/**
	 * Method is used to map response for  CapacityTemplate.
	 * 
	 * @param createdTemplateEntity entity class with information of
	 *            saved Capacity template detail.
	 * 
	 * @return CreateTemplateResponse mapped model class of 
	 * 			Create Template Response detail.
	 */
	@Named(CapacityConstants.MAPCREATETEMPLATERESPONSE)
	default CreateTemplateResponse mapCreateResponse(CapacityTemplateEntity createdTemplateEntity) {
		CreateTemplateResponse createTemplateResponse = new CreateTemplateResponse();
		createTemplateResponse.setCapacityTemplateId(createdTemplateEntity.getCapacityTemplateId());
		createTemplateResponse.setCapacityTemplateName(createdTemplateEntity.getCapacityTemplateNm());
		createTemplateResponse.setConceptId(createdTemplateEntity.getConceptId());
		createTemplateResponse.setIsDeletedFlag(createdTemplateEntity.getIsDeletedFlg());
		if(createdTemplateEntity.getExpiryDate() != null) {
			createTemplateResponse.setExpiryDate(createdTemplateEntity.getExpiryDate().toString());
			createTemplateResponse.setEffectiveDate(createdTemplateEntity.getEffectiveDate().toString());
		}
		createTemplateResponse.setSunDay(createdTemplateEntity.getSunFlg());
		createTemplateResponse.setMonDay(createdTemplateEntity.getMonFlg());
		createTemplateResponse.setTueDay(createdTemplateEntity.getTueFlg());
		createTemplateResponse.setWedDay(createdTemplateEntity.getWedFlg());
		createTemplateResponse.setThuDay(createdTemplateEntity.getThuFlg());
		createTemplateResponse.setFriDay(createdTemplateEntity.getFriFlg());
		createTemplateResponse.setSatDay(createdTemplateEntity.getSatFlg());
		createTemplateResponse.setSlotStartTime(createdTemplateEntity.getStartTime().toString());
		createTemplateResponse.setSlotEndTime(createdTemplateEntity.getEndTime().toString());
		createTemplateResponse.setCreatedBy(createdTemplateEntity.getCreatedBy());
		createTemplateResponse.setCreatedDateTime(createdTemplateEntity.getCreatedDatetime());
		createTemplateResponse.setLastModifiedBy(createdTemplateEntity.getLastModifiedBy());
		createTemplateResponse.setLastModifiedDateTime(createdTemplateEntity.getLastModifiedDatetime());
		return createTemplateResponse;
	}
	
	/**
	 * This mapper method is used to map data to capacity template slot entity class.
	 * 
	 * @param cs entity class containing the value of capacity slot.
	 * 
	 * @return SlotDetail model class containing the value of slot detail.
	 */
	@Named(CapacityConstants.MAPTOUPDATECAPACITYTEMPLATESLOTS)
	default SlotDetail mapToUpdateCapacityTemplateSlots(CapacitySlotEntity cs) {
		SlotDetail slotDetail = new SlotDetail();
		slotDetail.setSlotId(cs.getCapacitySlotId());
		slotDetail.setSlotTypeId(String.valueOf(cs.getCapacitySlotType().getCapacitySlotTypeId()));
		slotDetail.setStartTime(String.valueOf(cs.getStartTime()));
		slotDetail.setEndTime(String.valueOf(cs.getEndTime()));
		slotDetail.setIsDeletedFlg(cs.getIsDeletedFlg());
		slotDetail.setCapacityCount(cs.getCapacityCnt());
		return slotDetail;
	}

	/**
	 * This mapper method is used to map data to capacity template slot channel model class.
	 * 
	 * @param channelSlotDetails list of  model class containing the value of slot detail.
	 * 
	 * @param channelIds Set of string containing the value of channel id.
	 * 
	 * @param isSeletedFlags Map containing key value pair as string and string
	 *            containing the value of is selected flag.
	 *            
	 * @return List<SlotChannel> list of model class containing the value of
	 * 			  slot channel detail.
	 */
	@Named(CapacityConstants.MAPTOUPDATESLOTCHANNELRESPONSE)
	default List<SlotChannel> mapToUpdateSlotChannelResponse(MultiValuedMap<String, SlotDetail> channelSlotDetails,
			Set<String> channelIds, Map<String, String> isSeletedFlags) {
		List<SlotChannel> slotChannels = new ArrayList<>();
		channelIds.stream().filter(StringUtils::isNotBlank).forEach(channelId -> {
			List<SlotDetail> slotDetails = new ArrayList<>();
			SlotChannel slotChannel = new SlotChannel();
			slotChannel.setChannelId(new BigInteger(channelId));
			slotChannel.setIsSelectedFlag(isSeletedFlags.get(channelId));
			slotDetails.addAll(channelSlotDetails.get(channelId));
			slotChannel.setSlotDetails(slotDetails);
			slotChannels.add(slotChannel);
		});
		return slotChannels;
	}
	
	/**
	 * This mapper method is used to map data to capacity template entity class.
	 * 
	 * @param templateRequest model class containing the value of capacity 
	 * 					template to be created.
	 * 
	 * @param existingTemplate entity class containing the value of capacity 
	 * 					template.
	 */
	@Named(CapacityConstants.MAPTEMPLATEDAYSFROMTEMPLATECREATEUPDATEREQUEST)
	default void mapTemplateDaysFromTemplateCreateUpdateRequest(CreateCapacityTemplateRequest templateRequest,
			CapacityTemplateEntity existingTemplate) {
		existingTemplate.setSunFlg(templateRequest.getSunDay());
		existingTemplate.setMonFlg(templateRequest.getMonDay());
		existingTemplate.setTueFlg(templateRequest.getTueDay());
		existingTemplate.setWedFlg(templateRequest.getWedDay());
		existingTemplate.setThuFlg(templateRequest.getThuDay());
		existingTemplate.setFriFlg(templateRequest.getFriDay());
		existingTemplate.setSatFlg(templateRequest.getSatDay());
	}
	
	/**
	 * This mapper method is used to map data to capacity template entity class.
	 * 
	 * @param existingTemplate  entity class containing the value of capacity 
	 * 					template.
	 */
	@Named(CapacityConstants.SETTEMPLATEDAYSTONULLVALUE)
	default void setTemplateDaysToNullValue(CapacityTemplateEntity existingTemplate) {
		existingTemplate.setSunFlg(CapacityConstants.NULL);
		existingTemplate.setMonFlg(CapacityConstants.NULL);
		existingTemplate.setTueFlg(CapacityConstants.NULL);
		existingTemplate.setWedFlg(CapacityConstants.NULL);
		existingTemplate.setThuFlg(CapacityConstants.NULL);
		existingTemplate.setFriFlg(CapacityConstants.NULL);
		existingTemplate.setSatFlg(CapacityConstants.NULL);
	}
	
	/**
	 * This mapper method is used to map data to capacity template slot entity class.
	 * 
	 * @param createdBy String with information of user.
	 * 
	 * @param dateTime Instant with information of dateTime.
	 * 
	 * @param existingTemplate entity class containing the value of capacity 
	 * 					template.
	 * 
	 * @param channelEntity entity class containing the value of capacity
	 * 					channel.
	 * 
	 * @param slotDetailReq model class containing the detail of capacity
	 * 					slot.
	 * 
	 * @param capacitySlotTypeEntity entity class containing the value of
	 * 					capacity slot type.
	 * 
	 * @param reference optional of entity class containing value of reference.
	 * 
	 * @return CapacitySlotEntity entity class containing the value of
	 * 					capacity type.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYSLOTENTITY)
	default CapacitySlotEntity mapToCapacitySlotEntity(String createdBy, Instant dateTime,
			CapacityTemplateEntity existingTemplate, Optional<CapacityChannelEntity> channelEntity,
			SlotDetail slotDetailReq, Optional<CapacitySlotTypeEntity> capacitySlotTypeEntity,
			Optional<ReferenceEntity> reference) {
		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		channelEntity.ifPresent(capacitySlotEntity::setCapacityChannel);
		capacitySlotEntity.setStartTime(LocalTime.parse(slotDetailReq.getStartTime()));
		capacitySlotEntity.setEndTime(LocalTime.parse(slotDetailReq.getEndTime()));
		capacitySlotEntity.setCapacityCnt(slotDetailReq.getCapacityCount());
		capacitySlotTypeEntity.ifPresent(capacitySlotEntity::setCapacitySlotType);
		capacitySlotEntity.setCreatedBy(createdBy);
		capacitySlotEntity.setCreatedDatetime(dateTime);
		capacitySlotEntity.setLastModifiedBy(createdBy);
		capacitySlotEntity.setLastModifiedDatetime(dateTime);
		capacitySlotEntity.setIsDeletedFlg(CapacityConstants.N);
		capacitySlotEntity.setCapacityTemplate(existingTemplate);
		reference.ifPresent(capacitySlotEntity::setReference);
		return capacitySlotEntity;
	}
	
	/**
	 * This mapper method is used to map data to capacity template model class.
	 * 
	 * @param capacityTemplateEntity entity class containing the value of capacity 
	 * 					template.
	 * 
	 * @param capacityTemplateModel model class containing the value of capacity 
	 * 					template.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYTEMPLATEFROMENTITY)
	default void mapToCapacityTemplateFromEntity(CapacityTemplateEntity capacityTemplateEntity,
			CapacityTemplate capacityTemplateModel) {
		capacityTemplateModel.setSunDay(capacityTemplateEntity.getSunFlg());
		capacityTemplateModel.setMonDay(capacityTemplateEntity.getMonFlg());
		capacityTemplateModel.setTueDay(capacityTemplateEntity.getTueFlg());
		capacityTemplateModel.setWedDay(capacityTemplateEntity.getWedFlg());
		capacityTemplateModel.setThuDay(capacityTemplateEntity.getThuFlg());
		capacityTemplateModel.setFriDay(capacityTemplateEntity.getFriFlg());
		capacityTemplateModel.setSatDay(capacityTemplateEntity.getSatFlg());
	}
	
	/**
	 * 
	 * This method is used to map capacity channel from capacity template entity
	 * 
	 * @param capacityTemplateEntity entity class containing the detail of capacity 
	 * 									template.
	 * 
	 * @return List<Channel> list of model class containing the detail of channel.
	 */
	@Named(CapacityConstants.GETCAPACITYTEMPLATECHANNELS)
	default List<Channel> getCapacityTemplateChannels(CapacityTemplateEntity capacityTemplateEntity) {
		List<Channel> channels = new ArrayList<>();
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntity = capacityTemplateEntity
				.getCapacityTemplateAndCapacityChannels();
		capacityTemplateAndCapacityChannelEntity.stream().filter(Objects::nonNull).forEach(ctc -> {
			Channel channel = new Channel();
			channel.setCapacityChannelId(ctc.getCapacityChannel().getCapacityChannelId());
			channel.setCapacityChannelName(ctc.getCapacityChannel().getCapacityChannelNm());
			channel.setIsSelectedFlag(CapacityConstants.Y);
			channels.add(channel);
		});
		return channels;
	}
	
	/**
	 * 
	 * This method is used to map the slots corresponding to this template Id and
	 * channel Id
	 * 
	 * @param capacitySlots list of entity class with the detail of capacity slot.
	 * 
	 * @param channelSlotDetails model class containing the value of channel
	 * 							slot details.
	 * 
	 * @param channelIds Set of string containing the channel Id.
	 * 
	 * @param channelNames String containing value of channel names.
	 */
	@Named(CapacityConstants.MAPCAPACITYSLOTS)
	default void mapCapacitySlots(List<CapacitySlotEntity> capacitySlots,
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
			slotDetail.setCapacityCount(cs.getCapacityCnt());
			channelSlotDetails.put(String.valueOf(cs.getCapacityChannel().getCapacityChannelId()), slotDetail);
		});
	}
	
	/**
	 * This method is used to group slot channels using channel id
	 * 
	 * @param channelSlotDetails model class containing the value of channel
	 * 							slot details.
	 * 
	 * @param channelIds Set of string containing the channel Id.
	 * 
	 * @param channelNames String containing value of channel names.
	 * 
	 * @return List<SlotChannel> returning the list of model class with
	 * 							mapped values.
	 */
	@Named(CapacityConstants.MAPSLOTCHANNELS)
	default List<SlotChannel> mapSlotChannels(MultiValuedMap<String, SlotDetail> channelSlotDetails,
			Set<String> channelIds, Map<String, String> channelNames) {
		List<SlotChannel> slotChannels = new ArrayList<>();
		channelIds.stream().filter(StringUtils::isNotBlank).forEach(channelId -> {
			List<SlotDetail> slotDetails = new ArrayList<>();
			SlotChannel slotChannel = new SlotChannel();
			slotChannel.setChannelId(new BigInteger(channelId));
			slotChannel.setChannelName(channelNames.get(channelId));
			slotDetails.addAll(channelSlotDetails.get(channelId));
			slotChannel.setSlotDetails(slotDetails);
			slotChannels.add(slotChannel);
		});
		return slotChannels;
	}
	
	/** 
	 *  This method is to  retrieve the capacity template type and constructing the response based on
	 *  capacity template type details 
	 * 
	 * @param response model class containing the value of Capacity Template.
	 * 
	 * @param test entity class containing the value of Capacity Template.
	 */
	@Named(CapacityConstants.MAPTEMPLATETYPERESPONSE)
	default void mapTemplateTypeResponse(CreateTemplateResponse response, CapacityTemplateEntity test) {
		CapacityTemplateTypeEntity  type=test.getCapacityTemplateType();
		if(type!=null) {
		response.setTemplateTypeId(type.getCapacityTemplateTypeId());
		response.setTemplateTypeName(type.getCapacityTemplateTypeNm());
		}
	}

}
