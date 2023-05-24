package com.darden.dash.capacity.boh.controller;

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

import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.ViewRestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.service.RestaurantCapacityTemplateService;
import com.darden.dash.capacity.boh.validator.RestaurantCapacityTemplateValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.SpringContext;
import com.darden.dash.common.util.ConceptUtils;
import com.darden.dash.common.util.JwtUtils;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {RestaurantCapacityTemplateController.class, RestaurantCapacityTemplateValidator.class, SpringContext.class})
class RestaurantCapacityTemplateControllerTest {

	@MockBean
	RestaurantCapacityTemplateService restaurantCapacityTemplateService;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ConceptUtils conceptUtils;
	
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
	void testGetRestaurantCapacityTemplate() throws Exception {
		RestaurantCapacityTemplate restaurantCapacityTemplate = new RestaurantCapacityTemplate();
		Mockito.when(restaurantCapacityTemplateService.getRestaurantCapacityTempalteById(Mockito.any())).thenReturn(restaurantCapacityTemplate);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1//restaurant-capacity-template/{templateId}", 1)
				.headers(getHeaders())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	void getAllCapacityTemplates() throws Exception {
		List<ViewRestaurantCapacityTemplate> restaurantResponse = new ArrayList<ViewRestaurantCapacityTemplate>();
		Mockito.when(jwtUtils.isActionCodeExists(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(restaurantCapacityTemplateService.getAllCapacityTemplates(Mockito.any()))
				.thenReturn(restaurantResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurant-capacity-templates").headers(getHeaders())
				.param("isRefDataReq", "false").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
