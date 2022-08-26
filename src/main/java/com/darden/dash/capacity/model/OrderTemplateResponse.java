package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author asdattat
 *
 */
@Getter
@Setter
public class OrderTemplateResponse {

	private BigInteger status;
	private String title;
	private String correlationId;
	private List<OrderTemplate> orderTemplate;
}
