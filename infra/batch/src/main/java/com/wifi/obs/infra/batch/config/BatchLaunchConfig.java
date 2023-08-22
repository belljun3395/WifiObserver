package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.BatchConfig.BEAN_NAME_PREFIX;
import static com.wifi.obs.infra.batch.config.BatchDataSourceConfig.DATASOURCE_NAME;
import static com.wifi.obs.infra.batch.config.BatchSupportConfig.PROPERTY_BEAN_NAME;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Configuration
public class BatchLaunchConfig {
	private static final String JOB_LAUNCHER_APPLICATION_RUNNER_BEAN_NAME =
			BEAN_NAME_PREFIX + "JobLauncherApplicationRunner";
	private static final String JOB_EXECUTION_EXIT_CODE_GENERATOR_BEAN_NAME =
			BEAN_NAME_PREFIX + "JobExecutionExitCodeGenerator";
	private static final String JOB_OPERATOR_BEAN_NAME = BEAN_NAME_PREFIX + "JobOperator";
	private static final String BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME =
			BEAN_NAME_PREFIX + "BatchDataSourceScriptDatabaseInitializer";

	@Bean(name = JOB_LAUNCHER_APPLICATION_RUNNER_BEAN_NAME)
	public JobLauncherApplicationRunner jobLauncherApplicationRunner(
			JobLauncher jobLauncher,
			JobExplorer jobExplorer,
			JobRepository jobRepository,
			BatchProperties properties) {
		JobLauncherApplicationRunner runner =
				new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
		String jobNames = properties.getJob().getNames();
		if (StringUtils.hasText(jobNames)) {
			runner.setJobNames(jobNames);
		}
		return runner;
	}

	@Bean(name = JOB_EXECUTION_EXIT_CODE_GENERATOR_BEAN_NAME)
	public JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator() {
		return new JobExecutionExitCodeGenerator();
	}

	@Bean(name = JOB_OPERATOR_BEAN_NAME)
	public SimpleJobOperator jobOperator(
			ObjectProvider<JobParametersConverter> jobParametersConverter,
			JobExplorer jobExplorer,
			JobLauncher jobLauncher,
			ListableJobLocator jobRegistry,
			JobRepository jobRepository) {
		SimpleJobOperator factory = new SimpleJobOperator();
		factory.setJobExplorer(jobExplorer);
		factory.setJobLauncher(jobLauncher);
		factory.setJobRegistry(jobRegistry);
		factory.setJobRepository(jobRepository);
		jobParametersConverter.ifAvailable(factory::setJobParametersConverter);
		return factory;
	}

	@Profile(value = "local")
	@Bean(name = BATCH_DATA_SOURCE_SCRIPT_DATABASE_INITIALIZER_BEAN_NAME)
	BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			@Qualifier(value = PROPERTY_BEAN_NAME) BatchProperties properties) {
		return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
	}
}
