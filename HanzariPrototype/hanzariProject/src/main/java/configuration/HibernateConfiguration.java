package configuration;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateConfiguration {

	@Bean(name="entityManagerFactory")
	public SessionFactory sessionFactory() {
		return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	}
}