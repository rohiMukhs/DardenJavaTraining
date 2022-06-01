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


/**
 * @author skashala
 * The persistent class for the reference_type database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.REFERENCE_TYPE)
public class ReferenceTypeEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.REFERENCE_TYPE_ID)
	private BigInteger referenceTypeId;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	@Column(name=CapacityConstants.REFERENCE_TYPE_NM)
	private String referenceTypeNm;

	//bi-directional many-to-one association to Reference
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.REFERENCE_TYPE2)
	private List<ReferenceEntity> references;

}