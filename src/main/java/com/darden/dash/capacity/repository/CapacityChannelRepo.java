package com.darden.dash.capacity.repository;

import java.math.BigInteger;

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

}
