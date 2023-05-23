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


/**@author skashala
 * The persistent class for the capacity_slot database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="capacity_slot_transaction")
public class CapacitySlotTransactionEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid2", parameters = {})
	@GeneratedValue(generator = "generator")
	@Column(name="capacity_slot_transaction_id")
	private String capacitySlotTransactionId;

	@Column(name=CapacityConstants.CAPACITY_CNT)
	private Integer capacityCnt;

	@Column(name=CapacityConstants.END_TIME)
	private LocalTime endTime;

	@Column(name=CapacityConstants.START_TIME)
	private LocalTime startTime;
	
	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;
	
	@Column(name="location_id")
	private BigInteger locationId;
	
	@Column(name="business_date")
	private LocalDate businessDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_STATUS_REF_ID)
	private ReferenceEntity capacitySlotStatusReferenceEntity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="calculation_type_ref_id")
	private ReferenceEntity calculationTypeReference;
	
	@Column(name="revision_nbr")
	private BigInteger revisionNbr;
	
	@Column(name="is_modified_flg")
	private String isModifiedFlg;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="capacity_slot_id")
	private CapacitySlotEntity capacitySlotEntity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_CHANNEL_ID)
	private CapacityChannelEntity capacityChannel;

}