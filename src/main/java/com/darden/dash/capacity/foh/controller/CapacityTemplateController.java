package com.darden.dash.capacity.foh.controller;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.ViewCapacityChannels;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.model.ErrorResponse;
import com.darden.dash.common.util.JwtUtils;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * @author vraviran
 * 
 * 		 This controller is used to manage the different activities related
 * 		 capacity template for FOH.
 *       
 *
 */
@RestController
@RequestMapping(value = CapacityConstants.API_V1, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = CapacityConstants.CAPACITYMANAGEMENT_CROSS_ORIGIN)
public class CapacityTemplateController {
	
	private final JwtUtils jwtUtils;
	private CapacityManagementService capacityManagementService;
	private CapacityChannelService capacityChannelService;
	
	/**
	 * Autowiring required properties.
	 * 
	 * @param jwtUtils
	 * @param capacityManagementService
	 * @param capacityChannelService
	 */
	@Autowired
	public CapacityTemplateController(JwtUtils jwtUtils, CapacityManagementService capacityManagementService, CapacityChannelService capacityChannelService) {
		super();
		this.jwtUtils = jwtUtils;
		this.capacityManagementService = capacityManagementService;
		this.capacityChannelService = capacityChannelService;
	}

	/**
	 * This method is to get list of Capacity Templates from database along with
	 * list of channel data from CapacityTemplate,
	 * CapacityTemplateAndCapacityChannel , CapacitySlot ,CapacityChannel entities
	 * and mapping the capacity channel mapper for reference data Here validating
	 * the user has access for action.
	 *
	 * @param isRefDataReq flag based on which reference data to be fetched.
	 * 
	 * @param date String which contains for which date templates to be fetched.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return CapacityResponse model class to build response for capacity data.
	 * 
	 * @throws GeneralSecurityException security exception class that provides type safety for all the
	 * 									security-related exception
	 */
	@GetMapping(value = CapacityConstants.PATH_FOH_CAPACITY_TEMPLATES ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllCapacityTemplates(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId, @RequestParam Boolean isRefDataReq,@RequestParam(required = false)String date,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws GeneralSecurityException {
        // validating the access token
		jwtUtils.findUserDetail(accessToken);
		if(!jwtUtils.isActionCodeExists(accessToken, CapacityConstants.UA_CAPACITY_MANAGER_SLOTS_VIEW)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return capacityManagementService.getAllCapacityTemplatesBasedOnDate(isRefDataReq, RequestContext.getConcept(), date).build(CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}
	
	/**
	 * This method is to get list of  capacity channel and capacity slot type data
	 * for reference data Here validating the user has access for action.
	 *
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ViewCapacityChannels model class to build response for capacity channel data and slot type data.
	 * 
	 * @throws GeneralSecurityException security exception class that provides type safety for all the
	 * 									security-related exception
	 */
	@GetMapping(value = CapacityConstants.PATH_FOH_REFERENCEDATA ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.MSG_REFERENCE_DATA_LOADED_SUCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ViewCapacityChannels.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getReferenceData(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId, 
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws GeneralSecurityException {
        // validating the access token
		jwtUtils.findUserDetail(accessToken);
		if(!jwtUtils.isActionCodeExists(accessToken, CapacityConstants.UA_CAPACITY_MANAGER_SLOTS_VIEW)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		ReferenceDatum reference = new ReferenceDatum();
		reference.setCapacityChannel(capacityChannelService.getAllCapacityChannels());
		reference.setCapacitySlotTypes(capacityManagementService.getAllCapacitySlotTypes());
		return new ViewCapacityChannels(reference).build(CapacityConstants.MSG_REFERENCE_DATA_LOADED_SUCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}

}
