package com.darden.dash.capacity.model;

import javax.validation.constraints.Pattern;

import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author vraviran
 *
 *	This Model class is written for the purpose of storing the parameters
 *	of the delete Api to validate the values of parameters.
 *  
 */
@Getter
@AllArgsConstructor
public class DeleteCapacityTemplateRequest {

	@Pattern(regexp = CommonConstants.PATTERN_NUMERIC, message = ErrorCodeConstants.EC_4004)
	private String templateId;
	@Pattern(regexp = CommonConstants.PATTERN_DELETED_FLAG, message = ErrorCodeConstants.EC_4011)
	private String deletedFlag;
	
}
