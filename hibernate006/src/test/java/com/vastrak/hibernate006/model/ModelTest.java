package com.vastrak.hibernate006.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.hibernate006.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelTest {

	private static final Log logger = LogFactory.getLog(ModelTest.class);
	private static Session session = null;

	@Test
	public void test001_CreateEntities() {

		// create a technician
		Technician technician = new Technician(null, "Luis", null);
		// create a device
		Device device = new Device(null, "Machine A", new Date(), null);
		// create an issue
		Issue issue = new Issue(null, new Date(), new Date(), null);
		// create an item for that issue
		IssueItem item = new IssueItem(null, IssueType.MECHANICAL, CodeType.ACODE, LevelType.MEDIUM, "Fail");
		// add the item into the issue
		issue.addIssueItem(item);
		// add issue to technician
		technician.addIssue(issue);
		// add the same issue to device
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

		assertThat(device.getDevice_id()).isNotNull();
		assertThat(technician.getTechnician_id()).isNotNull();
		assertThat(issue.getIssue_id()).isNotNull();
		assertThat(item.getIssueItem_id()).isNotNull();

	}

	/**
	 * Helper to execute a Sql script file. It is required that there is no blank
	 * jump between queries. Ignore comments.
	 * 
	 * @param sqlscript to execute
	 * @throws Exception
	 */
	private static void executeSQLScript(String sqlscript) throws Exception {

		// the script must be save in src/test/resources
		// sqlscript = "src/test/resources/mysqlscript.sql"
		String sqlstr = null;
		try (FileReader fw = new FileReader(sqlscript);
				BufferedReader br = new BufferedReader(fw);
				Session session = HibernateUtil.getSessionFactory().openSession();) {
			Transaction tx = session.beginTransaction();
			while ((sqlstr = br.readLine()) != null) {
				session.createSQLQuery(sqlstr).executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			logger.error(">>>>> Exception when executing script at line: " + sqlstr);
			throw e;
		}
		logger.info(">>>>> No problems with script: " + sqlscript);
		// session and files are closed
	}

	@Test
	public void test002_populateEntities() {

		Throwable thrown = catchThrowable(() -> {
			executeSQLScript("src/test/resources/create-data.sql");
		});
		assertThat(thrown).isNull();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test003_namedQueriesFindAll() {

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.getNamedQuery("technician.findAll");
		List<Technician> technicians = query.getResultList();

		assertThat(technicians.size()).isEqualTo(3);

		logger.info(">>>>> Technicians list size: " + technicians.size()); // must be 3
		session.close();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test004_namedQueriesFindByName() {

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.getNamedQuery("technician.findByName");
		String name = "Arturo Tres López";
		query.setParameter("name", name);
		List<Technician> technicians = query.getResultList();

		assertThat(technicians.get(0).getName()).isEqualTo(name);

		logger.info(">>>>> FindByName: " + name + "=" + technicians.get(0).getName());

		session.close();

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test005_nameQueriesFindLikeName() {

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.getNamedQuery("technician.findLikeName");
		String name = "Arturo";
		query.setParameter("name", "%" + name + "%");
		List<Technician> technicians = query.getResultList();

		assertThat(technicians.get(0).getName()).containsOnlyOnce(name);

		logger.info(">>>>> FindLikeName: " + name + "=" + technicians.get(0).getName());

		session.close();
	}

	@Test
	public void test006_findByDeployBetweenDates() throws ParseException {

		// prev. Parse throws ParseException
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date frmDate = format.parse("2019-03-29 00:00:00.0");
		Date endDate = format.parse("2019-05-01 00:00:00.0");
		//
		session = HibernateUtil.getSessionFactory().openSession();
		String hql = "from Device de where de.deploy between :frmDate and :endDate";
		org.hibernate.query.Query<Device> query = session.createQuery(hql, Device.class);
		query.setParameter("frmDate", frmDate);
		query.setParameter("endDate", endDate);
		// Comment require enable property name "use_sql_comments" set to true
		// in hibernate.cfg.xml
		query.setComment(">> HQL filter between dates ");
		List<Device> results = query.list(); // never return null

		assertThat(results.size()).isEqualTo(2);

		for (Device de : results) {
			logger.info(">> " + de.getName() + " " + de.getDeploy());
		}

		session.close();
	}
	
	/**
	 * Helper to get a String from the result of a query.
	 * 
	 * @param results list of array objects resulting from a query.
	 * @return
	 */
	private String toLog(List<Object[]> results) {

		StringBuilder sb = new StringBuilder("\n");
		for (Object[] row : results) {
			for (Object col : row) {
				sb.append(col + ", ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test007_projectionOnDevice() {

		// projection query with select clause
		String hql = "select upper(de.name), de.deploy from Device de";

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		List<Object[]> results = query.getResultList();

		assertThat(results.size()).isEqualTo(4); //

		logger.info(">>> "+toLog(results));

		session.close();
	}

	/**
	 * Helper to change String to Date type.
	 * @param strDate formatted "yyyy-MM-dd HH:mm:ss"
	 * @return a Date type.
	 */
	private Date toDate(String strDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date frmDate = format.parse(strDate);
			return frmDate;
		} catch (ParseException e) {
			return null;
		}
	}
	
	@Test
	public void test008_obtainingUniqueResultDevice() {

		// 
		Date frmDate = toDate("2019-04-01 00:00:00.0");

		String hql = "from Device de where de.deploy>:frmDate";

		session = HibernateUtil.getSessionFactory().openSession();
		org.hibernate.query.Query<Device> query = session.createQuery(hql, Device.class);
		query.setParameter("frmDate", frmDate);
		query.setMaxResults(1); // or method will throw a NonUniqueResultException
		Device device = query.uniqueResult(); // can return null, we need check this option

		assertThat(device).isNotNull();

		logger.info(String.format(">> name: %s, deploy %s", device.getName(), device.getDeploy()));

		session.close();

		// not necessarily Hibernate just to pick off the first result and return it
		// either set the maximum results of the HQL query to 1,
		// or obtain the first object from the result list.

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test009_innerJoinTechnicianIssue() {

		// return resultset: (Technician.name + Issue.created + Issue.closed) x all
		// issues
		String hql = "select te.name, iss.created, iss.closed from Technician te inner join te.issues as iss";

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		List<Object[]> results = query.getResultList();

		logger.info(">>> results= " + results.size());

		assertThat(results.size()).isEqualTo(9); // igual a la cantidad de registros de issue

		logger.info(">>> "+ toLog(results));

		session.close();

	}

	@Test
	@SuppressWarnings("unchecked")
	public void test10_innerJoinTechnicianIssueDeviceNativeSQL() {

		// return resultset: (technician.name + device.name + issue.created +
		// issue.closed)
		// inner joint with 3 tables using native sql query.

		// Si la columna repite propiedad [ej. name] se produce una excepción:
		// NonUniqueDiscoveredSqlAliasException
		// la solución es poner un alias a cada columna que pueda producir ambiguedad.
		String sql = "SELECT technicians.name AS te, devices.name AS de, issues.created, issues.closed "
				+ "FROM technicians INNER JOIN issues ON technicians.technician_id = issues.technician_id "
				+ "INNER JOIN devices ON issues.device_id = devices.device_id ORDER BY te, de";

		session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql);
		List<Object[]> results = query.getResultList();

		assertThat(results.size()).isEqualTo(9);
		// first row: Arturo Tres López, Máquina A, 2019-06-05 00:00:00.0, 2019-06-12 01:00:00.0
		assertThat(results.get(0)[0]).isEqualTo("Arturo Tres López");
		assertThat(results.get(0)[1]).isEqualTo("Máquina A");
		
		logger.info(">>> results= " + results.size()); // 9 rows
		logger.info(">>> " + toLog(results).toString()); // log result
		
		session.close();
	}
	
}
