package com.darden.dash.capacity.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
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
	CapacityModelAndCapacityTemplateEntity findByCapacityTemplate(CapacityTemplateEntity capacityTemplate);
	
}
