package com.darden.dash.capacity.service;

import java.util.List;

import com.darden.dash.capacity.model.CapacityModel;

/**
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 * 		 Service Implementation class which holds method definitions which deals
 *       with capacity Model activities or any business logic related to CapacityModel.
 *
 */
public interface CapacityTemplateModelService {

	/**
	 * This service method is written for the purpose of retrieving the 
	 * list of capacity model based on concept Id along with the valye of
	 * capacity template and restaurant assigned to the model.
	 * 
	 * @return List<CapacityModel> list of model class containing the value 
	 * of capacity models.
	 */
	public List<CapacityModel> getAllCapacityModels();
	
}
