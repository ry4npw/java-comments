package pw.ry4n.comments.webapp.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Data and JDBC configuration.
 * 
 * @author Ryan Powell
 */
@Configuration
@EnableJpaRepositories("pw.ry4n.comments.data")
@EnableTransactionManagement
public class DatabaseConfig {
	@Value("${hibernate.packagesToScan}")
	protected String packagesToScan;
	@Value("${hibernate.dialect}")
	protected String hibernateDialect;
	@Value("${hibernate.show_sql}")
	protected String hibernateShowSql;
	@Value("${hibernate.hbm2ddl.auto}")
	protected String hibernateHbm2ddlAuto;
	@Value("${hibernate.generate_statistics}")
	protected String hibernateGenerateStatistics;

	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;

	/**
	 * A {@link DriverManagerDataSource} configuration.
	 * 
	 * @return {@link DataSource}
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	/**
	 * @return {@link HibernateExceptionTranslator}
	 */
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	/**
	 * Optional hibernate properties, see <a href=
	 * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html_single/#configuration-optional"
	 * >3.4. Optional configuration properties</a>.
	 * 
	 * @return {@link Properties}
	 */
	protected Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty(AvailableSettings.DIALECT,
				hibernateDialect);
		hibernateProperties.setProperty(AvailableSettings.HBM2DDL_AUTO,
				hibernateHbm2ddlAuto);
		hibernateProperties.setProperty(AvailableSettings.SHOW_SQL,
				hibernateShowSql);
		hibernateProperties.setProperty(AvailableSettings.GENERATE_STATISTICS,
				hibernateGenerateStatistics);
		return hibernateProperties;
	}

	/**
	 * @return {@link EntityManagerFactory}
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		// vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(packagesToScan);
		factory.setDataSource(dataSource());
		factory.setJpaProperties(hibernateProperties());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	/**
	 * @return {@link PlatformTransactionManager}
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory());
	}
}
