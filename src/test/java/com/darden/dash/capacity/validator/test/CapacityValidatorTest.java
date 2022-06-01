package com.darden.dash.capacity.validator.test;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class CapacityValidatorTest {
	
	@InjectMocks
	private CapacityValidator capacityValidator;
	
	@Mock
	private CapacityManagementService CapacityManagementService;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private ApplicationErrors applicationErrors;
	
	@Test
    void validateInDbForDeleteTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		DeleteCapacityTemplateRequest deleteRq = new DeleteCapacityTemplateRequest("99", "Y");
		Mockito.when(CapacityManagementService.validateCapacityTemplateId(deleteRq.getTemplateId())).thenReturn(false);
		capacityValidator.validate(deleteRq, OperationConstants.OPERATION_DELETE.getCode());
		assertNotNull(deleteRq);
    }
	
	@Test
	void validateInDbForCreateTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		request.setCapacityTemplateName("aaa");
		request.setConceptId(new BigInteger("1"));
		Mockito.when(CapacityManagementService.validateCapacityTemplateNm(request.getCapacityTemplateName())).thenReturn(false);
		capacityValidator.validate(request, OperationConstants.OPERATION_CREATE.getCode());
		assertNotNull(request);
	}

}
