package com.darden.dash.capacity.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.Region;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.validation.CapacityTemplateModelValidator;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.capacity.validation.ChannelValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.SpringContext;
import com.darden.dash.common.client.service.LocationServiceClient;
import com.darden.dash.common.util.ConceptUtils;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {CapacityManagementController.class, CapacityValidator.class, CapacityTemplateModelValidator.class,
		ChannelValidator.class, SpringContext.class})
public class CapacityManagementControllerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private ConceptUtils conceptUtils;
	
	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private LocationServiceClient locationServiceClient;
	
	@MockBean
	private CapacityManagementService capacityManagementService;
	
	@MockBean
	private CapacityChannelService capacityChannelService;
	
	@MockBean
	private CapacityTemplateModelService capacityTemplateModelService;
	
	@MockBean
	private LocationClient locationClient;
	
	@MockBean
	private CapacityTemplateRepo capacityTemplateRepo;
	
	private static CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
	private static CreateTemplateResponse response = new CreateTemplateResponse();
	
	private static List<BusinessDate> dates = new ArrayList<>();
	private static List<SlotDetail> detailList = new ArrayList<>();
	private static List<SlotChannel> slotList = new ArrayList<>();
	
	@BeforeAll
	static void beforeAll() {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		
		
		BusinessDate date = new BusinessDate();
		date.setDate("01/01/2011");
		dates.add(date);
		
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("dates");
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
	}
	
	public static final HttpHeaders getHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		headers.add("Correlation-Id", "ce65-4a57-ac3e-f7fa09e1a8");
		headers.add("Authorization", "Bearer " + "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+o");
		headers.add("Concept-Id","1");		
		return headers;
	}
	
	@Test
	void getAllCapacityTemplates() throws Exception {
		CapacityResponse capacityResponse = new CapacityResponse();
		Mockito.when(capacityManagementService.getAllCapacityTemplates(Mockito.anyBoolean(),Mockito.any()))
				.thenReturn(capacityResponse);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capacity-templates/")
				.param("isRefDataReq", "false")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	void testGetCapacityTempalteId() throws Exception
	{
		CapacityTemplate CapacityTemplate = new CapacityTemplate();
		Mockito.when(capacityManagementService.getCapacityTemplateById(Mockito.any())).thenReturn(CapacityTemplate);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capacity-templates/{templateId}", 1)
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	void shouldCreateTemplate() throws Exception
	{
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
		channel.setInterval(6);
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
				.param("deleteConfirmed", "Y")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isAccepted());
		
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
		response.setInterval(6);
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
		request.setInterval(6);
		Mockito.when(capacityChannelService.addCombinedChannel(Mockito.any(), Mockito.anyString())).thenReturn(response);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/combine-channels")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isCreated());
	}
	
	@Test
	void testShouldUpdateCapacityTemplate() throws Exception {
		
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
		List<CapacityTemplate> templateName = new ArrayList<>();
		CapacityTemplate tempNm = new CapacityTemplate();
		tempNm.setTemplateName("name");
		templateName.add(tempNm);
		mr.setCapacityTemplateList(templateName);
		Set<BigInteger> locN = new HashSet<>();
		locN.add(new BigInteger("1"));
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
		
		Mockito.when(capacityTemplateModelService.getAllCapacityModels(Mockito.anyString())).thenReturn(modelList);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locationList);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capacity-models/")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
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
		request.setTemplatesAssigned(TemplatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned=new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		request.setRestaurantsAssigned(restaurantsAssignedList);
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
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.ONE);
		
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
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
		request.setTemplatesAssigned(TemplatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList=new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned=new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		request.setRestaurantsAssigned(restaurantsAssignedList);
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
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.ONE);
		
		Mockito.when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
		Mockito.when(capacityTemplateModelService.updateCapacityModel(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(capacityTemplateModelResponse);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/capacity-models/{modelId}",1)
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().isAccepted());
	}
	
	@Test
    void shouldDeleteCapacityModelWhenIsDeletedFlagTrue() throws Exception {

       Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
        doNothing().when(capacityTemplateModelService).deleteTemplateModel("1", "User", "Y");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/capacity-model-list/{templateId}", 1)
                .param("deletedConfirm", "Y")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
	
	@Test
    void shouldDeleteCapacityModelWhenIsDeletedFlagFalse() throws Exception {
       Mockito.when(jwtUtils.findUserDetail(Mockito.any())).thenReturn("User");
        doNothing().when(capacityTemplateModelService).deleteTemplateModel("1", "User", "Y");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/capacity-model-list/{templateId}", 1)
                .param("deletedConfirm", "N")
                .headers(getHeaders())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
	
	@Test
	void getAllCapacityChannels() throws Exception {
		ReferenceDatum viewCapacityChannels = new ReferenceDatum();
		Mockito.when(capacityChannelService.getReferenceData())
				.thenReturn(viewCapacityChannels);
//		mockMvc.perform(get("/api/v1/capacity-channels/")
//				.headers(getHeaders())).andExpect(status().isOk())
//				.andExpect(result -> result.getResponse());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/capacity-channels/")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
}
