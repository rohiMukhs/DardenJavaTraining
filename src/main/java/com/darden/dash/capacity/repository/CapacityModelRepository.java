package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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

	/**
	 * 
	 * This method is to get capacityModel name using provided capacityModelName
	 * 
	 * @param capacityModelNm CapacityModelName of the Capacity Template Entity.
	 * 
	 * @return List<CapacityModelEntity>List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityModelEntity> findByCapacityModelNmAndConceptId(String capacityModelNm, BigInteger conceptId);
	
	/**
	 * This repository method is to retrieve the optional value of capacity Model
	 * Entity based on the value of capacity model id, is deleted flag and concept
	 * id provided in header.
	 * 
	 * @param capacityModelId BigInteger contains the value of capacity model id to be
	 * 							fetched.
	 * 
	 * @param isdeletedflg String contains the value of delete status of data to be
	 * 							fetched
	 * 						
	 * @param conceptId String contains the value of concept id from header.
	 * 
	 * @return Optional<CapacityModelEntity> Optional of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	Optional<CapacityModelEntity> findByCapacityModelIdAndConceptId(BigInteger capacityModelId, BigInteger conceptId);
	
//	/**
//	 * This repository method is to retrieve the optional value of capacity Model
//	 * Entity based on the value of capacity model name and concept id provided in 
//	 * header.
//	 * 
//	 * @param capacityModelNm String contains the value of capacity model name to be
//	 * 							fetched.
//	 * 
//	 * @param conceptId String contains the value of concept id from header.
//	 * 
//	 * @return Optional<CapacityModelEntity> Optional of Capacity Template entity class retrieved 
//	 * 									   based on the parameters passed.
//	 */
//	Optional<CapacityModelEntity> findByCapacityModelNmAndConceptId(String capacityModelNm, BigInteger conceptId);
	
//	/**
//	 * This repository method is to retrieve the optional value of capacity Model
//	 * Entity based on the value of capacity model id and concept id provided in 
//	 * header.
//	 * @param ModelId  String containig the value of model id
//	 * @param conceptId containg the value from header concept id
//	 * @return
//	 */
//	Optional<CapacityModelEntity> findByCapacityModelIdAndConceptId(BigInteger modelId, BigInteger conceptId);
}
