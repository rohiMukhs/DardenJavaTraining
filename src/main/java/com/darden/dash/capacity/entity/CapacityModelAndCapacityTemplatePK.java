package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * The primary key class for the capacity_model_and_capacity_template database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class CapacityModelAndCapacityTemplatePK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name=CapacityConstants.CAPACITY_MODEL_ID)
	private BigInteger capacityModelId;

	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_ID)
	private BigInteger capacityTemplateId;

}