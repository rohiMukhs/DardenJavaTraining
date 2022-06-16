package com.darden.dash.capacity.validator.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.capacity.service.impl.CapacityTemplateModelServiceImpl;
import com.darden.dash.capacity.validation.CapacityTemplateModelValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.exception.ApplicationException;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class CapacityTemplateModelValidatorTest {

	@InjectMocks
	private CapacityTemplateModelValidator capacityTemplateModelValidator;

	@Mock
	private CapacityTemplateModelService capacityTemplateModelService;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private ApplicationErrors applicationErrors;

	@Mock
	private CapacityTemplateRepo capacityTemplateRepo;
	
	@Mock
	private CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo;
	
	@InjectMocks
	private CapacityTemplateModelServiceImpl capacityTemplateModelServiceImpl;
	

	@Test
	void validateModelNmTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		List<CapacityModelEntity> capacityModelEntityList = new ArrayList<>();
		CapacityModelEntity request = new CapacityModelEntity();
		request.setCapacityModelNm("Test");
		capacityModelEntityList.add(request);
		CapacityModelRequest capacityModelRequest = new CapacityModelRequest();
		capacityModelRequest.setTemplateModelName("abc");
		List<TemplatesAssigned> templatesAssignedList = new ArrayList<>();
		TemplatesAssigned templatesAssigned = new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		templatesAssignedList.add(templatesAssigned);
		capacityModelRequest.setTemplatesAssigned(templatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		capacityModelRequest.setRestaurantsAssigned(restaurantsAssignedList);
		when(capacityTemplateModelService.validateModelTemplateNm(Mockito.any())).thenReturn(true);
		try {
			capacityTemplateModelValidator.validate(capacityModelRequest, OperationConstants.OPERATION_CREATE.getCode());
		} catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}

	@Test
	void validateBusinessDaysTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		List<CapacityModelEntity> capacityModelEntityList = new ArrayList<>();
		CapacityModelEntity request = new CapacityModelEntity();
		request.setCapacityModelNm("Test");
		capacityModelEntityList.add(request);
		CapacityModelRequest capacityModelRequest = new CapacityModelRequest();
		capacityModelRequest.setTemplateModelName("abc");
		List<TemplatesAssigned> templatesAssignedList = new ArrayList<>();
		TemplatesAssigned templatesAssigned = new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		templatesAssignedList.add(templatesAssigned);
		capacityModelRequest.setTemplatesAssigned(templatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.ONE);
		when(capacityTemplateRepo.findById(Mockito.any())).thenReturn(Optional.of(capacityTemplateEntity));
		when(capacityTemplateModelService.validateCapacityModelTemplateBusinessDates(Mockito.any())).thenReturn(true);
		capacityModelRequest.setRestaurantsAssigned(restaurantsAssignedList);
		when(capacityTemplateModelService.validateModelTemplateNm(Mockito.any())).thenReturn(false);
		try {
			capacityTemplateModelValidator.validate(capacityModelRequest, OperationConstants.OPERATION_CREATE.getCode());
		} catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}

	}


