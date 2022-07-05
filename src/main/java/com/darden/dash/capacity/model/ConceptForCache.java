package com.darden.dash.capacity.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This model class is written for the purpose of storing 
 * the value of cached concept data.
 *
 */
@Setter
@Getter
public class ConceptForCache implements Serializable {
	
	private Integer conceptId;
    private String conceptName;

}
