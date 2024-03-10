package com.observer.batch.config;

import static com.observer.batch.config.BatchConfig.BEAN_NAME_PREFIX;
import static com.observer.batch.config.DelegatedBatchConfigurer.DELEGATED_BATCH_CONFIGURER_BEAN_NAME;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {

	public static final String JOB_REGISTRY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRegistry";
	public static final String JOB_REPOSITORY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRepository";
	public static final String JOB_EXPLORER_BEAN_NAME = BEAN_NAME_PREFIX + "JobExplorer";

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
}
