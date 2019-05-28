package com.vastrak.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder // requiere @AllArgsConstructor
@Entity
@Table(name = "PATIENTS")
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long patient_id;
	@Column(length = 50)
	private String name;
	private Integer age;

	@Enumerated(EnumType.STRING) // Persist enum type property or field as a String
	@Column(length = 6)
	private Gender gender;

	
	// Esto crea una tabla con FK patient_id
	// No es una relación OneToMany
	@ElementCollection(fetch=FetchType.LAZY)  
    @CollectionTable(name = "BLOODPRESSURES", joinColumns = @JoinColumn(name = "patient_id"))
	@Column(name = "BPS")
	private List<BloodPressure> bloodPressures;

	@Embedded
	private PatientDetails patientDetails; // <- Embedded all its fields within the 'patients' table.

	@Override
	public boolean equals(Object o) {

		if ((o != null) && (o instanceof Patient)) {
			return (((Patient) o).patient_id) != null ? (((Patient) o).patient_id).equals(patient_id) : false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return patient_id.hashCode() + age.hashCode();
	}
	
	
}
