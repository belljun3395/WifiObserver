package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.config.BatchDataSourceConfig.DATASOURCE_NAME;
import static com.wifi.obs.infra.batch.config.BatchDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME;
import static com.wifi.obs.infra.batch.config.BatchSupportConfig.PROPERTY_BEAN_NAME;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.stereotype.Component;

@Component
public class DelegatedBatchConfigurer extends JpaBatchConfigurer {

	public static final String WIFIOBS_BATCH_CONFIGURER_BEAN_NAME = "wifiObsBatchConfigurer";

	public DelegatedBatchConfigurer(
			@Qualifier(value = PROPERTY_BEAN_NAME) BatchProperties properties,
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			TransactionManagerCustomizers transactionManagerCustomizers,
			@Qualifier(value = ENTITY_MANAGER_FACTORY_NAME) EntityManagerFactory entityManagerFactory) {
		super(properties, dataSource, transactionManagerCustomizers, entityManagerFactory);
	}
}
