package com.darden.dash.capacity.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDatePK;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.entity.ReferenceEntity;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndCapacityChannelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.CapacityTemplateTypeRepository;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.service.impl.CapacityManagementServiceImpl;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.entity.AppParameterEntity;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.exception.ApplicationException;
import com.darden.dash.common.service.AppParameterService;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.GlobalDataCall;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

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

	@Mock
	private CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepository;

	@Mock
	private AppParameterService appParameterService;

	@Mock
	private AuditService auditService;
	
	@Mock
	private GlobalDataCall globalDataCall;
	
	@Mock
	private CapacityChannelService capacityChannelService;

	public static final String ACCESS_TOKEN = "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+oaT0v3gTSxnMmfVudUPFbnkLG+YgOIQ8i49iT1ooTzS55gZUdqW2XajNbhkDtq40rJh9jVltkBfhY/JTpwAIRJW4Ebn+M6X9xjXwNub0U4wz4nUHK7VIHNoF61xrLiAMdUcxb1GrHaDvXEzPtcWNG/ngoz5L9KOJFwwdBvS/c76k7rVO1Rn3Y0MJHY9I6wQAGa3MHmcuIxCmmkQEI59sYVsoazwRfFd5s2KYxccqWG+EJK3zJ4yTueQstPGcsJ/wPXG0jPtVwgy7Ms61Ww3ydm1R4SjUIYemITvXr/v3uVBs5qizR7PWEBSZNKPBsNctMN1PoKrAs7PEkqh791fnfK4Txjg6/jSazZYCELAD/EjR/1pkn6cEKLH2L7cLA/n8WzkPg6bD3UwRp6MgTL9PhuE+juJu3mc0pR7LI7l6A9TYwnStsGiJm+R0JOZzIn/xjCCPDpTBXvC9rPMvg2rF1MWV78jVYSMWugQnhU3tP5HjMF3fK5NXFwZyRPt9Hm4MPNHLiY0/fKcoP/e2cPAcTxuJeOBM6BmIVPYu10kMLBzIMkCbcYTptv2WNgTVPJOi4W/Rl76+HJS62szMY4DPcf3fTqVnuXTj4R7vfuzS2RZOKCYER3JF1H80KAUa4VFTv2xIAVMALMuQesjobfz6r9o0qaWFbZDXLsMQ9denalcKMwDXeBPe1QilEwDbO6gtiRb6lD1w8mJ4mWrH57hAFwN4pE/uFmI/kvqRCjF/ca7hu5i30NAlAEcp9Y45H+Bo6lwx9VUeYrfTWlDTEUuRZ0+PEKGMOjnNj+kzHCOj3Smz4vt4NW+DG0bW/GS9++4lAOtwm3bRpEYYVycjhO7pwYB/qFpfvPkiHXwDRVTty5xiNNYeHxYdBvzeUFzphAAgAFEyRGrLJVO5dWshysu";

	@BeforeEach
	public void init() {
		CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);
		ReflectionTestUtils.setField(capacityManagementServiceImpl, "capacityTemplateMapper", capacityTemplateMapper);
	}

	@Test
	void testGetAllCapacityTemplates() {
		RequestContext.setConcept("1");
		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityModelAndCapacityTemplateEntity entity = new CapacityModelAndCapacityTemplateEntity();
		entity.setCapacityModel(new CapacityModelEntity());
		entity.setCapacityTemplate(new CapacityTemplateEntity());
		entity.setCreatedBy("uou");
		entity.setCreatedDatetime(Instant.now());
		entity.setId(new CapacityModelAndCapacityTemplatePK());
		entity.setLastModifiedBy("uouo");
		entity.setLastModifiedDatetime(Instant.now());
		list.add(entity);
		lenient().when(capacityTemplateRepo.findByConceptIdAndIsDeletedFlg(Mockito.any(), Mockito.any())).thenReturn((getAllCapacityTemplates()));
		lenient().when(capacityModelAndCapacityTemplateRepository.findAll()).thenReturn(list);
		lenient().when(capacityChannelService.getReferenceData()).thenReturn(new ReferenceDatum());
		CapacityResponse res = capacityManagementServiceImpl.getAllCapacityTemplates(false, "1");
		assertNotNull(res);
	}

	@Test
	void testGetAllCapacityTemplatesForInvalidConcept() {
		RequestContext.setConcept(null);
		Exception exception = assertThrows(RuntimeException.class, () -> {
			capacityManagementServiceImpl.getAllCapacityTemplates(true, "1");
		});
		assertTrue(instanceOf(ApplicationException.class).matches(exception));
	}

	private List<CapacityTemplateEntity> getAllCapacityTemplates() {
		List<CapacityTemplateEntity> capacityList = new ArrayList<>();
		List<CapacityModelAndCapacityTemplateEntity> cteList = new ArrayList<>();
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
		capacityTemplateEntity.setEffectiveDate(new Date());
		capacityTemplateEntity.setExpiryDate(new Date());
		capacityTemplateEntity.setCapacityTemplateType(type);
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
		capacitySlotEntity.setCapacityCnt(1);
		CapacityTemplateEntity capacityTemplateEntity=new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.ONE);
		capacitySlotEntity.setCapacityTemplate(capacityTemplateEntity);
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
	void testCreateTemplate() throws JsonProcessingException {
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
		capacitySlotEntity.setCapacityCnt(1);
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
		capacityTemplateAndCapacityChannelEntity.setCapacityChannel(getCapacityChannel());

		Optional<CapacitySlotTypeEntity> optSlotType = Optional.of(getCapacitySlotType());

		List<BusinessDate> date = new ArrayList<>();
		BusinessDate bdate = new BusinessDate();
		bdate.setDate("01/01/2011");
		date.add(bdate);

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Days");
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

		RequestContext.setConcept("1");

		Mockito.lenient().when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.when(capacityTemplateTypeRepository.findByCapacityTemplateTypeNm(Mockito.anyString())).thenReturn(type);
		Mockito.lenient().when(capacityTemplateAndBusinessDateRepository.save(Mockito.any())).thenReturn(dateEntity);
		Mockito.lenient().when(referenceRepository.findById(Mockito.any())).thenReturn(optRef);
		Mockito.lenient().when(capacityChannelRepo.findById(Mockito.any())).thenReturn(optChannel);
		Mockito.lenient().when(capacityTemplateAndCapacityChannelRepository.save(Mockito.any()))
				.thenReturn(capacityTemplateAndCapacityChannelEntity);
		Mockito.lenient().when(capacitySlotTypeRepository.findById(Mockito.any())).thenReturn(optSlotType);
		Mockito.lenient().when(capacitySlotRepository.save(Mockito.any())).thenReturn(capacitySlotEntity);
		CreateTemplateResponse res = capacityManagementServiceImpl.createTemplate(request,
				CapacityServiceImplTest.ACCESS_TOKEN);
		assertNotNull(res);
	}

	@Test
	void testValidateCapacityTemplateNm() {

		RequestContext.setConcept("1");

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

		Mockito.when(capacityTemplateRepo.findByCapacityTemplateNm(Mockito.anyString()))
				.thenReturn(capacityTemplateEntity);
		boolean res = capacityManagementServiceImpl.validateCapacityTemplateNm("Lorum Ipsum");
		assertEquals(true, res);
	}

	@Test
	void shouldSoftCapacityTemplate() throws Exception {
		AppParameterEntity appParameterEntitynew = new AppParameterEntity().toBuilder().id(1)
				.parameterName("CAPACITY_SOFT_DELETE").parameterLevel("enterprise").parameterValue("Y")
				.parameterDesc("Y=soft delete , N=Hard Delete").parameterValue("Y").createdBy("aaa")
				.createdDate(Instant.now()).modifiedBy("bbbb").modifiedDate(Instant.now()).build();
		Mockito.when(appParameterService.findByParameterName(Mockito.any())).thenReturn(appParameterEntitynew);
		final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityTemplateEntity test = new CapacityTemplateEntity();
		test.setCapacityTemplateId(new BigInteger("1"));
		test.setCreatedBy("aa");
		test.setCreatedDatetime(now);
		test.setLastModifiedBy("bbb");
		test.setLastModifiedDatetime(now);
		test.setIsDeletedFlg("N");
		test.setConceptId(new BigInteger("1"));

		Mockito.when(
				capacityTemplateRepo.findByCapacityTemplateIdAndConceptIdAndIsDeletedFlg(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(test));
		capacityManagementServiceImpl.deleteByTemplateId("1", "Y", "USER");

		assertEquals("Y", test.getIsDeletedFlg());

	}

	@Test
	void shouldHardDeleteCapacityTemplate() throws Exception {
		AppParameterEntity appParameterEntityHard = new AppParameterEntity().toBuilder().id(1)
				.parameterName("MENU_SOFT_DELETE").parameterLevel("enterprise").parameterValue("N")
				.parameterDesc("Y=soft delete , N=Hard Delete").parameterValue("N").createdBy("aaa")
				.createdDate(Instant.now()).modifiedBy("aaa").modifiedDate(Instant.now()).build();

		Mockito.when(appParameterService.findByParameterName(Mockito.any())).thenReturn(appParameterEntityHard);
		final Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		CapacityTemplateEntity test = new CapacityTemplateEntity();
		test.setCapacityTemplateId(new BigInteger("1"));
		test.setCreatedBy("aa");
		test.setCreatedDatetime(now);
		test.setLastModifiedBy("bbb");
		test.setLastModifiedDatetime(now);
		test.setIsDeletedFlg("N");
		test.setConceptId(new BigInteger("1"));
		Mockito.lenient().doNothing().when(capacityTemplateRepo).deleteById(Mockito.any());
		Mockito.when(
				capacityTemplateRepo.findByCapacityTemplateIdAndConceptIdAndIsDeletedFlg(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(test));
		capacityManagementServiceImpl.deleteByTemplateId("1", "Y", "USER");

		assertEquals("N", test.getIsDeletedFlg());

	}

	@Test
	void testValidateCapacityTemplateId() {
		RequestContext.setConcept("1");
		CapacityTemplateEntity test = new CapacityTemplateEntity();
		test.setCapacityTemplateId(new BigInteger("1"));
		test.setCreatedBy("aa");
		test.setCreatedDatetime(Instant.now());
		test.setLastModifiedBy("bbb");
		test.setLastModifiedDatetime(Instant.now());
		test.setIsDeletedFlg("N");
		test.setConceptId(new BigInteger("1"));
		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("name");
		CapacityModelAndCapacityTemplateEntity mAndT = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id = new CapacityModelAndCapacityTemplatePK();
		id.setCapacityModelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		mAndT.setId(id);
		mAndT.setCapacityTemplate(test);
		mAndT.setCapacityModel(model);
		Mockito.when(capacityTemplateRepo
				.findByCapacityTemplateIdAndConceptIdAndIsDeletedFlg(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(test));
		Mockito.lenient().doNothing().when(globalDataCall).raiseException(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any());
		Mockito.when(capacityModelAndCapacityTemplateRepository.findByCapacityTemplate(test))
				.thenReturn(Collections.singletonList(mAndT));
		ApplicationErrors applicationErrors = new ApplicationErrors();
		boolean res = capacityManagementServiceImpl.validateCapacityTemplateId("1", applicationErrors);
		assertEquals(true, res);
	}

	@Test
	void testValidateCapacityTemplateNmForCreate() {
		RequestContext.setConcept("1");

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
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		boolean res = capacityManagementServiceImpl.validateCapacityTemplateNmForCreate("aaa", "1");
		assertEquals(false, res);
	}

	@Test
	void testUpdateCapacityTemplate() throws JsonProcessingException {
		RequestContext.setConcept("1");

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
		Mockito.when(jwtUtils.findUserDetail(Mockito.anyString())).thenReturn("user");

		CapacityTemplateTypeEntity tType = new CapacityTemplateTypeEntity();
		tType.setCapacityTemplateTypeId(new BigInteger("1"));
		tType.setCapacityTemplateTypeNm("Dates");
		tType.setCreatedBy("aa");
		tType.setCreatedDatetime(Instant.now());
		tType.setIsDeletedFlg("N");
		tType.setLastModifiedBy("vv");
		tType.setLastModifiedDatetime(Instant.now());

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
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

		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacityTemplateTypeRepository.findByCapacityTemplateTypeNm(Mockito.anyString()))
				.thenReturn(tType);
		Mockito.lenient().doNothing().when(capacityTemplateAndBusinessDateRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.lenient().doNothing().when(capacitySlotRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.lenient().doNothing().when(capacityTemplateAndCapacityChannelRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacitySlotRepository.findByCapacityChannel(Mockito.any()))
				.thenReturn(getCapacitySlots());
		Mockito.lenient().doNothing().when(capacitySlotRepository).delete(Mockito.any());
		CreateTemplateResponse res = capacityManagementServiceImpl.updateCapacityTemplate(request, ACCESS_TOKEN,
				new BigInteger("1"));
		assertNotNull(res);
	}
	
	@Test
	void testUpdateCapacityTemplateDays() throws JsonProcessingException {
		RequestContext.setConcept("1");

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
		Mockito.when(jwtUtils.findUserDetail(Mockito.anyString())).thenReturn("user");

		CapacityTemplateTypeEntity tType = new CapacityTemplateTypeEntity();
		tType.setCapacityTemplateTypeId(new BigInteger("1"));
		tType.setCapacityTemplateTypeNm("Days");
		tType.setCreatedBy("aa");
		tType.setCreatedDatetime(Instant.now());
		tType.setIsDeletedFlg("N");
		tType.setLastModifiedBy("vv");
		tType.setLastModifiedDatetime(Instant.now());

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
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
		request.setTemplateTypeName("Days");
		CapacityChannelEntity capacityChannelEntity=new CapacityChannelEntity();
		capacityChannelEntity.setConceptId(BigInteger.ONE);
		Mockito.when(capacityChannelRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityChannelEntity));
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacityTemplateTypeRepository.findByCapacityTemplateTypeNm(Mockito.anyString()))
				.thenReturn(tType);
		Mockito.lenient().doNothing().when(capacityTemplateAndBusinessDateRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.lenient().doNothing().when(capacitySlotRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.lenient().doNothing().when(capacityTemplateAndCapacityChannelRepository).deleteAllBycapacityTemplate(Mockito.any());
		Mockito.when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacitySlotRepository.findByCapacityChannel(Mockito.any()))
				.thenReturn(getCapacitySlots());
		Mockito.lenient().doNothing().when(capacitySlotRepository).delete(Mockito.any());
		CreateTemplateResponse res = capacityManagementServiceImpl.updateCapacityTemplate(request, ACCESS_TOKEN,
				new BigInteger("1"));
		assertNotNull(res);
	}

	@Test
	void testValidateCapacityModelBusinessDates() {

		CapacityTemplateTypeEntity days = new CapacityTemplateTypeEntity();
		days.setCapacityTemplateTypeId(BigInteger.valueOf(1));
		days.setCapacityTemplateTypeNm("Days");

		CapacityTemplateEntity c1 = new CapacityTemplateEntity();
		c1.setCapacityTemplateId(BigInteger.valueOf(1));
		c1.setCapacityTemplateType(days);
		c1.setCapacityTemplateNm("Lorum Ipsum");
		c1.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c1.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c1.setMonFlg("Y");
		c1.setTueFlg("Y");
		c1.setWedFlg("Y");
		c1.setThuFlg("N");
		c1.setFriFlg("N");
		c1.setSatFlg("Y");
		c1.setSunFlg("Y");
		c1.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c1.setEndTime(java.time.LocalTime.parse("12:46:55"));

		CapacityTemplateEntity c2 = new CapacityTemplateEntity();
		c2.setCapacityTemplateId(BigInteger.valueOf(2));
		c2.setCapacityTemplateNm("Lm");
		c2.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c2.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c2.setCapacityTemplateType(days);
		c2.setMonFlg("N");
		c2.setTueFlg("N");
		c2.setWedFlg("N");
		c2.setThuFlg("N");
		c2.setFriFlg("N");
		c2.setSatFlg("N");
		c2.setSunFlg("N");
		c2.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c2.setEndTime(java.time.LocalTime.parse("12:46:55"));

		CapacityTemplateTypeEntity dates = new CapacityTemplateTypeEntity();
		dates.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		dates.setCapacityTemplateTypeNm("Dates");

		List<CapacityTemplateAndBusinessDateEntity> d1 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(Date.from(Instant.now()));
		done.setId(id);
		d1.add(done);

		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c3.setCapacityTemplateType(dates);
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));

		List<CapacityTemplateAndBusinessDateEntity> d2 = new ArrayList<>();

		CapacityTemplateEntity c4 = new CapacityTemplateEntity();
		c4.setCapacityTemplateId(BigInteger.valueOf(4));
		c4.setCapacityTemplateNm("Lmg");
		c4.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c4.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c4.setCapacityTemplateType(dates);
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c4.setEndTime(java.time.LocalTime.parse("12:46:55"));

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");

		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityModelAndCapacityTemplateEntity cmct = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK idct = new CapacityModelAndCapacityTemplatePK();
		idct.setCapacityModelId(new BigInteger("1"));
		idct.setCapacityTemplateId(new BigInteger("1"));
		cmct.setId(idct);
		cmct.setCapacityTemplate(c1);
		cmct.setCapacityModel(model);

		list.add(cmct);

		CapacityModelAndCapacityTemplateEntity cm2 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id2 = new CapacityModelAndCapacityTemplatePK();
		id2.setCapacityModelId(new BigInteger("1"));
		id2.setCapacityTemplateId(new BigInteger("2"));
		cm2.setId(id2);
		cm2.setCapacityTemplate(c2);
		cm2.setCapacityModel(model);

		list.add(cm2);

		CapacityModelAndCapacityTemplateEntity cm3 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id3 = new CapacityModelAndCapacityTemplatePK();
		id3.setCapacityModelId(new BigInteger("1"));
		id3.setCapacityTemplateId(new BigInteger("3"));
		cm3.setId(id3);
		cm3.setCapacityTemplate(c3);
		cm3.setCapacityModel(model);

		list.add(cm3);

		CapacityModelAndCapacityTemplateEntity cm4 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id4 = new CapacityModelAndCapacityTemplatePK();
		id4.setCapacityModelId(new BigInteger("1"));
		id4.setCapacityTemplateId(new BigInteger("4"));
		cm4.setId(id4);
		cm4.setCapacityTemplate(c4);
		cm4.setCapacityModel(model);

		list.add(cm4);

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Days");
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/10/2011");
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

		Mockito.when(capacityModelAndCapacityTemplateRepository.findAll()).thenReturn(list);
		boolean res = capacityManagementServiceImpl.validateCapacityModelBusinessDates(request,"1");
		assertEquals(false, res);
	}

	@Test
	void testValidateDate() {

		List<CapacityModelAndCapacityTemplateEntity> list = getListOfCapacityModelAndCapacityTemplate();

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(4);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Dates");
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/10/2011");
		List<BusinessDate> dat = new ArrayList<>();
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dat.add(date);
		request.setCapacityTemplateName("Dates");
		request.setBusinessDates(dat);
		when(capacityModelAndCapacityTemplateRepository.findAll()).thenReturn(list);
		boolean res2 = capacityManagementServiceImpl.validateCapacityModelBusinessDates(request,"1");
		assertEquals(false, res2);
	}

	private List<CapacityModelAndCapacityTemplateEntity> getListOfCapacityModelAndCapacityTemplate() {
		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityTemplateTypeEntity dates = new CapacityTemplateTypeEntity();
		dates.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		dates.setCapacityTemplateTypeNm("Dates");

		List<CapacityTemplateAndBusinessDateEntity> d1 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done.setId(id);
		d1.add(done);

		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c3.setCapacityTemplateType(dates);
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setCapacityTemplateAndBusinessDates(d1);

		List<CapacityTemplateAndBusinessDateEntity> d2 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done1 = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id1 = new CapacityTemplateAndBusinessDatePK();
		id1.setCapacityTemplateId(BigInteger.valueOf(3));
		id1.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done1.setId(id);
		d2.add(done);

		CapacityTemplateEntity c4 = new CapacityTemplateEntity();
		c4.setCapacityTemplateId(BigInteger.valueOf(4));
		c4.setCapacityTemplateNm("Lmg");
		c4.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c4.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c4.setCapacityTemplateType(dates);
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c4.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c4.setCapacityTemplateAndBusinessDates(d2);

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		
		CapacityModelAndCapacityTemplateEntity cm3 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id3 = new CapacityModelAndCapacityTemplatePK();
		id3.setCapacityModelId(new BigInteger("1"));
		id3.setCapacityTemplateId(new BigInteger("3"));
		cm3.setId(id3);
		cm3.setCapacityTemplate(c3);
		cm3.setCapacityModel(model);

		list.add(cm3);

		CapacityModelAndCapacityTemplateEntity cm4 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id4 = new CapacityModelAndCapacityTemplatePK();
		id4.setCapacityModelId(new BigInteger("1"));
		id4.setCapacityTemplateId(new BigInteger("4"));
		cm4.setId(id4);
		cm4.setCapacityTemplate(c4);
		cm4.setCapacityModel(model);

		list.add(cm4);
		return list;
	}

	@Test
	void testValidateDays() {

		CapacityTemplateTypeEntity dates = new CapacityTemplateTypeEntity();
		dates.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		dates.setCapacityTemplateTypeNm("Days");

		List<CapacityTemplateAndBusinessDateEntity> d1 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done.setId(id);
		d1.add(done);

		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c3.setCapacityTemplateType(dates);
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setMonFlg("N");
		c3.setTueFlg("N");
		c3.setWedFlg("N");
		c3.setThuFlg("N");
		c3.setFriFlg("N");
		c3.setSatFlg("N");
		c3.setSunFlg("N");

		List<CapacityTemplateAndBusinessDateEntity> d2 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done1 = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id1 = new CapacityTemplateAndBusinessDatePK();
		id1.setCapacityTemplateId(BigInteger.valueOf(3));
		id1.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done1.setId(id);
		d2.add(done);

		CapacityTemplateEntity c4 = new CapacityTemplateEntity();
		c4.setCapacityTemplateId(BigInteger.valueOf(4));
		c4.setCapacityTemplateNm("Lmg");
		c4.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c4.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c4.setCapacityTemplateType(dates);
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c4.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setMonFlg("Y");
		c4.setTueFlg("Y");
		c4.setWedFlg("Y");
		c4.setThuFlg("Y");
		c4.setFriFlg("Y");
		c4.setSatFlg("Y");
		c4.setSunFlg("Y");

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");

		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityModelAndCapacityTemplateEntity cm3 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id3 = new CapacityModelAndCapacityTemplatePK();
		id3.setCapacityModelId(new BigInteger("1"));
		id3.setCapacityTemplateId(new BigInteger("3"));
		cm3.setId(id3);
		cm3.setCapacityTemplate(c3);
		cm3.setCapacityModel(model);

		list.add(cm3);

		CapacityModelAndCapacityTemplateEntity cm4 = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id4 = new CapacityModelAndCapacityTemplatePK();
		id4.setCapacityModelId(new BigInteger("1"));
		id4.setCapacityTemplateId(new BigInteger("4"));
		cm4.setId(id4);
		cm4.setCapacityTemplate(c4);
		cm4.setCapacityModel(model);

		list.add(cm4);

		CreateCapacityTemplateRequest request = getTemplateRequestWithDates();
		when(capacityModelAndCapacityTemplateRepository.findAll()).thenReturn(list);
		boolean res2 = capacityManagementServiceImpl.validateCapacityModelBusinessDates(request,"1");
		assertEquals(false, res2);
	}

	private CreateCapacityTemplateRequest getTemplateRequestWithDates() {
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(4);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Days");
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/10/2011");
		request.setMonDay("Y");
		request.setTueDay("Y");
		request.setWedDay("Y");
		request.setThuDay("Y");
		request.setFriDay("Y");
		request.setSatDay("Y");
		request.setSunDay("Y");
		List<BusinessDate> dat = new ArrayList<>();
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dat.add(date);
		request.setCapacityTemplateName("Days");
		request.setBusinessDates(dat);
		return request;
	}

	@Test
	void testCreateTemplateDates() throws JsonProcessingException {
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
		CapacityTemplateAndBusinessDatePK pk = new CapacityTemplateAndBusinessDatePK();
		pk.setBusinessDate(new Date());
		pk.setCapacityTemplateId(BigInteger.ONE);
		dateEntity.setId(pk);

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
		capacitySlotEntity.setCapacityCnt(1);
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
		capacityTemplateAndCapacityChannelEntity.setCapacityChannel(getCapacityChannel());

		Optional<CapacitySlotTypeEntity> optSlotType = Optional.of(getCapacitySlotType());

		List<BusinessDate> date = new ArrayList<>();
		BusinessDate bdate = new BusinessDate();
		bdate.setDate("01/01/2011");
		date.add(bdate);

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Dated");
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
		type.setCapacityTemplateTypeNm("Dates");
		type.setIsDeletedFlg("N");

		RequestContext.setConcept("1");

		Mockito.lenient().when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.lenient().when(capacityTemplateTypeRepository.findByCapacityTemplateTypeNm(Mockito.anyString()))
				.thenReturn(type);
		Mockito.lenient().when(capacityTemplateAndBusinessDateRepository.save(Mockito.any())).thenReturn(dateEntity);
		Mockito.lenient().when(referenceRepository.findById(Mockito.any())).thenReturn(optRef);
		Mockito.lenient().when(capacityChannelRepo.findById(Mockito.any())).thenReturn(optChannel);
		Mockito.lenient().when(capacityTemplateAndCapacityChannelRepository.save(Mockito.any()))
				.thenReturn(capacityTemplateAndCapacityChannelEntity);
		Mockito.lenient().when(capacitySlotTypeRepository.findById(Mockito.any())).thenReturn(optSlotType);
		Mockito.lenient().when(capacitySlotRepository.save(Mockito.any())).thenReturn(capacitySlotEntity);
		CreateTemplateResponse res = capacityManagementServiceImpl.createTemplate(request,
				CapacityServiceImplTest.ACCESS_TOKEN);
		assertNotNull(res);
	}

	@Test
	void testValidateCapacityTemplateIdNegative() {
		when(capacityTemplateRepo
				.findByCapacityTemplateIdAndConceptIdAndIsDeletedFlg(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		try {
			ApplicationErrors applicationErrors = new ApplicationErrors();
			capacityManagementServiceImpl.validateCapacityTemplateId("1", applicationErrors);
		} catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}

	@Test
	void testDeleteByTemplateId() {
		when(appParameterService.findByParameterName(Mockito.anyString())).thenReturn(null);
		try {
			capacityManagementServiceImpl.deleteByTemplateId("1", "1", "1");
		} catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}
	
	@Test
	void testUpdateCapacityTemplateDates() throws JsonProcessingException {
		RequestContext.setConcept("1");

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
		CapacityTemplateAndBusinessDateEntity capacityTemplateAndBusinessDateEntity=new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK capacityTemplateAndBusinessDatePK=new CapacityTemplateAndBusinessDatePK();
		capacityTemplateAndBusinessDatePK.setBusinessDate(new Date());
		capacityTemplateAndBusinessDatePK.setCapacityTemplateId(BigInteger.ONE);
		capacityTemplateAndBusinessDateEntity.setId(capacityTemplateAndBusinessDatePK);
		capacityTemplateEntity.setCapacityTemplateAndBusinessDates(Collections.singletonList(capacityTemplateAndBusinessDateEntity));
		Mockito.when(jwtUtils.findUserDetail(Mockito.anyString())).thenReturn("user");

		CapacityTemplateTypeEntity tType = new CapacityTemplateTypeEntity();
		tType.setCapacityTemplateTypeId(new BigInteger("1"));
		tType.setCapacityTemplateTypeNm("Datess");
		tType.setCreatedBy("aa");
		tType.setCreatedDatetime(Instant.now());
		tType.setIsDeletedFlg("N");
		tType.setLastModifiedBy("vv");
		tType.setLastModifiedDatetime(Instant.now());

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
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
		request.setTemplateTypeName("Dates");
		BusinessDate businessDate=new BusinessDate();
		businessDate.setDate("02/02/2021");
		request.setBusinessDates(Collections.singletonList(businessDate));
		CapacityChannelEntity capacityChannelEntity=new CapacityChannelEntity();
		capacityChannelEntity.setConceptId(BigInteger.ONE);
		Mockito.lenient().doNothing().when(capacityTemplateAndBusinessDateRepository).deleteById(Mockito.any());
		Mockito.when(capacityChannelRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityChannelEntity));
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacityTemplateTypeRepository.findByCapacityTemplateTypeNm(Mockito.anyString()))
				.thenReturn(tType);
		Mockito.when(capacityTemplateRepo.save(Mockito.any())).thenReturn(capacityTemplateEntity);
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.lenient().when(capacitySlotRepository.findByCapacityChannel(Mockito.any()))
				.thenReturn(getCapacitySlots());
		Mockito.lenient().doNothing().when(capacitySlotRepository).delete(Mockito.any());
		CreateTemplateResponse res = capacityManagementServiceImpl.updateCapacityTemplate(request, ACCESS_TOKEN,
				new BigInteger("1"));
		assertNotNull(res);
	}
	
	@Test
	void testGetAllModelsRelatingToTemplateIdList() {
		RequestContext.setConcept("1");
		Set<BigInteger> temp = new HashSet<>();
		temp.add(BigInteger.ONE);
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(getAllCapacityTemplates());
		assertNotNull(capacityManagementServiceImpl.getAllModelsRelatingToTemplateIdList(temp));
	}
	
	@Test
	void testValidateTemplateDates() {
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(4);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Dates");
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/10/2011");
		request.setMonDay("Y");
		request.setTueDay("Y");
		request.setWedDay("Y");
		request.setThuDay("Y");
		request.setFriDay("Y");
		request.setSatDay("Y");
		request.setSunDay("Y");
		List<BusinessDate> dat = new ArrayList<>();
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dat.add(date);
		request.setCapacityTemplateName("Days");
		request.setBusinessDates(dat);
		Mockito.when(capacityModelAndCapacityTemplateRepository.findAll()).thenReturn(getListOfCapacityModelAndCapacityTemplate());
		boolean res = capacityManagementServiceImpl.validateCapacityModelBusinessDates(request, "3");
		assertEquals(true, res);
	}
	
}