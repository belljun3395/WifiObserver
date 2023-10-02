package com.wifi.obs.data.mysql.entity.history;

import static com.wifi.obs.data.mysql.entity.history.DisConnectHistoryEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = ENTITY_PREFIX + "_entity")
@Table(name = ENTITY_PREFIX + "_tb")
@SQLDelete(sql = "UPDATE disConnect_history_tb SET deleted=true WHERE id = ?")
public class DisConnectHistoryEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "disConnect_history";

	@Column(name = "end_time", nullable = false, updatable = false)
	private LocalDateTime endTime;

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
