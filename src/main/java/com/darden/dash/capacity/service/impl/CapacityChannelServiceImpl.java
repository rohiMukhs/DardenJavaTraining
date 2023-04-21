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
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.repository.CapacityChannelAndCombinedChannelRepository;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityTemplateAndCapacityChannelRepository;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.enums.AuditActionValues;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.model.DeleteResponseBodyFormat;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.GlobalDataCall;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author skashala
 * @date 25-May-2022
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity channel or any business logic related to Capacity
 *       channel
 */
@Slf4j
@Service
public class CapacityChannelServiceImpl implements CapacityChannelService{

	private CapacityChannelRepo  capacityChannelRepository;
	
	private CapacityChannelAndCombinedChannelRepository capacityChannelAndCombinedChannelRepository;
	
	private AuditService auditService;
	
	private CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
	
	private CapacityTemplateAndCapacityChannelRepository capacityTemplateAndCapacityChannelRepository;
	
	private GlobalDataCall globalDataCall;
	/**
	 * Autowiring required properties
	 * 
	 * @param capacityChannelRepository
	 * @param capacityChannelAndCombinedChannelRepository
	 * @param auditService
	 * @param capacityTemplateAndCapacityChannelRepository
	 */
	@Autowired
	public CapacityChannelServiceImpl(CapacityChannelRepo capacityChannelRepository,CapacityChannelAndCombinedChannelRepository capacityChannelAndCombinedChannelRepository,
		AuditService auditService, CapacityTemplateAndCapacityChannelRepository capacityTemplateAndCapacityChannelRepository, GlobalDataCall globalDataCall) {
		super();
		this.capacityChannelRepository = capacityChannelRepository;
		this.capacityChannelAndCombinedChannelRepository = capacityChannelAndCombinedChannelRepository;
		this.auditService = auditService;
		this.capacityTemplateAndCapacityChannelRepository = capacityTemplateAndCapacityChannelRepository;
		this.globalDataCall = globalDataCall;
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
		
		//iterating through request.
		Map<BigInteger,ChannelInformationRequest> editChannelsMap=editChannelInformationRequest
				.stream()
				//collecting the channel id and respective data 
				.collect(Collectors.toMap(ChannelInformationRequest::getCapacityChannelId,o->o));
		
		//iterating through request.
		List<BigInteger> allCapacityChannelIdList = editChannelInformationRequest
				.stream()
				//fetching all the channel ids.
				.map(ChannelInformationRequest::getCapacityChannelId)
				.collect(Collectors.toList());
		
		//Fetching the list of capacity channel entities for list for channel ids within the concept.
		List<CapacityChannelEntity> capacityChannelEntityList=capacityChannelRepository
				.findAllByCapacityChannelIdInAndConceptId(allCapacityChannelIdList, new BigInteger(RequestContext.getConcept()));
		
		//Mapping the request data to capacityChannelEntityList.
		capacityChannelMapper.mapToCapacityChannelEntityList(user, dateTime, editChannelsMap, capacityChannelEntityList);
		
		//Saving all the capacityChannelEntityList.
		List<CapacityChannelEntity> updatedCapacityChannelEntityList = capacityChannelRepository.saveAll(capacityChannelEntityList);
		
		//Mapping updated data to response model class.
		List<CapacityChannel> response = capacityChannelMapper.mapChannels(updatedCapacityChannelEntityList);
		
		//Adding opration performed on multiple data to audit table.
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
		
		//iterating through list of channel entities.
		capacityChannelEntityList
		.stream()
		.forEach(channel -> {
			//Adding operation performed for each entity to audit table using audit service.
			try {
				auditService.addAuditData(CapacityConstants.CAPACITY_CHANNEL, AuditActionValues.UPDATE, null, channel, user);
			} catch (JsonProcessingException e) {
				log.info(CapacityConstants.FAILED_TO_ADD_DATA_TO_AUDIT);
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
		
		//Fetching the CapacityChannelEntity for pos name within the concept.
				CapacityChannelEntity capacityChannelEntity = capacityChannelRepository
						.findByPosNameAndConceptId(validateChannel.getPosName(), new BigInteger(RequestContext.getConcept()));
		
		//condition to avoid self check.
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
	@Transactional
	@Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
	public CombineChannel addCombinedChannel(CreateCombineChannelRequest createCombinedChannelRequest,
			String userDetail) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		
		//Mapping request data to CapacityChannelEntity.
		CapacityChannelEntity capacityChannel = capacityChannelMapper.mapToChannelEntity(createCombinedChannelRequest, userDetail, dateTime);
		
		//Saving CapacityChannelEntity.
		CapacityChannelEntity savedCombinedCapacityChannel = capacityChannelRepository.save(capacityChannel);
		
		//Creating CapacityChannelAndCombinedChannelEntityList and saving.
		saveCapacityChannelAndCombinedChannelEntityList(createCombinedChannelRequest, userDetail, applicationErrors,
				savedCombinedCapacityChannel);
		
		//Mapping to response model class.
		CombineChannel response = capacityChannelMapper.mapToChannelResponse(savedCombinedCapacityChannel, createCombinedChannelRequest);
		
		//Adding to action performed to audit trail table using audit service.
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
		
		//if combination channels in request not empty.
		if (createCombinedChannelRequest.getChannels() != null
				&& !createCombinedChannelRequest.getChannels().isEmpty()) {
			
			//Mapping request data to entity and adding to list.
			mapToCapacityChannelAndCombinedChannelEntityList(createCombinedChannelRequest, userDetail,
					applicationErrors, savedCombinedCapacityChannel, capacityChannelAndCombinedChannelEntityList,
					capacityChannelFromDB);
			
			//Saving the list.
			capacityChannelAndCombinedChannelRepository.saveAll(capacityChannelAndCombinedChannelEntityList);
			
			//Setting the updated list to savedCombinedCapacityChannel.
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
		
		//iterating throught channels in request.
		for (String channleNm : createCombinedChannelRequest.getChannels()) {
			
			//Fetching the capacity channel for channel name within the concept.
			Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository
					.findByCapacityChannelNmAndConceptId(channleNm, new BigInteger(RequestContext.getConcept()));
			
			//Validating if present in database.
			capacityChannelFromDB = validateOptionalCapacityChannelEntity(applicationErrors, capacityChannelFromDB,
					optionalCapacityChannelEntity);
			
			//Mapping data to CapacityChannelAndCombinedChannelEntity.
			CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = addCapacityChannelAndCombinedChannel(
					savedCombinedCapacityChannel, capacityChannelFromDB, userDetail);
			
			//Adding to list.
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
		
		//if channel entity is empty raising the exception.
		if (optionalCapacityChannelEntity.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CAPACITY_CHANNEL_NM);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		else{
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
		
		//if savedCombinedCapacityChannel is not null and within concept.
		if (null != savedCombinedCapacityChannel && null != savedCombinedCapacityChannel.getConceptId()) {
			
			//Mapping data to capacityChannelAndCombinedChannelEntity.
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

		// Fetching the capacity channel for channel name within the concept.
		Optional<CapacityChannelEntity> optionalCapacityChannelEntity = capacityChannelRepository
				.findByCapacityChannelNmAndConceptId(capacityChannelNm, new BigInteger(RequestContext.getConcept()));
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
		
		//Fetching the capacity channel entity for pos name within the concept.
		CapacityChannelEntity capacityChannelEntity = capacityChannelRepository.findByPosNameAndConceptId(friendlyNm, new BigInteger(RequestContext.getConcept()));
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

		List<CapacityChannel> channels = getAllCapacityChannels();
		
		ReferenceDatum referenceDatum = new ReferenceDatum();
		//Setting list of channel to model class.
		referenceDatum.setCapacityChannel(channels);
		return referenceDatum;
	}

	/**
	 * This service method is used to return reference data for capacity
	 * channels its contains all capacity channel irrespective of basic or
	 * combine channel.
	 * 
	 * @return List<CapacityChannel> list of model class containing the value of
	 * capacity channels.
	 */
	@Override
	public List<CapacityChannel> getAllCapacityChannels() {
		List<CapacityChannel> channels = new ArrayList<>();
		// Fetching the list of channel entities within the concept.
		List<CapacityChannelEntity> channelEntities = capacityChannelRepository
				.findByConceptId(new BigInteger(RequestContext.getConcept()));

		//iterating through channel entities.
		channelEntities
		.stream()
		.forEach(ce -> {
			
			//Mapping to model class.
			CapacityChannel channel = mapToCapacityChannel(ce);
			
			//Adding to list.
			channels.add(channel);
		});
		return channels;
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
		
		//Mapping entity data to capacity channel model class.
		CapacityChannel channel = new CapacityChannel();
		channel.setCapacityChannelId(ce.getCapacityChannelId());
		channel.setCapacityChannelName(ce.getCapacityChannelNm());
		channel.setPosName(ce.getPosName());
		channel.setInterval(String.valueOf(ce.getInterval()));
		channel.setIsCombinedFlg(ce.getIsCombinedFlg());
		channel.setOperationalHoursEndTime(ce.getOperationalHoursEndTime().toString());
		channel.setOperationalHoursStartTime(ce.getOperationalHoursStartTime().toString());
		channel.setLastModifiedDate(ce.getLastModifiedDatetime().toString());
		
		//if isCombineFlag equals Y.
		if(ce.getIsCombinedFlg().equals(CapacityConstants.Y)) {
			List<Channel> underCombine = new ArrayList<>();
			
			//Fetching the list of CapacityChannelAndCombinedChannelEntity for capacity channel.
			List<CapacityChannelAndCombinedChannelEntity> combineChannel = capacityChannelAndCombinedChannelRepository.findByCapacityChannel2(ce);
			
			//iterating through combine channels.
			combineChannel
			.stream()
			.forEach(cc -> {
				
				//Mapping to channel model class.
				Channel cha = new Channel();
				cha.setCapacityChannelId(cc.getId().getCapacityChannelId());
				cha.setCapacityChannelName(cc.getCapacityChannel1().getCapacityChannelNm());
				
				//Adding to list.
				underCombine.add(cha);
			});
			
			//Setting combine channels to channel entity.
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
		
		// fetching all the capacity channel entities within the concept.
		List<CapacityChannelEntity> channelList = capacityChannelRepository
				.findByConceptId(new BigInteger(RequestContext.getConcept()));
		Set<Integer> value = new HashSet<>();
		
		//iterating through channel entity list.
		channelList
		.stream()
		.forEach(cl -> {
			
			//if is combine flag equals Y.
			if(cl.getIsCombinedFlg().equals(CapacityConstants.Y)) {
				
				//Fetching list of CapacityChannelAndCombinedChannelEntity for capacity channel entity.
				List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannelList = capacityChannelAndCombinedChannelRepository.findByCapacityChannel2(cl);
				Set<String> dbChannelNames = new HashSet<>();
				
				//iterating through list of CapacityChannelAndCombinedChannelEntity.
				capacityChannelAndCombinedChannelList
				.stream()
				.forEach(cccl ->
				
					//adding all channel names.
					dbChannelNames.add(cccl.getCapacityChannel1().getCapacityChannelNm()));
				
				//if channel names matches.
				if(channelsNames.equals(dbChannelNames)) {
					value.add(1);
				}
			}
		});
		return value;
	}
	
	/**
	 * This method is validate if the CombinedCapacityChannel is assigned to the CapacityTemplate Model 
	 * in the database it checks if there is any channelId is present in capacityTemplateAndCapacityChannel
	 * table for the validation.
	 * 
	 * @param channelId Channel Id of Capacity Channel to be validated 
	 * 						in database.
	 * 
	 * @param applicationErrors error class to raise error if validation
	 * 						fails.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
    public void checkDependencyCapacityChannelAndCapacityTemplate(String channelId, ApplicationErrors applicationErrors) {
    	
		//Fetching the CapacityChannelEntity by channel id within the concept.
      Optional<CapacityChannelEntity> dbChannelValue = capacityChannelRepository
      		.findByCapacityChannelIdAndConceptId(new BigInteger(channelId), new BigInteger(RequestContext.getConcept()));

      //Raising exception if no entity found.
       if(dbChannelValue.isEmpty()) {
           applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
                   CapacityConstants.CAPACITY_CHANNEL_ID);
           applicationErrors.raiseExceptionIfHasErrors();
       }
       
       CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
       if(dbChannelValue.isPresent())
    	   capacityChannelEntity = dbChannelValue.get();
		
         List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannelEntityList = capacityTemplateAndCapacityChannelRepository
        		 .findByCapacityChannel(capacityChannelEntity);
         
         if(CollectionUtils.isNotEmpty(capacityTemplateAndCapacityChannelEntityList)) {
        	 List<String> capacityTemplateNameList = new ArrayList<>();
        	 DeleteResponseBodyFormat capacityTemplateListBody = new DeleteResponseBodyFormat();
        	 
        	 List<String> headerFooterList = new ArrayList<>();
        	 List<DeleteResponseBodyFormat> deleteResponseBodyFormatList = new ArrayList<>();
        	 
        	 capacityTemplateAndCapacityChannelEntityList.stream().forEach( capacityTemplateAndCapacityChannelEntity ->
        	 	capacityTemplateNameList.add(capacityTemplateAndCapacityChannelEntity.getCapacityTemplate().getCapacityTemplateNm()));
        	 
        	 capacityTemplateListBody.setTitle(CapacityConstants.CAPACITY_TEMPLATE);
        	 capacityTemplateListBody.setListOfData(capacityTemplateNameList);
        	 
        	 headerFooterList.add(CapacityConstants.CAPACITY_TEMPLATE);
        	 deleteResponseBodyFormatList.add(capacityTemplateListBody);
        	 
        	 globalDataCall.raiseException(headerFooterList, deleteResponseBodyFormatList, capacityChannelEntity.getCapacityChannelNm(), applicationErrors);
        }
     }
    
    
    /**
	 * This service method is used to delete a combined channel.
	 *
	 *
	 * @param channelId Id of combined channel to be deleted
	 * 
	 * @param deleteConfirmed String denoting whether delete operation is confirmed
	 * 
	 * @param userDetail String denoting user's details
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 * 
	 * @returns String Combined Capactiy Channel Name
	 */
    
    @Override
    @Transactional
    @Caching(evict = { @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, key = CapacityConstants.CAPACITY_CHANNELS_CACHE_KEY),
            @CacheEvict(value = CapacityConstants.CAPACITY_TEMPLATE_CACHE, allEntries = true) })
    public String deleteCombinedChannel(String channelId, String deleteConfirmed, String userDetail) throws JsonProcessingException{
		
		ApplicationErrors applicationErrors = new ApplicationErrors();
		      
		//Fetch the combined capacity channel
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		Optional<CapacityChannelEntity> capacityChannelEntityOpt = capacityChannelRepository
         		.findByCapacityChannelIdAndConceptId(new BigInteger(channelId), new BigInteger(RequestContext.getConcept()));
		if(capacityChannelEntityOpt.isPresent())
			capacityChannelEntity = capacityChannelEntityOpt.get();
		
		if(deleteConfirmed.equals(CapacityConstants.Y)) {
			        
         // checking for dependencies with capacity template
         checkDependencyCapacityChannelAndCapacityTemplate(channelId, applicationErrors);
         
         // if no dependencies are present, then delete the combined channel
         // here we are deleting the relation between combined channel and capacity channel stored in capacityChannelAndCombinedChannelRepository
         // using composite key with combined channel id and capacity channel id stored in capacityChannelEntity capacityChannelAndCombinedChannels1
         capacityChannelEntity.getCapacityChannelAndCombinedChannels1().stream()
         .forEach(capacityChannelAndCombinedChannelEntity -> capacityChannelAndCombinedChannelRepository.delete(capacityChannelAndCombinedChannelEntity));
         
         
         // deleting the combined channel
         capacityChannelRepository.delete(capacityChannelEntity);
         
         auditService.addAuditData(CapacityConstants.CAPACITY_CHANNEL, AuditActionValues.DELETE_HARD, null, capacityChannelEntity, userDetail);
		}
         return capacityChannelEntity.getCapacityChannelNm();
    }
}
