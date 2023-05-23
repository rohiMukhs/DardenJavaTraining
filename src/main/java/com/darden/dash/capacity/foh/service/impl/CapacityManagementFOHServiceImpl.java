package com.darden.dash.capacity.foh.service.impl;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTransactionEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.foh.service.CapacityManagementFOHService;
import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.model.CapacitySlotTransaction;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTransactionRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.util.DateUtil;

/**
 * 
 * @author vijmohit
 * @date 16-May-2022
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity Management for FOH or any business logic related to
 *       Capacity Management
 */

@Service
public class CapacityManagementFOHServiceImpl implements CapacityManagementFOHService {

	private final CapacitySlotRepository capacitySlotRepository;
	
	private final CapacitySlotTransactionRepository capacitySlotTransactionRepository;
	
	private final ReferenceRepository referenceRepository;
	
	private final CapacityChannelRepo capacityChannelRepo;
	
	private final CapacityTemplateRepo capacityTemplateRepo;
	
	private final CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository;

	@Autowired
	public CapacityManagementFOHServiceImpl(CapacitySlotRepository capacitySlotRepository,
			CapacitySlotTransactionRepository capacitySlotTransactionRepository,ReferenceRepository referenceRepository,
			CapacityChannelRepo capacityChannelRepo, CapacityTemplateRepo capacityTemplateRepo,
			CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository) {
		super();
		this.capacitySlotRepository = capacitySlotRepository;
		this.capacitySlotTransactionRepository = capacitySlotTransactionRepository;
		this.referenceRepository = referenceRepository;
		this.capacityChannelRepo = capacityChannelRepo;
		this.capacityTemplateRepo = capacityTemplateRepo;
		this.capacityTemplateAndBusinessDateRepository = capacityTemplateAndBusinessDateRepository;
	}

	/**
	 * Method is for UPDATE operation. first it check capacity slot is empty or not
	 * then by using capacity slot id check capacity slot already present or not in
	 * DB if capacity slot present then update the capacity slot count and update
	 * capacity slot type then save the updated capacity slot.
	 * 
	 * @param capacitySlotRequest Request class containing the detail of Capacity
	 *                            Slot to be updated.
	 * 
	 * @param accessToken         Token used to authenticate the user and extract
	 *                            the userDetails for this API.
	 * 
	 * @param slotId              Slot Id of Capacity template to be updated.
	 * 
	 */

	@Override
	public void updateCapacitySlot(CapacitySlotRequest capacitySlotRequest, String userName) {
		
		Map<BigInteger, CapacityChannelEntity> capacityChannelEntityMap = capacityChannelRepo
				.findByConceptId(new BigInteger(RequestContext.getConcept()))
				.stream()
				.collect(Collectors.toMap(CapacityChannelEntity::getCapacityChannelId, Function.identity()));
				
				Optional<ReferenceEntity> capacitySlotReferenceEntityOpt = 
						referenceRepository.findByReferenceNmAndConceptId(capacitySlotRequest.getCapacitySlotType(), new BigInteger(RequestContext.getConcept()));
				
				List<BigInteger> slotIds = capacitySlotRequest
				.getSlots()
				.stream()
				.map(CapacitySlotTransaction::getSlotId)
				.collect(Collectors.toList());
				
				List<CapacitySlotEntity> capacitySlotList = capacitySlotRepository.findAllById(slotIds);
				LocalDate currentLocalDate = DateUtil.convertStringtoLocalDate(capacitySlotRequest.getCurrentDate());
				
				List<CapacitySlotTransactionEntity> capacitySlotTransactionEntities = capacitySlotList
				.stream()
				.map(capacitySlot -> {
					Optional<CapacitySlotTransactionEntity> capacitySlotTransactionEntityOpt = capacitySlot
					.getCapacitySlotTransactionEntities()
					.stream()
					.filter(transactionSlot -> transactionSlot!=null && transactionSlot.getBusinessDate().equals(currentLocalDate))
					.findAny();
					CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
					if(capacitySlotTransactionEntityOpt.isPresent())
						capacitySlotTransactionEntity = capacitySlotTransactionEntityOpt.get();
					else {
						capacitySlotTransactionEntity.setCapacitySlotEntity(capacitySlot);
						capacitySlotTransactionEntity.setCreatedBy(userName);
						capacitySlotTransactionEntity.setCreatedDatetime(Instant.now());
						capacitySlotTransactionEntity.setBusinessDate(currentLocalDate);
						capacitySlotTransactionEntity.setLocationId(capacitySlotRequest.getLocationId());
						capacitySlotTransactionEntity.setRevisionNbr(BigInteger.ONE);
						capacitySlotTransactionEntity.setStartTime(capacitySlot.getStartTime());
						capacitySlotTransactionEntity.setEndTime(capacitySlot.getEndTime());
						capacitySlotTransactionEntity.setCapacityChannel(capacitySlot.getCapacityChannel());
						capacitySlotTransactionEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
					}
					capacitySlotTransactionEntity.setCalculationTypeReference(capacitySlotReferenceEntityOpt.get());
					capacitySlotTransactionEntity.setCapacityCnt(capacitySlotRequest.getCapacityCount());
					capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(capacitySlotReferenceEntityOpt.get());
					capacitySlotTransactionEntity.setIsModifiedFlg("Y");
					capacitySlotTransactionEntity.setLastModifiedBy(userName);
					capacitySlotTransactionEntity.setLastModifiedDatetime(Instant.now());
					return capacitySlotTransactionEntity;
				})
				.collect(Collectors.toList());
				
				List<String> transactionIds = capacitySlotRequest
				.getSlots()
				.stream()
				.filter(Objects::nonNull)
				.filter(request -> request.getSlotId()==null)
				.filter(request -> request.getTransactionSlotId()!=null)
				.map(CapacitySlotTransaction::getTransactionSlotId)
				.collect(Collectors.toList());
				
				List<CapacitySlotTransactionEntity> transactionSlotEntities = capacitySlotTransactionRepository.findAllById(transactionIds);
				
				Map<String, CapacitySlotTransactionEntity> slotTransactionMap = transactionSlotEntities
				.stream()
				.collect(Collectors.toMap(CapacitySlotTransactionEntity::getCapacitySlotTransactionId, Function.identity()));
				
				List<CapacitySlotTransactionEntity> slotsForDefaultBaseChannels = capacitySlotRequest
				.getSlots()
				.stream()
				.filter(request -> request.getSlotId()==null)
				.map(requestedSlot -> {
					CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
					if(slotTransactionMap.containsKey(requestedSlot.getTransactionSlotId())) {
						capacitySlotTransactionEntity = slotTransactionMap.get(requestedSlot.getTransactionSlotId());
						capacitySlotTransactionEntity.setLastModifiedBy(userName);
						capacitySlotTransactionEntity.setLastModifiedDatetime(Instant.now());
						capacitySlotTransactionEntity.setCapacityCnt(capacitySlotRequest.getCapacityCount());
						capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(capacitySlotReferenceEntityOpt.get());
					}
					else {
						capacitySlotTransactionEntity.setCreatedBy(userName);
						capacitySlotTransactionEntity.setCreatedDatetime(Instant.now());
						capacitySlotTransactionEntity.setBusinessDate(currentLocalDate);
						capacitySlotTransactionEntity.setLocationId(capacitySlotRequest.getLocationId());
						capacitySlotTransactionEntity.setRevisionNbr(BigInteger.ONE);
						capacitySlotTransactionEntity.setStartTime(LocalTime.parse(requestedSlot.getStartTime()));
						capacitySlotTransactionEntity.setEndTime(LocalTime.parse(requestedSlot.getEndTime()));
						capacitySlotTransactionEntity.setCapacityChannel(capacityChannelEntityMap.get(capacitySlotRequest.getChannelId()));
						capacitySlotTransactionEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
						capacitySlotTransactionEntity.setCalculationTypeReference(capacitySlotReferenceEntityOpt.get());
						capacitySlotTransactionEntity.setCapacityCnt(capacitySlotRequest.getCapacityCount());
						capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(capacitySlotReferenceEntityOpt.get());
						capacitySlotTransactionEntity.setIsModifiedFlg("Y");
						capacitySlotTransactionEntity.setLastModifiedBy(userName);
						capacitySlotTransactionEntity.setLastModifiedDatetime(Instant.now());
					}
					return capacitySlotTransactionEntity;
				}).collect(Collectors.toList());
				
				capacitySlotTransactionEntities.addAll(slotsForDefaultBaseChannels);
				
				capacitySlotTransactionRepository.saveAll(capacitySlotTransactionEntities);
		
	}
	
	/**
	 * This method is to filterout capacity template based on date.
	 * 
	 * @param date String contains data of date.
	 * 
	 * @return List<CapacityTemplateEntity> returns the capacity template data.
	 */
	private List<CapacityTemplateEntity> basedOnDate(String date) {
		LocalDate requestedLocalDate = DateUtil.convertStringtoLocalDate(date);
		Date requestedDate = DateUtil.stringToDate(date);
		List<CapacityTemplateEntity> capacityTemplates = new ArrayList<>();
		
		Optional<CapacityTemplateAndBusinessDateEntity> templateByBussinessDate = capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(requestedDate, new BigInteger(RequestContext.getConcept()));
		if(templateByBussinessDate.isPresent())
			return Collections.singletonList(templateByBussinessDate.get().getCapacityTemplate());
		
		if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.MONDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findByMonFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.TUESDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findByTueFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.WEDNESDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findByWedFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.THURSDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findByThuFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.FRIDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findByFriFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SATURDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findBySatFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		else if(requestedLocalDate.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SUNDAY.toString())) {
			capacityTemplates = capacityTemplateRepo.findBySunFlgAndConceptId(CapacityConstants.Y, new BigInteger(RequestContext.getConcept()));
		}
		
		if(!capacityTemplates.isEmpty()) {
			 List<CapacityTemplateEntity> templateByWeekDay = capacityTemplates
			.stream()
			.filter(template -> template.getEffectiveDate() != null && template.getEffectiveDate().before(requestedDate))
			.filter(template -> template.getExpiryDate() == null || template.getExpiryDate().after(requestedDate))
			.collect(Collectors.toList());
			 if(!templateByWeekDay.isEmpty()) {
				 return templateByWeekDay;
			 }
		}
		return new ArrayList<>();
	}

	@Override
	public List<SlotChannel> getChannelAndSlotForDateWithPopulatingSlots(String currentDate) {

		LocalDate currentLocalDate = DateUtil.convertStringtoLocalDate(currentDate);
		List<CapacityTemplateEntity> capacityTemplateEntities = basedOnDate(currentDate);
		Optional<CapacityTemplateEntity> capacityTemplateEntityOpt = capacityTemplateEntities.stream().findFirst();
		
		List<CapacityChannelEntity> channelEntities = capacityChannelRepo
		.findByConceptId(new BigInteger(RequestContext.getConcept()));
		
		Map<BigInteger, String> channelNmMap = channelEntities
		.stream()
		.collect(Collectors.toMap(CapacityChannelEntity::getCapacityChannelId, CapacityChannelEntity::getCapacityChannelNm));
		
		Map<BigInteger, CapacityChannelEntity> baseChannelNmMap = channelEntities
		.stream()
		.filter(channel -> channel.getIsCombinedFlg().equalsIgnoreCase("N"))
		.collect(Collectors.toMap(CapacityChannelEntity::getCapacityChannelId, Function.identity()));
		MultiValuedMap<BigInteger, CapacitySlotTransaction> channelSlotDetails = new ArrayListValuedHashMap<>();
		
		if(capacityTemplateEntityOpt.isPresent()) {
			List<CapacitySlotEntity> capacitySlots = capacityTemplateEntityOpt.get().getCapacitySlots();
			Set<BigInteger> channelIds = new HashSet<>();
			capacitySlots.stream().filter(Objects::nonNull).forEach(cs -> {
				channelIds.add(cs.getCapacityChannel().getCapacityChannelId());
				CapacitySlotTransaction slotDetail = new CapacitySlotTransaction();
				Optional<CapacitySlotTransactionEntity> capacitySlotTransactionEntityOpt = cs
				.getCapacitySlotTransactionEntities()
				.stream()
				.filter(slot -> slot.getBusinessDate().equals(currentLocalDate))
				.findAny();
				mapToCapacitySlotTransaction(cs, slotDetail, capacitySlotTransactionEntityOpt);
				channelSlotDetails.put(cs.getCapacityChannel().getCapacityChannelId(), slotDetail);
			});
			
			baseChannelNmMap
			.keySet()
			.stream()
			.filter(channelId -> !channelIds.contains(channelId))
			.forEach(channelId -> {
				
				List<CapacitySlotTransactionEntity> capacitySlotTransactionForCurrentDate = capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(baseChannelNmMap.get(channelId), currentLocalDate);
				
				Map<LocalTime, CapacitySlotTransactionEntity> transctionMappedForStartTime = capacitySlotTransactionForCurrentDate
				.stream()
				.collect(Collectors.toMap(CapacitySlotTransactionEntity::getStartTime, Function.identity()));
				
				LocalTime start = capacityTemplateEntityOpt.get().getStartTime();
				LocalTime end = capacityTemplateEntityOpt.get().getEndTime();
				while(start.isBefore(end)) {
					CapacityChannelEntity capacityChannelEntity = baseChannelNmMap.get(channelId);
					CapacitySlotTransaction capacitySlotTransaction = new CapacitySlotTransaction();
					if(transctionMappedForStartTime.containsKey(start)) {
						mapTransactionSlotToPopulatedSlot(transctionMappedForStartTime, start, capacitySlotTransaction);
					}
					else {
						mapDefaultSlot(start, capacityChannelEntity, capacitySlotTransaction);
					}
					channelSlotDetails.put(channelId, capacitySlotTransaction);
					start = start.plusMinutes(15);
				}
			});
			
		}
		
		return channelSlotDetails
				.keySet()
				.stream()
				.map(channelId -> {
					List<SlotDetail> slotDetails = new ArrayList<>();
					SlotChannel slotChannel = new SlotChannel();
					slotChannel.setChannelId(channelId);
					slotChannel.setChannelName(channelNmMap.get(channelId));
					slotDetails.addAll(channelSlotDetails.get(channelId));
					slotChannel.setSlotDetails(slotDetails);
					return slotChannel;
				})
				.collect(Collectors.toList());
		
	}
	
	private void mapDefaultSlot(LocalTime start, CapacityChannelEntity capacityChannelEntity,
			CapacitySlotTransaction capacitySlotTransaction) {
		capacitySlotTransaction.setSlotStatusName("UNLIMITED");
		capacitySlotTransaction.setStartTime(start.toString());
		capacitySlotTransaction.setEndTime(start.plusMinutes(capacityChannelEntity.getInterval()).minusSeconds(1).toString());
	}

	private void mapTransactionSlotToPopulatedSlot(
			Map<LocalTime, CapacitySlotTransactionEntity> transctionMappedForStartTime, LocalTime start,
			CapacitySlotTransaction capacitySlotTransaction) {
		capacitySlotTransaction.setSlotStatusName(transctionMappedForStartTime.get(start).getCapacitySlotStatusReferenceEntity().getReferenceNm());
		capacitySlotTransaction.setStartTime(transctionMappedForStartTime.get(start).getStartTime().toString());
		capacitySlotTransaction.setEndTime(transctionMappedForStartTime.get(start).getEndTime().toString());
		capacitySlotTransaction.setSlotTypeId(transctionMappedForStartTime.get(start).getCapacitySlotStatusReferenceEntity().getReferenceId().toString());
		capacitySlotTransaction.setTransactionSlotId(transctionMappedForStartTime.get(start).getCapacitySlotTransactionId());
		if(transctionMappedForStartTime.get(start).getCapacitySlotStatusReferenceEntity().getReferenceNm().equalsIgnoreCase("OPEN"))
			capacitySlotTransaction.setCapacityCount(transctionMappedForStartTime.get(start).getCapacityCnt());
		else
			capacitySlotTransaction.setCapacityCount(0);
	}

	private void mapToCapacitySlotTransaction(CapacitySlotEntity cs, CapacitySlotTransaction slotDetail,
			Optional<CapacitySlotTransactionEntity> capacitySlotTransactionEntityOpt) {
		if(capacitySlotTransactionEntityOpt.isPresent()) {
			slotDetail.setTransactionSlotId(capacitySlotTransactionEntityOpt.get().getCapacitySlotTransactionId());
			slotDetail.setSlotTypeId(String.valueOf(capacitySlotTransactionEntityOpt.get().getCapacitySlotStatusReferenceEntity().getReferenceId()));
			slotDetail.setSlotStatusName(capacitySlotTransactionEntityOpt.get().getCapacitySlotStatusReferenceEntity().getReferenceNm());
			if(capacitySlotTransactionEntityOpt.get().getCapacitySlotStatusReferenceEntity().getReferenceNm().equalsIgnoreCase("OPEN"))
				slotDetail.setCapacityCount(capacitySlotTransactionEntityOpt.get().getCapacityCnt());
			else
				slotDetail.setCapacityCount(0);
		}
		else {
			slotDetail.setSlotTypeId(String.valueOf(cs.getReference().getReferenceId()));
			slotDetail.setSlotStatusName(cs.getReference().getReferenceNm());
			if(cs.getReference().getReferenceNm().equalsIgnoreCase("OPEN"))
				slotDetail.setCapacityCount(cs.getCapacityCnt());
			else
				slotDetail.setCapacityCount(0);
		}
		slotDetail.setSlotId(cs.getCapacitySlotId());
		slotDetail.setStartTime(String.valueOf(cs.getStartTime()));
		slotDetail.setEndTime(String.valueOf(cs.getEndTime()));
	}

}
