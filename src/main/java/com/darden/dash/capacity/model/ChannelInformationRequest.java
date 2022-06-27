package com.darden.dash.capacity.model;

import java.math.BigInteger;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author vraviran
 * 
 * This Model is written for purpose of passing the values for 
 * update and storing the response of updated value.
 *
 */
@Getter
@Setter
public class ChannelInformationRequest {
	
	@NotNull(message = ErrorCodeConstants.EC_4001)
	@Schema(example = CapacityConstants.CAPACITYCHANNELID)
	private BigInteger capacityChannelId;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Pattern(regexp = CapacityConstants.PATTERN_ALPHANUMERIC_WITH_ALL_SPL_CHARACTERS, message = ErrorCodeConstants.EC_4003)
	@Schema(example = CapacityConstants.POSNAME)
	private String posName;

	@NotNull(message = ErrorCodeConstants.EC_4001)
	@Min(value=5,message= CapacityConstants.INTERVAL_MIN)
	@Max(value=1440,message= CapacityConstants.INTERVAL_MAX)
	@Schema(example = CapacityConstants.INT_EXAMPLE)
	private Integer interval;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(example = CapacityConstants.TIME_EXAMPLE)
	private String operationHourStartTime;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(example = CapacityConstants.TIME_EXAMPLE)
	private String operationHourEndTime;

}