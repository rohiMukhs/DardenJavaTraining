package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;

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
	 * @param capacityChannelIdList
	 * @param conceptId
	 * @return List<CapacityChannelEntity>
	 */
	List<CapacityChannelEntity> findAllByCapacityChannelIdInAndConceptId(List<BigInteger> capacityChannelIdList, BigInteger conceptId);
	
	/**
	 * This method is used to fetch the value of capacity channel based on the value 
	 * of friendly name and concept id.
	 * 
	 * @param firendlynm
	 * @param conceptId
	 * @return CapacityChannelEntity
	 */
	CapacityChannelEntity findByFirendlyNmAndConceptId(String firendlynm, BigInteger conceptId);
}
