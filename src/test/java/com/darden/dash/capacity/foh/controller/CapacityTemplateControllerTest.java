package com.darden.dash.capacity.foh.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacitySlotTypeRefModel;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.util.JwtUtils;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {CapacityTemplateController.class})
class CapacityTemplateControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@MockBean
	private CapacityManagementService capacityManagementService;
	
	@MockBean
	private CapacityChannelService capacityChannelService;
	
	@BeforeAll
	static void beforeAll() {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
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
		Mockito.when(jwtUtils.isActionCodeExists(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(capacityManagementService.getAllCapacityTemplatesBasedOnDate(Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
				.thenReturn(capacityResponse);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/foh-capacity-templates")
				.headers(getHeaders())
				.param("isRefDataReq", "false")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	void getReferenceDataTest() throws Exception {
		List<CapacityChannel> capacityChannelList = new ArrayList<>();
		List<CapacitySlotTypeRefModel>  capacitySlotTypeList = new ArrayList<>();
		Mockito.when(jwtUtils.isActionCodeExists(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(capacityChannelService.getAllCapacityChannels()).thenReturn(capacityChannelList);
		Mockito.when(capacityManagementService.getAllCapacitySlotTypes()).thenReturn(capacitySlotTypeList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/foh-referencedata")
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
}
