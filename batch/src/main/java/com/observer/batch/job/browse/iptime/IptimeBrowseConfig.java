package com.observer.batch.job.browse.iptime;

import com.observer.batch.job.browse.iptime.step.IptimeAuthProcessor;
import com.observer.batch.job.browse.iptime.step.IptimeBrowseProcessor;
import com.observer.batch.job.browse.iptime.step.IptimeConnectHistoryWriter;
import com.observer.batch.support.listener.BrowseStepLoggingListener;
import com.observer.batch.support.param.TimeStamper;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.router.RouterEntity;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IptimeBrowseConfig {

	public static final String JOB_NAME = "IptimeBrowseJob";
	public static final String STEP_NAME = "iptimeBrowseStep";
	private static final int CHUNK_SIZE = 20;
	private static final String FIND_IPTIME_ROUTER_STATUS_ON_QUERY =
			"select r from router r where r.serviceType = 'IPTIME' and  r.status = 'ON' and r.deleted = false";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final TimeStamper timeStamper;
	private final BrowseStepLoggingListener browseStepLoggingListener;

	private final IptimeAuthProcessor iptimeAuthProcessor;
	private final IptimeBrowseProcessor iptimeBrowseProcessor;
	private final IptimeConnectHistoryWriter iptimeConnectHistoryWriter;

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
	public Job iptimeBrowseJob() {
		return this.jobBuilderFactory
				.get(JOB_NAME)
				.incrementer(timeStamper)
				.start(iptimeBrowseStep())
				.build();
	}

	@Bean
	public Step iptimeBrowseStep() {
		return this.stepBuilderFactory
				.get(STEP_NAME)
				.<RouterEntity, RouterUsersResponse>chunk(CHUNK_SIZE)
				.reader(reader())
				.processor(processor())
				.writer(iptimeConnectHistoryWriter)
				.transactionManager(transactionManager)
				.listener(browseStepLoggingListener)
				.build();
	}

	@Bean
	public JpaPagingItemReader<RouterEntity> reader() {
		return new JpaPagingItemReaderBuilder<RouterEntity>()
				.name("iptimeRouterReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString(FIND_IPTIME_ROUTER_STATUS_ON_QUERY)
				.build();
	}

	@Bean
	public ItemProcessor<RouterEntity, RouterUsersResponse> processor() {
		return new CompositeItemProcessorBuilder<RouterEntity, RouterUsersResponse>()
				.delegates(List.of(iptimeAuthProcessor, iptimeBrowseProcessor))
				.build();
	}
}