package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.ReferenceDatum;
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
	 * @param editChannelInformationRequest List of Request class containing information
	 * 					of list of capacity channels to be edited.
	 * 
	 * @param user	information of user operating on the update action.
	 * 
	 * @return List<ChannelInformationRequest> List of updated channel information in response.
	 * 
	 * @throws JsonProcessingException   if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@Override
	@Transactional
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key = CapacityConstants.CAPACITY_CHANNEL_CACHE_KEY),
            @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
	public List<CapacityChannel> editChannelInformation(List<ChannelInformationRequest> editChannelInformationRequest,String user) throws JsonProcessingException{
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		Map<BigInteger,ChannelInformationRequest> editChannelsMap=editChannelInformationRequest.stream().collect(Collectors.toMap(ChannelInformationRequest::getCapacityChannelId,o->o));
		List<BigInteger> allCapacityChannelIdList=editChannelInformationRequest.stream().map(ChannelInformationRequest::getCapacityChannelId).collect(Collectors.toList());
		List<CapacityChannelEntity> capacityChannelEntityList=capacityChannelRepository
				.findAllByCapacityChannelIdInAndConceptIdAndIsDeletedFlg(allCapacityChannelIdList, new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
		capacityChannelMapper.mapToCapacityChannelEntityList(user, dateTime, editChannelsMap, capacityChannelEntityList);
		List<CapacityChannelEntity> updatedCapacityChannelEntityList = capacityChannelRepository.saveAll(capacityChannelEntityList);
		List<CapacityChannel> response = capacityChannelMapper.mapChannels(updatedCapacityChannelEntityList);
		if(!updatedCapacityChannelEntityList.isEmpty()) {
			addToAuditTable(user, capacityChannelEntityList);
		}
		return response;
	}

	/**
	 * This method is to iterate throught multiple updated capacity channel and to 
	 * add it to audit table for each channel entity.
	 * 
	 * @param user	information of user operating on the update action.
	 * 
	 * @param capacityChannelEntityList entity class contains the list of entities.
	 * 
	 */
	private void addToAuditTable(String user, List<CapacityChannelEntity> capacityChannelEntityList) {
		capacityChannelEntityList.stream().forEach(channel -> {
			try {
				auditService.addAuditData(CapacityConstants.CAPACITY_CHANNEL, AuditActionValues.UPDATE, null, channel, user);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter friendly name and concept id is present in the database.
	 * This method is used for the validation of friendly in request body name with some 
	 * required condition to avoid duplicate values in database.
	 * 
	 * @param validateChannel request class containing detail of capacity 
	 * 						channel to be updated in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean friendlyNmValidation(ChannelInformationRequest validateChannel) {
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository
				.findByPosNameAndConceptIdAndIsDeletedFlg(validateChannel.getPosName(), new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
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
	 * @param createCombinedChannelRequest request class containing detail of
	 * 								capacity combine to be created.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @return CombineChannel model class containing detail of created 
	 * 							combined channel.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@Override
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
	public CombineChannel addCombinedChannel(CreateCombineChannelRequest createCombinedChannelRequest,
			String userDetail) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityChannelEntity capacityChannel = capacityChannelMapper.mapToChannelEntity(createCombinedChannelRequest, userDetail, dateTime);
		CapacityChannelEntity savedCombinedCapacityChannel = capacityChannelRepository.save(capacityChannel);
		saveCapacityChannelAndCombinedChannelEntityList(createCombinedChannelRequest, userDetail, applicationErrors,
				savedCombinedCapacityChannel);
		CombineChannel response = capacityChannelMapper.mapToChannelResponse(savedCombinedCapacityChannel, createCombinedChannelRequest);
		
		if(savedCombinedCapacityChannel.getCapacityChannelId() != null) {
			auditService.addAuditData(CapacityConstants.CAPACITY_CHANNEL, AuditActionValues.INSERT, null, savedCombinedCapacityChannel, userDetail);
		}
		
		return response;
	}

	/**
	 *This method is used to save data related to combination of capacity channel used while
	 *creating combine channel in capacity channel and combine channel table.
	 * 
	 * @param createCombinedChannelRequest request class containing detail of
	 * 								capacity combine to be created.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @param applicationErrors error class used to raise exception in case if 
	 * 						any validation fails.
	 * 
	 * @param savedCombinedCapacityChannel entity class containing the value of
	 * 						combine channel.
	 */
	private void saveCapacityChannelAndCombinedChannelEntityList(
			CreateCombineChannelRequest createCombinedChannelRequest, String userDetail,
			ApplicationErrors applicationErrors, CapacityChannelEntity savedCombinedCapacityChannel) {
		List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannelEntityList = new ArrayList<>();
		CapacityChannelEntity capacityChannelFromDB = new CapacityChannelEntity();
		if (createCombinedChannelRequest.getChannels() != null
				&& !createCombinedChannelRequest.getChannels().isEmpty()) {
			mapToCapacityChannelAndCombinedChannelEntityList(createCombinedChannelRequest, userDetail,
					applicationErrors, savedCombinedCapacityChannel, capacityChannelAndCombinedChannelEntityList,
					capacityChannelFromDB);
			capacityChannelAndCombinedChannelRepository.saveAll(capacityChannelAndCombinedChannelEntityList);
			savedCombinedCapacityChannel.setCapacityChannelAndCombinedChannels1(capacityChannelAndCombinedChannelEntityList);
		}
	}

	/**
	 * This method is used to map combination of capacity channel for combine channel
	 * to list of Capacity Channel And Combined Channel Entity class.
	 * 
	 * @param createCombinedChannelRequest request class containing detail of
	 * 								capacity combine to be created.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @param applicationErrors error class used to raise exception in case if 
	 * 						any validation fails.
	 * 
	 * @param savedCombinedCapacityChannel entity class containing the value of
	 * 						combine channel.
	 * 
	 * @param capacityChannelAndCombinedChannelEntityList list of entity class containing the
	 * 						value of Capacity Channel And Combined Channel.
	 * 
	 * @param capacityChannelFromDB list of entity class containing the value of
	 * 						capacity channel.
	 */
	private void mapToCapacityChannelAndCombinedChannelEntityList(
			CreateCombineChannelRequest createCombinedChannelRequest, String userDetail,
			ApplicationErrors applicationErrors, CapacityChannelEntity savedCombinedCapacityChannel,
			List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannelEntityList,
			CapacityChannelEntity capacityChannelFromDB) {
		for (String channleNm : createCombinedChannelRequest.getChannels()) {
			Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository
					.findByCapacityChannelNmAndConceptIdAndIsDeletedFlg(channleNm, new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
			capacityChannelFromDB = validateOptionalCapacityChannelEntity(applicationErrors, capacityChannelFromDB,
					optionalCapacityChannelEntity);
			CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = addCapacityChannelAndCombinedChannel(
					savedCombinedCapacityChannel, capacityChannelFromDB, userDetail);
			capacityChannelAndCombinedChannelEntityList.add(capacityChannelAndCombinedChannelEntity);
		}
	}

	/**
	 * This method is used to validate if capacityChannelFromDB value is 
	 * present or absent.Based on condition error exception will be raised.
	 * 
	 * @param applicationErrors error class used to raise exception in case if 
	 * 						any validation fails.
	 * 
	 * @param capacityChannelFromDB list of entity class containing the value of
	 * 						capacity channel.
	 * 
	 * @param optionalCapacityChannelEntity optional of entity class containing
	 * 						the value of capacity channel.
	 * 
	 * @return CapacityChannelEntity entity class containing
	 * 						the value of capacity channel.
	 */
	private CapacityChannelEntity validateOptionalCapacityChannelEntity(ApplicationErrors applicationErrors,
			CapacityChannelEntity capacityChannelFromDB,
			Optional<CapacityChannelEntity> optionalCapacityChannelEntity) {
		if (optionalCapacityChannelEntity.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_CHANNEL_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		if (optionalCapacityChannelEntity.isPresent()) {
			capacityChannelFromDB = optionalCapacityChannelEntity.get();
		}
		return capacityChannelFromDB;
	}
	
	/**
	 * This service method is used to add values of Combined capacity channel
	 * and capacity channels that need to be assigned to capacity channel to
	 * CapacityChannelAndCombinedChannelEntity and value is returned.
	 * 
	 * @param savedCombinedCapacityChannel entity class with detail of created combine 
	 * 									channel.
	 * 
	 * @param capacityChannelFromDB entity class with detail of capacity channel
	 * 								 retrieved from database.
	 * 
	 * @param changedBy information of user for the created by value of Capacity
	 * 								Channel And Combined Channel Entity.
	 * 
	 * @return CapacityChannelAndCombinedChannelEntity 	entity class containing the 
	 * 								detail of Capacity Channel And Combined Channel.
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
	 * @param capacityChannelNm Capacity Channel Name to be validated in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateChannelNmValidation(String capacityChannelNm) {
		Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository
				.findByCapacityChannelNmAndConceptIdAndIsDeletedFlg(capacityChannelNm, new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
		return optionalCapacityChannelEntity.isPresent();
	}

	/**
	 * This method is used to return the boolean value based on the condition if the
	 * pair of passing parameter friendly name and concept id is present in the database.
	 * This method is used for the validation of friendly name in request body.
	 * 
	 * @param friendlyNm friendly Name to be validated in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	@Override
	public boolean validateChannelFriendlyNmValidation(String friendlyNm) {
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository.findByPosNameAndConceptIdAndIsDeletedFlg(friendlyNm, new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
		return capacityChannelEntity != null;
	}
	
	/**
	 * This service method is used to return reference data for capacity
	 * channels its contains all capacity channel irrespective of basic or
	 * combine channel.
	 * 
	 * @return ReferenceDatum list of model class containing the value of
	 * reference data.
	 * 
	 */
	@Override
	public ReferenceDatum getReferenceData() {
		
		List<CapacityChannelEntity> channelEntities = capacityChannelRepository
				.findByConceptIdAndIsDeletedFlg(new BigInteger(RequestContext.getConcept()), CapacityConstants.N);
		ReferenceDatum referenceDatum = new ReferenceDatum();
		List<CapacityChannel> channels = new ArrayList<>();
		channelEntities.stream().forEach(ce -> {
			CapacityChannel channel = mapToCapacityChannel(ce);
			channels.add(channel);
		});
		referenceDatum.setCapacityChannel(channels);
		return referenceDatum;
	}

	/**
	 * This method is to map capacity channel entity value to capacity channel
	 * model class.
	 * 
	 * @param ce entity class containing the value of
	 * 						capacity channel.
	 * 
	 * @return CapacityChannel model class containing the value of
	 * 						capacity channel.
	 */
	private CapacityChannel mapToCapacityChannel(CapacityChannelEntity ce) {
		CapacityChannel channel = new CapacityChannel();
		channel.setCapacityChannelId(ce.getCapacityChannelId());
		channel.setCapacityChannelName(ce.getCapacityChannelNm());
		channel.setPosName(ce.getPosName());
		channel.setInterval(String.valueOf(ce.getInterval()));
		channel.setIsCombinedFlg(ce.getIsCombinedFlg());
		channel.setOperationalHoursEndTime(ce.getOperationalHoursEndTime().toString());
		channel.setOperationalHoursStartTime(ce.getOperationalHoursStartTime().toString());
		if(ce.getIsCombinedFlg().equals(CapacityConstants.Y)) {
			List<Channel> underCombine = new ArrayList<>();
			List<CapacityChannelAndCombinedChannelEntity> combineChannel = capacityChannelAndCombinedChannelRepository.findByCapacityChannel2(ce);
			combineChannel.stream().forEach(cc -> {
				Channel cha = new Channel();
				cha.setCapacityChannelId(cc.getId().getCapacityChannelId());
				cha.setCapacityChannelName(cc.getCapacityChannel1().getCapacityChannelNm());
				underCombine.add(cha);
			});
			channel.setCombinedChannels(underCombine);
		}
		return channel;
	}

	/**
	 * This service method is to validate if the combination of selected 
	 * channels already present in the database.
	 * 
	 * @param channelsNames set of selected channel names.
	 * 
	 * @return Set<Integer> return the value of set of Integer.Based on 
	 * 				condition value is set.
	 */
	@Override
	public Set<Integer> validateBaseChannelCombindation(Set<String> channelsNames) {
		List<CapacityChannelEntity> channelList = capacityChannelRepository.findAll();
		Set<Integer> value = new HashSet<>();
		channelList.stream().forEach(cl -> {
			if(cl.getIsCombinedFlg().equals(CapacityConstants.Y)) {
				List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannelList = capacityChannelAndCombinedChannelRepository.findByCapacityChannel2(cl);
				Set<String> dbChannelNames = new HashSet<>();
				capacityChannelAndCombinedChannelList.stream().forEach(cccl ->
					dbChannelNames.add(cccl.getCapacityChannel1().getCapacityChannelNm()
							));
				if(channelsNames.equals(dbChannelNames)) {
					value.add(1);
				}
			}
		});
		return value;
	}
}
