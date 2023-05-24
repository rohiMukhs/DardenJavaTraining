package com.darden.dash.capacity.boh.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vlsowjan
 * 
 * The primary key class for the Restaurant Template And BusinessDate database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class RestaurantTemplateAndBusinessDatePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name=CapacityConstants.RESTAURANT_TEMPLATE_ID, insertable=false, updatable=false)
	private BigInteger restaurantTemplateId;

	@Column(name=CapacityConstants.BUSINESS_DATE ,insertable=false, updatable=false)
	private Date businessDate;

}