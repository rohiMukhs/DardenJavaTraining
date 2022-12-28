package com.darden.dash.capacity.model;

import javax.validation.constraints.Pattern;

import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class DeleteCapacityChannelRequest {

	@Pattern(regexp = CommonConstants.PATTERN_NUMERIC, message = ErrorCodeConstants.EC_4004)
	private String channelId;
	@Pattern(regexp = CommonConstants.PATTERN_DELETE_COMPLETE_FLAG, message = ErrorCodeConstants.EC_4011)
	private String deletedFlag;

}
