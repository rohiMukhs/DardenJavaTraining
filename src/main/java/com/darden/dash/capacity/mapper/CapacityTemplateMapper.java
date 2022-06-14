package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.util.DateUtil;
import com.darden.dash.common.RequestContext;

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
	@Mapping(source = CapacityConstants.MAP_SUN_FLG, target = CapacityConstants.SUN_DAY)
	@Mapping(source = CapacityConstants.MAP_MON_FLG, target = CapacityConstants.MON_DAY)
	@Mapping(source = CapacityConstants.MAP_TUE_FLG, target = CapacityConstants.TUE_DAY)
	@Mapping(source = CapacityConstants.MAP_WED_FLG, target = CapacityConstants.WED_DAY)
	@Mapping(source = CapacityConstants.MAP_THU_FLG, target = CapacityConstants.THU_DAY)
	@Mapping(source = CapacityConstants.MAP_FRI_FLG, target = CapacityConstants.FRI_DAY)
	@Mapping(source = CapacityConstants.MAP_SAT_FLG, target = CapacityConstants.SAT_DAY)
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
		capacityModel.setEffectiveDate(DateUtil.instantToDate(capacityEntity.getEffectiveDate()));
		capacityModel.setExpiryDate(DateUtil.instantToDate(capacityEntity.getExpiryDate()));
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
		templateEntity.setExpiryDate(DateUtil.stringToDate(templateRequest.getExpiryDate()));
		templateEntity.setEffectiveDate(DateUtil.stringToDate(templateRequest.getEffectiveDate()));
		templateEntity.setCapacityTemplateType(templateType);
		templateEntity.setStartTime(LocalTime.parse(templateRequest.getSlotStartTime()));
		templateEntity.setEndTime(LocalTime.parse(templateRequest.getSlotEndTime()));
		if (null != templateType.getCapacityTemplateTypeNm() && CapacityConstants.DAYS.equals(templateType.getCapacityTemplateTypeNm())) {
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
		capacityTemplateAndCapacityChannelEntity.setIsSelectedFlag(CapacityConstants.Y);
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
	default CreateTemplateResponse mapCreateResponse(CapacityTemplateEntity createdTemplateEntity) {
		CreateTemplateResponse createTemplateResponse = new CreateTemplateResponse();
		createTemplateResponse.setCapacityTemplateId(createdTemplateEntity.getCapacityTemplateId());
		createTemplateResponse.setCapacityTemplateName(createdTemplateEntity.getCapacityTemplateNm());
		createTemplateResponse.setConceptId(createdTemplateEntity.getConceptId());
		createTemplateResponse.setIsDeletedFlag(createdTemplateEntity.getIsDeletedFlg());
		createTemplateResponse.setEffectiveDate(createdTemplateEntity.getEffectiveDate().toString());
		createTemplateResponse.setExpiryDate(createdTemplateEntity.getExpiryDate().toString());
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

}
