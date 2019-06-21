package com.vastrak.hibernate006.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "DEVICES")
public class Device implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long device_id;
	@Column(nullable=false, length=50)
	private String name;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date deploy;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "device_id") // se crea una FK en la tabla ISSUE con referencia a DEVICES
	private List<Issue> issues = new ArrayList<>();
	
	/**
	 * Helper methods to update unidirectional associations
	 * Add an issue torom to the entity
	 * @param issue
	 */
	public void addIssue(Issue issue) {
		if(issues == null) {
			issues = new ArrayList<>();
		}
		issues.add(issue);
	}
	
	/**
	 * Helper methods to update unidirectional associations
	 * Remove an issue from the entity
	 * @param issue
	 */
	public void removeIssue(Issue issue) {
		if(issues == null) {
			issues = new ArrayList<>();
		}
		issues.remove(issue);
	}	

	
}

