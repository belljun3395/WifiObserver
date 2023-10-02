package com.wifi.obs.data.mysql.entity.meta;

import static com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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
@SQLDelete(sql = "UPDATE connect_history_meta_tb SET deleted=true WHERE id = ?")
public class ConnectHistoryMetaEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "connect_history_meta";

	@Column(name = ENTITY_PREFIX + "_time_day", nullable = false, updatable = false)
	private Long connectedTimeOnDay;

	@Column(name = ENTITY_PREFIX + "_time_month", nullable = false, updatable = false)
	private Long connectedTimeOnMonth;

	@Column(name = ENTITY_PREFIX + "_month", updatable = false)
	private Long month;

	@Column(name = ENTITY_PREFIX + "_day", nullable = false, updatable = false)
	private Long day;

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
