package com.vastrak.hibernate004.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.hibernate004.util.HibernateUtil;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelTest {
	
	private static final Log logger = LogFactory.getLog(ModelTest.class);

	private Session session = null;
	private static PermanentEmployee pempLuis = null;
	private static ContractualEmployee cempCarlos = null;
	
	@Test
	public void Test001_SaveEmployees() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		
		pempLuis = new PermanentEmployee(null, "Luis", 5600.00);
		PermanentEmployee pempJose = new PermanentEmployee(null, "Jose", 3700.00);

		cempCarlos = new ContractualEmployee(null, "Carlos", 10.5, 160.00);
		
		Transaction tx = session.beginTransaction();
		session.save(pempLuis);
		session.save(pempJose);
		session.save(cempCarlos);
		tx.commit();
		
		logger.info(">>>> save: "+pempLuis);   // notar que asigna id
		logger.info(">>>> save: "+pempJose);
		logger.info(">>>> save: "+cempCarlos);
		
		session.close();		
		
		assertThat(pempLuis.getId()).isNotNull();  // tiene que tener id asignado!
		assertThat(cempCarlos.getId()).isNotNull();
		
	}
	
	@Test
	public void Test002_RetrieveEmployees() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		// find Luis, he have a empIdLuis id number
		// This method never returns an uninitialized instance
		// Don't use load, You should not use load method to determine if an instance exists 
		// (use get() instead). 
		// Use load only to retrieve an instance that you assume exists, 
		// where non-existence would be an actual error.
		PermanentEmployee found = session.get(PermanentEmployee.class, pempLuis.getId());
		
		logger.info(">>>> found: "+found);
		
		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(pempLuis.getId()); // from employee
		assertThat(found.getSalary()).isEqualTo(pempLuis.getSalary()); // from permanentemployee
		
		session.close();
	}
	
	
	@Test
	public void Test003_UpdateEmployees() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		ContractualEmployee found = session.get(ContractualEmployee.class, cempCarlos.getId()); // id persist in Employee

		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo(cempCarlos.getName());
		
		// we try to change name to Lucho
		
		found.setName("Lucho");
		Transaction tx = session.beginTransaction();
		session.save(found);
		tx.commit();
		
		session.close();
		
		session = HibernateUtil.getSessionFactory().openSession();
		cempCarlos = session.get(ContractualEmployee.class, cempCarlos.getId()); // id persist in Employee
		
		assertThat(cempCarlos).isNotNull();
		assertThat(cempCarlos.getName()).isEqualTo("Lucho");
		
		session.close();
	}
	
	@Test
	public void test004_DeleteEmployees() {
		
		// delete record Carlos (or Lucho)
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.delete(cempCarlos);
		tx.commit();
		session.close();
		// check that
		session = HibernateUtil.getSessionFactory().openSession();
		ContractualEmployee found = session.get(ContractualEmployee.class, cempCarlos.getId()); // id persist in Employee		
		
		assertThat(found).isNull();
		
	}
	
	
	
}
