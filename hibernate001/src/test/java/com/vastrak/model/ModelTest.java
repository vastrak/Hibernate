package com.vastrak.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.vastrak.util.HibernateUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Allows the user to choose the order of execution of the methods within a test class
public class ModelTest {

	private static final Log logger = LogFactory.getLog(ModelTest.class);
	private static User userMiguel = null;
	private static Article articleMiguel2 = null;

	/**
	 * 
	 * 
	 */
	@BeforeClass // in JUnit 5 use @BeforeAll, this is a static method
	@SuppressWarnings("unchecked")
	public static void removeAllData() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		
		// El nombre de la tabla es case sensitive
		Query<User> queryUser = session.createQuery("DELETE FROM User"); // la tabla es case-sensitive
		int eliminados = queryUser.executeUpdate();
		logger.info("--------> Deleted rows of User: " + eliminados);
		

		Query<Article> queryArticle = session.createQuery("DELETE FROM Article");
		eliminados = queryArticle.executeUpdate();
		logger.info("--------> Deleted rows of Article: " + eliminados);
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test001_addUserAndArticle() {

		// creo un usuario 
		userMiguel = new User();
		userMiguel.setName("Miguel");
		userMiguel.setEmail("miguelnuno@aol.com");

		// creo un artículo 
		Article articleMiguel1 = new Article();
		articleMiguel1.setTitle("Mi primer artículo");
		articleMiguel1.setBody("Soy Miguel y digo que es invierno.");
		// asocio el artículo con el usuario 
		articleMiguel1.setUser(userMiguel);
		
		// creo un segundo artículo 
		articleMiguel2 = new Article();
		articleMiguel2.setTitle("Mi segundo artículo");
		articleMiguel2.setBody("Soy Miguel y digo que es verano.");		
		//
		articleMiguel2.setUser(userMiguel);
		
		// asocio el usuario con los artículos
		userMiguel.addArticle(articleMiguel1);
		userMiguel.addArticle(articleMiguel2);

		//
		// los ids no fueron asignados todavía
		//
		assertNull(userMiguel.getUser_id());
		assertNull(articleMiguel1.getArticle_id());
		assertNull(articleMiguel2.getArticle_id());
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.getTransaction();
		tx.begin();

		session.save(userMiguel); // guardando la tabla padre, se guardan los hijos
								  // pero tiene que estar asociado de antes y 
								  // existir una relación @OneToMany
		tx.commit();

		logger.info("-------->  Added: " + userMiguel); // <- ya tiene el id asignado
		
		//
		// asignados ids por hibernate.
		//
		assertNotNull(userMiguel.getUser_id());
		assertNotNull(articleMiguel1.getArticle_id());
		assertNotNull(articleMiguel2.getArticle_id());

	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test002_readingUser() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String userName = "Miguel";
		Query<User> query = session.createQuery("from User u where u.name=:name");
		query.setParameter("name", userName);
		User user = query.uniqueResult();
		logger.info("--------> Reading: " + user);
		assertNotNull(user);
		assertEquals(user, userMiguel);  // son iguales si sus ids son iguales.

	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test003_readingArticlesFromUser() {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		
		// recuperamos los dos artículos del usuario desde User
		Long id = userMiguel.getUser_id();
		Query<User> query = session.createQuery("from User u where u.user_id=:id");
		query.setParameter("id", id);
		User user = query.uniqueResult();
		List<Article>  listArticles = user.getArticles();
		
		assertTrue(listArticles.size()==2);
		// dos artículos son iguales si tienen el mismo id y el mismo título.
		assertTrue(listArticles.contains(articleMiguel2));
		session.close();
		
	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test004_readingArticlesByTitle() {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		
		String title = articleMiguel2.getTitle();
		Query<Article> query = session.createQuery("from Article a where a.title=:title");
		query.setParameter("title", title);
		List<Article> listArticles = query.getResultList();
		
		assertTrue(listArticles.size()==1);
		assertTrue(listArticles.get(0).equals(articleMiguel2));
		assertTrue(listArticles.get(0).getUser().equals(userMiguel));
		
		session.close();
	}

	/**
	 * 
	 * 
	 */
	@Test
	public void test005_updateUserChangeEmail() {

		String oldemail = userMiguel.getEmail();
		String newemail = "nuevomiguel@adelaida.com";
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String userName = userMiguel.getName();
		Query<User> query = session.createQuery("from User u where u.name=:name");
		query.setParameter("name", userName);
		User user = query.uniqueResult();

		assertNotNull(user);  // <- recuperé un usuario único

		// Cambio por el nuevo correo
		user.setEmail(newemail);
		// Que sea distinto!
		assertFalse(oldemail.equals(user.getEmail()));

		// Hago un update de los datos!
		Transaction tx = session.getTransaction();
		tx.begin();
		session.saveOrUpdate(user);
		tx.commit();
		session.close();

		// Recupero el usuario para comprobar que realmente
		// se cambió el correo
		session = HibernateUtil.getSessionFactory().openSession();
		query = session.createQuery("from User u where u.user_id=:id");
		query.setParameter("id", userMiguel.getUser_id());
		user = query.uniqueResult();
		//
		assertTrue(newemail.equals(user.getEmail()));
		//
		session.close();
	}
	
	/**
	 *
	 * 
	 */
	@Test
	public void test006_updateArticleChangeBody() {
		
		Long idarticle = articleMiguel2.getArticle_id();
		String oldbody = articleMiguel2.getBody();
		String newBody = "Hasta el 40 de mayo...";

		assertFalse(oldbody.equals(newBody)); // ya nos vale!
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<User> query = session.createQuery("from User u where u.user_id=:id");
		query.setParameter("id", userMiguel.getUser_id());
		User user = query.uniqueResult();
		
		assertNotNull(user);
		
		user.getArticles()
		    .stream()
		    .filter(article -> article.getArticle_id().equals(idarticle))
		    .findFirst().
		    ifPresent(article -> article.setBody(newBody));
		
		Transaction tx = session.getTransaction();
		tx.begin();
		session.merge(user);
		tx.commit();
		session.close();

		// ahora hay que recuperarlo y ver el cambio.
		session = HibernateUtil.getSessionFactory().openSession();
		Query<Article> queryByArticleId = session.createQuery("from Article a where a.article_id=:id");
		queryByArticleId.setParameter("id", articleMiguel2.getArticle_id());
		Article article = queryByArticleId.uniqueResult();
		
		assertNotNull(article);
		assertTrue(article.equals(articleMiguel2)); // iguales si tienen el mismo id y título
		assertTrue(article.getBody().equals(newBody)); 
		
		session.close();
		
	}
	

	/**
	 * 
	 * @param session
	 * @param id
	 * @return User encontrado en la entidad User a partir de su id
	 */
	@SuppressWarnings("unchecked")
	private User getUserById(Session session, Long id) {
		User retUser = null;
		if(id!=null) {
			Query<User> query = session.createQuery("from User u where u.user_id=:id");
			query.setParameter("id", id);
			retUser = query.uniqueResult();
		}
		return retUser;
	}

	/**
	 * 
	 * @param session
	 * @param id
	 * @return Article encontrado en la entidad Article a partir de su id.
	 */
	@SuppressWarnings("unchecked")
	private Article getArticleById(Session session, Long id) {
		Article retArticle = null;
		if(id!=null) {
			Query<Article> query = session.createQuery("from Article a where a.article_id=:id");
			query.setParameter("id", id);
			retArticle = query.uniqueResult();			
		}
		return retArticle;
	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test007_removeAnArticleOfAnUser() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		User user = getUserById(session, userMiguel.getUser_id());
		boolean existArticle = user.getArticles().contains(articleMiguel2);
		
		assertTrue(existArticle);
		
		session = HibernateUtil.getSessionFactory().getCurrentSession(); // si commit() requiere openSession()
		Transaction tx = session.getTransaction();
		tx.begin();		
		session.remove(articleMiguel2);
		tx.commit();

		session = HibernateUtil.getSessionFactory().openSession();
		user = getUserById(session, userMiguel.getUser_id());
		existArticle = user.getArticles().contains(articleMiguel2);
		session.close();
		
		assertFalse(existArticle);

	}
	
	/**
	 * 
	 * 
	 */
	@Test
	public void test008_removeUserAndAllHisArticles() {
		
		// recuperamos un usuario y uno de sus artículos
		Session session = HibernateUtil.getSessionFactory().openSession();
		User user = getUserById(session, userMiguel.getUser_id());
		assertNotNull(user);
		List<Article> listArticles = user.getArticles();
		assertNotNull(listArticles);
		// nos quedamos con el id del artículo
		Long article_id0 = listArticles.get(0).getArticle_id();
		assertNotNull(article_id0);

		// eliminamos el usuario y todos sus artículos
		Transaction tx = session.getTransaction();
		tx.begin();		
		session.remove(user); // elimino de la tabla padre y elimina autom. de las tablas hijas
		tx.commit();		
		session.close();
		
		// no se puede recuperar el usuario
		session = HibernateUtil.getSessionFactory().openSession();
		user = getUserById(session, userMiguel.getUser_id());
		session.close();
		assertNull(user);
		
		// no se pueden recuperar los articulos de usuario
		// borrados en cascada
		session = HibernateUtil.getSessionFactory().openSession();
		Article article = getArticleById(session, article_id0);
		session.close();
		assertNull(article);
		
	}
	
	/**
	 *
	 *
	 * 
	 */
	@Test
	public void test009_notAllowedToAddArticlesWithoutUserPersisted() {

		// creo un artículo sin usuario
		Article newArticle1 = new Article();
		newArticle1.setTitle("Nobody wrote to me");
		newArticle1.setBody("Nobody in the whole world");
		// creo un artículo con un usuario no persistido en la tabla User
		Article newArticle2 = new Article();
		newArticle2.setTitle("Nobody wrote to me too");
		newArticle2.setBody("Nobody I know...");		
		User fakeUser = new User();
		fakeUser.setName("Pablo");
		fakeUser.setEmail("pablo@pablo.com");
		newArticle2.setUser(fakeUser);
		
		// Intento persistir los artículos en la tabla Articles
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.getTransaction();
		tx.begin();		
		session.save(newArticle1);
		session.save(newArticle2);
		session.close();
		
		// me asigna un id, pero no lo guarda en la tabla
		assertNotNull(newArticle1.getArticle_id());
		assertNotNull(newArticle2.getArticle_id());
		logger.info("--------> article1_id: "+newArticle1.getArticle_id());
		logger.info("--------> article2_id: "+newArticle1.getArticle_id());
		
		// intento recuperar el artículo sin usuario
		session = HibernateUtil.getSessionFactory().openSession();
		Article article1 = getArticleById(session, newArticle1.getArticle_id());
		// intento recuperar el artículo del usuario fake (que no está persistido)
		Article article2 = getArticleById(session, newArticle2.getArticle_id());
		session.close();
		// pero no están en la tabla!
		assertNull(article1);
		assertNull(article2);

		session.close();		
		
	}

}
