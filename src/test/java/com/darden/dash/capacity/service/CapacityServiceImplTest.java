package com.darden.dash.capacity.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateResponseSlot;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndCapacityChannelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.CapacityTemplateTypeRepository;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.service.impl.CapacityManagementServiceImpl;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.exception.ApplicationException;
import com.darden.dash.common.util.JwtUtils;

@ExtendWith({ MockitoExtension.class })
class CapacityServiceImplTest {

	@InjectMocks
	CapacityManagementServiceImpl capacityManagementServiceImpl;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private CapacityTemplateRepo capacityTemplateRepo;

	@Mock
	private CapacityChannelRepo capacityChannelRepo;
	
	@Mock
	private CapacityTemplateTypeRepository capacityTemplateTypeRepository;
	
	@Mock
	private CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository;
	
	@Mock
	private CapacitySlotTypeRepository capacitySlotTypeRepository;
	
	@Mock
	private ReferenceRepository referenceRepository;
	
	@Mock
	private CapacitySlotRepository capacitySlotRepository;
	
	@Mock
	private CapacityTemplateAndCapacityChannelRepository capacityTemplateAndCapacityChannelRepository;
	
	public static final String ACCESS_TOKEN = "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+oaT0v3gTSxnMmfVudUPFbnkLG+YgOIQ8i49iT1ooTzS55gZUdqW2XajNbhkDtq40rJh9jVltkBfhY/JTpwAIRJW4Ebn+M6X9xjXwNub0U4wz4nUHK7VIHNoF61xrLiAMdUcxb1GrHaDvXEzPtcWNG/ngoz5L9KOJFwwdBvS/c76k7rVO1Rn3Y0MJHY9I6wQAGa3MHmcuIxCmmkQEI59sYVsoazwRfFd5s2KYxccqWG+EJK3zJ4yTueQstPGcsJ/wPXG0jPtVwgy7Ms61Ww3ydm1R4SjUIYemITvXr/v3uVBs5qizR7PWEBSZNKPBsNctMN1PoKrAs7PEkqh791fnfK4Txjg6/jSazZYCELAD/EjR/1pkn6cEKLH2L7cLA/n8WzkPg6bD3UwRp6MgTL9PhuE+juJu3mc0pR7LI7l6A9TYwnStsGiJm+R0JOZzIn/xjCCPDpTBXvC9rPMvg2rF1MWV78jVYSMWugQnhU3tP5HjMF3fK5NXFwZyRPt9Hm4MPNHLiY0/fKcoP/e2cPAcTxuJeOBM6BmIVPYu10kMLBzIMkCbcYTptv2WNgTVPJOi4W/Rl76+HJS62szMY4DPcf3fTqVnuXTj4R7vfuzS2RZOKCYER3JF1H80KAUa4VFTv2xIAVMALMuQesjobfz6r9o0qaWFbZDXLsMQ9denalcKMwDXeBPe1QilEwDbO6gtiRb6lD1w8mJ4mWrH57hAFwN4pE/uFmI/kvqRCjF/ca7hu5i30NAlAEcp9Y45H+Bo6lwx9VUeYrfTWlDTEUuRZ0+PEKGMOjnNj+kzHCOj3Smz4vt4NW+DG0bW/GS9++4lAOtwm3bRpEYYVycjhO7pwYB/qFpfvPkiHXwDRVTty5xiNNYeHxYdBvzeUFzphAAgAFEyRGrLJVO5dWshysu";

	@BeforeEach
	public void init() {
		CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);
		ReflectionTestUtils.setField(capacityManagementServiceImpl, "capacityTemplateMapper", capacityTemplateMapper);
		CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
		ReflectionTestUtils.setField(capacityManagementServiceImpl, "capacityChannelMapper", capacityChannelMapper);
	}

	@Test
	void testGetAllCapacityTemplates() {
		RequestContext.setConcept("1");
		when(capacityTemplateRepo.findByConceptId(BigInteger.ONE)).thenReturn((getAllCapacityTemplates()));
		when(capacityChannelRepo.findAll()).thenReturn(Collections.singletonList(getCapacityChannel()));
		ResponseEntity<Object> res = capacityManagementServiceImpl.getAllCapacityTemplates();
		assertNotNull(res);
	}
	
	@Test
	void testGetAllCapacityTemplatesForInvalidConcept() {
		RequestContext.setConcept(null);
		Exception exception = assertThrows(RuntimeException.class, () -> {
			capacityManagementServiceImpl.getAllCapacityTemplates();
	    });
		assertTrue(instanceOf(ApplicationException.class).matches(exception));
	}

	private List<CapacityTemplateEntity> getAllCapacityTemplates() {
		List<CapacityTemplateEntity> capacityList = new ArrayList<>();
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setCapacityTemplateNm("Lorum Ipsum");
		capacityTemplateEntity.setEffectiveDate(new Date());
		capacityTemplateEntity.setExpiryDate(new Date());
		capacityTemplateEntity.setMonFlg("Y");
		capacityTemplateEntity.setTueFlg("Y");
		capacityTemplateEntity.setWedFlg("Y");
		capacityTemplateEntity.setThuFlg("N");
		capacityTemplateEntity.setFriFlg("N");
		capacityTemplateEntity.setSatFlg("Y");
		capacityTemplateEntity.setSunFlg("Y");
		capacityTemplateEntity.setConceptId(BigInteger.ONE);
		capacityTemplateEntity.setStartTime(java.time.LocalTime.parse("11:46:55"));
		capacityTemplateEntity.setEndTime(java.time.LocalTime.parse("12:46:55"));
		capacityTemplateEntity.setCapacityTemplateAndCapacityChannels(getCapacityTemplateAndChannels());
		capacityTemplateEntity.setCapacitySlots(getCapacitySlots());
		capacityList.add(capacityTemplateEntity);
		return capacityList;
	}

	private List<CapacitySlotEntity> getCapacitySlots() {
		List<CapacitySlotEntity> capacitySlotEntities = new ArrayList<>();
		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt("1");
		capacitySlotEntity.setCapacitySlotId(BigInteger.valueOf(2));
		capacitySlotEntity.setStartTime(java.time.LocalTime.parse("11:00:00"));
		capacitySlotEntity.setEndTime(java.time.LocalTime.parse("11:46:55"));
		capacitySlotEntity.setCapacitySlotType(getCapacitySlotType());
		capacitySlotEntity.setCapacityChannel(getCapacityChannel());
		capacitySlotEntity.setIsDeletedFlg("N");
		capacitySlotEntities.add(capacitySlotEntity);
		return capacitySlotEntities;
	}

	private CapacitySlotTypeEntity getCapacitySlotType() {
		CapacitySlotTypeEntity capacitySlotTypeEntity = new CapacitySlotTypeEntity();
		capacitySlotTypeEntity.setCapacitySlotTypeId(BigInteger.ONE);
		capacitySlotTypeEntity.setCapacitySlotTypeNm("Test");
		return capacitySlotTypeEntity;
	}

	private List<CapacityTemplateAndCapacityChannelEntity> getCapacityTemplateAndChannels() {
		List<CapacityTemplateAndCapacityChannelEntity> list = new ArrayList<>();
		CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new CapacityTemplateAndCapacityChannelEntity();
		capacityTemplateAndCapacityChannelEntity.setIsSelectedFlag("N");
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
		capacityChannelEntity.setFirendlyNm("togo");
		capacityChannelEntity.setInterval(0);
		capacityChannelEntity.setCapacityChannelAndCombinedChannels1(getCombinedCapacityChannel());
		return capacityChannelEntity;
	}

	private List<CapacityChannelAndCombinedChannelEntity> getCombinedCapacityChannel() {
		CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity=new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("Togo");
		capacityChannelEntity.setFirendlyNm("togo");
		capacityChannelEntity.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		capacityChannelEntity.setOperationalHoursEndTime(Time.valueOf("12:40:55"));
		capacityChannelEntity.setInterval(0);
		capacityChannelAndCombinedChannelEntity.setCapacityChannel2(capacityChannelEntity);
		return Collections.singletonList(capacityChannelAndCombinedChannelEntity);
	}
	
	@Test
	void testCreateTemplate() {
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setCapacityTemplateNm("Lorum Ipsum");
		capacityTemplateEntity.setEffectiveDate(new Date());
		capacityTemplateEntity.setExpiryDate(new Date());
		capacityTemplateEntity.setMonFlg("Y");
		capacityTemplateEntity.setTueFlg("Y");
		capacityTemplateEntity.setWedFlg("Y");
		capacityTemplateEntity.setThuFlg("N");
		capacityTemplateEntity.setFriFlg("N");
		capacityTemplateEntity.setSatFlg("Y");
		capacityTemplateEntity.setSunFlg("Y");
		capacityTemplateEntity.setStartTime(java.time.LocalTime.parse("11:46:55"));
		capacityTemplateEntity.setEndTime(java.time.LocalTime.parse("12:46:55"));
		capacityTemplateEntity.setCapacityTemplateAndCapacityChannels(getCapacityTemplateAndChannels());
		capacityTemplateEntity.setCapacitySlots(getCapacitySlots());
		CapacityTemplateAndBusinessDateEntity dateEntity = new CapacityTemplateAndBusinessDateEntity();
		
		ReferenceEntity reference = new ReferenceEntity();
		reference.setConceptId(new BigInteger("1"));
		reference.setReferenceId(new BigInteger("1"));
		reference.setReferenceCd("21212");
		reference.setReferenceDesc("dnskndnjks");
		reference.setReferenceNm("refNm");
		Optional<ReferenceEntity> optRef = Optional.of(reference);
		
		CapacityChannelEntity channel = new CapacityChannelEntity();
		channel.setCapacityChannelId(new BigInteger("1"));
		channel.setConceptId(new BigInteger("1"));
		channel.setCapacityChannelNm("pogo");
		channel.setCapacitySlots(getCapacitySlots());
		channel.setIsCombinedFlg("N");
		channel.setOperationalHoursEndTime(Time.valueOf("11:40:55"));
		channel.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		Optional<CapacityChannelEntity> optChannel = Optional.of(channel);
		
		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt("1");
		capacitySlotEntity.setCapacitySlotId(BigInteger.valueOf(2));
		capacitySlotEntity.setStartTime(java.time.LocalTime.parse("11:00:00"));
		capacitySlotEntity.setEndTime(java.time.LocalTime.parse("11:46:55"));
		capacitySlotEntity.setCapacitySlotType(getCapacitySlotType());
		capacitySlotEntity.setCapacityChannel(getCapacityChannel());
		capacitySlotEntity.setIsDeletedFlg("N");
		
		CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new CapacityTemplateAndCapacityChannelEntity();
		CapacityTemplateAndCapacityChannelPK id = new CapacityTemplateAndCapacityChannelPK();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		capacityTemplateAndCapacityChannelEntity.setId(id);
		capacityTemplateAndCapacityChannelEntity.setIsSelectedFlag("N");
		capacityTemplateAndCapacityChannelEntity.setCapacityChannel(getCapacityChannel());
		
		Optional<CapacitySlotTypeEntity> optSlotType = Optional.of(getCapacitySlotType());
		
		List<BusinessDate> date = new ArrayList<>();
		BusinessDate bdate = new BusinessDate();
		bdate.setDate("01/01/2011");
		date.add(bdate);
		
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount("1");
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<CreateResponseSlot> slotList = new ArrayList<>();
		CreateResponseSlot slot = new CreateResponseSlot();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setBusinessDates(date);
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/01/2011");
		request.setMonDay("Y");
		request.setTueDay("Y");
		request.setWedDay("Y");
		request.setThuDay("N");
		request.setFriDay("N");
		request.setSatDay("Y");
		request.setSunDay("Y");
		request.setSlotStartTime("01:02");
		request.setSlotEndTime("02:09");
		request.setIsDeletedFlag("N");
		request.setSlotChannels(slotList);
		
		CapacityTemplateTypeEntity type = new CapacityTemplateTypeEntity();
		type.setCapacityTemplateTypeId(new BigInteger("1"));
		type.setCapacityTemplateTypeNm("Days");
		type.setIsDeletedFlg("N");
		Optional<CapacityTemplateTypeEntity> optType = Optional.of(type);
		
		RequestContext.setConcept("1");
		
		Mockito.when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.when(capacityTemplateTypeRepository.findById(Mockito.any())).thenReturn(optType);
		Mockito.lenient().when(capacityTemplateAndBusinessDateRepository.save(Mockito.any())).thenReturn(dateEntity);
		Mockito.when(referenceRepository.findById(Mockito.any())).thenReturn(optRef);
		Mockito.when(capacityChannelRepo.findById(Mockito.any())).thenReturn(optChannel);
		Mockito.when(capacityTemplateAndCapacityChannelRepository.save(Mockito.any())).thenReturn(capacityTemplateAndCapacityChannelEntity);
		Mockito.when(capacitySlotTypeRepository.findById(Mockito.any())).thenReturn(optSlotType);
		Mockito.when(capacitySlotRepository.save(Mockito.any())).thenReturn(capacitySlotEntity);
		CreateTemplateResponse res = capacityManagementServiceImpl.createTemplate(request, CapacityServiceImplTest.ACCESS_TOKEN);
		assertNotNull(res);
	}
}
