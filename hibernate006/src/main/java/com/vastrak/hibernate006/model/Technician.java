package com.vastrak.hibernate006.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "TECHNICIANS")
@NamedQueries({ @NamedQuery(name = "technician.findAll", query = "from Technician te"),
		@NamedQuery(name = "technician.findByName", query = "from Technician te where te.name=:name"),
		@NamedQuery(name = "technician.findLikeName", query = "from Technician te where te.name like :name")
		})  // JPQL names queries
public class Technician implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long technician_id;
	@Column(nullable = false, length = 50)
	private String name;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "technician_id") // se crea una FK en la tabla ISSUES con referencia a TECHNICIANS
	private List<Issue> issues = new ArrayList<>();

	/**
	 * Helper methods to update unidirectional associations Add an issue to the
	 * entity
	 * 
	 * @param issue
	 */
	public void addIssue(Issue issue) {
		if (issues == null) {
			issues = new ArrayList<>();
		}
		issues.add(issue);
	}

	/**
	 * Helper methods to update unidirectional associations Remove an issue from the
	 * entity
	 * 
	 * @param issue
	 */
	public void removeIssue(Issue issue) {
		if (issues == null) {
			issues = new ArrayList<>();
		}
		issues.remove(issue);
	}

}
