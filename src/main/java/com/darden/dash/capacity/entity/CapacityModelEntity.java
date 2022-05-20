package com.darden.dash.capacity.entity;

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

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;


/**
 * @author skashala
 * The persistent class for the capacity_model database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_MODEL)
public class CapacityModelEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_MODEL_ID)
	private BigInteger capacityModelId;

	@Column(name=CapacityConstants.CAPACITY_MODEL_NM)
	private String capacityModelNm;

	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	//bi-directional many-to-one association to CapacityModelAndCapacityTemplate
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_MODEL2)
	private List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplates;

	//bi-directional many-to-one association to CapacityModelAndLocation
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_MODEL2)
	private List<CapacityModelAndLocationEntity> capacityModelAndLocations;

	
}