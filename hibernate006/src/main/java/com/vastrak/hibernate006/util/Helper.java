package com.vastrak.hibernate006.util;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;



public class Helper {

	private static final Log logger = LogFactory.getLog(Helper.class);
	
	/**
	 * Helper to execute a Sql script file. It is required that there is no blank
	 * jump between queries. Ignore comments.
	 * 
	 * @param sqlscript to execute
	 * @throws Exception
	 */
	public static void executeSQLScript(String sqlscript) throws Exception {

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
}
