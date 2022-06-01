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
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;


/**@author skashala
 * The persistent class for the capacity_template_type database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_TEMPLATE_TYPE)
public class CapacityTemplateTypeEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_TYPE_ID)
	private BigInteger capacityTemplateTypeId;

	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_TYPE_NM)
	private String capacityTemplateTypeNm;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	//bi-directional many-to-one association to CapacityTemplate
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_TEMPLATE_TYPE3)
	private List<CapacityTemplateEntity> capacityTemplates;

	}