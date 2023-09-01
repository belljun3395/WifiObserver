package com.wifi.obs.data.mysql.entity.history;

import static com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = ENTITY_PREFIX + "_entity")
@EntityListeners({AuditingEntityListener.class})
@Table(
		name = ENTITY_PREFIX + "_tb",
		indexes = {
			@Index(name = "idx_wifi_service_id_device_id", columnList = "wifi_service_id, device_id"),
			@Index(name = "idx_start_time", columnList = "start_time")
		})
@SQLDelete(sql = "UPDATE connect_history_tb SET deleted=true WHERE id = ?")
public class ConnectHistoryEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "connect_history";

	@Column(name = "start_time", nullable = false, updatable = false)
	private LocalDateTime startTime;

	@Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = DeviceEntity.ENTITY_PREFIX + "_id",
			nullable = false,
			foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
			updatable = false)
	private DeviceEntity device;

	@Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = WifiServiceEntity.ENTITY_PREFIX + "_id",
			nullable = false,
			foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
			updatable = false)
	private WifiServiceEntity wifiService;
}
