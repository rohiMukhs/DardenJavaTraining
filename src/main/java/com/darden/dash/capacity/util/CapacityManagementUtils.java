package com.darden.dash.capacity.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.darden.dash.common.client.service.ConceptClient;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.model.Concept;
/**
 * 
 * @author skashala
 *
 */

@Component
public class CapacityManagementUtils {

	private ConceptClient conceptClient;

	@Autowired
	public CapacityManagementUtils(ConceptClient conceptClient) {
		this.conceptClient = conceptClient;
	}

	/**
	 * This method is a util method which has logic to check the validity of
	 * conceptId. If this conecptId valid check is made & exception being thrown by
	 * this method.
	 * 
	 * @param conceptId         - primary key of concept
	 * @param applicationErrors - applicationErrors object corresponding to which
	 *                          records have to picked from ErrorDetails table.
	 * @return boolean -Boolean flag which indicates if the string parameter passed
	 *         in the argument exists.
	 */
	public boolean validateConceptId(String conceptId, ApplicationErrors applicationErrors) {
		List<Concept> concepts = conceptClient.getAllConcepts();
		if (CollectionUtils.isEmpty(concepts)) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CONCEPT_ID);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		List<Concept> existingConcept = concepts.stream()
				.filter(concept -> concept.getConceptId() == Integer.parseInt(conceptId)).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(existingConcept)) {
			if (!ObjectUtils.isEmpty(applicationErrors)) {
				applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
						CapacityConstants.CONCEPT_ID);
				applicationErrors.raiseExceptionIfHasErrors();
			}
			return false;
		}
		return true;
	}
}
