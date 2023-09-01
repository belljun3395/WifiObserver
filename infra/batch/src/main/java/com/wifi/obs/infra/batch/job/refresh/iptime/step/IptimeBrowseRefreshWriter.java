package com.wifi.obs.infra.batch.job.refresh.iptime.step;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import com.wifi.obs.data.mysql.entity.history.DisConnectHistoryEntity;
import com.wifi.obs.data.mysql.repository.history.connect.ConnectHistoryRepository;
import com.wifi.obs.data.mysql.repository.history.disConnect.DisConnectHistoryRepository;
import com.wifi.obs.infra.batch.service.SaveHistoryMeatService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseRefreshWriter implements ItemWriter<ConnectHistoryEntity> {

	private static final Long REFRESH_ADJUST_TIME = 5L;

	private final SaveHistoryMeatService saveHistoryMeatService;

	private final ConnectHistoryRepository connectHistoryRepository;
	private final DisConnectHistoryRepository disConnectHistoryRepository;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void write(List<? extends ConnectHistoryEntity> items) throws Exception {
		LocalDateTime now = LocalDateTime.now();

		for (ConnectHistoryEntity connectHistory : items) {
			Long durationTime = getDurationTime(connectHistory.getStartTime(), now);
			durationTime += REFRESH_ADJUST_TIME;

			log.debug("=== saveDisConnectHistory ===");
			disConnectHistoryRepository.save(
					DisConnectHistoryEntity.builder()
							.device(connectHistory.getDevice())
							.wifiService(connectHistory.getWifiService())
							.endTime(now)
							.build());

			log.debug("=== saveHistoryMeta ===");
			saveHistoryMeatService.execute(
					connectHistory, connectHistory.getWifiService(), now, durationTime);

			log.debug("=== deleteConnectHistory ===");
			connectHistoryRepository.deleteById(connectHistory.getId());
		}
	}

	private Long getDurationTime(LocalDateTime startTime, LocalDateTime nowTime) {
		int hour = nowTime.getHour() - startTime.getHour();
		int minute = nowTime.getMinute() - startTime.getMinute();
		return (long) (hour * 60 + minute);
	}
}
