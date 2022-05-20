package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;

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
 * The persistent class for the capacity_slot_calc_param database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_SLOT_CALC_PARAM)
public class CapacitySlotCalcParamEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_SLOT_CALC_PARAM_ID)
	private BigInteger capacitySlotCalcParamId;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	@Column(name=CapacityConstants.PARAM_KEY)
	private String paramKey;

	@Column(name=CapacityConstants.PARAM_VALUE)
	private String paramValue;

	//bi-directional many-to-one association to CapacitySlotType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_SLOT_TYPE_ID)
	private CapacitySlotTypeEntity capacitySlotType;

}