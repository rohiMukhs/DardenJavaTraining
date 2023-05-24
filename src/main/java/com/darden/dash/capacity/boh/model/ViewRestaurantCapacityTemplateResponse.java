package com.darden.dash.capacity.boh.model;

import java.io.Serializable;
import java.util.List;

import com.darden.dash.common.model.ServiceResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author TuhinM
 *
 *         This Response class is written for the purpose of showing the values
 *         of Get Request for Restaurant Capacity Template
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ViewRestaurantCapacityTemplateResponse extends ServiceResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ViewRestaurantCapacityTemplate> restaurantCapacityTemplate;

	public ViewRestaurantCapacityTemplateResponse(List<ViewRestaurantCapacityTemplate> restaurantCapacityTemplate) {
		super();
		this.restaurantCapacityTemplate = restaurantCapacityTemplate;

	}
}
