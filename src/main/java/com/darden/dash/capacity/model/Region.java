package com.darden.dash.capacity.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *       values of Region from client call.
 *
 */
@Getter
@Setter
public class Region implements Serializable
{
private String regionName;
private Integer regionId;
}
