package com.darden.dash.capacity.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.ReferenceEntity;
import java.util.List;

/**
 * 
 * @author vraviran
 * @date 19-May-2022
 * 
 * 
 * 		 The purpose of this ReferenceRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       Reference table in database using the entity class
 */
@Transactional
@Repository
public interface ReferenceRepository extends JpaRepository<ReferenceEntity, BigInteger>{
	
	List<ReferenceEntity> findByConceptId(BigInteger conceptid);

}
