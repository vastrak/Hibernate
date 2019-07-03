package com.vastrak.hibernate006.model;

import static com.vastrak.hibernate006.util.Helper.executeSQLScript;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.hibernate006.util.HibernateUtil;

/**
 * 	I want to check the behavior of the query and not case by case. 
 *	This is not a complete test as it should be done. 
 * 
 * @author Christian
 *
 */

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

	@AfterClass
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

	// Using select
	@Test
	public void test001_CriteriaSelectAllFromTechnicians() {

		session = HibernateUtil.getSessionFactory().openSession();
		// Create an instance of CriteriaBuilder
		CriteriaBuilder builder = session.getCriteriaBuilder();
		// Create an instance of CriteriaQuery
		CriteriaQuery<Technician> criteria = builder.createQuery(Technician.class);
		// Select all from Technician
		Root<Technician> root = criteria.from(Technician.class);
		criteria.select(root);

		// Create an instance of org.hibernate.query.Query
		Query<Technician> query = session.createQuery(criteria);
		// Get results
		List<Technician> results = query.getResultList();

		assertThat(results).hasSize(3);

		aTech = results.get(0); // technician_id=1, name="Luis Uno Zelaya" ...

		logger.info(">>> Select All \n" + toLog(results));

		session.close();

	}

	// Select with parameters
	@Test
	public void test002_CriteriaSelectWithParametersTechnician() {

		session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Technician> criteria = builder.createQuery(Technician.class);
		Root<Technician> root = criteria.from(Technician.class);
		// add parameters
		criteria.select(root).where(builder.like(root.get("name"), builder.parameter(String.class, "name")));
		// set parameters
		List<Technician> results = session.createQuery(criteria).setParameter("name", aTech.getName()).getResultList();

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getName()).isEqualTo(aTech.getName());

		logger.info(">>> Select with parameter: " + toLog(results));

		session.close();

	}

	// Using Like
	@Test
	public void test003_CriteriaSelectWhereLikeTechnicians() {

		session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Technician> criteria = builder.createQuery(Technician.class);
		Root<Technician> root = criteria.from(Technician.class);
		criteria.select(root).where(builder.like(root.get("name"), "%Luis%")); // where like!

		List<Technician> results = session.createQuery(criteria).getResultList();

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getName()).isEqualTo(aTech.getName());

		logger.info(">>> Select Where Like \n" + toLog(results));

		session.close();
	}

	// Using notEquals
	@Test
	public void test004_CriteriaSelectWhereNotLikeTechnicians() {

		// boilerplate code
		session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Technician> criteria = builder.createQuery(Technician.class);
		Root<Technician> root = criteria.from(Technician.class);
		//
		criteria.select(root).where(builder.not(builder.like(root.get("name"), "%Luis%")));
		List<Technician> results = session.createQuery(criteria).getResultList();

		assertThat(results).hasSize(2);

		// expected result.
		// Technician(technician_id=2, name=José Dos Carrera, issues=[])
		// Technician(technician_id=3, name=Arturo Tres López, issues=[Issue(issue_id=2,
		
		logger.info(">>>> Where NOT Like \n" + toLog(results));

		session.close();

	}

	@Test
	public void test005_InnerJoinTechnicianIssue() {

		// boilerplate code
		session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Technician> criteria = builder.createQuery(Technician.class);
		Root<Technician> root = criteria.from(Technician.class);
		Join<Technician, Issue> issue = root.join("issues", JoinType.INNER);
		criteria.where(builder.equal(issue.get("issue_id"), 1L)); // <- filter issue_id = 1

		List<Technician> results = session.createQuery(criteria).getResultList();

		// expected result.
		// Technician(technician_id=1, name=Luis Uno Zelaya, issues=[Issue(issue_id=1...

		assertThat(results).hasSize(1);

		logger.info(">>>>> " + results.size());
		logger.info(">>>>> " + toLog(results));

		session.close();

	}

}
