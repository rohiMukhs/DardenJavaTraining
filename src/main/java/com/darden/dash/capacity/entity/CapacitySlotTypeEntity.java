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
 * The persistent class for the capacity_slot_type database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_SLOT_TYPE)
public class CapacitySlotTypeEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_SLOT_TYPE_ID)
	private BigInteger capacitySlotTypeId;

	@Column(name=CapacityConstants.CAPACITY_SLOT_TYPE_NM)
	private String capacitySlotTypeNm;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	//bi-directional many-to-one association to CapacitySlot
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_SLOT_TYPE2)
	private List<CapacitySlotEntity> capacitySlots;

	//bi-directional many-to-one association to CapacitySlotCalcParam
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_SLOT_TYPE2)
	private List<CapacitySlotCalcParamEntity> capacitySlotCalcParams;

	}