package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * The persistent class for the capacity slot transaction database table
 *
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_SLOT_TRANSACTION)
public class CapacitySlotTransactionEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = CapacityConstants.GENERATOR, strategy = CapacityConstants.UUID2, parameters = {})
	@GeneratedValue(generator = CapacityConstants.GENERATOR)
	@Column(name=CapacityConstants.CAPACITY_SLOT_TRANSACTION_ID)
	private String capacitySlotTransactionId;

	@Column(name=CapacityConstants.CAPACITY_CNT)
	private Integer capacityCnt;

	@Column(name=CapacityConstants.END_TIME)
	private LocalTime endTime;

	@Column(name=CapacityConstants.START_TIME)
	private LocalTime startTime;
	
	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;
	
	@Column(name=CapacityConstants.LOCATION_ID)
	private BigInteger locationId;
	
	@Column(name=CapacityConstants.BUSINESS_DATE)
	private LocalDate businessDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_STATUS_REF_ID)
	private ReferenceEntity capacitySlotStatusReferenceEntity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CALCULATION_TYPE_REF_ID)
	private ReferenceEntity calculationTypeReference;
	
	@Column(name=CapacityConstants.REVISION_NBR)
	private BigInteger revisionNbr;
	
	@Column(name=CapacityConstants.IS_MODIFIED_FLG)
	private String isModifiedFlg;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_ID)
	private CapacitySlotEntity capacitySlotEntity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_CHANNEL_ID)
	private CapacityChannelEntity capacityChannel;

}