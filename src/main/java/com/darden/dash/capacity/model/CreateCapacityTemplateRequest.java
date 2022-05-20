package com.darden.dash.capacity.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *  values of Create Request for Capacity Template
 */
@Getter
@Setter
public class CreateCapacityTemplateRequest extends CapacityTemplateCommon {
	
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Length(max = 40, message = ErrorCodeConstants.EC_4002)
	@Pattern(regexp = CommonConstants.PATTERN_BEFORE_AFTER_SPACE, message = ErrorCodeConstants.EC_4014)
	@Pattern(regexp = CommonConstants.PATTERN_ALPHANUMERIC, message = ErrorCodeConstants.EC_4003)
	private String capacityTemplateName;
	
	private List<CreateResponseSlot> slotChannels;
	
}
