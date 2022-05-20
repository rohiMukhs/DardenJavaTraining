package com.darden.dash.capacity.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;


/**
 * @author skashala
 * The persistent class for the capacity_model_and_capacity_template database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_MODEL_AND_CAPACITY_TEMPLATE)
public class CapacityModelAndCapacityTemplateEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CapacityModelAndCapacityTemplatePK id;

	//bi-directional many-to-one association to CapacityModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_MODEL_ID,insertable=false, updatable=false)
	private CapacityModelEntity capacityModel;

	//bi-directional many-to-one association to CapacityTemplate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_TEMPLATE_ID,insertable=false, updatable=false)
	private CapacityTemplateEntity capacityTemplate;

}