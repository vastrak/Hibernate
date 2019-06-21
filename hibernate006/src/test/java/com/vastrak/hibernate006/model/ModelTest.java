package com.vastrak.hibernate006.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

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
			session.save(device);  // persist device
			session.save(technician); // persit technician 
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

}
