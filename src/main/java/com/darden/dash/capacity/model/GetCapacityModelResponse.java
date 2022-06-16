package com.darden.dash.capacity.model;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 * ResponseEntity values of capacity  model and restaurants data.
 *
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetCapacityModelResponse extends ServiceResponse{
	
	private List<CapacityModel> capacityModels;
	private List<Locations> locations;

	public GetCapacityModelResponse(List<CapacityModel> capacityModels, List<Locations> locations) {
		super();
		this.capacityModels = capacityModels;
		this .locations = locations;
	}
}
