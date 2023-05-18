package com.darden.dash.capacity.boh.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.darden.dash.capacity.entity.Audit;
import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vlsowjan 
 * The persistent class for the restaurant_template database
 *         table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = CapacityConstants.RESTAURANT_TEMPLATE)
public class RestaurantTemplateEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = CapacityConstants.RESTAURANT_TEMPLATE_ID)
	private BigInteger resturantTemplateId;

	@Column(name = CapacityConstants.RESTAURANT_TEMPLATE_NAME)
	private String resturantTemplateNm;

	@Column(name = CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name = CapacityConstants.LOCATION_ID)
	private BigInteger locationId;

	@Column(name = CapacityConstants.EFFECTIVE_DATE)
	private Date effectiveDate;

	@Column(name = CapacityConstants.EXPIRY_DATE)
	private Date expiryDate;

	@Column(name = CapacityConstants.END_TIME)
	private LocalTime endTime;

	@Column(name = CapacityConstants.FRI_FLG)
	private String friFlg;

	@Column(name = CapacityConstants.MON_FLG)
	private String monFlg;

	@Column(name = CapacityConstants.SAT_FLG)
	private String satFlg;

	@Column(name = CapacityConstants.START_TIME)
	private LocalTime startTime;

	@Column(name = CapacityConstants.SUN_FLG)
	private String sunFlg;

	@Column(name = CapacityConstants.THU_FLG)
	private String thuFlg;

	@Column(name = CapacityConstants.TUE_FLG)
	private String tueFlg;

	@Column(name = CapacityConstants.WED_FLG)
	private String wedFlg;

	// bi-directional many-to-one association to RestaurantTemplateSlot
	@JsonBackReference
	@OneToMany(mappedBy = CapacityConstants.RESTAURANT_TEMPLATE2)
	private List<RestaurantTemplateSlotEntity> restaurantSlots;

	// bi-directional many-to-one association to RestaurantTemplateType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = CapacityConstants.RESTAURANT_TEMPLATE_TYPE_ID)
	private RestaurantTemplateTypeEntity restaurantTemplateType;

	// bi-directional many-to-one association to RestaurantTemplateAndBusinessDate
	@JsonBackReference
	@OneToMany(mappedBy = CapacityConstants.RESTAURANT_TEMPLATE2)
	private List<RestaurantTemplateAndBusinessDateEntity> restaurantTemplateAndBusinessDates;

	// bi-directional many-to-one association to RestaurantTemplateAndCapacityChannel
	@JsonBackReference
	@OneToMany(mappedBy = CapacityConstants.RESTAURANT_TEMPLATE2)
	private List<RestaurantTemplateAndCapacityChannelEntity> restaurantTemplateAndCapacityChannels;

}