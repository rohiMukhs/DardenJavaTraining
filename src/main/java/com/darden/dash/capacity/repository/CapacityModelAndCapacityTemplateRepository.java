package com.darden.dash.capacity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;

/**
 * 
 * @author vraviran
 *
 *		 The purpose of this CapacityModelAndCapacityTemplateRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       CapacityModelAndCapacityTemplate table in database using the entity class
 */
@Transactional
@Repository
public interface CapacityModelAndCapacityTemplateRepository extends JpaRepository<CapacityModelAndCapacityTemplateEntity, CapacityModelAndCapacityTemplatePK>{
	
	/**
	 * This method is used to fetch the value of  CapacityModelAndCapacityTemplateEntity 
	 * based on the value of capacityTemplateEntity.
	 * 
	 * @param capacityTemplate Capacity Template entity of the Capacity Channel
	 *						   And CapacityTemplate Entity.
	 * 
	 * @return CapacityModelAndCapacityTemplateEntity Capacity Channel And Capacity Template  Entity
	 * 					 class retrieved based on the parameters passed.
	 */
	List<CapacityModelAndCapacityTemplateEntity> findByCapacityTemplate(CapacityTemplateEntity capacityTemplate);
	
	/**
	 * This method is used to fetch list of  Capacity Channel And 
	 * Capacity Template Entity based on the value of combine capacity 
	 * model.
	 * 
	 * @param capacitymodel entity class containing the value of capacity
	 * 				model.
	 * 
	 * @return List<CapacityModelAndCapacityTemplateEntity> returned list
	 * 		of entity class with the value of model and template assigned.
	 */
	List<CapacityModelAndCapacityTemplateEntity> findByCapacityModel(CapacityModelEntity capacitymodel);
	
	/**
	 * This repository method is to deleted the value of Capacity Channel And 
	 * Capacity Template Entity based on the value of combine capacity 
	 * model.
	 * 
	 * @param capacitymodel entity class containing the value of capacity
	 * 				model.
	 */
	void deleteByCapacityModel(CapacityModelEntity capacitymodel);
	
	/**
	 * his repository method is to deleted the value of Capacity Model And CapacityTempla Entity based on the value of 
	 * capacityModelEntity 
	 * mode
	 * @param capacityModelEntity
	 */
	void deleteAllByCapacityModel(CapacityModelEntity capacityModelEntity);
	
}
