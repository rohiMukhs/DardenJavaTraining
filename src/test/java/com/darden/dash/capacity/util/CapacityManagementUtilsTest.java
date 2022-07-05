package com.darden.dash.capacity.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.model.ConceptForCache;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.exception.ApplicationException;

@ExtendWith({ MockitoExtension.class })
class CapacityManagementUtilsTest {

	@Mock
	private CapacityTemplateModelService capacityTemplateModelService;

	@InjectMocks
	private CapacityManagementUtils capacityManagementUtils;

	@Test
	void testValidateConceptId() {
		List<ConceptForCache> concepts = new ArrayList<>();
		ConceptForCache concept = new ConceptForCache();
		concept.setConceptId(1);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(capacityTemplateModelService.getCacheConceptData()).thenReturn(concepts);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		assertTrue(capacityManagementUtils.validateConceptId("1", applicationErrors));
	}
	
	@Test
	void testValidateConceptIdNegative() {
		List<ConceptForCache> concepts = new ArrayList<>();
		ConceptForCache concept = new ConceptForCache();
		concept.setConceptId(2);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(capacityTemplateModelService.getCacheConceptData()).thenReturn(concepts);
		assertFalse(capacityManagementUtils.validateConceptId("1", null));
	}
	
	@Test
	void testValidateConceptIdForError() {
		List<ConceptForCache> concepts = new ArrayList<>();
		ConceptForCache concept = new ConceptForCache();
		concept.setConceptId(2);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(capacityTemplateModelService.getCacheConceptData()).thenReturn(concepts);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		try {
			capacityManagementUtils.validateConceptId("1", applicationErrors);
		}catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}
	
	@Test
	void testValidateConceptIdForErrorConceptsEmpty() {
		when(capacityTemplateModelService.getCacheConceptData()).thenReturn(null);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		try {
			capacityManagementUtils.validateConceptId("1", applicationErrors);
		}catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}

}
