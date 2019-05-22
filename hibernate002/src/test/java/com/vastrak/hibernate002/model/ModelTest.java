package com.vastrak.hibernate002.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.model.Monument;
import com.vastrak.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelTest {

	private static final Log logger = LogFactory.getLog(ModelTest.class);

	private Session session = null;
	private static Long id = null;
	
	/**
	 * Convierte en Date un String del tipo dd/MM/yyyy
	 * @param dateString ej. "14/04/1865"
	 * @return Date o null si hay un error en el formato de la fecha 
	 * 
	 */
	private Date getDate(String dateString) {
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.parse(dateString);
		} catch (ParseException ex) {
			logger.error("Error getting Calendar with string " + dateString + " " + ex.getMessage());
			return null;
		}
	}

	
	/**
	 * Retorna un String con los datos concatenados de una lista
	 * <E> debería tener implementado toString();
	 * @param list<E> 
	 * @return String con los elementos de la lista
	 */
	private <E> String toStringList(List<E> list) {
		StringBuilder sb = null;
		int i = 0;
		for(E e: list) {
			if(sb==null) {
				sb = new StringBuilder();
			}
			sb.append((++i)+":"+e.toString()+" ");
		}
		return sb.toString().trim();
	}
	
	
	@Test
	public void test001_SaveAndRetrieveMonuments() {

		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<String> listFounders = new ArrayList<>();
		listFounders.add("Luis");
		listFounders.add("Jorge");
		listFounders.add("Susana");
		Monument monument = new Monument(null, "Memorial", getDate("17/03/1954"), listFounders);
				
		// persitimos la instancia de Monument
		try {
			tx = session.beginTransaction();
			session.save(monument);
			id = monument.getMonument_id();
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) {
				logger.info("Rollback!");
				tx.rollback();
			}
			throw he;
		} finally {
			session.close();
		}
		
		// recuperamos la instancia
		session = HibernateUtil.getSessionFactory().openSession();
		Monument foundMonument = session.load(Monument.class, id);
		logger.info(">>>>> "+toStringList(foundMonument.getListFounders()));

		
		assertThat(foundMonument).isNotNull();
		assertThat(foundMonument).hasNoNullFieldsOrProperties();
		assertThat(foundMonument.getMonument_id()).isEqualByComparingTo(id);
		assertThat(foundMonument.getListFounders()).hasSize(3);
		assertThat(foundMonument.getListFounders()).contains("Jorge");
		
		session.close(); // <- close al final, sino los objetos de la lista estarán detached 
		// y no podrán ser accesidos.
		// org.hibernate.LazyInitializationException: could not initialize proxy
	}
	
	/**
	 * Se necesita que los valores de la lista implementen equals y hashCode
	 * 
	 */
	@Test
	public void test002_removeFounder() {
		
		// recupero el Monumento
		session = HibernateUtil.getSessionFactory().openSession();
		Monument foundMonument = session.load(Monument.class, id); //<- id del test anterior

		// comprueblo que el fundador está en la lista
		assertThat(foundMonument.getListFounders()).contains("Jorge");
		
		// REMOVE, lo quito de la lista
		boolean removed = foundMonument.getListFounders().remove("Jorge");
		assertThat(removed).isTrue();
		
		// guardo el monumento sin el fundador en la lista.
		Transaction tx = session.beginTransaction();
		session.save(foundMonument);	
		tx.commit();
		session.close();
		
		// abro una nueva session y compruebo que se eliminó el fundador.
		session = HibernateUtil.getSessionFactory().openSession();
		foundMonument = session.load(Monument.class, id); //<- id del test anterior		
		assertThat(foundMonument.getListFounders()).doesNotContain("Jorge");
		
		session.close(); // <- close al final, sino los objetos de la lista estarán detached 
		// y no podrán ser accesidos.
		// org.hibernate.LazyInitializationException: could not initialize proxy
	}

}
