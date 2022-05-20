package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateResponseSlot;
import com.darden.dash.capacity.model.CreateTemplateResponse;
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
	 * @param capacityTemplate
	 * @return CapacityTemplate
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
	 * @param capacityEntity
	 * @param capacityModel
	 */
	@AfterMapping
	default void map(CapacityTemplateEntity capacityEntity, @MappingTarget CapacityTemplate capacityModel) {
		capacityModel.setEffectiveDate(DateUtil.instantToDate(capacityEntity.getEffectiveDate()));
		capacityModel.setExpiryDate(DateUtil.instantToDate(capacityEntity.getExpiryDate()));
	}

	/**
	 * Method to map data to capacityTemplateEntity
	 * 
	 * @param templateRequest
	 * @param templateType
	 * @param createdBy
	 * @param dateTime
	 * @return CapacityTemplateEntity
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_ENTITY)
	default CapacityTemplateEntity mapToTemplate(CreateCapacityTemplateRequest templateRequest,
			Optional<CapacityTemplateTypeEntity> templateType, String createdBy, Instant dateTime) {
		CapacityTemplateEntity templateEntity = new CapacityTemplateEntity();
		templateEntity.setCapacityTemplateNm(templateRequest.getCapacityTemplateName());
		templateEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		templateEntity.setIsDeletedFlg(templateRequest.getIsDeletedFlag());
		templateEntity.setCreatedBy(createdBy);
		templateEntity.setCreatedDatetime(dateTime);
		templateEntity.setLastModifiedBy(createdBy);
		templateEntity.setLastModifiedDatetime(dateTime);
		templateEntity.setExpiryDate(DateUtil.stringToDate(templateRequest.getEffectiveDate()));
		templateEntity.setEffectiveDate(DateUtil.stringToDate(templateRequest.getExpiryDate()));
		templateType.ifPresent(templateEntity::setCapacityTemplateType);
		templateEntity.setStartTime(LocalTime.parse(templateRequest.getSlotStartTime()));
		templateEntity.setEndTime(LocalTime.parse(templateRequest.getSlotEndTime()));
		String templateName = templateType.map(CapacityTemplateTypeEntity::getCapacityTemplateTypeNm)
				.orElse(StringUtils.EMPTY);
		if (CapacityConstants.DAYS.equals(templateName)) {
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
	 * @param createdTemplateEntity
	 * @param t
	 * @param createdBy
	 * @param dateTime
	 * @return CapacityTemplateAndBusinessDateEntity
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
	 * @param createdTemplateEntity
	 * @param reference
	 * @param slotTypeEntity
	 * @param channelEntity
	 * @param t
	 * @param s
	 * @param createdBy
	 * @param dateTime
	 * @return CapacitySlotEntity
	 */
	@Named(CapacityConstants.MAP_TO_SLOT_ENTITY)
	default CapacitySlotEntity mapToSlot(CapacityTemplateEntity createdTemplateEntity,
			Optional<ReferenceEntity> reference, Optional<CapacitySlotTypeEntity> slotTypeEntity,
			Optional<CapacityChannelEntity> channelEntity, CreateResponseSlot t, SlotDetail s, String createdBy) {
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
	 * @param createdTemplateEntity
	 * @param channelEntity
	 * @param t
	 * @param createdBy
	 * @param dateTime
	 * @return CapacityTemplateAndCapacityChannelEntity
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_CHANNEL_ENTITY)
	default CapacityTemplateAndCapacityChannelEntity mapToTemplateAndChannelEntity(
			CapacityTemplateEntity createdTemplateEntity, Optional<CapacityChannelEntity> channelEntity,
			CreateResponseSlot t, String createdBy, Instant dateTime) {
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
	 * @param createdTemplateEntity
	 * @param responseDate
	 * @param responseChannelList
	 * @return CreateTemplateResponse
	 */
	@Named(CapacityConstants.MAP_TO_TEMPLATE_RESPONSE)
	default CreateTemplateResponse mapToCreateTemplateResponse(CapacityTemplateEntity createdTemplateEntity,
			List<BusinessDate> responseDate, List<CreateResponseSlot> responseChannelList,
			CreateCapacityTemplateRequest templateRequest) {
		CreateTemplateResponse createTemplateResponse = new CreateTemplateResponse();
		createTemplateResponse.setCapacityTemplateId(createdTemplateEntity.getCapacityTemplateId());
		createTemplateResponse.setCapacityTemplateName(createdTemplateEntity.getCapacityTemplateNm());
		createTemplateResponse.setConceptId(createdTemplateEntity.getConceptId());
		createTemplateResponse.setIsDeletedFlag(createdTemplateEntity.getIsDeletedFlg());
		createTemplateResponse.setEffectiveDate(createdTemplateEntity.getEffectiveDate().toString());
		createTemplateResponse.setExpiryDate(createdTemplateEntity.getExpiryDate().toString());
		createTemplateResponse.setTemplateTypeId(templateRequest.getTemplateTypeId());
		createTemplateResponse.setTemplateTypeName(templateRequest.getTemplateTypeName());
		createTemplateResponse.setMonDay(createdTemplateEntity.getMonFlg());
		createTemplateResponse.setTueDay(createdTemplateEntity.getTueFlg());
		createTemplateResponse.setWedDay(createdTemplateEntity.getWedFlg());
		createTemplateResponse.setThuDay(createdTemplateEntity.getThuFlg());
		createTemplateResponse.setFriDay(createdTemplateEntity.getFriFlg());
		createTemplateResponse.setSatDay(createdTemplateEntity.getSatFlg());
		createTemplateResponse.setBusinessDates(responseDate);
		createTemplateResponse.setSlotStartTime(createdTemplateEntity.getStartTime().toString());
		createTemplateResponse.setSlotEndTime(createdTemplateEntity.getEndTime().toString());
		createTemplateResponse.setCreatedBy(createdTemplateEntity.getCreatedBy());
		createTemplateResponse.setCreatedDateTime(createdTemplateEntity.getCreatedDatetime());
		createTemplateResponse.setLastModifiedBy(createdTemplateEntity.getLastModifiedBy());
		createTemplateResponse.setLastModifiedDateTime(createdTemplateEntity.getLastModifiedDatetime());
		createTemplateResponse.setSlotChannels(responseChannelList);
		return createTemplateResponse;
	}

	/**
	 * Method to map data to SlotDetail
	 * 
	 * @param savedSlot
	 * @param s
	 * @return SlotDetail
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

}
