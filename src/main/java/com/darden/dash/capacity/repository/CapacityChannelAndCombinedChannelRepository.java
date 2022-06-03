package com.darden.dash.capacity.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelPK;

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

}
