package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.time.Instant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	@Schema(example = "capacityChannelId")
	private BigInteger capacityChannelId;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(example = "friendlyName")
	private String friendlyName;

	@NotNull(message = ErrorCodeConstants.EC_4001)
	@Min(value=5,message="Interval should be Minimum value is 5")
	@Max(value=1440,message="Interval should be Maximum value is 1440")
	@Schema(example = "123")
	private Integer interval;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(example = "HH:mm:ss")
	private String operationHourStartTime;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(example = "HH:mm:ss")
	private String operationHourEndTime;

}
