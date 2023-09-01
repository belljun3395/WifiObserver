package com.wifi.obs.infra.batch.service;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveHistoryMeatService {

	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(
			ConnectHistoryEntity connectHistory,
			WifiServiceEntity service,
			LocalDateTime now,
			Long durationTime) {

		// 날짜를 기준으로 접속 기록 메타 데이터 조회
		Optional<ConnectHistoryMetaEntity> dayHistoryMeta =
				connectHistoryMetaRepository.findTopByDeviceAndDayAndMonthOrderByIdDesc(
						connectHistory.getDevice(),
						(long) now.getDayOfMonth(),
						(long) now.getMonth().getValue());

		if (dayHistoryMeta.isEmpty()) {
			// 달을 기준으로 접속 기록 메타 데이터 조회
			Optional<ConnectHistoryMetaEntity> monthHistoryMeta =
					connectHistoryMetaRepository.findTopByDeviceAndMonthOrderByIdDesc(
							connectHistory.getDevice(), (long) now.getMonth().getValue());

			browseMonthAndSaveNotConnectTodayHistoryMeta(
					monthHistoryMeta, service, connectHistory.getDevice(), now, durationTime);
		} else {
			ConnectHistoryMetaEntity history = dayHistoryMeta.get();

			// 해당 일의 접속 기록을 바탕으로 당일 접속 기록 메타 데이터 생성
			connectHistoryMetaRepository.save(
					ConnectHistoryMetaEntity.builder()
							.connectedTimeOnDay(history.getConnectedTimeOnDay() + durationTime)
							.connectedTimeOnMonth(history.getConnectedTimeOnMonth() + durationTime)
							.month(history.getMonth())
							.day(history.getDay())
							.device(history.getDevice())
							.wifiService(service)
							.build());
		}
	}

	private void browseMonthAndSaveNotConnectTodayHistoryMeta(
			Optional<ConnectHistoryMetaEntity> monthHistoryMeta,
			WifiServiceEntity service,
			DeviceEntity device,
			LocalDateTime now,
			Long durationTime) {

		if (monthHistoryMeta.isEmpty()) {
			// 해당 달에 처음 접속하는 경우
			connectHistoryMetaRepository.save(
					ConnectHistoryMetaEntity.builder()
							.connectedTimeOnDay(durationTime)
							.connectedTimeOnMonth(durationTime)
							.month((long) now.getMonth().getValue())
							.day((long) now.getDayOfMonth())
							.device(device)
							.wifiService(service)
							.build());
		} else {
			// 해당 달에 접속 기록이 있는 경우
			ConnectHistoryMetaEntity history = monthHistoryMeta.get();

			// 해당 달의 최근 접속 기록을 바탕으로 당일 접속 기록 메타 데이터 생성
			connectHistoryMetaRepository.save(
					ConnectHistoryMetaEntity.builder()
							.connectedTimeOnDay(history.getConnectedTimeOnDay() + durationTime)
							.connectedTimeOnMonth(history.getConnectedTimeOnMonth() + durationTime)
							.month(history.getMonth())
							.day((long) now.getDayOfMonth())
							.device(device)
							.wifiService(service)
							.build());
		}
	}
}
