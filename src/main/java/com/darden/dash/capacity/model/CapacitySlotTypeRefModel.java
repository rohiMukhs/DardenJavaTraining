package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author vraviran
 * 
 * This model is for displaying capacity slot type data.
 *
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class CapacitySlotTypeRefModel implements Serializable {

	private BigInteger slotTypeId;
	private String slotTypeName;
	
}