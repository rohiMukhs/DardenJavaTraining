package com.darden.dash.capacity.model;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessDate {

	@JsonFormat(pattern = CapacityConstants.MM_DD_YYYY)
	private String date;
	
}
