package com.darden.dash.capacity.boh.model;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CapacitySlotCount;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.constant.CommonConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vraviran
 *
 *         This Model class is written for the purpose of showing the values of
 *         Create Request for Capacity Template
 */
@Getter
@Setter
public class CreateRestaurantCapacityTemplateRequest {

	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Length(max = 40, message = ErrorCodeConstants.EC_4002)
	@Pattern(regexp = CommonConstants.PATTERN_BEFORE_AFTER_SPACE, message = ErrorCodeConstants.EC_4014)
	@Pattern(regexp = CapacityConstants.PATTERN_ALPHANUMERIC_GLOBAL_SPL_CHARACTERS, message = ErrorCodeConstants.EC_4003)
	@Schema(description = "Capacity Template Name", example = "Template1")
	private String capacityTemplateName;
	@Schema(description = "Effective Date", example = "2023-05-25")
	private String effectiveDate;
	@Schema(description = "Expiry Date", example = "2023-05-25")
	private String expiryDate;
	@Schema(description = "Concept Id", example = "1")
	private BigInteger conceptId;
	@Schema(description = "Template Type Id", example = "1")
	private BigInteger templateTypeId;
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(description = "Template Type Name", example = "Days/Dates")
	private String templateTypeName;
	@Schema(description = "Sun Day", example = "Y")
	private String sunDay;
	@Schema(description = "Mon Day", example = "Y")
	private String monDay;
	@Schema(description = "Tue Day", example = "Y")
	private String tueDay;
	@Schema(description = "Wed Day", example = "Y")
	private String wedDay;
	@Schema(description = "Thu Day", example = "Y")
	private String thuDay;
	@Schema(description = "Fri Day", example = "Y")
	private String friDay;
	@Schema(description = "Sat Day", example = "Y")
	private String satDay;
	private List<BusinessDate> businessDates;
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(description = "Slot StartTime", example = "11:15")
	private String slotStartTime;
	@NotBlank(message = ErrorCodeConstants.EC_4001)
	@Schema(description = "Slot EndTime", example = "11:30")
	private String slotEndTime;
	@Valid
	@NotEmpty(message = ErrorCodeConstants.EC_4001)
	List<CapacitySlotCount> slots;

}
