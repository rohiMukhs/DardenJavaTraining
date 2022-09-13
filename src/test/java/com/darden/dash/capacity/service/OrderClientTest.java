package com.darden.dash.capacity.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.darden.dash.capacity.client.OrderClientImpl;
import com.darden.dash.capacity.model.GetLocationsResponse;
import com.darden.dash.capacity.model.OrderList;
import com.darden.dash.capacity.model.OrderTemplate;
import com.darden.dash.capacity.model.OrderTemplateResponse;
import com.darden.dash.common.client.RestCallGet;
import com.darden.dash.common.exception.ApplicationException;

@ExtendWith({ MockitoExtension.class })
class OrderClientTest {
	
	@Mock
	RestTemplate restTemplate;

	@Mock
	RestCallGet<GetLocationsResponse> restCallGet;

	@Mock
	HttpClientErrorException exception;
	
	@InjectMocks
	OrderClientImpl OrderClientImpl ;
	
	@Test
	void getLocationExceptionTest() {
		
		boolean flag = false;
		
		try {
			OrderClientImpl.getAllOrderTemplates();
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void getLocationSuccessTest() {
		
		List<OrderList> orderList = new ArrayList<>();
		orderList.add(new OrderList());
		List<OrderTemplate> OrderTemplateList = new ArrayList<>();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList);
		OrderTemplateResponse orderTemplateResponse = new OrderTemplateResponse();
		orderTemplateResponse.setOrderTemplate(OrderTemplateList);
		orderTemplateResponse.setCorrelationId("1j1jn21");
		orderTemplateResponse.setStatus(new BigInteger("200"));
		orderTemplateResponse.setTitle("title");
		
		
		Mockito.when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				any(Class.class))).thenReturn(ResponseEntity.ok(orderTemplateResponse));
		
		ParameterizedTypeReference mock = Mockito.mock(ParameterizedTypeReference.class);
		
		List<OrderTemplate> orderTemplate = OrderClientImpl.getAllOrderTemplates();
		assertNotNull(orderTemplate);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void getLocationApplicationExceptionTest() {
		
		Mockito.when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				any(Class.class))).thenThrow(HttpClientErrorException.class);
		ParameterizedTypeReference mock = Mockito.mock(ParameterizedTypeReference.class);
		
boolean flag = false;
		
		try {
			OrderClientImpl.getAllOrderTemplates();
		}
		catch (ApplicationException e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
}
