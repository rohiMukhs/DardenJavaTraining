package com.darden.dash.capacity.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessDate {

	@JsonFormat(pattern = "MM/dd/yyyy")
	private String date;
	
}
