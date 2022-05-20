package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.List;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
/**
 * This Model class is common across all the model classes
 * 
 * @author skashala
 *
 */
@Getter
@Setter
public class CapacityTemplateCommon {
	

	@JsonFormat(pattern = CapacityConstants.MM_DD_YYYY)
	private String effectiveDate;
	
	@JsonFormat(pattern = CapacityConstants.MM_DD_YYYY)
	private String expiryDate;
	private BigInteger conceptId;
	private String isDeletedFlag;
	private BigInteger templateTypeId;
	private String templateTypeName;
	private String sunDay;
	private String monDay;
	private String tueDay;
	private String wedDay;
	private String thuDay;
	private String friDay;
	private String satDay;
	private List<BusinessDate> businessDates;
	private String slotStartTime;
	private String slotEndTime;

	public CapacityTemplateCommon() {
		super();
	}

}