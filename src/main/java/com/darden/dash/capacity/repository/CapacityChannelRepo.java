package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityChannelEntity;

/**
 * 
 * @author skashala
 * @date 16-May-2022
 * 
 * 
 * The purpose of this CapacityChannelrepo interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       capacity channel table in database using the entity class
 */

@Transactional
@Repository
public interface CapacityChannelRepo extends JpaRepository<CapacityChannelEntity, BigInteger> {
	
	/**
	 * This method is used to fetch list of  capacity channels based on the value
	 * of capacity channel id and concept id.
	 * 
	 * @param capacityChannelIdList List of Id of the Capacity Channel Entity.
	 * 
	 * @param conceptId ConceptId of the capacity channel entity to be retrieved.
	 * 
	 * @return List<CapacityChannelEntity> List of Capacity Channel entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityChannelEntity> findAllByCapacityChannelIdInAndConceptId(List<BigInteger> capacityChannelIdList, BigInteger conceptId);
	
	/**
	 * This method is used to fetch the value of capacity channel based on the value 
	 * of friendly name and concept id.
	 * 
	 * @param friendlynm  Friendly name of the Capacity Channel Entity.
	 * 
	 * @param conceptId  ConceptId of the Capacity Channel Entity.
	 * 
	 * @return CapacityChannelEntity Capacity Channel entity class retrieved 
	 * 									   based on the parameters passed. 
	 */
	CapacityChannelEntity findByPosNameAndConceptId(String firendlynm, BigInteger conceptId);
	
	/**
	 * This method is used to fetch the value of capacity channel based on the value 
	 * of capacityChannel name and concept id.
	 * 
	 * @param capacityChannelNm Capacity Channel name of the Capacity Channel Entity.
	 * 
	 * @param conceptId ConceptId of the Capacity Channel Entity.
	 * 
	 * @return Optional<CapacityChannelEntity> Capacity Channel entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	Optional<CapacityChannelEntity> findByCapacityChannelNmAndConceptId(String capacityChannelNm, BigInteger conceptId);
	
	/**
	 * This method is used to fetch the value of list of capacity channel based on the value 
	 * concept id.
	 * 
	 * @param conceptId ConceptId of the Capacity Channel Entity.
	
	 */

	List<CapacityChannelEntity> findByConceptId(BigInteger conceptId);
	
	
}
