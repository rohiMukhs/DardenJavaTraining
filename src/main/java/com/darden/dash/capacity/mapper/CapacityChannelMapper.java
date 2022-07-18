package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.darden.dash.capacity.model.ChannelInformationRequest;
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
	 * @param capacityChannelEntites entity class with information of
	 *                                      Capacity Channel.
	 * 
	 * @return List<CapacityChannel> List of mapped entity class of Capacity channel.
	 */
	List<CapacityChannel> mapChannels(List<CapacityChannelEntity> capacityChannelEntites);
	
	@Mapping(source = CapacityConstants.MAPPER_CAPACITY_CHANNEL_NM, target = CapacityConstants.MAPPER_CAPACITY_CHANNEL_NAME)
	@Mapping(target = CapacityConstants.MAPPER_OPERATIONAL_HOURS_START_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAPPER_OPERATIONAL_HOURS_END_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAPPER_COMBINED_CHANNELS, ignore = true)
	CapacityChannel map(CapacityChannelEntity capacityChannelEntity);

	/**
	 * This method is to map capacity channel entity to capacity channel for converting 
	 * date format to string format for specific fields
	 * 
	 * @param capacityChannelEntity Entity class with information of
	 *                                      capacity Channel value.
	 *                                      
	 * @param capacityChannel mapped model class of capacity Channel information.
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
	 * @param createCombinedChannelRequest request class with information of
	 *                                      Combined Channel to be created.
	 * 
	 * @param userDetail information of createdBy String value.
	 * 
	 * @param dateTime information of dateTime Instant value
	 * 
	 * @return CapacityChannelEntity mapped entity class of Capacity 
	 * 								 Capacity Channel information.
	 */
	@Named(CapacityConstants.MAP_TO_CHANNEL_ENTITY)
	default CapacityChannelEntity mapToChannelEntity(CreateCombineChannelRequest createCombinedChannelRequest, String userDetail, Instant dateTime) {
		CapacityChannelEntity capacityChannel=new CapacityChannelEntity();
		capacityChannel.setCapacityChannelNm(createCombinedChannelRequest.getCombinedChannelName());
		capacityChannel.setIsDeletedFlg(CapacityConstants.N);
		capacityChannel.setIsCombinedFlg(CapacityConstants.Y);
		capacityChannel.setConceptId(new BigInteger(RequestContext.getConcept()));
		capacityChannel.setPosName(createCombinedChannelRequest.getPosName());
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
	 * @param savedCombinedCapacityChannel Entity class with information of
	 *                                      Capacity Channel.
	 *                                      
	 * @param capacityChannelFromDB Request class with information of
	 *                                      Capacity Channel Entity.
	 *                                      
	 * @param changedBy information of createdBy String value.
	 * 
	 * @return CapacityChannelAndCombinedChannelEntity mapped entity class of Capacity 
	 * 												   Channel And Combined Channel information.
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
	 * @param savedCombinedCapacityChannel model class with information of
	 *                                      saved Combined Capacity Channel.
	 *                                      
	 * @param createCombinedChannelRequest Request class with information of
	 *                                      create Combined Channel.
	 *                                      
	 * @return CombineChannel mapped model class of Combine Channel information.
	 */
	@Named(CapacityConstants.MAP_TO_COMBINE_CHANNEL_RESPONSE)
	default CombineChannel mapToChannelResponse(CapacityChannelEntity savedCombinedCapacityChannel, CreateCombineChannelRequest createCombinedChannelRequest) {
		CombineChannel response = new CombineChannel();
		response.setCombinedChannelId(savedCombinedCapacityChannel.getCapacityChannelId());
		response.setCombinedChannelName(savedCombinedCapacityChannel.getCapacityChannelNm());
		response.setCombinedFlg(savedCombinedCapacityChannel.getIsCombinedFlg());
		response.setChannels(createCombinedChannelRequest.getChannels());
		response.setPosName(savedCombinedCapacityChannel.getPosName());
		response.setInterval(savedCombinedCapacityChannel.getInterval());
		response.setOperationHourStartTime(savedCombinedCapacityChannel.getOperationalHoursStartTime().toString());
		response.setOperationHourEndTime(savedCombinedCapacityChannel.getOperationalHoursEndTime().toString());
		response.setCreatedBy(savedCombinedCapacityChannel.getCreatedBy());
		response.setCreatedDateTime(savedCombinedCapacityChannel.getCreatedDatetime());
		response.setLastModifiedBy(savedCombinedCapacityChannel.getLastModifiedBy());
		response.setLastModifiedDateTime(savedCombinedCapacityChannel.getLastModifiedDatetime());
		return response;
	}

	/**
	 * This mapper method is used to map the data of updating channels 
	 * to Capacity Channel entity class.
	 * 
	 * @param user information of createdBy String value.
	 * 
	 * @param dateTime information of dateTime Instant value.
	 * 
	 * @param capacityChannel mapped model class of capacity Channel information.
	 * 
	 * @param channelInformationRequest model class containing the list of channels
	 * 							to be updated.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYCHANNELENTITY)
	default void mapToCapacityChannelEntity(String user, Instant dateTime, CapacityChannelEntity capacityChannel,
			ChannelInformationRequest channelInformationRequest) {
		capacityChannel.setPosName(channelInformationRequest.getPosName());
		capacityChannel.setInterval(channelInformationRequest.getInterval());
		capacityChannel.setOperationalHoursStartTime(Time.valueOf(LocalTime.parse(channelInformationRequest.getOperationHourStartTime())));
		capacityChannel.setOperationalHoursEndTime(Time.valueOf(LocalTime.parse(channelInformationRequest.getOperationHourEndTime())));
		capacityChannel.setLastModifiedBy(user);
		capacityChannel.setLastModifiedDatetime(dateTime);
	}

	/**
	 * This mapper method is used to map the data of updating channels 
	 * to Capacity Channel entity class.
	 * 
	 * @param user
	 * @param dateTime
	 * @param editChannelsMap
	 * @param capacityChannelEntityList
	 */
	@Named(CapacityConstants.MAPTOCAPACITYCHANNELENTITYLIST)
	default void mapToCapacityChannelEntityList(String user, Instant dateTime,
			Map<BigInteger, ChannelInformationRequest> editChannelsMap,
			List<CapacityChannelEntity> capacityChannelEntityList) {
		capacityChannelEntityList.stream().filter(Objects::nonNull).forEach(capacityChannel -> {
			ChannelInformationRequest channelInformationRequest=editChannelsMap.get(capacityChannel.getCapacityChannelId());
			if(channelInformationRequest != null) {
				mapToCapacityChannelEntity(user, dateTime, capacityChannel, channelInformationRequest);
			}
		});
	}
}
