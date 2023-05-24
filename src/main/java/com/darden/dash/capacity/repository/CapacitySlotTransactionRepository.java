package com.darden.dash.capacity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotTransactionEntity;

/**
 * 
 * @author vraviran
 * 
 * The purpose of this CapacitySlotTransactionRepository interface is to extend the
 * properties of JpaRepository to perform the CRUD operation on the
 * capacity Slot transactrion table in database using the entity class.
 *
 */
@Repository
public interface CapacitySlotTransactionRepository extends JpaRepository<CapacitySlotTransactionEntity, String> {
	
	/**
	 * finds by list of channel entities and bussiness date.
	 * 
	 * @param capacityChannel contains list of channel entities.
	 * 
	 * @param businessDate contains bussiness date data.
	 * 
	 * @return List<CapacitySlotTransactionEntity> contains slot transaction entities.
	 */
	List<CapacitySlotTransactionEntity> findByCapacityChannelInAndBusinessDate(List<CapacityChannelEntity> capacityChannel, LocalDate businessDate);
	
	/**
	 * finds by channel entity and bussiness date.
	 * 
	 * @param capacityChannel contains channel entity.
	 * 
	 * @param businessDate contains bussiness date data.
	 * 
	 * @return List<CapacitySlotTransactionEntity> contains slot transaction entities.
	 */
	List<CapacitySlotTransactionEntity> findByCapacityChannelAndBusinessDate(CapacityChannelEntity capacityChannel, LocalDate businessDate);
	
}