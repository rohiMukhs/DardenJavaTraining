package com.darden.dash.capacity.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.darden.dash.capacity.model.OrderTemplate;
import com.darden.dash.capacity.model.OrderTemplateResponse;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.client.RestCallGet;
import com.darden.dash.common.exception.ApplicationException;

/**
 * 
 * @author asdattat
 * 
 * OrderClientImpl class which holds method implementation for methods
 * declared in {@link orderClient} which deals with restTemplate call for
 * retrieving orderTemplates detail from order micro service. order micro service
 * holds all orderTemplates related details.
 *
 */
@Service
public class OrderClientImpl implements OrderClient {

	@Autowired
	private RestTemplate restTemplate;

	
	@Value("${orders.url}")
	private String url;	
	
	/**
	 * This method is retrieving list of orderTemplates detail from order micro service
	 * based on data present in DB. A rest call is made from capacity management
	 * application to order micro service to get ordertemplates related details.
	 *
	 * @return OrderTemplate list - List of OrderTemplate having orderTemplate details of each. 
	 *                          This data comes from order Micro service.
	 */
	
	@Override
	public List<OrderTemplate> getAllOrderTemplates() {
	    StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url);
		try {
			RequestContext.getAuthorizationToken();
			RestCallGet<OrderTemplateResponse> restCallGet = new RestCallGet<>(restTemplate, urlBuilder.toString(),
					OrderTemplateResponse.class);
			OrderTemplateResponse response = restCallGet.execute();
			return response.getOrderTemplate();
		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ApplicationException();
			}
			throw new ApplicationException();
		}
	}

}
