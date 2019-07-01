package com.vastrak.hibernate006.model;

import static com.vastrak.hibernate006.util.Helper.executeSQLScript;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.hibernate006.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelTestCriteria {

	private static final Log logger = LogFactory.getLog(ModelTestCriteria.class);
	private static Session session = null;
	private static Technician aTech = null;

	
	/**
	 * Create all the entities and add a data to all of them.
	 */
	private static void createEntities() {

			Technician technician = new Technician(null, "Luis", null);
			Device device = new Device(null, "Machine A", new Date(), null);
			Issue issue = new Issue(null, new Date(), new Date(), null);
			IssueItem item = new IssueItem(null, IssueType.MECHANICAL, CodeType.ACODE, LevelType.MEDIUM, "Fail");
			issue.addIssueItem(item);
			technician.addIssue(issue);
			device.addIssue(issue);

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				Transaction tx = session.beginTransaction();
				session.save(device); // persist device
				session.save(technician); // persist technician
				tx.commit();
				session.close();
			} catch (Exception e) {
				logger.info(">>>> An exception has occurred " + e.getMessage());
				assertThat(e).isNull();
			}
	}
	
	@BeforeClass
	public static void createData() {
		
		Throwable thrown = catchThrowable(() -> {
			createEntities();
			executeSQLScript("src/test/resources/create-data.sql");
		});
		assertThat(thrown).isNull();
		
	}
	
	//@AfterClass 
	public static void cleanUp() {

		Throwable thrown = catchThrowable(() -> {
			executeSQLScript("src/test/resources/clean-up.sql");
		});
		assertThat(thrown).isNull();
		
	}
	
	//
	// Since Hibernate 5.2, the Hibernate Criteria API is deprecated
	// and new development is focused on the JPA Criteria API.
	//

	/**
	 * Helper to log a generic list
	 * 
	 * @param results List<T> elements.
	 * @return String
	 */
	private <T> String toLog(List<T> results) {

		StringBuilder sb = new StringBuilder(" ");
		for (T t : results) {
			sb.append(t.toString() + "\n");
		}
		return sb.toString();
	}

	@Test
	public void test001_CriteriaSelectAllFromTechnicians() {

		session = HibernateUtil.getSessionFactory().openSession();
		// Create an instance of CriteriaBuilder
		CriteriaBuilder cb = session.getCriteriaBuilder();
		// Create an instance of CriteriaQuery
		CriteriaQuery<Technician> cq = cb.createQuery(Technician.class);
		// Select all from Technician
		Root<Technician> root = cq.from(Technician.class);
		cq.select(root);

		// Create an instance of org.hibernate.query.Query
		Query<Technician> query = session.createQuery(cq);
		// Get results
		List<Technician> results = query.getResultList();

		assertThat(results).hasSize(3);

		aTech = results.get(0); // technician_id=1, name="Luis Uno Zelaya" ...

		logger.info(">>> Select All \n" + toLog(results));

		session.close();

	}
	
	@Test
	public void test002_CriteriaSelectWhereTechnicians() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Technician> cq = cb.createQuery(Technician.class);
		Root<Technician> root = cq.from(Technician.class);
		cq.select(root).where(cb.like(root.get("name"), "%Luis%"));  // where like!
		
		Query<Technician> query = session.createQuery(cq);
		List<Technician> results = query.getResultList();
		
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getName()).isEqualTo(aTech.getName());
		
		logger.info(">>> Select Where Like \n"+ toLog(results));
		
		session.close();
	}

}
