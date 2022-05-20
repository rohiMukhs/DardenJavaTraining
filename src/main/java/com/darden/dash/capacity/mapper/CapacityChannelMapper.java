package com.darden.dash.capacity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.util.CapacityConstants;

/**
 * @author skashala
 * @date 16-May-2022
 * 
 * This class is used to map the capacity channel entity to
 *         capacity channel model class
 */
@Mapper
public interface CapacityChannelMapper {

	/**
	 * This method is used to map the list of CapacityChannelEntity to list of CapacityChannel
	 * 
	 * @param capacityChannelEntites
	 * @return List<CapacityChannel>
	 */
	List<CapacityChannel> mapChannels(List<CapacityChannelEntity> capacityChannelEntites);
	
	@Mapping(source = CapacityConstants.MAPPER_FIRENDLY_NM, target = CapacityConstants.MAPPER_FIRENDLY_NAME)
	@Mapping(source = CapacityConstants.MAPPER_CAPACITY_CHANNEL_NM, target = CapacityConstants.MAPPER_CAPACITY_CHANNEL_NAME)
	@Mapping(target = CapacityConstants.MAPPER_OPERATIONAL_HOURS_START_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAPPER_OPERATIONAL_HOURS_END_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAPPER_COMBINED_CHANNELS, ignore = true)
	CapacityChannel map(CapacityChannelEntity capacityChannelEntity);

	/**
	 * This method is to map capacity channel entity to capacity channel for converting 
	 * date format to string format for specific fields
	 * 
	 * @param capacityChannelEntity
	 * @param capacityChannel
	 */
	@AfterMapping
	default void map(CapacityChannelEntity capacityChannelEntity, @MappingTarget CapacityChannel capacityChannel) {
		capacityChannel
				.setOperationalHoursStartTime(String.valueOf(capacityChannelEntity.getOperationalHoursStartTime()));
		capacityChannel.setOperationalHoursEndTime(String.valueOf(capacityChannelEntity.getOperationalHoursEndTime()));
		if (CapacityConstants.Y.equalsIgnoreCase(capacityChannelEntity.getIsCombinedFlg())) {
			List<CapacityChannelAndCombinedChannelEntity> combinedChannelsEntity = capacityChannelEntity
					.getCapacityChannelAndCombinedChannels1();
			List<Channel> combinedChannels = new ArrayList<>();
			combinedChannelsEntity.stream().filter(Objects::nonNull).forEach(t -> {
				Channel channel = new Channel();
				channel.setCapacityChannelId(t.getCapacityChannel2().getCapacityChannelId());
				channel.setCapacityChannelName(t.getCapacityChannel2().getCapacityChannelNm());
				combinedChannels.add(channel);
			});
			capacityChannel.setCombinedChannels(combinedChannels);
		}
	}

}
