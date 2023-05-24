package com.darden.dash.capacity.boh.service.impl;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateSlotEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateSlotTypeEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateTypeEntity;
import com.darden.dash.capacity.boh.mapper.RestaurantCapacityTemplateMapper;
import com.darden.dash.capacity.boh.model.ViewRestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.repository.RestaurantTemplateRepository;
import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndCapacityChannelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.repository.CapacityTemplateTypeRepository;
import com.darden.dash.capacity.repository.ReferenceRepository;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.service.AppParameterService;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.GlobalDataCall;
import com.darden.dash.common.util.JwtUtils;

@ExtendWith({ MockitoExtension.class })
class RestaurantCapacityTemplateServiceImplTest {

	@InjectMocks
	RestaurantCapacityTemplateServiceImpl restaurantCapacityTemplateServiceImpl;

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
	
	@Mock
	private RestaurantTemplateRepository restaurantTemplateRepository;
	
	@Mock
	RestaurantTemplateTypeEntity restaurantTemplateTypeEntity;
	
	
	@Mock
	RestaurantCapacityTemplateMapper restaurantTemplateMapper = Mappers.getMapper(RestaurantCapacityTemplateMapper.class);
	
	public static final String ACCESS_TOKEN = "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+oaT0v3gTSxnMmfVudUPFbnkLG+YgOIQ8i49iT1ooTzS55gZUdqW2XajNbhkDtq40rJh9jVltkBfhY/JTpwAIRJW4Ebn+M6X9xjXwNub0U4wz4nUHK7VIHNoF61xrLiAMdUcxb1GrHaDvXEzPtcWNG/ngoz5L9KOJFwwdBvS/c76k7rVO1Rn3Y0MJHY9I6wQAGa3MHmcuIxCmmkQEI59sYVsoazwRfFd5s2KYxccqWG+EJK3zJ4yTueQstPGcsJ/wPXG0jPtVwgy7Ms61Ww3ydm1R4SjUIYemITvXr/v3uVBs5qizR7PWEBSZNKPBsNctMN1PoKrAs7PEkqh791fnfK4Txjg6/jSazZYCELAD/EjR/1pkn6cEKLH2L7cLA/n8WzkPg6bD3UwRp6MgTL9PhuE+juJu3mc0pR7LI7l6A9TYwnStsGiJm+R0JOZzIn/xjCCPDpTBXvC9rPMvg2rF1MWV78jVYSMWugQnhU3tP5HjMF3fK5NXFwZyRPt9Hm4MPNHLiY0/fKcoP/e2cPAcTxuJeOBM6BmIVPYu10kMLBzIMkCbcYTptv2WNgTVPJOi4W/Rl76+HJS62szMY4DPcf3fTqVnuXTj4R7vfuzS2RZOKCYER3JF1H80KAUa4VFTv2xIAVMALMuQesjobfz6r9o0qaWFbZDXLsMQ9denalcKMwDXeBPe1QilEwDbO6gtiRb6lD1w8mJ4mWrH57hAFwN4pE/uFmI/kvqRCjF/ca7hu5i30NAlAEcp9Y45H+Bo6lwx9VUeYrfTWlDTEUuRZ0+PEKGMOjnNj+kzHCOj3Smz4vt4NW+DG0bW/GS9++4lAOtwm3bRpEYYVycjhO7pwYB/qFpfvPkiHXwDRVTty5xiNNYeHxYdBvzeUFzphAAgAFEyRGrLJVO5dWshysu";
	
	
	
	
	RestaurantTemplateEntity capacityList = new RestaurantTemplateEntity();
	List<CapacityModelAndCapacityTemplateEntity> cteList = new ArrayList<>();
	RestaurantTemplateEntity capacityTemplateEntity = new RestaurantTemplateEntity();
	@BeforeEach
	public void init() {
		
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		RestaurantCapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(RestaurantCapacityTemplateMapper.class);
		ReflectionTestUtils.setField(restaurantCapacityTemplateServiceImpl, "restaurantTemplateMapper", capacityTemplateMapper);
		
		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(BigInteger.ONE);
		model.setCapacityModelNm("hhijhihxss");
		CapacityModelAndCapacityTemplateEntity cte = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK pid = new CapacityModelAndCapacityTemplatePK();
		pid.setCapacityModelId(BigInteger.ONE);
		pid.setCapacityTemplateId(BigInteger.ONE);
		cte.setId(pid);
		cte.setCapacityModel(model);
		cteList.add(cte);
		RestaurantTemplateTypeEntity type = new RestaurantTemplateTypeEntity();
		type.setRestaurantTemplateTypeId(new BigInteger("1"));
		type.setRestaurantTemplateTypeNm("Date");
		
		capacityTemplateEntity.setRestaurantTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setResturantTemplateNm("Lorum Ipsum");
		capacityTemplateEntity.setEffectiveDate(new Date());
		capacityTemplateEntity.setExpiryDate(new Date());
		capacityTemplateEntity.setRestaurantTemplateType(type);
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
		capacityTemplateEntity.setRestaurantTemplateAndCapacityChannels(getCapacityTemplateAndChannels());
		capacityTemplateEntity.setRestaurantSlots(getCapacitySlots());
	}
	
	@Test
	void testGetAllCapacityTemplateId() throws Exception {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), "restaurantTemplateId");
		Mockito.when(restaurantTemplateRepository.findByRestaurantTemplateIdAndConceptId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(capacityTemplateEntity));
		restaurantCapacityTemplateServiceImpl.getRestaurantCapacityTempalteById(new BigInteger("1"));

		assertNotNull(capacityTemplateEntity);
	}
	
	private List<RestaurantTemplateSlotEntity> getCapacitySlots() {
		List<RestaurantTemplateSlotEntity> capacitySlotEntities = new ArrayList<>();
		RestaurantTemplateSlotEntity capacitySlotEntity = new RestaurantTemplateSlotEntity();
		capacitySlotEntity.setCapacityCnt(1);
		RestaurantTemplateEntity capacityTemplateEntity=new RestaurantTemplateEntity();
		capacityTemplateEntity.setRestaurantTemplateId(BigInteger.ONE);
		capacitySlotEntity.setRestaurantTemplate(capacityTemplateEntity);
		capacitySlotEntity.setRestaurantSlotId(BigInteger.valueOf(2));
		capacitySlotEntity.setStartTime(java.time.LocalTime.parse("11:00:00"));
		capacitySlotEntity.setEndTime(java.time.LocalTime.parse("11:46:55"));
		capacitySlotEntity.setRestaurantTemplateSlotType(getCapacitySlotType());
		capacitySlotEntity.setCapacityChannel(getCapacityChannel());
		capacitySlotEntities.add(capacitySlotEntity);
		return capacitySlotEntities;
	}
	
	private List<RestaurantTemplateAndCapacityChannelEntity> getCapacityTemplateAndChannels() {
		List<RestaurantTemplateAndCapacityChannelEntity> list = new ArrayList<>();
		RestaurantTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new RestaurantTemplateAndCapacityChannelEntity();
		RestaurantTemplateAndCapacityChannelPK id = new RestaurantTemplateAndCapacityChannelPK();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setRestaurantTemplateId(new BigInteger("1"));
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

	private RestaurantTemplateSlotTypeEntity getCapacitySlotType() {
		RestaurantTemplateSlotTypeEntity capacitySlotTypeEntity = new RestaurantTemplateSlotTypeEntity();
		capacitySlotTypeEntity.setRestaurantTemplateSlotTypeId(BigInteger.ONE);
		capacitySlotTypeEntity.setResturantTemplateSlotTypeNm("Test");
		return capacitySlotTypeEntity;
	}

	@Test
	void testGetAllRestaurantTemplates() throws ParseException {
		RequestContext.setConcept("1");
		List<RestaurantTemplateEntity> list = new ArrayList<>();
		RestaurantTemplateEntity entity = new RestaurantTemplateEntity();
		Date date1= new SimpleDateFormat("dd/MM/yyyy").parse("26/12/2022");
		entity.setEffectiveDate(date1);
		entity.setExpiryDate(date1);
		entity.setResturantTemplateNm("tuhin");
		entity.setRestaurantTemplateType(restaurantTemplateTypeEntity);
		entity.setCreatedBy("uou");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("uouo");
		entity.setLastModifiedDatetime(Instant.now());
		list.add(entity);
		Mockito.when(restaurantTemplateRepository.findByConceptId(Mockito.any())).thenReturn(list);
		List<ViewRestaurantCapacityTemplate> res = restaurantCapacityTemplateServiceImpl.getAllCapacityTemplates(new BigInteger(RequestContext.getConcept()));
		assertNotNull(res);
	}
}
