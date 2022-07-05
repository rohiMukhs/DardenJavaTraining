package com.darden.dash.capacity.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for all Service responses as well as delete api responses.
 * 
 * @author alisht
 * @author salfayee
 * @author nehajare
 *
 */

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServiceResponse {

	@Schema(description = CapacityConstants.MATCHES_THE_HTTP_STATUS_CODE, defaultValue = CapacityConstants.STATUS_CODE_SUCCESS, required = true)
	private Integer status;
	@Schema(description = CapacityConstants.SUCCESS, defaultValue = CapacityConstants.SUCCESS, required = true)
	private String title;
	@Schema(description = CapacityConstants.CORRELATION_ID, defaultValue = CapacityConstants.D64CF01B_CE65_4A57_AC3E_F7FA09E1A87F, required = true)
	private String correlationId;

	public ResponseEntity<Object> build(String title, Integer status) {
		this.title = title;
		this.status = status;
		this.correlationId = RequestContext.getCorrelationId();
		return new ResponseEntity<>(this, HttpStatus.valueOf(status));

	}

}
