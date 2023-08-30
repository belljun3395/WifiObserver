package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.BatchConfig.BEAN_NAME_PREFIX;
import static com.wifi.obs.infra.batch.config.BatchDataSourceConfig.DATASOURCE_NAME;
import static com.wifi.obs.infra.batch.config.BatchSupportConfig.PROPERTY_BEAN_NAME;
import static com.wifi.obs.infra.batch.config.DelegatedBatchConfigurer.DELEGATED_BATCH_CONFIGURER_BEAN_NAME;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BatchMetaDataConfig {

	public static final String JOB_REGISTRY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRegistry";

	public static final String JOB_REPOSITORY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRepository";
	public static final String JOB_EXPLORER_BEAN_NAME = BEAN_NAME_PREFIX + "JobExplorer";
	private static final String BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME =
			BEAN_NAME_PREFIX + "BatchDataSourceScriptDatabaseInitializer";

	@Bean(name = JOB_REGISTRY_BEAN_NAME)
	public JobRegistry jobRegistry() {
		return new MapJobRegistry();
	}

	@Bean(name = JOB_REPOSITORY_BEAN_NAME)
	public JobRepository jobRepository(
			@Qualifier(value = DELEGATED_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return configurer.getJobRepository();
	}

	@Bean(name = JOB_EXPLORER_BEAN_NAME)
	public JobExplorer jobExplorer(
			@Qualifier(value = DELEGATED_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return configurer.getJobExplorer();
	}

	@Profile(value = "local")
	@Bean(name = BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME)
	BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			@Qualifier(value = PROPERTY_BEAN_NAME) BatchProperties properties) {
		return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
	}
}
