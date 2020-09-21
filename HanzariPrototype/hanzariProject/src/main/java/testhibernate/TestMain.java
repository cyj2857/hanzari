package testhibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Figure;

public class TestMain {

	private static SessionFactory sessionFactory;
	
	public static void main(String[] args) {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Figure figure = new Figure();
		figure.setFigure_id("5");
		figure.setFigure_name("도형5");
		
		System.out.println("1");

		Session session = sessionFactory.openSession();
		System.out.println("2");

		try {
			Transaction tx = session.beginTransaction();
			System.out.println("3");

			session.save(figure); // instead of SQL statement
			System.out.println("4");

			tx.commit();
			System.out.println("5");

		} catch (Exception e) {
			System.out.println("6");
			e.printStackTrace();

		} finally {
			session.close();
			sessionFactory.close();
		}
	}

}
