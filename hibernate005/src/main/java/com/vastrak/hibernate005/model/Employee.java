package com.vastrak.hibernate005.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name="EMPLOYEES")
@IdClass(EmployeeId.class)  // <- Composite-id Class
public class Employee {

	@Id
	private String name;  // Composite-id Class uses @Ids  
	@Id
	private String code;
	@Column
	private Double salary;
}
