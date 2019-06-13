package com.vastrak.hibernate004.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn(name = "emp_id") // Table per subclass strategy of inheritance
public class PermanentEmployee extends Employee {

	@Column(name = "salary")
	private Double salary;
	
	public PermanentEmployee() {}

	/**
	 * PermanentEmployee constructor, extends from Employee
	 * 
	 * @param id     : Employee
	 * @param name   : Employee
	 * @param salary : Permanent Employee
	 */
	public PermanentEmployee(Long id, String name, Double salary) {
		super(id, name);
		this.salary = salary;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {

		return "PermanentEmployee [ getId()=" + getId() + ", getName()=" + getName() + ", salary=" + salary + "]";
	}

}
