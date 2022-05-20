package com.darden.dash.capacity.service;

import org.springframework.http.ResponseEntity;
/**
 * 
 * @author skashala
 * @date 16-May-2022
 * 
 *		 Service Implementation class which holds method definitions which deals
 *       with capacity template activities or any business logic related to CapacityTemplate
 *       
 */

public interface CapacityManagementService {
	
	/**This method is to get All capacity Template records.
	 * 
	 * @return ResponseEntity
	 */

	ResponseEntity<Object> getAllCapacityTemplates();

}
