package com.darden.dash.capacity.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.darden.dash.capacity.model.ConceptForCache;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.error.ApplicationErrors;
/**
 * 
 * @author skashala
 *
 */

@Component
public class CapacityManagementUtils {

	private CapacityTemplateModelService capacityTemplateModelService;
	/**
	 * Autowiring.
	 * 
	 * @param capacityTemplateModelService
	 */
	@Autowired
	public CapacityManagementUtils(CapacityTemplateModelService capacityTemplateModelService) {
		this.capacityTemplateModelService = capacityTemplateModelService;
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
		List<ConceptForCache> concepts = capacityTemplateModelService.getCacheConceptData();
		if (CollectionUtils.isEmpty(concepts)) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012),
					CapacityConstants.CONCEPT_ID);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		List<ConceptForCache> existingConcept = concepts.stream()
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
