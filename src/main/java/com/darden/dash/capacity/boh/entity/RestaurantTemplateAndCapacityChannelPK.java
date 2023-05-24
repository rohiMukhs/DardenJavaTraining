package com.darden.dash.capacity.boh.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vlsowjan
 * 
 * The primary key class for the restaurant_template_and_capacity_channel database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class RestaurantTemplateAndCapacityChannelPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name=CapacityConstants.CAPACITY_CHANNEL_ID)
	private BigInteger capacityChannelId;

	@Column(name=CapacityConstants.RESTAURANT_TEMPLATE_ID)
	private BigInteger restaurantTemplateId;

	}