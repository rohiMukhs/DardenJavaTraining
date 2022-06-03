package com.darden.dash.capacity.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateCapacityTemplateResponse;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.CreateCombineChannelResponse;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.model.EditChannelResponse;
import com.darden.dash.capacity.model.ServiceResponse;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.capacity.validation.ChannelValidator;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.model.ErrorResponse;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * @author Skashala
 * @date 16-May-2022
 * 
 *       This controller is used to manage the capacity template for different
 *       activities, getting capacity templates,creating the capacity
 *       template,deleting the capacity template, updating the capacity template
 */
@RestController
@RequestMapping(value = CapacityConstants.API_V1, produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = CapacityConstants.CAPACITYMANAGEMENT_CROSS_ORIGIN)
public class CapacityManagementController {

	private CapacityManagementService capacityManagementService;
	
	private final JwtUtils jwtUtils;
	
	private CapacityValidator capacityValidator;
	
	private CapacityChannelService capacityChannelService;
	
	private ChannelValidator channelValidator;

	@Autowired
	public CapacityManagementController(CapacityManagementService capacityManagementService, JwtUtils jwtUtils, CapacityValidator capacityValidator
			, CapacityChannelService capacityChannelService, ChannelValidator channelValidator) {
		super();
		this.jwtUtils = jwtUtils;
		this.capacityManagementService = capacityManagementService;
		this.capacityValidator = capacityValidator;
		this.capacityChannelService = capacityChannelService;
		this.channelValidator = channelValidator;
	}

	/**
	 * * Method is used to get the list of all Capacity Templates and Reference from
	 * the Capacity Management service. Then, map the retrieved Templates to the
	 * list of CapacityTemplateResponse object. At last the API successful response
	 * is built and returned.
	 * 
	 * @param accessToken
	 * @return ResponseEntity<Object>
	 */
	@GetMapping(value = CapacityConstants.CAPACITY_TEMPLATES ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllCapacityTemplates(
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) {
        // validating the access token
		jwtUtils.findUserDetail(accessToken);
		return capacityManagementService.getAllCapacityTemplates();

	}
	
	/**
	 * Method is used for CREATE operation for the respective concept. The request
	 * body is sent to CapacityTemplate service. Before sending the request
	 * body @CreateCapacityTemplateRequest is validated. After that data is mapped to the
	 * CapacityTemplateEntity ,CapacitySlotEntity,CapacityTemplateAndCapacityChannelEntity,
	 * CapacityTemplateAndBussinessDateEntity, then sent to the CapacityTemplate service and 
	 * returned the saved data. At last the API successful response is built and returned.
	 * 
	 * 
	 * @param createCapacityTemplateRequest
	 * @param accessToken
	 * @return ResponseEntity<Object>
	 * @throws JsonProcessingException
	 */
	@PostMapping(value = CapacityConstants.CAPACITY_TEMPLATES ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCapacityTemplate(@RequestBody CreateCapacityTemplateRequest createCapacityTemplateRequest,
			@Parameter @RequestHeader (name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		
		jwtUtils.findUserDetail(accessToken);
		
		capacityValidator.validate(createCapacityTemplateRequest, OperationConstants.OPERATION_CREATE.getCode());
		
		return new CreateCapacityTemplateResponse(capacityManagementService.createTemplate(createCapacityTemplateRequest, accessToken)).build(CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_201);
		
	}
	
	/**
	 * Method is used for UPDATE operation for the respective concept. This request body
	 * contains the list of capacity channel values of the required field to be updated. Before 
	 * sending the request body @ChannelListRequest is validated for such as validation for not null
	 * and not blank and to avoid the duplicates among the list of channels.Using the authorization bearer
	 * token value user name is retrieved.Both channelListRequest and user detail is passed in capacity
	 * channel service and the edited data is retrieved with additional fields to be displayed in response.
	 * At last the API updated successful response is built and returned. 
	 * 
	 * 
	 * 
	 * @param channelListRequest
	 * @param accessToken
	 * @return ResponseEntity<Object>
	 * @throws JsonProcessingException
	 */
	@PutMapping(value = CapacityConstants.COMBINED_CHANNELS , produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> editChannelInformation(
			@RequestBody ChannelListRequest channelListRequest,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		String userDetail = jwtUtils.findUserDetail(accessToken);
		channelValidator.validate(channelListRequest,OperationConstants.OPERATION_UPDATE.getCode());
		return new EditChannelResponse(capacityChannelService.editChannelInformation(channelListRequest.getChannels(), userDetail)).build(CapacityConstants.CHANNEL_UPDATED , CapacityConstants.STATUS_CODE_INT_202);

	}
	
	/**
	 * Method is used for DELETE operation for the respective concept. The
	 * parameters are sent to CapacityTemplate service. Before sending the parameters,
	 * those are validated based on the validation if parameter doesn't match validation
	 * applicationError is thrown with specific error code. After that, @CenterTemplateEntity 
	 * is fetched form the CapacityTemplate service based on the value of parameter value set in
	 * appParameter entity Soft delete and Hard delete operation is performed on CapacityTemplate
	 * Entity. At last the API successful response is built and returned.
	 * 
	 * @param templateId
	 * @param deletedFlag
	 * @param accessToken
	 * @return ResponseEntity<Object>
	 * @throws JsonProcessingException
	 */
	@PatchMapping(value = CapacityConstants.CAPACITY_TEMPLATE_WITH_TEMPLATEID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_202, description = CapacityConstants.CAPACITY_TEMPLATE_DELETED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> deleteTemplate(@PathVariable String templateId, @RequestParam String deletedFlag,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		String userDetail = jwtUtils.findUserDetail(accessToken);
		
		capacityValidator.validate(new DeleteCapacityTemplateRequest(templateId, deletedFlag), OperationConstants.OPERATION_DELETE.getCode());
		
		capacityManagementService.deleteByTemplateId(templateId, deletedFlag, userDetail);
		
		return new ServiceResponse().build(CapacityConstants.CAPACITY_TEMPLATE_DELETED, CapacityConstants.STATUS_CODE_INT_202);
	}
	
	/**
	 *  Method is used for CREATE operation for the respective concept. The request body CreateCombineChannelRequest
	 * contains the all the data for combineCapacityChannel to be created and all the CapacityChannels to be assigned. 
	 * Before sending the request body CreateCombineChannelRequest is validated for such as validation for not null
	 * and not blank and to avoid the duplicates among the list of channels.Using the authorization bearer
	 * token value user name is retrieved.Both CreateCombineChannelRequest and user detail is passed in capacity
	 * channel service and the Created data is retrieved with additional fields to be displayed in response.
	 * At last the API updated successful response is built and returned. 
	 * 
	 * 
	 * @param createCombineChannelRequest
	 * @param accessToken
	 * @return ResponseEntity<Object>
	 * @throws JsonProcessingException
	 */
	@PostMapping(value = CapacityConstants.COMBINE_CHANNELS ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema())),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCombineCapacityChannel(@RequestBody CreateCombineChannelRequest createCombineChannelRequest,
			@Parameter @RequestHeader (name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		
		String userDetail = jwtUtils.findUserDetail(accessToken);
		
		channelValidator.validate(createCombineChannelRequest, OperationConstants.OPERATION_CREATE.getCode());
		
		return new CreateCombineChannelResponse(capacityChannelService.addCombinedChannel(createCombineChannelRequest, userDetail)).build(CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_201);
		
	}
}