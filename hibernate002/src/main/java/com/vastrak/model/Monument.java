package com.vastrak.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="monuments")
public class Monument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long monument_id;
	
	@Column(nullable = false, length = 50) // that is Varchar(50)  
	private String name;
	
	@Basic
	@Temporal(TemporalType.DATE)
	private Date built;

	// esto crea una tabla "founders" con los campos
	// "monument_id" (FK)
	// "listFounders" varchar(255)
	@ElementCollection(fetch=FetchType.LAZY) // recordar no cerrar la session
	@CollectionTable(						 // hasta que no se recuperen los elementos de la lista
	        name="founders",
	        joinColumns=@JoinColumn(name="monument_id")
	  )
	private List<String> listFounders;
	
	public Monument() {}

	public Monument(Long monument_id, String name, Date built, List<String> listFounders) {
		super();
		this.monument_id = monument_id;
		this.name = name;
		this.built = built;
		this.listFounders = listFounders;
	}

	public Long getMonument_id() {
		return monument_id;
	}

	public void setMonument_id(Long monument_id) {
		this.monument_id = monument_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBuilt() {
		return built;
	}

	public void setBuilt(Date built) {
		this.built = built;
	}

	public List<String> getListFounders() {
		return listFounders;
	}

	public void setListFounders(List<String> listFounders) {
		this.listFounders = listFounders;
	}

	public void addFounder(String funderName) {
		this.listFounders.add(funderName);
	}

	@Override
	public String toString() {
		return "Monument [monument_id=" + monument_id + ", name=" + name + ", built=" + built + "]";
	}
	
}
