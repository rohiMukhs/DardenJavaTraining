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

import com.darden.dash.common.client.service.ConceptClient;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.exception.ApplicationException;
import com.darden.dash.common.model.Concept;

@ExtendWith({ MockitoExtension.class })
class CapacityManagementUtilsTest {

	@Mock
	private ConceptClient conceptClient;

	@InjectMocks
	private CapacityManagementUtils capacityManagementUtils;

	@Test
	void testValidateConceptId() {
		List<Concept> concepts = new ArrayList<>();
		Concept concept = new Concept();
		concept.setConceptId(1);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(conceptClient.getAllConcepts()).thenReturn(concepts);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		assertTrue(capacityManagementUtils.validateConceptId("1", applicationErrors));
	}
	
	@Test
	void testValidateConceptIdNegative() {
		List<Concept> concepts = new ArrayList<>();
		Concept concept = new Concept();
		concept.setConceptId(2);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(conceptClient.getAllConcepts()).thenReturn(concepts);
		assertFalse(capacityManagementUtils.validateConceptId("1", null));
	}
	
	@Test
	void testValidateConceptIdForError() {
		List<Concept> concepts = new ArrayList<>();
		Concept concept = new Concept();
		concept.setConceptId(2);
		concept.setConceptName("Test");
		concepts.add(concept);
		when(conceptClient.getAllConcepts()).thenReturn(concepts);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		try {
			capacityManagementUtils.validateConceptId("1", applicationErrors);
		}catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}
	
	@Test
	void testValidateConceptIdForErrorConceptsEmpty() {
		when(conceptClient.getAllConcepts()).thenReturn(null);
		ApplicationErrors applicationErrors = new ApplicationErrors();
		try {
			capacityManagementUtils.validateConceptId("1", applicationErrors);
		}catch (Exception e) {
			assertTrue(e instanceof ApplicationException);
		}
	}

}
