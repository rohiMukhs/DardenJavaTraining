package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.repository.CapacityChannelAndCombinedChannelRepository;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.enums.AuditActionValues;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;

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
	
	private CapacityChannelAndCombinedChannelRepository capacityChannelAndCombinedChannelRepository;
	
	private AuditService auditService;
	
	private CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
	/**
	 * Autowiring required properties
	 * 
	 * @param capacityChannelRepository
	 * @param capacityChannelAndCombinedChannelRepository
	 * @param auditService
	 */
	@Autowired
	public CapacityChannelServiceImpl(CapacityChannelRepo capacityChannelRepository,CapacityChannelAndCombinedChannelRepository capacityChannelAndCombinedChannelRepository,
		AuditService auditService) {
		super();
		this.capacityChannelRepository = capacityChannelRepository;
		this.capacityChannelAndCombinedChannelRepository = capacityChannelAndCombinedChannelRepository;
		this.auditService = auditService;
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
	@Transactional
	public List<CapacityChannel> editChannelInformation(List<ChannelInformationRequest> editChannelInformationRequest,String user) throws JsonProcessingException{
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
		if(!capacityChannelEntityList.isEmpty()) {
			auditService.addAuditData(CapacityConstants.CAPACITY_TEMPLATE, AuditActionValues.UPDATE, null, capacityChannelEntityList, user);
		}
		return response;
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter friendly name and concept id is present in the database.
	 * This method is used for the validation of friendly in request body name with some 
	 * required condition to avoid duplicate values in database.
	 * 
	 * @param friendlyName
	 * @return boolean
	 */
	@Override
	public boolean friendlyNmValidation(ChannelInformationRequest validateChannel) {
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository.findByFirendlyNmAndConceptId(validateChannel.getFriendlyName(), new BigInteger(RequestContext.getConcept()));
		if(capacityChannelEntity != null && capacityChannelEntity.getCapacityChannelId().equals(validateChannel.getCapacityChannelId()))
			return CapacityConstants.FALSE;
		else
			return capacityChannelEntity != null;
	}

	/**
	 * This method is used for the CREATE operation for creating Combine channel
	 * based on the value of channel to be created and the value of channel to be 
	 * assigned to the combine channel.The data is mapped to capacity channel entity
	 * from request.The channel to be assigned are retrieved based on value of the
	 * channel name and and conceptId passed in the header.If the value is not present 
	 * in the database application error is raised.The list of channel entity to be assigned
	 * is saved to capacityChannel And CombinedChannel table. Saved Combine channel entity
	 * value is mapped to response and the mapped response is returned.
	 * 
	 * @param createCombinedChannelRequest
	 * @param userDetail
	 * @return CombineChannel
	 */
	@Override
	public CombineChannel addCombinedChannel(CreateCombineChannelRequest createCombinedChannelRequest,
			String userDetail) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityChannelEntity capacityChannel = capacityChannelMapper.mapToChannelEntity(createCombinedChannelRequest, userDetail, dateTime);
		CapacityChannelEntity savedCombinedCapacityChannel = capacityChannelRepository.save(capacityChannel);
		List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannelEntityList = new ArrayList<>();
		CapacityChannelEntity capacityChannelFromDB = new CapacityChannelEntity();
		if (createCombinedChannelRequest.getChannels() != null
				&& !createCombinedChannelRequest.getChannels().isEmpty()) {
			for (String channleNm : createCombinedChannelRequest.getChannels()) {
				Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository.findByCapacityChannelNmAndConceptId(channleNm, new BigInteger(RequestContext.getConcept()));
				if (optionalCapacityChannelEntity.isEmpty()) {
					applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
							CapacityConstants.CAPACITY_CHANNEL_NM);
					applicationErrors.raiseExceptionIfHasErrors();
				}
				if (optionalCapacityChannelEntity.isPresent()) {
					capacityChannelFromDB = optionalCapacityChannelEntity.get();
				}
				CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = addCapacityChannelAndCombinedChannel(
						savedCombinedCapacityChannel, capacityChannelFromDB, userDetail);
				capacityChannelAndCombinedChannelEntityList.add(capacityChannelAndCombinedChannelEntity);
			}

			capacityChannelAndCombinedChannelRepository.saveAll(capacityChannelAndCombinedChannelEntityList);
			savedCombinedCapacityChannel.setCapacityChannelAndCombinedChannels1(capacityChannelAndCombinedChannelEntityList);
		}
		CombineChannel response = capacityChannelMapper.mapToChannelResponse(savedCombinedCapacityChannel, createCombinedChannelRequest);
		
		if(savedCombinedCapacityChannel.getCapacityChannelId() != null) {
			auditService.addAuditData(CapacityConstants.CAPACITY_CHANNEL, AuditActionValues.INSERT, null, savedCombinedCapacityChannel, userDetail);
		}
		
		return response;
	}
	
	/**
	 * This service method is used to add values of Combined capacity channel
	 * and capacity channels that need to be assigned to capacity channel to
	 * CapacityChannelAndCombinedChannelEntity and value is returned.
	 * 
	 * @param savedCombinedCapacityChannel
	 * @param capacityChannelFromDB
	 * @param changedBy
	 * @return CapacityChannelAndCombinedChannelEntity
	 */
	public CapacityChannelAndCombinedChannelEntity addCapacityChannelAndCombinedChannel(CapacityChannelEntity savedCombinedCapacityChannel,
			CapacityChannelEntity capacityChannelFromDB, String changedBy) {
		CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = new CapacityChannelAndCombinedChannelEntity();
		if (null != savedCombinedCapacityChannel && null != savedCombinedCapacityChannel.getConceptId()) {
			capacityChannelAndCombinedChannelEntity = capacityChannelMapper.mapToChannelAndCombineChannel(savedCombinedCapacityChannel, capacityChannelFromDB, changedBy);
		}
		return capacityChannelAndCombinedChannelEntity;
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter capacityChannel name and concept id is present in the database.
	 * This method is used for the validation of friendly name in request body. 
	 * 
	 * @param capacityChannelNm
	 * @return boolean
	 */
	@Override
	public boolean validateChannelNmValidation(String capacityChannelNm) {
		Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository.findByCapacityChannelNmAndConceptId(capacityChannelNm, new BigInteger(RequestContext.getConcept()));
		return optionalCapacityChannelEntity.isPresent();
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter friendly name and concept id is present in the database.
	 * This method is used for the validation of friendly name in request body.
	 * 
	 * @param 
	 * @return
	 */
	@Override
	public boolean validateChannelFriendlyNmValidation(String friendlyNm) {
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository.findByFirendlyNmAndConceptId(friendlyNm, new BigInteger(RequestContext.getConcept()));
		return capacityChannelEntity != null;
	}

}
