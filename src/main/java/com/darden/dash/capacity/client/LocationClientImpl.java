package com.darden.dash.capacity.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.darden.dash.capacity.model.GetLocationsResponse;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.client.RestCallGet;
import com.darden.dash.common.exception.ApplicationException;

/**
 * 
 * @author vraviran
 * 
 * LocationClientImpl class which holds method implementation for methods
 * declared in {@link LocationClient} which deals with restTemplate call for
 * retrieving location detail from location micro service. Location micro service
 * holds all restaurant related details.
 *
 */
@Service
public class LocationClientImpl implements LocationClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${locations.url}")
	private String url;
	
	/**
	 * This method is retrieving list of locations detail from location micro service
	 * based on data present in DB. A rest call is made from capacity management
	 * application to location micro service to get locations related details.
	 *
	 * @return location list - List of locations having restaurant details of each. 
	 *                          This data comes from Location Micro service.
	 */
	@Override
	@Cacheable(value = CapacityConstants.RESTAURANTS_CACHE)
	public List<Locations> getAllRestaurants() {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url);
		try {
			RequestContext.getAuthorizationToken();
			RestCallGet<GetLocationsResponse> restCallGet = new RestCallGet<>(restTemplate, urlBuilder.toString(),
					GetLocationsResponse.class);
			GetLocationsResponse response = restCallGet.execute();
			return response.getLocations();
		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ApplicationException();
			}
			throw new ApplicationException();
		}
	}
}
