package com.wifi.obs.infra.batch.config;

import com.wifi.obs.infra.batch.BatchConfig;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.stereotype.Component;

@Component(value = DelegatedBatchConfigurer.DELEGATED_BATCH_CONFIGURER_BEAN_NAME)
public class DelegatedBatchConfigurer extends JpaBatchConfigurer {

	public static final String DELEGATED_BATCH_CONFIGURER_BEAN_NAME =
			BatchConfig.BEAN_NAME_PREFIX + "Configurer";

	public DelegatedBatchConfigurer(
			@Qualifier(value = BatchSupportConfig.PROPERTY_BEAN_NAME) BatchProperties properties,
			@Qualifier(value = BatchDataSourceConfig.DATASOURCE_NAME) DataSource dataSource,
			TransactionManagerCustomizers transactionManagerCustomizers,
			@Qualifier(value = BatchDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME)
					EntityManagerFactory entityManagerFactory) {
		super(properties, dataSource, transactionManagerCustomizers, entityManagerFactory);
	}
}
