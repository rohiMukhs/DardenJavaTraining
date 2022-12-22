package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Date;
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
 * The persistent class for the capacity_template database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_TEMPLATE)
public class CapacityTemplateEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_ID)
	private BigInteger capacityTemplateId;

	@Column(name=CapacityConstants.CAPACITY_TEMPLATE_NM)
	private String capacityTemplateNm;

	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name=CapacityConstants.EFFECTIVE_DATE)
	private Date effectiveDate;

	@Column(name=CapacityConstants.EXPIRY_DATE)
	private Date expiryDate;

	@Column(name=CapacityConstants.END_TIME)
	private LocalTime endTime;

	@Column(name=CapacityConstants.FRI_FLG)
	private String friFlg;

	@Column(name=CapacityConstants.MON_FLG)
	private String monFlg;

	@Column(name=CapacityConstants.SAT_FLG)
	private String satFlg;

	@Column(name=CapacityConstants.START_TIME)
	private LocalTime startTime;

	@Column(name=CapacityConstants.SUN_FLG)
	private String sunFlg;

	@Column(name=CapacityConstants.THU_FLG)
	private String thuFlg;

	@Column(name=CapacityConstants.TUE_FLG)
	private String tueFlg;

	@Column(name=CapacityConstants.WED_FLG)
	private String wedFlg;

	//bi-directional many-to-one association to CapacityModelAndCapacityTemplate
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_TEMPLATE2)
	private List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplates;

	//bi-directional many-to-one association to CapacitySlot
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_TEMPLATE2)
	private List<CapacitySlotEntity> capacitySlots;

	//bi-directional many-to-one association to CapacityTemplateType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_TEMPLATE_TYPE_ID)
	private CapacityTemplateTypeEntity capacityTemplateType;

	//bi-directional many-to-one association to CapacityTemplateAndBusinessDate
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_TEMPLATE2)
	private List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDates;

	//bi-directional many-to-one association to CapacityTemplateAndCapacityChannel
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_TEMPLATE2)
	private List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannels;

}