package com.darden.dash.capacity.boh.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.darden.dash.capacity.entity.Audit;
import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;


/**@author vlsowjan
 * The persistent class for the restauarnt_template_type database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.RESTAURANT_TEMPLATE_TYPE)
public class RestaurantTemplateTypeEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.RESTAURANT_TEMPLATE_TYPE_ID)
	private BigInteger restaurantTemplateTypeId;

	@Column(name=CapacityConstants.RESTAURANT_TEMPLATE_TYPE_NAME)
	private String restaurantTemplateTypeNm;

	//bi-directional many-to-one association to CapacityTemplate
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.RESTAURANT_TEMPLATE_TYPE3)
	private List<RestaurantTemplateEntity> restaurantTemplates;

	}