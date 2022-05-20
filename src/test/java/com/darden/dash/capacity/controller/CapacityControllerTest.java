package com.darden.dash.capacity.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateResponseSlot;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = { CapacityManagementController.class })
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
	private CapacityValidator capacityValidator;

	public static final String ACCESS_TOKEN = "7TeL7QMI9tSbvx38:k20boY/U/dAEM13LBKgS+oaT0v3gTSxnMmfVudUPFbnkLG+YgOIQ8i49iT1ooTzS55gZUdqW2XajNbhkDtq40rJh9jVltkBfhY/JTpwAIRJW4Ebn+M6X9xjXwNub0U4wz4nUHK7VIHNoF61xrLiAMdUcxb1GrHaDvXEzPtcWNG/ngoz5L9KOJFwwdBvS/c76k7rVO1Rn3Y0MJHY9I6wQAGa3MHmcuIxCmmkQEI59sYVsoazwRfFd5s2KYxccqWG+EJK3zJ4yTueQstPGcsJ/wPXG0jPtVwgy7Ms61Ww3ydm1R4SjUIYemITvXr/v3uVBs5qizR7PWEBSZNKPBsNctMN1PoKrAs7PEkqh791fnfK4Txjg6/jSazZYCELAD/EjR/1pkn6cEKLH2L7cLA/n8WzkPg6bD3UwRp6MgTL9PhuE+juJu3mc0pR7LI7l6A9TYwnStsGiJm+R0JOZzIn/xjCCPDpTBXvC9rPMvg2rF1MWV78jVYSMWugQnhU3tP5HjMF3fK5NXFwZyRPt9Hm4MPNHLiY0/fKcoP/e2cPAcTxuJeOBM6BmIVPYu10kMLBzIMkCbcYTptv2WNgTVPJOi4W/Rl76+HJS62szMY4DPcf3fTqVnuXTj4R7vfuzS2RZOKCYER3JF1H80KAUa4VFTv2xIAVMALMuQesjobfz6r9o0qaWFbZDXLsMQ9denalcKMwDXeBPe1QilEwDbO6gtiRb6lD1w8mJ4mWrH57hAFwN4pE/uFmI/kvqRCjF/ca7hu5i30NAlAEcp9Y45H+Bo6lwx9VUeYrfTWlDTEUuRZ0+PEKGMOjnNj+kzHCOj3Smz4vt4NW+DG0bW/GS9++4lAOtwm3bRpEYYVycjhO7pwYB/qFpfvPkiHXwDRVTty5xiNNYeHxYdBvzeUFzphAAgAFEyRGrLJVO5dWshysu";

	@BeforeEach
	public void contextLoad() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	void getAllCapacityTemplates() throws Exception {
		Mockito.when(capacityManagementService.getAllCapacityTemplates())
				.thenReturn(new ResponseEntity<>(getAllTemplates(), HttpStatus.OK));
		mockMvc.perform(get("/api/v1/capacity-templates/").headers(getHeaders())).andExpect(status().isOk())
		.andExpect(result -> result.getResponse());

	}

	public static final HttpHeaders getHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		headers.add("Correlation-Id", "ce65-4a57-ac3e-f7fa09e1a8");
		headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
		return headers;
	}

	private CapacityResponse getAllTemplates() {
		CapacityResponse response=new CapacityResponse();
		response.setCorrelationId(UUID.randomUUID().toString());
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
		response.setCapacityTemplates(capacityTemplateList);
		response.build(CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, 200);
		return response;
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
		List<CreateResponseSlot> slotList = new ArrayList<>();
		CreateResponseSlot slot = new CreateResponseSlot();
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
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/capacity-templates/").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(status().isCreated());
		
	}

}