package com.vastrak.hibernate006.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * HibernateUtil is a helper class, which will help us to set and get SessionFactory.
 * We need hibernate.cfg.xml for create a SessionFactory 
 * This cfg.xml file is in the root, next to the pom.xml (not web project)
 * <p>
 * Usage:
 * SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
 * Session session = sessionFactory.openSession();
 * <p>
 * For reuse an already created session:
 * SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
 * Session session = sessionFactory.getCurrentSession();
 * <p>
 * For open a stateless session (without cache)
 * SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
 * Session session = sessionFactory.openStatelessSession();
 *
 * @author Christian
 * 
 * 
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final Log logger = LogFactory.getLog(HibernateUtil.class);

	// static block for initializer SessionFactory instance
	static {
		try {
			sessionFactory = new Configuration().configure(new File("hibernate.cfg.xml")).buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Fail in HibernateUtil.class: initial SessionFactory creation failed." + ex);
			// Signals that an unexpected exception has occurred in a static initializer. 
			// An ExceptionInInitializerError is thrown to indicate that an exception occurred
			// during evaluation of a static initializer or the initializer for a static variable.
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * 
	 * @return SessionFactory
	 */
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * Close caches and connection pools.
	 * <p>
	 * It's our responsibility to flush or close it once we are done 
	 * with the database operation.
	 */
	public static void shutdown() {
		getSessionFactory().close();
	}

}
