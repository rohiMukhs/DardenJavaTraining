package com.darden.dash.capacity.foh.service;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTransactionEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.foh.service.impl.CapacityManagementFOHServiceImpl;
import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.model.CapacitySlotTransaction;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTransactionRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.util.DateUtil;

@ExtendWith(MockitoExtension.class)
class CapacityManagementFOHServiceTest {

	@InjectMocks
	private CapacityManagementFOHServiceImpl capacityManagementFOHService;

	@Mock
	private CapacitySlotRepository capacitySlotRepository;
	
	@Mock
	private CapacitySlotTransactionRepository capacitySlotTransactionRepository;
	
	@Mock
	private ReferenceRepository referenceRepository;
	
	@Mock
	private CapacityChannelRepo capacityChannelRepo;
	
	@Mock
	private CapacityTemplateRepo capacityTemplateRepo;
	
	@Mock
	private CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository;
	
	

	@Test
	void testUpdateCapacitySlot() {
		
		RequestContext.setConcept("1");
		
		ReferenceEntity ref = new ReferenceEntity();
		ref.setReferenceId(BigInteger.ONE);
		ref.setReferenceNm("OPEN");
		
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("tg");
		capacityChannelEntity.setInterval(15);

		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt(4);
		capacitySlotEntity.setCapacitySlotId(new BigInteger("1"));
		capacitySlotEntity.setConceptId(new BigInteger("5"));
		capacitySlotEntity.setEndTime(LocalTime.now());
		capacitySlotEntity.setStartTime(LocalTime.now());
		capacitySlotEntity.setReference(ref);
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
		capacitySlotTransactionEntity.setBusinessDate(DateUtil.convertStringtoLocalDate("01/01/2000"));
		capacitySlotTransactionEntity.setCapacityChannel(capacityChannelEntity);
		capacitySlotTransactionEntity.setCapacityCnt(10);
		capacitySlotTransactionEntity.setEndTime(LocalTime.NOON);
		capacitySlotTransactionEntity.setStartTime(LocalTime.NOON.minusMinutes(10));
		capacitySlotTransactionEntity.setCapacitySlotTransactionId("qiojoiqjis");
		capacitySlotTransactionEntity.setCapacitySlotEntity(capacitySlotEntity);
		
		capacitySlotEntity.setCapacitySlotTransactionEntities(Collections.singletonList(capacitySlotTransactionEntity));
		

		Mockito.when(referenceRepository.findByReferenceNmAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(Optional.of(ref));
		
		CapacitySlotRequest capacitySlotRequest = new CapacitySlotRequest();
		capacitySlotRequest.setCapacityCount(12);
		capacitySlotRequest.setCapacitySlotType("OPEN");
		capacitySlotRequest.setChannelId(BigInteger.ONE);
		capacitySlotRequest.setCurrentDate("01/01/2000");
		capacitySlotRequest.setLocationId(BigInteger.ONE);
		List<CapacitySlotTransaction> slots = new ArrayList<>();
		
		CapacitySlotTransaction slot1 = new CapacitySlotTransaction();
		slot1.setSlotId(BigInteger.ONE);
		slot1.setStartTime("01:01");
		slot1.setEndTime("10:10");
		slots.add(slot1);
		
		CapacitySlotTransaction slot2 = new CapacitySlotTransaction();
		slot2.setTransactionSlotId("hqsbdjhbwbd");
		slot2.setStartTime("01:01");
		slot2.setEndTime("10:10");
		slots.add(slot2);
		
		capacitySlotRequest.setSlots(slots);
		
		Mockito.when(capacitySlotRepository.findAllById(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotEntity));
		
		Mockito.when(capacitySlotTransactionRepository.findAllById(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		Mockito.when(capacitySlotTransactionRepository.saveAll(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		capacityManagementFOHService.updateCapacitySlot(capacitySlotRequest, "aa");
		assertNotNull(capacitySlotRequest);
	}

	@Test
	void testUpdateCapacitySlotTwo() {
		
		RequestContext.setConcept("1");
		
		ReferenceEntity ref = new ReferenceEntity();
		ref.setReferenceId(BigInteger.ONE);
		ref.setReferenceNm("OPEN");
		
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("tg");
		capacityChannelEntity.setInterval(15);

		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt(4);
		capacitySlotEntity.setCapacitySlotId(new BigInteger("1"));
		capacitySlotEntity.setConceptId(new BigInteger("5"));
		capacitySlotEntity.setEndTime(LocalTime.now());
		capacitySlotEntity.setStartTime(LocalTime.now());
		capacitySlotEntity.setReference(ref);
		capacitySlotEntity.setCapacitySlotTransactionEntities(Collections.EMPTY_LIST);
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
		capacitySlotTransactionEntity.setBusinessDate(DateUtil.convertStringtoLocalDate("01/01/2000"));
		capacitySlotTransactionEntity.setCapacityChannel(capacityChannelEntity);
		capacitySlotTransactionEntity.setCapacityCnt(10);
		capacitySlotTransactionEntity.setEndTime(LocalTime.NOON);
		capacitySlotTransactionEntity.setStartTime(LocalTime.NOON.minusMinutes(10));
		capacitySlotTransactionEntity.setCapacitySlotTransactionId("hqsbdjhbwbd");
		capacitySlotTransactionEntity.setCapacitySlotEntity(capacitySlotEntity);
		capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(ref);

		Mockito.when(referenceRepository.findByReferenceNmAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(Optional.of(ref));
		
		CapacitySlotRequest capacitySlotRequest = new CapacitySlotRequest();
		capacitySlotRequest.setCapacityCount(12);
		capacitySlotRequest.setCapacitySlotType("OPEN");
		capacitySlotRequest.setChannelId(BigInteger.ONE);
		capacitySlotRequest.setCurrentDate("01/01/2000");
		capacitySlotRequest.setLocationId(BigInteger.ONE);
		List<CapacitySlotTransaction> slots = new ArrayList<>();
		
		CapacitySlotTransaction slot1 = new CapacitySlotTransaction();
		slot1.setSlotId(BigInteger.ONE);
		slot1.setStartTime("01:01");
		slot1.setEndTime("10:10");
		slots.add(slot1);
		
		CapacitySlotTransaction slot2 = new CapacitySlotTransaction();
		slot2.setTransactionSlotId("hqsbdjhbwbd");
		slot2.setStartTime("01:01");
		slot2.setEndTime("10:10");
		slots.add(slot2);
		
		capacitySlotRequest.setSlots(slots);
		
		Mockito.when(capacitySlotRepository.findAllById(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotEntity));
		
		Mockito.when(capacitySlotTransactionRepository.findAllById(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		Mockito.when(capacitySlotTransactionRepository.saveAll(Mockito.anyIterable())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		capacityManagementFOHService.updateCapacitySlot(capacitySlotRequest, "aa");
		assertNotNull(capacitySlotRequest);
	}
	
	private List<CapacityTemplateEntity> getAllCapacityTemplates() {
		List<CapacityTemplateEntity> capacityList = new ArrayList<>();
		List<CapacityModelAndCapacityTemplateEntity> cteList = new ArrayList<>();
		Date effective = DateUtil.stringToDate("01/01/2000");
		Date expiry = DateUtil.stringToDate("01/01/3000");
		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(BigInteger.ONE);
		model.setCapacityModelNm("hhijhihxss");
		CapacityModelAndCapacityTemplateEntity cte  = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK pid = new CapacityModelAndCapacityTemplatePK();
		pid.setCapacityModelId(BigInteger.ONE);
		pid.setCapacityTemplateId(BigInteger.ONE);
		cte.setId(pid);
		cte.setCapacityModel(model);
		cteList.add(cte);
		CapacityTemplateTypeEntity type = new CapacityTemplateTypeEntity();
		type.setCapacityTemplateTypeId(new BigInteger("1"));
		type.setCapacityTemplateTypeNm("Date");
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setCapacityModelAndCapacityTemplates(cteList);
		capacityTemplateEntity.setCapacityTemplateNm("Lorum Ipsum");
		capacityTemplateEntity.setEffectiveDate(effective);
		capacityTemplateEntity.setExpiryDate(expiry);
		capacityTemplateEntity.setCapacityTemplateType(type);
		capacityTemplateEntity.setMonFlg("Y");
		capacityTemplateEntity.setTueFlg("Y");
		capacityTemplateEntity.setWedFlg("Y");
		capacityTemplateEntity.setThuFlg("N");
		capacityTemplateEntity.setFriFlg("N");
		capacityTemplateEntity.setSatFlg("Y");
		capacityTemplateEntity.setSunFlg("Y");
		capacityTemplateEntity.setConceptId(BigInteger.ONE);
		capacityTemplateEntity.setStartTime(java.time.LocalTime.parse("11:00:00"));
		capacityTemplateEntity.setEndTime(java.time.LocalTime.parse("12:00:00"));
		capacityTemplateEntity.setCapacityTemplateAndCapacityChannels(getCapacityTemplateAndChannels());
		capacityTemplateEntity.setCapacitySlots(getCapacitySlots());
		capacityList.add(capacityTemplateEntity);
		return capacityList;
	}
	
	private List<CapacitySlotEntity> getCapacitySlots() {
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
		capacitySlotTransactionEntity.setBusinessDate(DateUtil.convertStringtoLocalDate("01/01/2000"));
		capacitySlotTransactionEntity.setCapacityCnt(10);
		capacitySlotTransactionEntity.setEndTime(LocalTime.parse("11:14:59"));
		capacitySlotTransactionEntity.setStartTime(LocalTime.parse("11:00:00"));
		capacitySlotTransactionEntity.setBusinessDate(DateUtil.convertStringtoLocalDate("05/23/2023"));
		capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(reference);
		List<CapacitySlotEntity> capacitySlotEntities = new ArrayList<>();
		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt(1);
		capacitySlotEntity.setReference(reference);
		CapacityTemplateEntity capacityTemplateEntity=new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.ONE);
		capacitySlotEntity.setCapacityTemplate(capacityTemplateEntity);
		capacitySlotEntity.setCapacitySlotId(BigInteger.valueOf(2));
		capacitySlotEntity.setStartTime(LocalTime.parse("11:00:00"));
		capacitySlotEntity.setEndTime(LocalTime.parse("11:14:59"));
		capacitySlotEntity.setCapacitySlotType(getCapacitySlotType());
		capacitySlotEntity.setCapacityChannel(getCapacityChannel());
		capacitySlotTransactionEntity.setCapacitySlotEntity(capacitySlotEntity);
		capacitySlotEntity.setCapacitySlotTransactionEntities(Collections.singletonList(capacitySlotTransactionEntity));
		capacitySlotEntities.add(capacitySlotEntity);
		
		CapacitySlotEntity capacitySlotEntityTwo = new CapacitySlotEntity();
		capacitySlotEntityTwo.setCapacityCnt(1);
		capacitySlotEntityTwo.setReference(reference);
		capacitySlotEntityTwo.setCapacityCnt(1);
		capacitySlotEntityTwo.setCapacitySlotId(BigInteger.TWO);
		capacitySlotEntityTwo.setStartTime(LocalTime.parse("11:15:00"));
		capacitySlotEntityTwo.setEndTime(LocalTime.parse("11:29:59"));
		capacitySlotEntityTwo.setCapacityChannel(getCapacityChannel());
		capacitySlotEntityTwo.setCapacitySlotTransactionEntities(Collections.EMPTY_LIST);
		capacitySlotEntities.add(capacitySlotEntityTwo);
		
		return capacitySlotEntities;
	}
	
	private List<CapacityTemplateAndCapacityChannelEntity> getCapacityTemplateAndChannels() {
		List<CapacityTemplateAndCapacityChannelEntity> list = new ArrayList<>();
		CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new CapacityTemplateAndCapacityChannelEntity();
		CapacityTemplateAndCapacityChannelPK id = new CapacityTemplateAndCapacityChannelPK();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		capacityTemplateAndCapacityChannelEntity.setId(id);
		capacityTemplateAndCapacityChannelEntity.setCapacityChannel(getCapacityChannel());
		list.add(capacityTemplateAndCapacityChannelEntity);
		return list;
	}
	
	private CapacityChannelEntity getCapacityChannel() {
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("Togo");
		capacityChannelEntity.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		capacityChannelEntity.setOperationalHoursEndTime(Time.valueOf("12:40:55"));
		capacityChannelEntity.setIsCombinedFlg("Y");
		capacityChannelEntity.setPosName("togo");
		capacityChannelEntity.setInterval(0);
		capacityChannelEntity.setCapacityChannelAndCombinedChannels1(getCombinedCapacityChannel());
		return capacityChannelEntity;
	}

	private CapacitySlotTypeEntity getCapacitySlotType() {
		CapacitySlotTypeEntity capacitySlotTypeEntity = new CapacitySlotTypeEntity();
		capacitySlotTypeEntity.setCapacitySlotTypeId(BigInteger.ONE);
		capacitySlotTypeEntity.setCapacitySlotTypeNm("Test");
		return capacitySlotTypeEntity;
	}
	
	private List<CapacityChannelAndCombinedChannelEntity> getCombinedCapacityChannel() {
		CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity = new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("Togo");
		capacityChannelEntity.setPosName("togo");
		capacityChannelEntity.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		capacityChannelEntity.setOperationalHoursEndTime(Time.valueOf("12:40:55"));
		capacityChannelEntity.setInterval(0);
		capacityChannelAndCombinedChannelEntity.setCapacityChannel2(capacityChannelEntity);
		return Collections.singletonList(capacityChannelAndCombinedChannelEntity);
	}
	
	@Test
	void testGetCapacityTemplateForSpecificDate() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		CapacityTemplateAndBusinessDateEntity entity = new CapacityTemplateAndBusinessDateEntity();
		entity.setCapacityTemplate(getAllCapacityTemplates().get(0));
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(entity));
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/23/2023");
		assertNotNull(reference);
	}

	private CapacitySlotTransactionEntity getCapacitySlotTransactionEntity(ReferenceEntity reference) {
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = new CapacitySlotTransactionEntity();
		capacitySlotTransactionEntity.setBusinessDate(DateUtil.convertStringtoLocalDate("01/01/2000"));
		capacitySlotTransactionEntity.setCapacityCnt(10);
		capacitySlotTransactionEntity.setEndTime(LocalTime.parse("11:14:59"));
		capacitySlotTransactionEntity.setStartTime(LocalTime.parse("11:00:00"));
		capacitySlotTransactionEntity.setCapacitySlotTransactionId("qiojoiqjis");
		capacitySlotTransactionEntity.setCapacitySlotStatusReferenceEntity(reference);
		return capacitySlotTransactionEntity;
	}

	private List<CapacityChannelEntity> getBaseChannels() {
		List<CapacityChannelEntity> capacityChannelList = new ArrayList<>();
		CapacityChannelEntity channelOne = new CapacityChannelEntity();
		channelOne.setCapacityChannelNm("togo");
		channelOne.setCapacityChannelId(BigInteger.ONE);
		channelOne.setInterval(15);
		channelOne.setIsCombinedFlg("N");
		capacityChannelList.add(channelOne);
		
		CapacityChannelEntity channelTwo = new CapacityChannelEntity();
		channelTwo.setCapacityChannelNm("catering pickup");
		channelTwo.setCapacityChannelId(BigInteger.TWO);
		channelTwo.setInterval(15);
		channelTwo.setIsCombinedFlg("N");
		capacityChannelList.add(channelTwo);
		
		CapacityChannelEntity channelTThree = new CapacityChannelEntity();
		channelTThree.setCapacityChannelNm("catering delivery");
		channelTThree.setCapacityChannelId(new BigInteger("3"));
		channelTThree.setInterval(15);
		channelTThree.setIsCombinedFlg("N");
		capacityChannelList.add(channelTThree);
		return capacityChannelList;
	}
	
	@Test
	void testGetCapacityTemplateForMonday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findByMonFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/22/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForTuesday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findByTueFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/23/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForWednesday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findByWedFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/24/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForThursday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findByThuFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/25/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForFriday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findByFriFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/26/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForSaturday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findBySatFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/27/2023");
		assertNotNull(reference);
	}
	
	@Test
	void testGetCapacityTemplateForSunday() {
		
		RequestContext.setConcept("1");
		List<CapacityChannelEntity> capacityChannelList = getBaseChannels();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setReferenceId(BigInteger.ONE);
		reference.setReferenceNm("OPEN");
		
		CapacitySlotTransactionEntity capacitySlotTransactionEntity = getCapacitySlotTransactionEntity(reference);
		
		Mockito.when(capacityChannelRepo.findByConceptId(Mockito.any())).thenReturn(capacityChannelList);
		
		Mockito.when(capacityTemplateAndBusinessDateRepository.findByIdBusinessDateAndConceptId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(capacityTemplateRepo.findBySunFlgAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(getAllCapacityTemplates());
		Mockito.when(capacitySlotTransactionRepository.findByCapacityChannelAndBusinessDate(Mockito.any(), Mockito.any())).thenReturn(Collections.singletonList(capacitySlotTransactionEntity));
		
		capacityManagementFOHService.getChannelAndSlotForDateWithPopulatingSlots("05/28/2023");
		assertNotNull(reference);
	}
}