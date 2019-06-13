package com.vastrak.hibernate004.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn(name="emp_id")  // Table per subclass strategy of inheritance
public class ContractualEmployee extends Employee {
	
	@Column(name="hourly_rate")
	private Double hourlyRate;
	@Column(name="contract_period")
	private Double contractPeriodo;

	
	public ContractualEmployee() {}
	
	/**
	 * ContractualEmployee constructor, extends from Employee
	 * 
	 * @param id				:Employee
	 * @param name				:Employee
	 * @param hourlyRate		:ContractualEmployee
	 * @param contractPeriodo   :ContractualEmployee
	 */
	public ContractualEmployee(Long id, String name, Double hourlyRate, Double contractPeriodo) {
		super(id, name);
		this.hourlyRate = hourlyRate;
		this.contractPeriodo = contractPeriodo;
	}
	
	public Double getHourlyRate() {
		return hourlyRate;
	}
	
	public void setHourlyRate(Double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	
	public Double getContractPeriodo() {
		return contractPeriodo;
	}
	
	public void setContractPeriodo(Double contractPeriodo) {
		this.contractPeriodo = contractPeriodo;
	}

	@Override
	public String toString() {
		return "ContractualEmployee [ getId()="+ getId() + ", getName()=" + getName() +", hourlyRate=" + hourlyRate + ", contractPeriodo=" + contractPeriodo +  "]";
	}
	
}
