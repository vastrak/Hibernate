package com.vastrak.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.util.HibernateUtil;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PatientTest {
	
	private static final String NAME = "Luis";
	private static final String ADDRESS = "Ave. St. Patrick";
	private static final String CELL1 = "999.99.99.99";
	private static final String CELL2 = "888.88.88.88";
	private static final Integer AGE = 25;
	private static final Gender GENDER = Gender.MALE;
	private static final Integer SYSTOLIC = 140;
	private static final Integer DIASTOLIC = 95;
	private static final Integer BPM = 78;
	private static final Date DATE = new Date();
	private static final Log logger = LogFactory.getLog(PatientTest.class);
	private static Patient patientLuis;
	private static Long id;  
	private Session session;
	
	@Test
	public void test001_RreatePatient_And_Read() {

		// set a new patient, transient instance
		PatientDetails patientDetailsLuis = new PatientDetails(ADDRESS, CELL1, CELL2);
		List<BloodPressure> listBPLuis = new ArrayList<>();
		listBPLuis.add(new BloodPressure(SYSTOLIC, DIASTOLIC, BPM, DATE));
		patientLuis = new Patient.PatientBuilder().patient_id(null) // new patients have null id
				.name(NAME)
				.age(AGE)
				.gender(GENDER)
				.bloodPressures(listBPLuis)
				.patientDetails(patientDetailsLuis)
				.build();
		
		// Persist the new patient
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(patientLuis);
		id = patientLuis.getPatient_id(); // we have a new id != null
		tx.commit();
		session.close();
		
		// Find the new patient
		session = HibernateUtil.getSessionFactory().openSession();
		Patient found = session.load(Patient.class, id);
		
		logger.info(">>>>>>>>>> original date: "+DATE.getTime()+" retrieved date: "+(found.getBloodPressures().get(0).getDate().getTime()));
		
		assertThat(found).isNotNull();
		assertThat(found.getPatient_id()).isEqualTo(id);
		assertThat(found.getName()).isEqualTo(NAME);
		assertThat(found.getPatientDetails()).isEqualTo(new PatientDetails(ADDRESS, CELL1, CELL2));
		assertThat(found.getBloodPressures().get(0).getSystolic()).isEqualTo(SYSTOLIC);
		assertThat(found.getBloodPressures().get(0).getDiastolic()).isEqualTo(DIASTOLIC);
		assertThat(found.getBloodPressures().get(0).getBpm()).isEqualTo(BPM);
		
		session.close();
	}
	
	@Test
	public void test002_UpdatePatient() {
		
		final int NEWAGE = 35;
		final String NEWADDRESS = "Ave. Paul";
		final int NEWSYSTOLIC = 135;
	
		session = HibernateUtil.getSessionFactory().openSession();
		Patient oldPatient = session.load(Patient.class, id);
		Transaction tx = session.beginTransaction();
		oldPatient.setAge(NEWAGE);
		oldPatient.getPatientDetails().setAddress(NEWADDRESS);
		oldPatient.getBloodPressures().get(0).setSystolic(NEWSYSTOLIC);
		session.save(oldPatient);
		tx.commit();
		session.close();
		
		session = HibernateUtil.getSessionFactory().openSession();
		Patient found = session.load(Patient.class, id);
		
		assertThat(found).isNotNull();
		assertThat(found.getPatient_id()).isEqualTo(id);
		assertThat(found.getName()).isEqualTo(NAME);
		assertThat(found.getAge()).isEqualTo(NEWAGE);
		assertThat(found.getPatientDetails()).isEqualTo(new PatientDetails(NEWADDRESS, CELL1, CELL2));
		assertThat(found.getBloodPressures().get(0).getSystolic()).isEqualTo(NEWSYSTOLIC);
		assertThat(found.getBloodPressures().get(0).getDiastolic()).isEqualTo(DIASTOLIC);
		assertThat(found.getBloodPressures().get(0).getBpm()).isEqualTo(BPM);
		
		session.close();		
	}
	
	@Test
	public void test003_DeleteList() {
		
		session = HibernateUtil.getSessionFactory().openSession();
		Patient oldPatient = session.load(Patient.class, id);
		Transaction tx = session.beginTransaction();
		oldPatient.getBloodPressures().remove(0);
		session.save(oldPatient);
		tx.commit();
		session.close();

		session = HibernateUtil.getSessionFactory().openSession();
		Patient found = session.load(Patient.class, id);		

		assertThat(found).isNotNull();
		assertThat(found.getPatient_id()).isEqualTo(id);
		assertThat(found.getBloodPressures().size()).isEqualTo(0);
		
		session.close();
	}
	

}
