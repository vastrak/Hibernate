package com.vastrak.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable 
public class BloodPressure {
	
	private Integer systolic;  // upper#
	private Integer diastolic; // lower#
	private Integer bpm;       // beats per minute 
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

}
