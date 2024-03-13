package com.observer.batch.config;

import static com.observer.batch.config.BatchConfig.BEAN_NAME_PREFIX;
import static com.observer.batch.config.BatchPropertyConfig.PROPERTY_BEAN_NAME;

import com.observer.data.config.JpaDataSourceConfig;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchDataSourceConfig {

	public static final String BASE_PACKAGE = BatchConfig.BASE_PACKAGE;

	// base property prefix for jpa datasource
	private static final String BASE_PROPERTY_PREFIX = BatchConfig.PROPERTY_PREFIX;

	// bean name for jpa datasource configuration
	public static final String ENTITY_BEAN_NAME_PREFIX = BatchConfig.BEAN_NAME_PREFIX;
	public static final String ENTITY_MANAGER_FACTORY_NAME =
			ENTITY_BEAN_NAME_PREFIX + "ManagerFactory";
	public static final String TRANSACTION_MANAGER_NAME =
			ENTITY_BEAN_NAME_PREFIX + "TransactionManager";
	public static final String DATASOURCE_NAME = ENTITY_BEAN_NAME_PREFIX + "DataSource";
	public static final String BATCH_API_CHAINED_TRANSACTION_MANAGER =
			"batchApiChainedTransactionManager";
	private static final String JPA_PROPERTIES_NAME = ENTITY_BEAN_NAME_PREFIX + "JpaProperties";
	private static final String HIBERNATE_PROPERTIES_NAME =
			ENTITY_BEAN_NAME_PREFIX + "HibernateProperties";
	private static final String JPA_VENDOR_ADAPTER_NAME =
			ENTITY_BEAN_NAME_PREFIX + "JpaVendorAdapter";
	private static final String PERSIST_UNIT = ENTITY_BEAN_NAME_PREFIX + "PersistenceUnit";
	private static final String ENTITY_MANAGER_FACTORY_BUILDER_NAME =
			ENTITY_BEAN_NAME_PREFIX + "ManagerFactoryBuilder";
	private static final String BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME =
			BEAN_NAME_PREFIX + "BatchDataSourceScriptDatabaseInitializer";

	@Bean(name = DATASOURCE_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".datasource")
	// @BatchDataSource
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = JPA_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".jpa")
	public JpaProperties jpaProperties() {
		return new JpaProperties();
	}

	@Bean(name = HIBERNATE_PROPERTIES_NAME)
	@ConfigurationProperties(prefix = BASE_PROPERTY_PREFIX + ".jpa.hibernate")
	public HibernateProperties hibernateProperties() {
		return new HibernateProperties();
	}

	@Bean(name = JPA_VENDOR_ADAPTER_NAME)
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean(name = ENTITY_MANAGER_FACTORY_BUILDER_NAME)
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
			@Qualifier(value = JPA_VENDOR_ADAPTER_NAME) JpaVendorAdapter jpaVendorAdapter,
			@Qualifier(value = JPA_PROPERTIES_NAME) JpaProperties jpaProperties,
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {

		Map<String, String> jpaPropertyMap = jpaProperties.getProperties();
		return new EntityManagerFactoryBuilder(
				jpaVendorAdapter, jpaPropertyMap, persistenceUnitManager.getIfAvailable());
	}

	@Bean(name = ENTITY_MANAGER_FACTORY_NAME)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			@Qualifier(value = ENTITY_MANAGER_FACTORY_BUILDER_NAME) EntityManagerFactoryBuilder builder) {
		Map<String, String> jpaPropertyMap = jpaProperties().getProperties();
		Map<String, Object> hibernatePropertyMap =
				hibernateProperties().determineHibernateProperties(jpaPropertyMap, new HibernateSettings());
		return builder
				.dataSource(dataSource)
				.properties(hibernatePropertyMap)
				.persistenceUnit(PERSIST_UNIT)
				.packages(BASE_PACKAGE)
				.build();
	}

	@Bean(name = TRANSACTION_MANAGER_NAME)
	public PlatformTransactionManager transactionManager(
			@Qualifier(ENTITY_MANAGER_FACTORY_NAME) EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	// todo: JtaTransactionManager 와 ChainedTransactionManager 비교 해보기
	@Bean(name = BATCH_API_CHAINED_TRANSACTION_MANAGER)
	public PlatformTransactionManager chainedTransactionManager(
			@Qualifier(TRANSACTION_MANAGER_NAME) PlatformTransactionManager batchTransactionManager,
			@Qualifier(JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
					PlatformTransactionManager apiTransactionManager) {
		return new ChainedTransactionManager(batchTransactionManager, apiTransactionManager);
	}

	@Bean(name = BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME)
	BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			@Qualifier(value = PROPERTY_BEAN_NAME) BatchProperties properties) {
		return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
	}
}
