package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * The primary key class for the capacity_template_and_business_date database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class CapacityTemplateAndBusinessDatePK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_ID, insertable=false, updatable=false)
	private BigInteger capacityTemplateId;

	@Column(name=CapacityConstants.BUSINESS_DATE ,insertable=false, updatable=false)
	private Date businessDate;

}