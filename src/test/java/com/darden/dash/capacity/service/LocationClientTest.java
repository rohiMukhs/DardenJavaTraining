package com.darden.dash.capacity.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

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

import com.darden.dash.capacity.client.LocationClientImpl;
import com.darden.dash.capacity.model.GetLocationsResponse;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.common.client.RestCallGet;
import com.darden.dash.common.exception.ApplicationException;

@ExtendWith({ MockitoExtension.class })
class LocationClientTest {
	
	@Mock
	RestTemplate restTemplate;

	@Mock
	RestCallGet<GetLocationsResponse> restCallGet;

	@Mock
	HttpClientErrorException exception;
	
	@InjectMocks
	LocationClientImpl locationClientImpl;
	
	@Test
	void getLocationExceptionTest() {
		
		boolean flag = false;
		
		try {
			locationClientImpl.getAllRestaurants();
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void getLocationSuccessTest() {
		
		List<Locations> locationList = new ArrayList<>();
		locationList.add(new Locations());
		GetLocationsResponse locationsResponse = new GetLocationsResponse();
		locationsResponse.setLocations(locationList);
		locationsResponse.setStatus(200);
		locationsResponse.setCorrelationId("1j1jn21");
		locationsResponse.setTitle("title");
		locationsResponse.getCorrelationId();
		locationsResponse.getStatus();
		locationsResponse.getTitle();
		
		Mockito.when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				any(Class.class))).thenReturn(ResponseEntity.ok(locationsResponse));
		
		ParameterizedTypeReference mock = Mockito.mock(ParameterizedTypeReference.class);
		
		List<Locations> locations = locationClientImpl.getAllRestaurants();
		assertNotNull(locations);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void getLocationApplicationExceptionTest() {
		
		Mockito.when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class),
				any(Class.class))).thenThrow(HttpClientErrorException.class);
		ParameterizedTypeReference mock = Mockito.mock(ParameterizedTypeReference.class);
		
boolean flag = false;
		
		try {
			locationClientImpl.getAllRestaurants();
		}
		catch (ApplicationException e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
}
