package com.wifi.obs.infra.batch.job.browse.iptime;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.infra.batch.job.browse.iptime.step.IptimeAuthProcessor;
import com.wifi.obs.infra.batch.job.browse.iptime.step.IptimeBrowseProcessor;
import com.wifi.obs.infra.batch.job.browse.iptime.step.IptimeConnectHistoryWriter;
import com.wifi.obs.infra.batch.support.listener.BrowseStepSlackLoggingListener;
import com.wifi.obs.infra.batch.support.param.TimeStamper;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
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
	private static final String FIND_IPTIME_STATUS_ON_QUERY =
			"select w from wifi_service_entity w where w.serviceType = 'IPTIME' and  w.status = 'ON' and w.deleted = false";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final TimeStamper timeStamper;
	private final BrowseStepSlackLoggingListener browseStepSlackLoggingListener;

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
				.<WifiServiceEntity, IptimeOnConnectUserInfosResponse>chunk(CHUNK_SIZE)
				.reader(iptimeBrowseReader())
				.processor(iptimeClientProcessor())
				.writer(iptimeConnectHistoryWriter)
				.transactionManager(transactionManager)
				.listener(browseStepSlackLoggingListener)
				.build();
	}

	@Bean
	public JpaPagingItemReader<WifiServiceEntity> iptimeBrowseReader() {
		return new JpaPagingItemReaderBuilder<WifiServiceEntity>()
				.name("iptimeBrowseReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString(FIND_IPTIME_STATUS_ON_QUERY)
				.build();
	}

	@Bean
	public ItemProcessor<WifiServiceEntity, IptimeOnConnectUserInfosResponse>
			iptimeClientProcessor() {

		return new CompositeItemProcessorBuilder<WifiServiceEntity, IptimeOnConnectUserInfosResponse>()
				.delegates(List.of(iptimeAuthProcessor, iptimeBrowseProcessor))
				.build();
	}
}
