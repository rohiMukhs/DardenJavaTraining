package com.darden.dash.capacity.boh.model;

import java.io.Serializable;

import com.darden.dash.common.model.ServiceResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author gunsaik
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class RestaurantCapacityTemplateResponse extends ServiceResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private RestaurantCapacityTemplate restuarantTemplate;

	public RestaurantCapacityTemplateResponse(RestaurantCapacityTemplate restuarantTemplate) {
		super();
		this.restuarantTemplate = restuarantTemplate;

	}

}
