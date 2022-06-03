package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelPK;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;

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
				channel.setIsSelectedFlag(CapacityConstants.Y);
				combinedChannels.add(channel);
			});
			capacityChannel.setCombinedChannels(combinedChannels);
		}
	}
	
	/**
	 * This mapper method is used to map the data for creating a combine channel
	 * to capacityChannelEntity.
	 * 
	 * @param createCombinedChannelRequest
	 * @param userDetail
	 * @param dateTime
	 * @return CapacityChannelEntity
	 */
	@Named(CapacityConstants.MAP_TO_CHANNEL_ENTITY)
	default CapacityChannelEntity mapToChannelEntity(CreateCombineChannelRequest createCombinedChannelRequest, String userDetail, Instant dateTime) {
		CapacityChannelEntity capacityChannel=new CapacityChannelEntity();
		capacityChannel.setCapacityChannelNm(createCombinedChannelRequest.getCombinedChannelName());
		capacityChannel.setIsDeletedFlg(CapacityConstants.N);
		capacityChannel.setIsCombinedFlg(CapacityConstants.Y);
		capacityChannel.setConceptId(new BigInteger(RequestContext.getConcept()));
		capacityChannel.setFirendlyNm(createCombinedChannelRequest.getFriendlyName());
		capacityChannel.setInterval(createCombinedChannelRequest.getInterval());
		capacityChannel.setOperationalHoursStartTime(Time.valueOf(LocalTime.parse(createCombinedChannelRequest.getStartTime())));
		capacityChannel.setOperationalHoursEndTime(Time.valueOf(LocalTime.parse(createCombinedChannelRequest.getEndTime())));
		capacityChannel.setLastModifiedBy(userDetail);
		capacityChannel.setLastModifiedDatetime(dateTime);
		capacityChannel.setCreatedBy(userDetail);
		capacityChannel.setCreatedDatetime(dateTime);
		return capacityChannel;
	}
	
	/**
	 * This mapper method is used to map the data of channel to be assigned to 
	 * Combine channel to CapacityChannelAndCombinedChannelEntity
	 * 
	 * @param savedCombinedCapacityChannel
	 * @param capacityChannelFromDB
	 * @param changedBy
	 * @return CapacityChannelAndCombinedChannelEntity
	 */
	@Named(CapacityConstants.MAP_TO_CHANNEL_AND_COMBINED_CHANNEL_ENTITY)
	default CapacityChannelAndCombinedChannelEntity mapToChannelAndCombineChannel(CapacityChannelEntity savedCombinedCapacityChannel, CapacityChannelEntity capacityChannelFromDB, String changedBy) {
		CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelAndCombinedChannelPK id = new CapacityChannelAndCombinedChannelPK();
		id.setCombinedCapacityChannelId(savedCombinedCapacityChannel.getCapacityChannelId());
		id.setCapacityChannelId(capacityChannelFromDB.getCapacityChannelId());
		capacityChannelAndCombinedChannelEntity.setId(id);
		capacityChannelAndCombinedChannelEntity.setCapacityChannel1(capacityChannelFromDB);
		capacityChannelAndCombinedChannelEntity.setCapacityChannel2(savedCombinedCapacityChannel);
		capacityChannelAndCombinedChannelEntity.setCreatedBy(changedBy);
		capacityChannelAndCombinedChannelEntity.setLastModifiedBy(changedBy);
		capacityChannelAndCombinedChannelEntity.setCreatedDatetime(Instant.now());
		capacityChannelAndCombinedChannelEntity.setLastModifiedDatetime(Instant.now());
		return capacityChannelAndCombinedChannelEntity;
	}
	
	/**
	 * This mapper method is used to map the data of Create Combine channel 
	 * along with the channels assigned to CombineChannel for response.
	 * 
	 * @param savedCombinedCapacityChannel
	 * @param createCombinedChannelRequest
	 * @return CombineChannel
	 */
	@Named(CapacityConstants.MAP_TO_COMBINE_CHANNEL_RESPONSE)
	default CombineChannel mapToChannelResponse(CapacityChannelEntity savedCombinedCapacityChannel, CreateCombineChannelRequest createCombinedChannelRequest) {
		CombineChannel response = new CombineChannel();
		response.setCombinedChannelId(savedCombinedCapacityChannel.getCapacityChannelId());
		response.setCombinedChannelName(savedCombinedCapacityChannel.getCapacityChannelNm());
		response.setCombinedFlg(savedCombinedCapacityChannel.getIsCombinedFlg());
		response.setChannels(createCombinedChannelRequest.getChannels());
		response.setFriendlyName(savedCombinedCapacityChannel.getFirendlyNm());
		response.setInterval(savedCombinedCapacityChannel.getInterval());
		response.setOperationHourStartTime(savedCombinedCapacityChannel.getOperationalHoursStartTime().toString());
		response.setOperationHourEndTime(savedCombinedCapacityChannel.getOperationalHoursEndTime().toString());
		response.setCreatedBy(savedCombinedCapacityChannel.getCreatedBy());
		response.setCreatedDateTime(savedCombinedCapacityChannel.getCreatedDatetime());
		response.setLastModifiedBy(savedCombinedCapacityChannel.getLastModifiedBy());
		response.setLastModifiedDateTime(savedCombinedCapacityChannel.getLastModifiedDatetime());
		return response;
	}

}
