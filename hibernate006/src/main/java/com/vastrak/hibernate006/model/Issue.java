package com.vastrak.hibernate006.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "ISSUES")
public class Issue implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long issue_id;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date closed;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "issue_id") // se crea una FK en la tabla ISSUEITEMS con referencia a ISSUES
	private List<IssueItem> issueItems = new ArrayList<>();
		
	/**
	 * Helper methods to update unidirectional associations
	 * Add an IssueItem to entity
	 * @param issueItem
	 */
	public void addIssueItem(IssueItem issueItem) {
		
		if(issueItems == null) {
			issueItems = new ArrayList<>();
		}
		issueItems.add(issueItem);
	}
	
	/**
	 * Helper methods to update unidirectional associations
	 * Remove an IssueItem from entity
	 * @param issueItem
	 */
	public void removeIssueItem(IssueItem issueItem) {
		if(issueItems == null) {
			issueItems = new ArrayList<>();
		}
		issueItems.remove(issueItem);
	}
	
}
