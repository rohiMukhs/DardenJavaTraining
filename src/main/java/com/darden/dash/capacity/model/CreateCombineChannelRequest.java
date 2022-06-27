package com.darden.dash.capacity.model;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is created for the purpose of passing the
 * data of combine channel to be created with validation added for
 * not null, not blank, etc.and the data of channels that are 
 * assigned to the combine capacity channel.
 *
 */
@Setter
@Getter
public class CreateCombineChannelRequest {
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Length(max = 40, message = ErrorCodeConstants.EC_4002)
	@Pattern(regexp = CommonConstants.PATTERN_BEFORE_AFTER_SPACE, message = ErrorCodeConstants.EC_4014)
	@Pattern(regexp = CapacityConstants.PATTERN_ALPHANUMERIC_EXCLUDING, message = ErrorCodeConstants.EC_4003)
	private String combinedChannelName;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Length(max = 40, message = ErrorCodeConstants.EC_4002)
	@Pattern(regexp = CommonConstants.PATTERN_BEFORE_AFTER_SPACE, message = ErrorCodeConstants.EC_4014)
	@Pattern(regexp = CapacityConstants.PATTERN_ALPHANUMERIC_WITH_ALL_SPL_CHARACTERS, message = ErrorCodeConstants.EC_4003)
	private String posName;
	
	@NotEmpty(message = ErrorCodeConstants.EC_4001)
	private Set<String> channels;
	
	@NotNull(message = ErrorCodeConstants.EC_4001)
	@Min(value=5,message= CapacityConstants.INTERVAL_MIN)
	@Max(value=1440,message= CapacityConstants.INTERVAL_MAX)
	private Integer interval;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	private String startTime;
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	private String endTime;
	
}
