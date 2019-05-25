package com.vastrak.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PatientDetails {
	
	@Column(length = 100)
	private String address;
	@Column(length = 15)
	private String cellPhone1;
	@Column(length = 15)
	private String cellPhone2;
	
}
