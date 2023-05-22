package com.darden.dash.capacity.boh.mapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateSlotEntity;
import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.RestaurantChannel;
import com.darden.dash.capacity.boh.model.RestaurantSlotChannel;
import com.darden.dash.capacity.boh.model.RestaurantSlotDetail;
import com.darden.dash.capacity.util.CapacityConstants;

@Mapper
public interface RestaurantCapacityTemplateMapper {
	
	@Mapping(target = CapacityConstants.RESTAURANT_CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.RESTAURANT_SLOT_CHANNELS, ignore = true)
	//@Mapping(target = CapacityConstants.SLOT_END_TIME, ignore = true)
	//@Mapping(target = CapacityConstants.SLOT_START_TIME, ignore = true)
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
	@Mapping(source = "resturantTemplateNm", target = CapacityConstants.TEMPLATE_NAME)
	RestaurantCapacityTemplate map(RestaurantTemplateEntity capacityTemplate);
	
	@Mapping(target = CapacityConstants.RESTAURANT_CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.RESTAURANT_SLOT_CHANNELS, ignore = true)
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
	@Mapping(source = "resturantTemplateNm", target = CapacityConstants.TEMPLATE_NAME)
	RestaurantCapacityTemplate mapRestaurantTemplate(RestaurantTemplateEntity capacityTemplate);

	default List<RestaurantChannel> getRestaurantTemplateChannels(RestaurantTemplateEntity capacityTemplateEntity) {
		List<RestaurantChannel> channels = new ArrayList<>();
		List<RestaurantTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntity = capacityTemplateEntity
				.getRestaurantTemplateAndCapacityChannels();
		capacityTemplateAndCapacityChannelEntity.stream().filter(Objects::nonNull).forEach(ctc -> {
			RestaurantChannel channel = new RestaurantChannel();
			channel.setRestaurantChannelId((ctc.getCapacityChannel().getCapacityChannelId()));
			channel.setRestaurantChannelName((ctc.getCapacityChannel().getCapacityChannelNm()));
			channel.setIsSelectedFlag(CapacityConstants.Y);
			channels.add(channel);
		});
		return channels;
	}

	default void mapCapacitySlots(List<RestaurantTemplateSlotEntity> capacitySlots,
			MultiValuedMap<String, RestaurantSlotDetail> channelSlotDetails, Set<String> channelIds,
			Map<String, String> channelNames) {
		capacitySlots.stream().filter(Objects::nonNull).forEach(cs -> {
			String channelId = String.valueOf(cs.getCapacityChannel().getCapacityChannelId());
			channelIds.add(channelId);
			channelNames.put(channelId, cs.getCapacityChannel().getCapacityChannelNm());
			RestaurantSlotDetail slotDetail = new RestaurantSlotDetail();
			slotDetail.setSlotId(cs.getRestaurantSlotId());
			slotDetail.setSlotTypeId(String.valueOf(cs.getRestaurantTemplateSlotType().getRestaurantTemplateSlotTypeId()));
			slotDetail.setStartTime(String.valueOf(cs.getStartTime()));
			slotDetail.setEndTime(String.valueOf(cs.getEndTime()));
			slotDetail.setCapacityCount(cs.getCapacityCnt());
			channelSlotDetails.put(String.valueOf(cs.getCapacityChannel().getCapacityChannelId()), slotDetail);
		});
	}
	
	@Named(CapacityConstants.MAPSLOTCHANNELS)
	default List<RestaurantSlotChannel> mapSlotChannels(MultiValuedMap<String, RestaurantSlotDetail> channelSlotDetails,
			Set<String> channelIds, Map<String, String> channelNames) {
		List<RestaurantSlotChannel> slotChannels = new ArrayList<>();
		channelIds.stream().filter(StringUtils::isNotBlank).forEach(channelId -> {
			List<RestaurantSlotDetail> slotDetails = new ArrayList<>();
			RestaurantSlotChannel slotChannel = new RestaurantSlotChannel();
			slotChannel.setChannelId(new BigInteger(channelId));
			slotChannel.setChannelName(channelNames.get(channelId));
			slotDetails.addAll(channelSlotDetails.get(channelId));
			slotChannel.setRestaurantSlotDetails(slotDetails);
			slotChannels.add(slotChannel);
		});
		return slotChannels;
	}
	
	default void mapToCapacityRestaurantTemplateFromEntity(RestaurantTemplateEntity capacityTemplateEntity,
			RestaurantCapacityTemplate capacityTemplateModel) {
		capacityTemplateModel.setSunDay(capacityTemplateEntity.getSunFlg());
		capacityTemplateModel.setMonDay(capacityTemplateEntity.getMonFlg());
		capacityTemplateModel.setTueDay(capacityTemplateEntity.getTueFlg());
		capacityTemplateModel.setWedDay(capacityTemplateEntity.getWedFlg());
		capacityTemplateModel.setThuDay(capacityTemplateEntity.getThuFlg());
		capacityTemplateModel.setFriDay(capacityTemplateEntity.getFriFlg());
		capacityTemplateModel.setSatDay(capacityTemplateEntity.getSatFlg());
	}
}
