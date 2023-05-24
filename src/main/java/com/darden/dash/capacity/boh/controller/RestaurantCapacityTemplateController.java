package com.darden.dash.capacity.boh.controller;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplateResponse;
import com.darden.dash.capacity.boh.model.ViewRestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.ViewRestaurantCapacityTemplateResponse;
import com.darden.dash.capacity.boh.service.RestaurantCapacityTemplateService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.model.ErrorResponse;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * @author gunsaik
 * 
 *         This controller is used to manage the different activities related
 *         capacity template for BOH.
 * 
 *
 */
@RestController
@RequestMapping(value = CapacityConstants.API_V1, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = CapacityConstants.CAPACITYMANAGEMENT_CROSS_ORIGIN)
public class RestaurantCapacityTemplateController {

	private final JwtUtils jwtUtils;

	private final RestaurantCapacityTemplateService restaurantCapacityTemplateService;

	/**
	 * Autowiring required properties.
	 * 
	 * @param jwtUtils
	 * @param capacityManagementService
	 * @param capacityChannelService
	 */
	@Autowired
	public RestaurantCapacityTemplateController(JwtUtils jwtUtils,
			RestaurantCapacityTemplateService restaurantCapacityTemplateService) {
		super();
		this.jwtUtils = jwtUtils;
		this.restaurantCapacityTemplateService = restaurantCapacityTemplateService;

	}

	/**
	 * This method is to get list of Restairant Templates from database
	 *
	 * @param isRefDataReq flag based on which reference data to be fetched.
	 * 
	 * 
	 * @param accessToken  Token used to authenticate the user and extract the
	 *                     userDetails for this API
	 * 
	 * @return RestaurantResponse model class to build response for capacity data.
	 * 
	 * @throws GeneralSecurityException security exception class that provides type
	 *                                  safety for all the security-related
	 *                                  exception
	 */
	@GetMapping(value = CapacityConstants.PATH_RESTAURANT_CAPACITY_TEMPLATES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ViewRestaurantCapacityTemplateResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllRestaurantCapacityTemplates(
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		List<ViewRestaurantCapacityTemplate> capacityTemplateEntity = restaurantCapacityTemplateService
				.getAllCapacityTemplates(new BigInteger(RequestContext.getConcept()));
		return new ViewRestaurantCapacityTemplateResponse(capacityTemplateEntity).build(
				CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}

	/**
	 * Method is used to get the a Capacity template using Id for the respective
	 * concept and Reference from the {@link CapacityManagementService} Then map the
	 * retrieved Capacity template of @CapacityTemplateResponse object. At last the
	 * API successful response is built and returned.
	 *
	 * @param accessToken token used to authenticate the user and extract the
	 *                    userName for this application
	 * @return CapacityTemplateResponse return entire response to the user.
	 * @throws JsonProcessingException if any JSON processing exception is thrown at
	 *                                 runtime e.g JSON parsing
	 */
	@GetMapping(value = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_WITH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestaurantCapacityTemplateResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getRestaurantCapacityTempalteId(@PathVariable String templateId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		RestaurantCapacityTemplate restaurantCapacityTemplate = null;
		BigInteger bigTemplateId = new BigInteger(templateId);
		if (!templateId.isEmpty()) {
			restaurantCapacityTemplate = restaurantCapacityTemplateService
					.getRestaurantCapacityTempalteById(bigTemplateId);
		}
		return new RestaurantCapacityTemplateResponse(restaurantCapacityTemplate)
				.build(CapacityConstants.RESTUARANT_TEMPLATE_LOADED_SUCESSFULLY, CapacityConstants.STATUS_CODE_200);

	}

}
