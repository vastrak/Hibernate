package com.vastrak.hibernate005.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.hibernate005.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelTest {

	private static final Log logger = LogFactory.getLog(ModelTest.class);
	private static Session session = null;
	private static final String NAME = "Luis";
	private static final String CODE = "LE0012A";

	@Test 
	public void test001_createEmployees() {

		Employee Luis = new Employee(NAME, CODE, 4560.0);

		// create
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(Luis);
		tx.commit();
		session.close();
		//
		// retrieve require two steps
		// specify the ID with an instance of EmployeeId and get that.
		session = HibernateUtil.getSessionFactory().openSession();
		EmployeeId employeeId = new EmployeeId(NAME, CODE); // <- create key!
		Employee foundEmployee = (Employee) session.get(Employee.class, employeeId);
		session.close();
		
		logger.info(">>>> "+foundEmployee);
		
		assertThat(foundEmployee).isNotNull().isEqualTo(Luis);
		
	}	

	
	@Test (expected = javax.persistence.PersistenceException.class)
	public void test002_ConstraintViolationException() {

		try(Session sn = HibernateUtil.getSessionFactory().openSession()) {
			Employee falseEmployee = new Employee(NAME, CODE, 7852.0); // or trying to save Luis
			Transaction tx = sn.beginTransaction();
			sn.save(falseEmployee); // // trying to save Luis 
			tx.commit();
		} catch (Exception e) {
			throw e;
		}

	}

	@Test
	public void test003_updateEmployee() {
		
		// update salary
		session = HibernateUtil.getSessionFactory().openSession();
		EmployeeId employeeId = new EmployeeId(NAME, CODE);
		Employee foundEmployee = (Employee) session.get(Employee.class, employeeId);
		foundEmployee.setSalary(15050.0);
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(foundEmployee);
		Employee updatedEmployee = foundEmployee;
		tx.commit();
		
		// retrieve
		foundEmployee = (Employee) session.get(Employee.class, employeeId);
		session.close();
		
		assertThat(foundEmployee).isNotNull().isEqualTo(updatedEmployee);
		
	}
	
	@Test
	public void test004_deleteEmployee() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		EmployeeId employeeId = new EmployeeId(NAME, CODE);
		Employee foundEmployee = (Employee) session.get(Employee.class, employeeId);
		session.delete(foundEmployee);
		tx.commit();
		// retrieve and check
		assertThat((Employee) session.get(Employee.class, employeeId)).isNull();
		session.close();
	}
	
	
}
