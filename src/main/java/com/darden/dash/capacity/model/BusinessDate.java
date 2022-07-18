package com.darden.dash.capacity.model;

import java.io.Serializable;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author vravian
 * 
 * * This Model class is written for the purpose of showing the values of business date
 *
 */
@Getter
@Setter
public class BusinessDate implements Serializable {

	@JsonFormat(pattern = CapacityConstants.MM_DD_YYYY)
	private String date;
	
}
