package com.darden.dash.capacity.boh.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.darden.dash.capacity.entity.Audit;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;


/**
 * @author gunsaik
 * 
 * The persistent class for the Restaurant_template_and_capacity_channel database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.RESTAURANT_TEMPLATE_AND_CAPACITY_CHANNEL)
public class RestaurantTemplateAndCapacityChannelEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RestaurantTemplateAndCapacityChannelPK id;

	@Column(name = CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name = CapacityConstants.LOCATION_ID)
	private BigInteger locationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = CapacityConstants.CAPACITY_CHANNEL_ID, insertable = false, updatable = false)
	private CapacityChannelEntity capacityChannel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = CapacityConstants.RESTAURANT_TEMPLATE_ID, insertable = false, updatable = false)
	private RestaurantTemplateEntity restaurantTemplate;

}