package com.observer.batch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component(value = DelegatedBatchConfigurer.DELEGATED_BATCH_CONFIGURER_BEAN_NAME)
public class DelegatedBatchConfigurer extends JpaBatchConfigurer {

	public static final String DELEGATED_BATCH_CONFIGURER_BEAN_NAME =
			BatchConfig.BEAN_NAME_PREFIX + "Configurer";

	public PlatformTransactionManager transactionManager;

	public DelegatedBatchConfigurer(
			@Qualifier(value = BatchPropertyConfig.PROPERTY_BEAN_NAME) BatchProperties properties,
			@Qualifier(value = BatchDataSourceConfig.DATASOURCE_NAME) DataSource dataSource,
			TransactionManagerCustomizers transactionManagerCustomizers,
			@Qualifier(value = BatchDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME)
					EntityManagerFactory entityManagerFactory,
			@Qualifier(value = BatchDataSourceConfig.TRANSACTION_MANAGER_NAME)
					PlatformTransactionManager transactionManager) {
		super(properties, dataSource, transactionManagerCustomizers, entityManagerFactory);
		this.transactionManager = transactionManager;
	}

	@Override
	protected PlatformTransactionManager createTransactionManager() {
		return transactionManager;
	}
}
