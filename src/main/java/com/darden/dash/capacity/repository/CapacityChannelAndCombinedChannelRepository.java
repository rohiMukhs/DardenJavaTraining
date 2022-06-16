package com.darden.dash.capacity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelPK;
import com.darden.dash.capacity.entity.CapacityChannelEntity;

/**
 * 
 * @author vraviran
 * 
 * 	The purpose of this CapacityChannelAndCombinedChannelRepository interface 
 * 	is to extend the properties of JpaRepository to perform the CRUD operation on the
 *  Capacity Channel And Combined Channel table in database using the entity class
 *
 */
@Transactional
@Repository
public interface CapacityChannelAndCombinedChannelRepository extends JpaRepository<CapacityChannelAndCombinedChannelEntity, CapacityChannelAndCombinedChannelPK>{
	
	/**
	 * This method is used to fetch list of  Capacity Channel And 
	 * Combined Channel Entity based on the value of combine capacity 
	 * channel.
	 * 
	 * @param capacityChannel2 contains the value of combine channel.
	 * 
	 * @return List<CapacityChannelAndCombinedChannelEntity> returns list
	 * 			of entity class with channel and combined channel data.
	 */
	List<CapacityChannelAndCombinedChannelEntity> findByCapacityChannel2(CapacityChannelEntity capacityChannel2);
}
