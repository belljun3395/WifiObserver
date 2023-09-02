package com.wifi.obs.infra.batch.job.refresh.iptime;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import com.wifi.obs.infra.batch.job.refresh.iptime.step.IptimeBrowseRefreshWriter;
import com.wifi.obs.infra.batch.support.listener.RefreshStepSlackLoggingListener;
import com.wifi.obs.infra.batch.support.param.TimeStamper;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IptimeBrowseRefreshConfig {
	public static final String JOB_NAME = "IptimeBrowseRefreshJob";
	public static final String STEP_NAME = "IptimeBrowseRefreshStep";
	private static final int CHUNK_SIZE = 20;
	private static final String FIND_ALL_CONNECT_HISTORY_QUERY =
			"select c from connect_history_entity c where c.deleted = false";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final TimeStamper timeStamper;
	private final RefreshStepSlackLoggingListener refreshStepSlackLoggingListener;

	private final IptimeBrowseRefreshWriter iptimeBrowseRefreshWriter;

	private EntityManagerFactory entityManagerFactory;
	private PlatformTransactionManager transactionManager;

	@Autowired
	public void setEntityManagerFactory(
			@Qualifier(value = JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME)
					EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Autowired
	public void setTransactionManager(
			@Qualifier(value = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
					PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Bean(name = JOB_NAME)
	public Job iptimeBrowseRefreshJob() {
		return this.jobBuilderFactory
				.get(JOB_NAME)
				.incrementer(timeStamper)
				.start(iptimeBrowseRefreshStep())
				.build();
	}

	@Bean
	public Step iptimeBrowseRefreshStep() {
		return this.stepBuilderFactory
				.get(STEP_NAME)
				.<ConnectHistoryEntity, ConnectHistoryEntity>chunk(CHUNK_SIZE)
				.reader(iptimeBrowseRefreshReader())
				.writer(iptimeBrowseRefreshWriter)
				.transactionManager(transactionManager)
				.listener(refreshStepSlackLoggingListener)
				.build();
	}

	@Bean
	public JpaPagingItemReader<ConnectHistoryEntity> iptimeBrowseRefreshReader() {
		return new JpaPagingItemReaderBuilder<ConnectHistoryEntity>()
				.name("iptimeBrowseRefreshReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString(FIND_ALL_CONNECT_HISTORY_QUERY)
				.build();
	}
}
