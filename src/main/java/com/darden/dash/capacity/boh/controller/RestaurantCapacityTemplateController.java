package com.darden.dash.capacity.boh.controller;

import java.math.BigInteger;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.darden.dash.capacity.boh.model.CreateRestaurantCapacityTemplateRequest;
import com.darden.dash.capacity.foh.service.CapacityManagementFOHService;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityModelResponse;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CapacityTemplateResponse;
import com.darden.dash.capacity.model.DeleteModelTemplateRequest;
import com.darden.dash.capacity.model.ListOfCapacityTemplateIds;
import com.darden.dash.capacity.model.ListOfModelResponse;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.model.ViewCapacityChannels;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.model.ErrorResponse;
import com.darden.dash.common.model.ServiceResponse;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * @author vlsowjan
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

	/**
	 * Autowiring required properties.
	 * 
	 * @param jwtUtils
	 * @param capacityManagementService
	 * @param capacityChannelService
	 */
	@Autowired
	public RestaurantCapacityTemplateController(JwtUtils jwtUtils) {
		super();
		this.jwtUtils = jwtUtils;

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
	 * @param date         String which contains for which date templates to be
	 *                     fetched.
	 * 
	 * @param accessToken  Token used to authenticate the user and extract the
	 *                     userDetails for this API
	 * 
	 * @return CapacityResponse model class to build response for capacity data.
	 * 
	 * @throws GeneralSecurityException security exception class that provides type
	 *                                  safety for all the security-related
	 *                                  exception
	 */
	@GetMapping(value = CapacityConstants.PATH_RESTAURANT_CAPACITY_TEMPLATES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllCapacityTemplates(
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws Exception {
		jwtUtils.findUserDetail(accessToken);

		return null;
	}

	/**
	 * This method is to get list of capacity channel and capacity slot type data
	 * for reference data Here validating the user has access for action.
	 *
	 * @param accessToken Token used to authenticate the user and extract the
	 *                    userDetails for this API
	 * 
	 * @return ViewCapacityChannels model class to build response for capacity
	 *         channel data and slot type data.
	 * 
	 * @throws GeneralSecurityException security exception class that provides type
	 *                                  safety for all the security-related
	 *                                  exception
	 */
	@GetMapping(value = CapacityConstants.PATH_BOH_REFERENCEDATA, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.REFERENCE_DATA_LOADED_SUCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getReferenceData(
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);

		return null;
	}

	/**
	 * This method is used for post operation to get all model list detail based on
	 * the value of list of templates provided in request body.
	 * 
	 * @param conceptId                 String contains the value of concept id from
	 *                                  headers.
	 * 
	 * @param listOfCapacityTemplateIds model class containing the list of capacity
	 *                                  templates id.
	 * 
	 * @param accessToken               Token used to authenticate the user and
	 *                                  extract the userDetails for this API
	 * 
	 * @return ResponseEntity<Object> Response class with information of the Object
	 *         containing model list detail.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PostMapping(value = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCapacityTemplate(
			@RequestBody CreateRestaurantCapacityTemplateRequest createRestaurantCapacityTemplateRequest,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);

		return null;
	}

	/**
	 * Method is used for UPDATE operation for the respective concept Id.The request
	 * body is sent to CapacityModel service. Before sending the request body
	 * CapacityModelRequest is validated. After that data is mapped from the
	 * RestaurantsAssigned ,TemplatesAssigned then sent to the CapacityModel service
	 * and returned the updated data. At last the API successful response is built
	 * and returned.
	 * 
	 * 
	 * @param modelId              String containing the value of capacity template
	 *                             model to be updated.
	 * 
	 * @param capacityModelRequest Request class with information to create Model
	 *                             Template.
	 * 
	 * @param accessToken          Token used to authenticate the user and extract
	 *                             the userDetails for this API
	 * 
	 * @return ResponseEntity<Object> Response class with information of the Object
	 *         containing created/modified Capacity Model detail.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PutMapping(value = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_WITH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_UPDATED, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_UPDATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> updateCapacityTemplate(@RequestBody CreateRestaurantCapacityTemplateRequest createRestaurantCapacityTemplateRequest,@PathVariable String templateId,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {

		jwtUtils.findUserDetail(accessToken);

		return null;
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
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getCapacityTempalteId(@PathVariable String templateId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);

		return null;
	}

	/**
	 * Method is used for DELETE Capacity model Template for the respective concept.
	 * The parameters are sent to CapacityModelTemplate service. Before sending the
	 * parameters, those are validated based on the validation if parameter doesn't
	 * match validation applicationError is thrown with specific error code. The
	 * Delete status response code is created for dynamic response whether the Model
	 * is deleted or Ready for the delete At last the API successful response is
	 * built and returned.
	 * 
	 * @param templateId  Id of a capacity model that needs to be deleted.
	 * 
	 * @param confirm     Flag containing confirmation regarding deleting flag or
	 *                    checking dependency.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                    userDetails for this API
	 * 
	 * @return ServiceResponse ServiceResponse Response class returned after
	 *         successful deletion
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */

	@PatchMapping(value = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_WITH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_202, description = CapacityConstants.RESTAURANT_CAPACITY_TEMPLATE_DELETED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> deleteTemplateModel(@PathVariable String templateId,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {

		jwtUtils.findUserDetail(accessToken);

		return null;
	}

}
