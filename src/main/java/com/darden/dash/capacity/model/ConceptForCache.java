package com.darden.dash.capacity.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConceptForCache implements Serializable {
	
	private Integer conceptId;
    private String conceptName;

}
