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
import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;


/**
 * @author vlsowjan
 * The persistent class for the RESTAURANT_TEMPLATE_AND_BUSINESS_DATE database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.RESTAURANT_TEMPLATE_AND_BUSINESS_DATE)
public class RestaurantTemplateAndBusinessDateEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RestaurantTemplateAndBusinessDatePK id;
	
	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId; 

	//bi-directional many-to-one association to CapacityTemplate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.RESTAURANT_TEMPLATE_ID,insertable=false, updatable=false)
	private RestaurantTemplateEntity restaurantTemplate;

}