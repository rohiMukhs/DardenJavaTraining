package com.darden.dash.capacity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;

/**
 * 
 * @author vraviran
 * 
 * 		 The purpose of this CapacityModelAndLocationRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       CapacityModelAndLocation table in database using the entity class
 *
 */
@Transactional
@Repository
public interface CapacityModelAndLocationRepository extends JpaRepository<CapacityModelAndLocationEntity, CapacityModelAndLocationPK>{
	
	/**
	 * This method is used to fetch list of  Capacity Channel And 
	 * location Entity based on the value of combine capacity 
	 * model.
	 * 
	 * @param capacitymodel entity class containing the value of capacity
	 * 				model.
	 * 
	 * @return List<CapacityModelAndLocationEntity> returned list
	 * 		of entity class with the value of model and location assigned.
	 */
	List<CapacityModelAndLocationEntity> findByCapacityModel(CapacityModelEntity capacitymodel);

}
