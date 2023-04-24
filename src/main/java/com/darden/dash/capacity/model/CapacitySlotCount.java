package com.darden.dash.capacity.model;

import java.math.BigInteger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vijmohit
 * 
 *         This Model class is written for the purpose of showing the values of
 *         Create Request for Capacity Slot Request
 */

@Getter
@Setter
public class CapacitySlotCount {

	@NotNull(message = ErrorCodeConstants.EC_4001)
	private BigInteger capacitySlotId;

	@NotBlank(message = ErrorCodeConstants.EC_4001)
	private Integer capacityCount;

	@NotNull(message = ErrorCodeConstants.EC_4001)
	private BigInteger capacitySlotTypeId;
}
