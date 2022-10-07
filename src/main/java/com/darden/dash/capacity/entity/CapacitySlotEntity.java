package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name=CapacityConstants.CAPACITY_SLOT)
public class CapacitySlotEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_SLOT_ID)
	private BigInteger capacitySlotId;

	@Column(name=CapacityConstants.CAPACITY_CNT)
	private Integer capacityCnt;

	@Column(name=CapacityConstants.END_TIME)
	private LocalTime endTime;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	@Column(name=CapacityConstants.START_TIME)
	private LocalTime startTime;
	
	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	//bi-directional many-to-one association to CapacityChannel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_CHANNEL_ID)
	private CapacityChannelEntity capacityChannel;

	//bi-directional many-to-one association to CapacitySlotType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_TYPE_ID)
	private CapacitySlotTypeEntity capacitySlotType;

	//bi-directional many-to-one association to CapacityTemplate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_TEMPLATE_ID)
	private CapacityTemplateEntity capacityTemplate;

	//bi-directional many-to-one association to Reference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_STATUS_REF_ID)
	private ReferenceEntity reference;

}