package com.darden.dash.capacity.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.CapacityTemplateNames;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.Region;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.validation.CapacityTemplateModelValidator;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.capacity.validation.ChannelValidator;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = { CapacityManagementController.class, ChannelValidator.class,CapacityValidator.class })
public class CapacityControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private CapacityManagementService capacityManagementService;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@MockBean
	private CapacityChannelService capacityChannelService;
	
	@MockBean
	private CapacityTemplateModelService capacityTemplateModelService;
	
	@MockBean
	private ChannelValidator channelValidator;
	
	@MockBean
	private LocationClient locationClient;
	
	@MockBean
	private CapacityValidator capacityValidator;
	
	@MockBean
	private CapacityTemplateModelValidator capacityTemplateModelValidator;

	public static final String ACCESS_TOKEN = "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+oaT0v3gTSxnMmfVudUPFbnkLG+YgOIQ8i49iT1ooTzS55gZUdqW2XajNbhkDtq40rJh9jVltkBfhY/JTpwAIRJW4Ebn+M6X9xjXwNub0U4wz4nUHK7VIHNoF61xrLiAMdUcxb1GrHaDvXEzPtcWNG/ngoz5L9KOJFwwdBvS/c76k7rVO1Rn3Y0MJHY9I6wQAGa3MHmcuIxCmmkQEI59sYVsoazwRfFd5s2KYxccqWG+EJK3zJ4yTueQstPGcsJ/wPXG0jPtVwgy7Ms61Ww3ydm1R4SjUIYemITvXr/v3uVBs5qizR7PWEBSZNKPBsNctMN1PoKrAs7PEkqh791fnfK4Txjg6/jSazZYCELAD/EjR/1pkn6cEKLH2L7cLA/n8WzkPg6bD3UwRp6MgTL9PhuE+juJu3mc0pR7LI7l6A9TYwnStsGiJm+R0JOZzIn/xjCCPDpTBXvC9rPMvg2rF1MWV78jVYSMWugQnhU3tP5HjMF3fK5NXFwZyRPt9Hm4MPNHLiY0/fKcoP/e2cPAcTxuJeOBM6BmIVPYu10kMLBzIMkCbcYTptv2WNgTVPJOi4W/Rl76+HJS62szMY4DPcf3fTqVnuXTj4R7vfuzS2RZOKCYER3JF1H80KAUa4VFTv2xIAVMALMuQesjobfz6r9o0qaWFbZDXLsMQ9denalcKMwDXeBPe1QilEwDbO6gtiRb6lD1w8mJ4mWrH57hAFwN4pE/uFmI/kvqRCjF/ca7hu5i30NAlAEcp9Y45H+Bo6lwx9VUeYrfTWlDTEUuRZ0+PEKGMOjnNj+kzHCOj3Smz4vt4NW+DG0bW/GS9++4lAOtwm3bRpEYYVycjhO7pwYB/qFpfvPkiHXwDRVTty5xiNNYeHxYdBvzeUFzphAAgAFEyRGrLJVO5dWshysu";

	@BeforeEach
	public void contextLoad() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	void getAllCapacityTemplates() throws Exception {
		Mockito.when(capacityManagementService.getAllCapacityTemplates())
				.thenReturn(getAllTemplates());
		mockMvc.perform(get("/api/v1/capacity-templates/").headers(getHeaders())).andExpect(status().isOk())
		.andExpect(result -> result.getResponse());

	}

	public static final HttpHeaders getHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		headers.add("Correlation-Id", "ce65-4a57-ac3e-f7fa09e1a8");
		headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
		headers.add("Concept-Id","1");		
		return headers;
	}

	private List<CapacityTemplate> getAllTemplates() {
		List<CapacityTemplate> capacityTemplateList = new ArrayList<>();
		CapacityTemplate capacityTemplate= new CapacityTemplate();
		capacityTemplate.setCapacityTemplateId(String.valueOf(2));
		capacityTemplate.setEffectiveDate(String.valueOf(new Date()));
		capacityTemplate.setExpiryDate(String.valueOf(new Date()));
		capacityTemplate.setMonDay("Y");
		capacityTemplate.setTueDay("Y");
		capacityTemplate.setWedDay("Y");
		capacityTemplate.setThuDay("Y");
		capacityTemplate.setFriDay("Y");
		capacityTemplate.setSatDay("Y");
		capacityTemplate.setSunDay("Y");
		capacityTemplateList.add(capacityTemplate);
		return capacityTemplateList;
	}
	
	@Test
	void shouldCreateTemplate() throws Exception{
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		CreateTemplateResponse response = new CreateTemplateResponse();
		List<BusinessDate> dates = new ArrayList<>();
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dates.add(date);
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount("1");
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
		request.setBusinessDates(dates);
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
		
		response.setCapacityTemplateId(new BigInteger("1"));
		response.setCapacityTemplateName("name");
		response.setConceptId(new BigInteger("1"));
		response.setEffectiveDate("01/01/2011");
		response.setExpiryDate("01/01/2011");
		response.setIsDeletedFlag("N");
		response.setSlotStartTime("01:02");
		response.setSlotEndTime("02:09");
		response.setTemplateTypeId(new BigInteger("1"));
		response.setCapacityTemplateName("Days");
		response.setMonDay("Y");
		response.setTueDay("Y");
		response.setWedDay("Y");
		response.setThuDay("N");
		response.setFriDay("N");
		response.setSatDay("Y");
		response.setSunDay("Y");
		response.setBusinessDates(dates);
		response.setSlotChannels(slotList);
		response.setCreatedBy("user");
		response.setCreatedDateTime(Instant.now());
		response.setLastModifiedBy("user");
		response.setLastModifiedDateTime(Instant.now());
		
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		Mockito.when(capacityManagementService.createTemplate(Mockito.any(), Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/capacity-templates")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isCreated());
		
	}
	
	@Test
	void shouldUpdateChannels() throws Exception{
		
		ChannelListRequest request = new ChannelListRequest();
		
		List<ChannelInformationRequest> channelList = new ArrayList<>();
		ChannelInformationRequest channel = new ChannelInformationRequest();
		channel.setCapacityChannelId(new BigInteger("1"));
		channel.setPosName("frnm");
		channel.setInterval(2);
		channel.setOperationHourStartTime("01:01:01");
		channel.setOperationHourEndTime("02:02:02");
		
		List<CapacityChannel> responseList = new ArrayList<>();
		
		CapacityChannel response = new CapacityChannel();
		response.setCapacityChannelId(new BigInteger("1"));
		response.setCapacityChannelName("abc");
		response.setPosName("frnm");
		response.setInterval("2");
		response.setIsCombinedFlg("N");
		response.setOperationalHoursEndTime("02:02:02");
		response.setOperationalHoursStartTime("01:01:01");
		
		responseList.add(response);
		
		channelList.add(channel);
		
		request.setChannels(channelList);
		
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		Mockito.when(capacityChannelService.editChannelInformation(Mockito.anyList(),Mockito.anyString())).thenReturn(responseList);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/combine-channels")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))).andExpect(status().isAccepted());
		
	}
	
	@Test
	void testShouldDeleteCapacityTemplate() throws Exception{
		
		Mockito.when(capacityManagementService.deleteByTemplateId(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn("templateNm");
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/capacity-templates/{templateId}", 1)
				.param("deletedFlag", "Y")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
				.andExpect(jsonPath("status", is(202)));
		
	}
	
	@Test
	void testShouldCreateCombineChannel() throws Exception{
		Set<String> s = new HashSet<>();
		s.add("a");
		s.add("b");
		CombineChannel response = new CombineChannel();
		response.setChannels(s);
		response.setCombinedChannelId(new BigInteger("1"));
		response.setCombinedChannelName("aaa");
		response.setCombinedFlg("Y");
		response.setCreatedBy("aaa");
		response.setCreatedDateTime(Instant.now());
		response.setPosName("a");
		response.setInterval(1);
		response.setLastModifiedBy("aa");
		response.setLastModifiedDateTime(Instant.now());
		response.setOperationHourEndTime("00:11");
		response.setOperationHourStartTime("00:00");
		response.getChannels();
		response.getCombinedChannelId();
		response.getCombinedChannelName();
		response.getCombinedFlg();
		response.getCreatedBy();
		response.getCreatedDateTime();
		response.getPosName();
		response.getInterval();
		response.getLastModifiedBy();
		response.getLastModifiedDateTime();
		response.getOperationHourEndTime();
		response.getOperationHourStartTime();
		CreateCombineChannelRequest request = new CreateCombineChannelRequest();
		request.setChannels(s);
		request.setCombinedChannelName("aaa");
		request.setPosName("a");
		request.setEndTime("00:11");
		request.setStartTime("00:00");
		request.setInterval(1);
		Mockito.when(capacityChannelService.addCombinedChannel(Mockito.any(), Mockito.anyString())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/combine-channels")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isCreated());
	}
	
	@Test
	void testShouldUpdateCapacityTemplate() throws Exception {
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		CreateTemplateResponse response = new CreateTemplateResponse();
		List<BusinessDate> dates = new ArrayList<>();
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dates.add(date);
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount("1");
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
		request.setBusinessDates(dates);
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
		
		response.setCapacityTemplateId(new BigInteger("1"));
		response.setCapacityTemplateName("name");
		response.setConceptId(new BigInteger("1"));
		response.setEffectiveDate("01/01/2011");
		response.setExpiryDate("01/01/2011");
		response.setIsDeletedFlag("N");
		response.setSlotStartTime("01:02");
		response.setSlotEndTime("02:09");
		response.setTemplateTypeId(new BigInteger("1"));
		response.setCapacityTemplateName("Days");
		response.setMonDay("Y");
		response.setTueDay("Y");
		response.setWedDay("Y");
		response.setThuDay("N");
		response.setFriDay("N");
		response.setSatDay("Y");
		response.setSunDay("Y");
		response.setBusinessDates(dates);
		response.setSlotChannels(slotList);
		response.setCreatedBy("user");
		response.setCreatedDateTime(Instant.now());
		response.setLastModifiedBy("user");
		response.setLastModifiedDateTime(Instant.now());
		Mockito.when(capacityManagementService.updateCapacityTemplate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/capacity-templates/{templateId}",1)
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isAccepted());
	}
	
	@Test
	void testShouldReturnCapacityModels() throws Exception{
		List<CapacityModel> modelList = new ArrayList<>();
		CapacityModel mr = new CapacityModel();
		mr.setCapacityModelId(new BigInteger("1"));
		mr.setCapacityModelName("name");
		List<CapacityTemplateNames> templateName = new ArrayList<>();
		CapacityTemplateNames tempNm = new CapacityTemplateNames();
		tempNm.setTemplateName("name");
		templateName.add(tempNm);
		mr.setCapacityTemplateList(templateName);
		Set<BigInteger> locN = new HashSet<>();
		locN.add(new BigInteger("1"));
		mr.setRestaurants(locN);
		mr.setCreatedBy("name");
		mr.setCreatedDateTime(Instant.now());
		mr.setIsDeletedFlg("N");
		mr.setLastModifiedBy("nn");
		mr.setLastModifiedDateTime(Instant.now());
		modelList.add(mr);
		mr.getCapacityModelId();
		mr.getCapacityModelName();
		mr.getCapacityTemplateList();
		mr.getCreatedBy();
		mr.getCreatedDateTime();
		mr.getIsDeletedFlg();
		mr.getLastModifiedBy();
		mr.getLastModifiedDateTime();
		mr.getRestaurants();
		List<Locations> locationList = new ArrayList<>();
		Locations l = new Locations();
		l.setAddressState("aaa");
		l.setLastModifiedDateTime(Instant.now());
		l.setLocationDescription("desc");
		l.setLocationId(new BigInteger("1"));
		Region r = new Region();
		r.setRegionId(1);
		r.setRegionName("name");
		l.setRegion(r);
		l.setRestaurantNumber(new BigInteger("112"));
		locationList.add(l);
		l.getAddressState();
		l.getLocationDescription();
		l.getLastModifiedDateTime();
		l.getRegion();
		Mockito.when(capacityTemplateModelService.getAllCapacityModels()).thenReturn(modelList);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locationList);
		mockMvc.perform(get("/api/v1/capacity-models/").headers(getHeaders())).andExpect(status().isOk())
		.andExpect(result -> result.getResponse());
	}
	

	@Test
	void createCapacityModelTest() throws Exception{
		CapacityModelRequest request=new CapacityModelRequest();
		request.setTemplateModelName("abc");
		List<TemplatesAssigned> TemplatesAssignedList=new ArrayList<>();
		TemplatesAssigned templatesAssigned=new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		TemplatesAssignedList.add(templatesAssigned);
		List<RestaurantsAssigned> restaurantsAssignedList=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned=new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		CapacityTemplateModel capacityTemplateModelResponse=new CapacityTemplateModel();
		capacityTemplateModelResponse.setCreatedBy("user");
		capacityTemplateModelResponse.setCreatedDateTime(Instant.now());
		capacityTemplateModelResponse.setLastModifiedBy("user");
		capacityTemplateModelResponse.setLastModifiedDateTime(Instant.now());
		List<TemplatesAssigned> TemplatesAssignedList1=new ArrayList<>();
		TemplatesAssigned templatesAssigned1=new TemplatesAssigned();
		templatesAssigned1.setTemplateId("101");
		templatesAssigned1.setTemplateName("Test101");
		TemplatesAssignedList1.add(templatesAssigned1);
		List<RestaurantsAssigned> restaurantsAssignedList1=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned1=new RestaurantsAssigned();
		restaurantsAssigned1.setLocationId("1111");
		restaurantsAssignedList1.add(restaurantsAssigned1);
		
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		Mockito.when(capacityTemplateModelService.createCapacityModel(Mockito.any(), Mockito.any())).thenReturn(capacityTemplateModelResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/capacity-models")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isCreated());
	}
	
	@Test
	void updateCapacityModelTest() throws Exception{
		CapacityModelRequest request=new CapacityModelRequest();
		request.setTemplateModelName("abc");
		List<TemplatesAssigned> TemplatesAssignedList=new ArrayList<>();
		TemplatesAssigned templatesAssigned=new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		TemplatesAssignedList.add(templatesAssigned);
		List<RestaurantsAssigned> restaurantsAssignedList=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned=new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		CapacityTemplateModel capacityTemplateModelResponse=new CapacityTemplateModel();
		capacityTemplateModelResponse.setCreatedBy("user");
		capacityTemplateModelResponse.setCreatedDateTime(Instant.now());
		capacityTemplateModelResponse.setLastModifiedBy("user");
		capacityTemplateModelResponse.setLastModifiedDateTime(Instant.now());
		List<TemplatesAssigned> TemplatesAssignedList1=new ArrayList<>();
		TemplatesAssigned templatesAssigned1=new TemplatesAssigned();
		templatesAssigned1.setTemplateId("101");
		templatesAssigned1.setTemplateName("Test101");
		TemplatesAssignedList1.add(templatesAssigned1);
		List<RestaurantsAssigned> restaurantsAssignedList1=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned1=new RestaurantsAssigned();
		restaurantsAssigned1.setLocationId("1111");
		restaurantsAssignedList1.add(restaurantsAssigned1);
		
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		Mockito.when(capacityTemplateModelService.updateCapacityModel(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(capacityTemplateModelResponse);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/capacity-models/{modelId}",1)
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isAccepted());
	}

}
