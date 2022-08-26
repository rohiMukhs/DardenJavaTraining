package com.darden.dash.capacity.model;

import javax.validation.constraints.Pattern;

import com.darden.dash.common.constant.CommonConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *This is the model class for delete Course Request. This class is used to
 * take and validate the request data entered while DELETE api takes place
 * 
 * @author asdattat
 */
@Getter
@AllArgsConstructor
public class DeleteModelTemplateRequest {

	@Pattern(regexp = CommonConstants.PATTERN_NUMERIC, message = "4004")
	private String courseId;
	@Pattern(regexp = CommonConstants.PATTERN_DELETE_COMPLETE_FLAG, message = "4013")
	private String  deleteConfirmed;

}
