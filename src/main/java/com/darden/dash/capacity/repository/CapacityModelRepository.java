package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityModelEntity;

/**
 * 
 * @author vraviran
 * @date 16-Jun-2022
 * 
 *  	 The purpose of this CapacityModelRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       CapacityModel table in database using the entity class
 *
 */
@Transactional
@Repository
public interface CapacityModelRepository extends JpaRepository<CapacityModelEntity, BigInteger>{
	
	/**
	 * This method is fetching for list of capacity Model entity 
	 * based on the value of conceptId
	 * 
	 * @param conceptId value from the header.
	 * 
	 * @return List<CapacityModelEntity> list of 
	 *  entity class containing the value of capacity
	 *  model.
	 */
	List<CapacityModelEntity> findByConceptId(BigInteger conceptId);

}
