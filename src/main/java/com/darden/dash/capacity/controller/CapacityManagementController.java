package com.darden.dash.capacity.controller;

import java.math.BigInteger;

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

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityModelResponse;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CapacityTemplateResponse;
import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateCapacityTemplateResponse;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.CreateCombineChannelResponse;
import com.darden.dash.capacity.model.DeleteCapacityChannelRequest;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.model.DeleteModelTemplateRequest;
import com.darden.dash.capacity.model.EditChannelResponse;
import com.darden.dash.capacity.model.GetCapacityModelResponse;
import com.darden.dash.capacity.model.ListOfCapacityTemplateIds;
import com.darden.dash.capacity.model.ListOfModelResponse;
import com.darden.dash.capacity.model.ViewCapacityChannels;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.validation.CapacityTemplateModelValidator;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.capacity.validation.ChannelValidator;
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
	
	private CapacityTemplateModelService capacityTemplateModelService;
	
	private LocationClient locationClient;
	
	private ChannelValidator channelValidator;
	
	private CapacityTemplateModelValidator capacityTemplateModelValidator;

	@Autowired
	public CapacityManagementController(CapacityManagementService capacityManagementService, JwtUtils jwtUtils, CapacityValidator capacityValidator
			, CapacityChannelService capacityChannelService, CapacityTemplateModelService capacityTemplateModelService
			, LocationClient locationClient, ChannelValidator channelValidator, CapacityTemplateModelValidator capacityTemplateModelValidator) {
		super();
		this.jwtUtils = jwtUtils;
		this.capacityManagementService = capacityManagementService;
		this.capacityValidator = capacityValidator;
		this.capacityChannelService = capacityChannelService;
		this.capacityTemplateModelService = capacityTemplateModelService;
		this.locationClient = locationClient;
		this.channelValidator = channelValidator;
		this.capacityTemplateModelValidator = capacityTemplateModelValidator;
	}

	/**
	 * * Method is used to get the list of all Capacity Templates and Reference from
	 * the Capacity Management service. Then, map the retrieved Templates to the
	 * list of CapacityTemplateResponse object. At last the API successful response
	 * is built and returned.
	 * 
	 * @param accessToken 	Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ResponseEntity<Object> Response that contains the list of all Capacity Templates with
	 * 								  required reference for drop down value.  
	 */
	@GetMapping(value = CapacityConstants.CAPACITY_TEMPLATES ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllCapacityTemplates(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId, @RequestParam Boolean isRefDataReq,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
        // validating the access token
		jwtUtils.findUserDetail(accessToken);
		capacityValidator.validate(null, OperationConstants.OPERATION_GET.getCode());
		return capacityManagementService.getAllCapacityTemplates(isRefDataReq, RequestContext.getConcept()).build(CapacityConstants.CAPACITY_TEMPLATE_LOADED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_200);
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
	@GetMapping(value = CapacityConstants.GET_TEMPLATES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_TEMPLATE_SINGLE_API_TITLE, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityTemplateResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getCapacityTempalteId(@PathVariable String templateId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken,
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		capacityValidator.validate(null, OperationConstants.OPERATION_GET.getCode());
		CapacityTemplate capacityTemplate = null;
		if (Integer.parseInt(templateId) != 0) {
			capacityTemplate = capacityManagementService.getCapacityTemplateById(new BigInteger(templateId));
		}
		return new CapacityTemplateResponse(capacityTemplate, capacityChannelService.getReferenceData())
				.build(CapacityConstants.CAPACITY_TEMPLATE_SINGLE_API_TITLE, CapacityConstants.STATUS_CODE_200);

	}

	/**
	 * Method is used for CREATE operation for the respective concept. The request
	 * body is sent to CapacityTemplate service. Before sending the request
	 * body @CreateCapacityTemplateRequest is validated. After that data is mapped to the
	 * CapacityTemplateEntity ,CapacitySlotEntity,CapacityTemplateAndCapacityChannelEntity,
	 * CapacityTemplateAndBussinessDateEntity, then sent to the CapacityTemplate service and 
	 * returned the saved data. At last the API successful response is built and returned.
	 * 
	 * @param accessToken 	Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @param createCapacityTemplateRequest Request class with information to
	 * 										create capacity Template.
	 * 
	 * @return ResponseEntity<Object> Response class with information of the
	 *         						  Object containing created/modified Capacity Template detail.
	 *         
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PostMapping(value = CapacityConstants.CAPACITY_TEMPLATES ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateCapacityTemplateResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCapacityTemplate(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@RequestBody CreateCapacityTemplateRequest createCapacityTemplateRequest,
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
	 * @param channelListRequest Request class with information to
	 * 							 update Capacity Template.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return EditChannelResponse Response class with information of the
	 *         					   Object containing created/modified Capacity Template detail.
	 *         
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PutMapping(value = CapacityConstants.COMBINED_CHANNELS , produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CHANNEL_UPDATED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EditChannelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> editChannelInformation(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
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
	 * @param templateId Id of a capacity template that needs to be deleted.
	 * 
	 * @param deletedFlag Flag containing deletion information for Capacity Template.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ServiceResponse ServiceResponse Response class returned after successful deletion
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PatchMapping(value = CapacityConstants.CAPACITY_TEMPLATE_WITH_TEMPLATEID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_202, description = CapacityConstants.CAPACITY_TEMPLATE_DELETED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> deleteTemplate(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@PathVariable String templateId, @RequestParam String deleteConfirmed,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		String userDetail = jwtUtils.findUserDetail(accessToken);
		
		capacityValidator.validate(new DeleteCapacityTemplateRequest(templateId, deleteConfirmed), OperationConstants.OPERATION_DELETE.getCode());
		
		String capacityTemplateNm = capacityManagementService.deleteByTemplateId(templateId, deleteConfirmed, userDetail);
		
		return new ServiceResponse().build((CapacityConstants.Y.equals(deleteConfirmed))? capacityTemplateNm.concat(CapacityConstants.CAPACITY_TEMPLATE_DELETED)
				: capacityTemplateNm.concat(CapacityConstants.CAPACITY_MODEL_READY_FOR_DELETE)
				, CapacityConstants.STATUS_CODE_INT_202);
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
	 * @param createCombineChannelRequest Request class with information to
	 * 							          create Combine Channel Request.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                    userDetails for this API.
	 *                      
	 * @return CreateCombineChannelResponse Response class with information of the
	 *         					  			Object containing created/modified Combine Channel detail.
	 *         
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PostMapping(value = CapacityConstants.COMBINE_CHANNELS ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_CHANNELS_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateCombineChannelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCombineCapacityChannel(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@RequestBody CreateCombineChannelRequest createCombineChannelRequest,
			@Parameter @RequestHeader (name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		
		String userDetail = jwtUtils.findUserDetail(accessToken);
		
		channelValidator.validate(createCombineChannelRequest, OperationConstants.OPERATION_CREATE.getCode());
		
		return new CreateCombineChannelResponse(capacityChannelService.addCombinedChannel(createCombineChannelRequest, userDetail)).build(CapacityConstants.CAPACITY_CHANNELS_CREATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_201);
		
	}
	
	/**
	 * Method is used for UPDATE operation for the respective concept. The request
	 * body is sent to CapacityTemplate service. Before sending the request
	 * body @CreateCapacityTemplateRequest is validated. After that data is mapped
	 * to the CapacityTemplateEntity
	 * ,CapacitySlotEntity,CapacityTemplateAndCapacityChannelEntity,
	 * CapacityTemplateAndBussinessDateEntity, then sent to the CapacityTemplate
	 * service and returned the saved data. At last the API successful response is
	 * built and returned.
	 * 
	 * 
	 * 
	 * @param createCapacityTemplateRequest Request class with information to
	 * 							            edit multiple Combine Channel Request.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                    userDetails for this API.
	 *                    
	 * @param templateId Flag containing deletion information for Capacity Template.
	 * 
	 * @return CreateCapacityTemplateResponse Response class with information of the
	 *         					  			  Object containing created/modified Combine Channel detail.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PutMapping(value = CapacityConstants.EDIT_TEMPLATES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_UPDATED, description = CapacityConstants.CAPACITY_TEMPLATE_UPDATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreateCapacityTemplateResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> updateCapacityTemplate(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Schema(type = CapacityConstants.INTEGER) @PathVariable String templateId,
			@RequestBody CreateCapacityTemplateRequest createCapacityTemplateRequest,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		capacityValidator.validate(createCapacityTemplateRequest, OperationConstants.OPERATION_UPDATE.getCode(),templateId);
		return new CreateCapacityTemplateResponse(
				capacityManagementService.updateCapacityTemplate(createCapacityTemplateRequest, accessToken,new BigInteger(templateId)))
				.build(CapacityConstants.CAPACITY_TEMPLATE_UPDATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_202);

	}
	
	/**
	 * Method is used for GET operation for the respective concept.Prior to 
	 * the service call authorization of access token and validation of header
	 * is done. Based on value of the concept Id list of capacity Model entity
	 * is retrieved. Required value from capacity template and location detail
	 * are mapped to the model class.List of locations from client call is 
	 * attached to the response body for the reference of restaurants detail.
	 * 
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                    userDetails for this API.
	 *                    
	 * @return GetCapacityModelResponse this model class returns data of capacity model
	 * 						and restaurant details
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@GetMapping(value = CapacityConstants.CAPACITY_MODELS ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_MODEL_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetCapacityModelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllModels(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) 
					throws JsonProcessingException{
		jwtUtils.findUserDetail(accessToken);
		capacityTemplateModelValidator.validate(null, OperationConstants.OPERATION_GET.getCode());
		return new GetCapacityModelResponse(capacityTemplateModelService.getAllCapacityModels(RequestContext.getConcept()), locationClient.getAllRestaurants()).build(CapacityConstants.CAPACITY_MODEL_LOADED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}
	
	/**
	 * Method is used for CREATE operation for the respective concept. The request
	 * body is sent to CapacityModel service. Before sending the request
	 * body CapacityModelRequest is validated. After that data is mapped from the
	 * RestaurantsAssigned ,TemplatesAssigned then sent to the CapacityModel service and 
	 * returned the saved data. At last the API successful response is built and returned.
	 * 
	 * @param accessToken 	Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @param CapacityModelRequest Request class with information to
	 * 										create Model Template.
	 * 
	 * @return ResponseEntity<Object> Response class with information of the
	 *         						  Object containing created/modified Capacity Model detail.
	 *         
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PostMapping(value = CapacityConstants.CAPACITY_MODELS ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityModelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCapacityTemplate(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@RequestBody CapacityModelRequest capacityModelRequest,
			@Parameter @RequestHeader (name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		capacityTemplateModelValidator.validate(capacityModelRequest, OperationConstants.OPERATION_CREATE.getCode());
		return new CapacityModelResponse(capacityTemplateModelService.createCapacityModel(capacityModelRequest, accessToken)).build(CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_201);
		
	}
	
	/**
	 * Method is used for UPDATE operation for the respective concept Id.The request
	 * body is sent to CapacityModel service. Before sending the request
	 * body CapacityModelRequest is validated. After that data is mapped from the
	 * RestaurantsAssigned ,TemplatesAssigned then sent to the CapacityModel service and 
	 * returned the updated data. At last the API successful response is built and returned.
	 * 
	 * 
	 * @param modelId String containing the value of capacity template model to be updated.
	 * 
	 * @param capacityModelRequest Request class with information to
	 * 										create Model Template.
	 * 
	 * @param accessToken  Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ResponseEntity<Object> Response class with information of the
	 *         						  Object containing created/modified Capacity Model detail.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PutMapping(value = CapacityConstants.CAPACITY_MODELS_WITH_MODEL_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_UPDATED, description = CapacityConstants.CAPACITY_MODEL_UPDATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityModelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> updateCapacityModel(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@Schema(type = CapacityConstants.INTEGER) @PathVariable String modelId,
			@RequestBody CapacityModelRequest capacityModelRequest,
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		String user = jwtUtils.findUserDetail(accessToken);
		capacityTemplateModelValidator.validate(capacityModelRequest, OperationConstants.OPERATION_UPDATE.getCode(), modelId);
		return new CapacityModelResponse(capacityTemplateModelService.updateCapacityModel(modelId, capacityModelRequest, user)).build(CapacityConstants.CAPACITY_MODEL_UPDATED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_INT_202);
	}
	
	/**
	 * This method is used for post operation to get all model list detail based on the value of list of
	 * templates provided in request body.
	 * 
	 * @param conceptId String contains the value of concept id from headers.
	 * 
	 * @param listOfCapacityTemplateIds model class containing the list of capacity templates
	 * 								id.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ResponseEntity<Object> Response class with information of the
	 *         						  Object containing model list detail.
	 *         
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PostMapping(value = CapacityConstants.CAPACITYMODELLIST ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_201, description = CapacityConstants.CAPACITY_TEMPLATE_CREATED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CapacityModelResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> createCapacityTemplate(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@RequestBody ListOfCapacityTemplateIds listOfCapacityTemplateIds,
			@Parameter @RequestHeader (name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		return new ListOfModelResponse(capacityManagementService.getAllModelsRelatingToTemplateIdList(listOfCapacityTemplateIds.getTemplateIdList())).build(CapacityConstants.ALL_MODELS_LOADED_SUCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}	
	
	/**
	 * Method is used for DELETE Capacity model Template  for the respective concept. The
	 * parameters are sent to CapacityModelTemplate service. Before sending the parameters,
	 * those are validated based on the validation if parameter doesn't match validation
	 * applicationError is thrown with specific error code. 
	 * The Delete status response code is created for dynamic response whether the Model is deleted or Ready for the delete  
	 *  At last the API successful response is built and returned.
	 * 
	 * @param templateId Id of a capacity model that needs to be deleted.
	 * 
	 * @param confirm Flag containing confirmation regarding deleting flag or checking dependency.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ServiceResponse ServiceResponse Response class returned after successful deletion
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	
	@PatchMapping(value = CapacityConstants.CAPACITY_MODEL_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_202, description = CapacityConstants.CAPACITY_TEMPLATE_DELETED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> deleteTemplateModel(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,@PathVariable String templateId,@RequestParam String deletedConfirm,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {
		String userDetail = jwtUtils.findUserDetail(accessToken);
		
		capacityTemplateModelValidator.validate(new DeleteModelTemplateRequest(templateId,deletedConfirm), OperationConstants.OPERATION_DELETE.getCode());
		
		capacityTemplateModelService.deleteTemplateModel(templateId, userDetail,deletedConfirm);
		String deleteStatus = (CapacityConstants.Y.equals(deletedConfirm))? CapacityConstants.CAPACITY_TEMPLATE_DELETED: CapacityConstants.CAPACITY_MODEL_READY_FOR_DELETE;
		return new ServiceResponse().build(deleteStatus, CapacityConstants.STATUS_CODE_200);
	}
	
	/**
	 * This Get method is used to get all the capacity channel and combine channel data based on the concept
	 * Id.ViewCapacityChannels is used to build response entity to display all the capacity and combine 
	 * channels.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 *                      
	 * @return ViewCapacityChannels model class used to build response entity to
	 * 						display all the static and combine channels.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@GetMapping(value = CapacityConstants.CAPACITY_CHANNELS_PATH ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_SUCCESS, description = CapacityConstants.CAPACITY_CHANNELS_LOADED_SUCCESSFULLY, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ViewCapacityChannels.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> getAllCapacityChannels(@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId, 
			@Parameter @RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken) throws JsonProcessingException {
		jwtUtils.findUserDetail(accessToken);
		channelValidator.validate(null, OperationConstants.OPERATION_GET.getCode());
		return new ViewCapacityChannels(capacityChannelService.getReferenceData()).build(CapacityConstants.CAPACITY_CHANNELS_LOADED_SUCCESSFULLY, CapacityConstants.STATUS_CODE_200);
	}
	
	
	/**
	 * Method is used for DELETE operation for the respective concept. The request
	 * path variable channelId contains the id of combineCapacityChannel to be
	 * deleted. Before sending the request, DeleteCapacityChannelRequest is
	 * constructed with channelId and deleteConfirmed variable and validated for 
	 * whether a combined channel with the given channelId is present and whether the
	 * deleteConfirmed is true. Using the authorization bearer token
	 * value user name is retrieved.Both channelId and user detail
	 * is passed in capacity channel service and the deleted data is added to the
	 * audit service. At last the API response is built with successful response 
	 * and returned.
	 *
	 *
	 * @param channelId Id of combined channel to be deleted
	 * 
	 * @param deleteConfirmed String denoting whether delete action is confirmed
	 *
	 * @param accessToken                 Token used to authenticate the user and
	 *                                    extract the userDetails for this API.
	 *
	 * @return ServiceResponse class with information of the
	 *         Object containing Combine Channel detail.
	 *
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	@PatchMapping(value = CapacityConstants.CAPACITY_CHANNEL_WITH_CHANNELID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_202, description = CapacityConstants.CAPACITY_CHANNEL_DELETED, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_400, description = CapacityConstants.BAD_REQUEST, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = CapacityConstants.STATUS_CODE_405, description = CapacityConstants.METHOD_NOT_ALLOWED, content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))) })
	public ResponseEntity<Object> deleteCombinedChannel(
			@RequestHeader(name = CapacityConstants.HEADER_CONCEPT_ID, required = true) String conceptId,
			@PathVariable String channelId, @RequestParam String deleteConfirmed,
			@RequestHeader(name = CapacityConstants.AUTHORIZATION, defaultValue = CapacityConstants.BEARER_ACCESS_TOKEN, required = true) String accessToken)
			throws JsonProcessingException {

		String userDetail = jwtUtils.findUserDetail(accessToken);

		channelValidator.validate(new DeleteCapacityChannelRequest(channelId, deleteConfirmed),
				OperationConstants.OPERATION_DELETE.getCode());
		
		// service method to delete combined channel 
		String combinedChannelNm = capacityChannelService.deleteCombinedChannel(channelId, deleteConfirmed, userDetail);

		return new ServiceResponse()
				.build((CapacityConstants.Y.equals(deleteConfirmed) ? combinedChannelNm.concat(CapacityConstants.CAPACITY_CHANNEL_DELETED)
						: combinedChannelNm.concat(CapacityConstants.CAPACITY_MODEL_READY_FOR_DELETE)), CapacityConstants.STATUS_CODE_INT_202);

	}
}