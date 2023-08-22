package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.BatchConfig.BEAN_NAME_PREFIX;
import static com.wifi.obs.infra.batch.config.DelegatedBatchConfigurer.WIFIOBS_BATCH_CONFIGURER_BEAN_NAME;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchStructureConfig {

	public static final String JOB_REPOSITORY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRepository";
	public static final String JOB_LAUNCHER_BEAN_NAME = BEAN_NAME_PREFIX + "JobLauncher";
	public static final String JOB_EXPLORER_BEAN_NAME = BEAN_NAME_PREFIX + "JobExplorer";
	public static final String JOB_REGISTRY_BEAN_NAME = BEAN_NAME_PREFIX + "JobRegistry";
	public static final String JOB_BUILDER_FACTORY_BEAN_NAME = BEAN_NAME_PREFIX + "JobBuilderFactory";
	public static final String STEP_BUILDER_FACTORY_BEAN_NAME =
			BEAN_NAME_PREFIX + "StepBuilderFactory";

	@Bean(name = JOB_REPOSITORY_BEAN_NAME)
	public JobRepository jobRepository(
			@Qualifier(value = "delegatedBatchConfigurer") BatchConfigurer configurer) throws Exception {
		return configurer.getJobRepository();
	}

	@Bean(name = JOB_LAUNCHER_BEAN_NAME)
	public JobLauncher jobLauncher(
			@Qualifier(value = WIFIOBS_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		simpleJobLauncher.setJobRepository(configurer.getJobRepository());
		return simpleJobLauncher;
	}

	@Bean(name = JOB_EXPLORER_BEAN_NAME)
	public JobExplorer jobExplorer(
			@Qualifier(value = WIFIOBS_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return configurer.getJobExplorer();
	}

	@Bean(name = JOB_REGISTRY_BEAN_NAME)
	public JobRegistry jobRegistry() {
		return new MapJobRegistry();
	}

	@Bean(name = JOB_BUILDER_FACTORY_BEAN_NAME)
	public JobBuilderFactory jobBuilderFactory(
			@Qualifier(value = WIFIOBS_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return new JobBuilderFactory(configurer.getJobRepository());
	}

	@Bean(name = STEP_BUILDER_FACTORY_BEAN_NAME)
	public StepBuilderFactory stepBuilderFactory(
			@Qualifier(value = WIFIOBS_BATCH_CONFIGURER_BEAN_NAME) BatchConfigurer configurer)
			throws Exception {
		return new StepBuilderFactory(
				configurer.getJobRepository(), configurer.getTransactionManager());
	}
}
