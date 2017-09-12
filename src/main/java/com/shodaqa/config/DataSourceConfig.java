package com.shodaqa.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})
public class DataSourceConfig {
	
	@Value("${bonecp.url}")
	private String jdbcUrl;
	
	@Value("${bonecp.username}")
	private String jdbcUsername;
	
	@Value("${bonecp.password}")
	private String jdbcPassword;
	
	@Value("${bonecp.driverClass}")
	private String driverClass;
	
	@Value("${bonecp.idleMaxAgeInMinutes}")
	private Integer idleMaxAgeInMinutes;
	
	@Value("${bonecp.idleConnectionTestPeriodInMinutes}")
	private Integer idleConnectionTestPeriodInMinutes;
	
	@Value("${bonecp.maxConnectionsPerPartition}")
	private Integer maxConnectionsPerPartition;
	
	@Value("${bonecp.minConnectionsPerPartition}")
	private Integer minConnectionsPerPartition;
	
	@Value("${bonecp.partitionCount}")
	private Integer partitionCount;
	
	@Value("${bonecp.acquireIncrement}")
	private Integer acquireIncrement;
	
	@Value("${bonecp.statementsCacheSize}")
	private Integer statementsCacheSize;
	
	@Autowired
	private Environment env;
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUsername(jdbcUsername);
		dataSource.setPassword(jdbcPassword);
		dataSource.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriodInMinutes);
		dataSource.setIdleMaxAgeInMinutes(idleMaxAgeInMinutes);
		dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
		dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
		dataSource.setPartitionCount(partitionCount);
		dataSource.setAcquireIncrement(acquireIncrement);
		dataSource.setStatementsCacheSize(statementsCacheSize);
		return dataSource;
	}
	
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put(AvailableSettings.DIALECT,
				env.getRequiredProperty("hibernate.dialect"));
		properties.put(AvailableSettings.SHOW_SQL,
				env.getRequiredProperty("hibernate.show_sql"));
		properties.put(AvailableSettings.STATEMENT_BATCH_SIZE,
				env.getRequiredProperty("hibernate.batch.size"));
		properties.put(AvailableSettings.HBM2DDL_AUTO,
				env.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS,
				env.getRequiredProperty("hibernate.enable_lazy_load_no_trans"));
		properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS,
				env.getRequiredProperty("hibernate.current.session.context.class"));
		properties.put(AvailableSettings.GENERATE_STATISTICS,
				env.getRequiredProperty("hibernate.generate_statistics"));
		return properties;
	}
	
	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(env.getRequiredProperty("scan.package"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}
	
	
	/**
	 * Initialize Transaction Manager
	 *
	 * @param sessionFactory
	 * @return HibernateTransactionManager
	 */
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}


}