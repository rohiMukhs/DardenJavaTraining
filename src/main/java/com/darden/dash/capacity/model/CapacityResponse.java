package com.darden.dash.capacity.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 *  values of capacity template response
 *
 */

@Getter
@Setter
@NoArgsConstructor
public class CapacityResponse extends ServiceResponse {

	private List<CapacityTemplate> capacityTemplates = new ArrayList<>();
	private List<ReferenceDatum> referenceData = new ArrayList<>();

	
}