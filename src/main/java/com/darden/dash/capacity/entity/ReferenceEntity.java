package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;


/**@author skashala
 * The persistent class for the reference database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.REFERENCE)
public class ReferenceEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.REFERENCE_ID)
	private BigInteger referenceId;

	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name=CapacityConstants.EFFECTIVE_LAST_DATETIME)
	private Instant effectiveLastDatetime;

	@Column(name=CapacityConstants.EFFECTIVE_START_DATETIME)
	private Instant effectiveStartDatetime;

	@Column(name=CapacityConstants.REFERENCE_CD)
	private String referenceCd;

	@Column(name=CapacityConstants.REFERENCE_DESC)
	private String referenceDesc;

	@Column(name=CapacityConstants.REFERENCE_NM)
	private String referenceNm;

	//bi-directional many-to-one association to CapacitySlot
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.REFERENCE)
	private List<CapacitySlotEntity> capacitySlots;

	//bi-directional many-to-one association to ReferenceType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.REFERENCE_TYPE_ID)
	private ReferenceTypeEntity referenceType;

	}