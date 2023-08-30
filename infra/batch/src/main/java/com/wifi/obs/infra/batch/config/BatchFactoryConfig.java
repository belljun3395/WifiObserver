package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.BatchConfig.BEAN_NAME_PREFIX;
import static com.wifi.obs.infra.batch.config.DelegatedBatchConfigurer.DELEGATED_BATCH_CONFIGURER_BEAN_NAME;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchFactoryConfig {

	public static final String JOB_BUILDER_FACTORY_BEAN_NAME = BEAN_NAME_PREFIX + "JobBuilderFactory";
	public static final String STEP_BUILDER_FACTORY_BEAN_NAME =
			BEAN_NAME_PREFIX + "StepBuilderFactory";

	@Bean(name = JOB_BUILDER_FACTORY_BEAN_NAME)
	public JobBuilderFactory jobBuilderFactory(
			@Qualifier(value = DELEGATED_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return new JobBuilderFactory(configurer.getJobRepository());
	}

	@Bean(name = STEP_BUILDER_FACTORY_BEAN_NAME)
	public StepBuilderFactory stepBuilderFactory(
			@Qualifier(value = DELEGATED_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return new StepBuilderFactory(
				configurer.getJobRepository(), configurer.getTransactionManager());
	}
}
