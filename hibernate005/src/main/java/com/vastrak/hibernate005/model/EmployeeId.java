package com.vastrak.hibernate005.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;


/**
 * Composite Key in Hibernate:
 * Multiple ID Properties as a Class Identifier Type 
 * 
 * Note that Hibernate requires any ID class to implement the java.io.Serializable 
 * interface
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmployeeId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;   
	private String code;
}
