package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.common.RequestContext;

/**
 * 
 * @author skashala
 * @date 25-May-2022
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity channel or any business logic related to Capacity
 *       channel
 */
@Service
public class CapacityChannelServiceImpl implements CapacityChannelService{
	
	private CapacityChannelRepo  capacityChannelRepository;
	
	private CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
	/**
	 * Autowiring required properties
	 * 
	 * @param capacityChannelRepository
	 */
	@Autowired
	public CapacityChannelServiceImpl(CapacityChannelRepo capacityChannelRepository) {
		super();
		this.capacityChannelRepository = capacityChannelRepository;
	}

	/**
	 * This Method is used for the UPDATE operation performing on list of capacity 
	 * channels.First the capacity channels that are to be updated are fetched based 
	 * upon the capacity channel id from request body and the concept Id from the 
	 * headers. The required field values to be updated is set in the list of entities
	 * from the request body which contains the values of list of channels.After setting
	 * all the values in list of entities all entities are saved at once using the 
	 * repository operation and the value of updated list of capacity channels is returned 
	 * in response
	 * 
	 * 
	 * @param editChannelInformationRequest
	 * @param user
	 * @return List<ChannelInformationRequest>
	 */
	@Override
	public List<CapacityChannel> editChannelInformation(List<ChannelInformationRequest> editChannelInformationRequest,String user) {
		Map<BigInteger,ChannelInformationRequest> editChannelsMap=editChannelInformationRequest.stream().collect(Collectors.toMap(ChannelInformationRequest::getCapacityChannelId,o->o));
		List<BigInteger> allCapacityChannelIdList=editChannelInformationRequest.stream().map(ChannelInformationRequest::getCapacityChannelId).collect(Collectors.toList());
		List<CapacityChannelEntity> capacityChannelEntityList=capacityChannelRepository.findAllByCapacityChannelIdInAndConceptId(allCapacityChannelIdList, new BigInteger(RequestContext.getConcept()));
		capacityChannelEntityList.stream().filter(Objects::nonNull).forEach(capacityChannel -> {
			ChannelInformationRequest channelInformationRequest=editChannelsMap.get(capacityChannel.getCapacityChannelId());
			if(channelInformationRequest != null) {
				capacityChannel.setFirendlyNm(channelInformationRequest.getFriendlyName());
				capacityChannel.setInterval(channelInformationRequest.getInterval());
				capacityChannel.setOperationalHoursStartTime(Time.valueOf(channelInformationRequest.getOperationHourStartTime()));
				capacityChannel.setOperationalHoursEndTime(Time.valueOf(channelInformationRequest.getOperationHourEndTime()));
				capacityChannel.setLastModifiedBy(user);
				Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
				capacityChannel.setLastModifiedDatetime(dateTime);
			}
		});
		capacityChannelRepository.saveAll(capacityChannelEntityList);
		List<CapacityChannelEntity> updatedCapacityChannelEntityList=capacityChannelRepository.findAllByCapacityChannelIdInAndConceptId(allCapacityChannelIdList, new BigInteger(RequestContext.getConcept()));
		List<CapacityChannel> response = capacityChannelMapper.mapChannels(updatedCapacityChannelEntityList);
		return response;
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter friendly name and concept id is present in the database.
	 * This method is used for the validation of friendly name in request body.
	 * 
	 * @param friendlyName
	 * @return boolean
	 */
	@Override
	public boolean friendlyNmValidation(String friendlyName) {
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository.findByFirendlyNmAndConceptId(friendlyName, new BigInteger(RequestContext.getConcept()));
		return capacityChannelEntity != null;
	}

}
