package com.darden.dash.capacity.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.Getter;
import lombok.Setter;
/**
 *  @author skashala
 * 
 * This Model class is written for the purpose of showing the
 *  values of Create Request for Capacity Model
 */
@Getter
@Setter
public class CapacityModelRequest {
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Length(max = 40, message = ErrorCodeConstants.EC_4002)
	@Pattern(regexp = CommonConstants.PATTERN_BEFORE_AFTER_SPACE, message = ErrorCodeConstants.EC_4014)
	@Pattern(regexp = CapacityConstants.PATTERN_ALPHANUMERIC_GLOBAL_SPL_CHARACTERS, message = ErrorCodeConstants.EC_4003)
	private String templateModelName;
	private List<TemplatesAssigned> templatesAssigned;
	private List<RestaurantsAssigned> restaurantsAssigned;

}

