package com.wifi.obs.data.mysql.entity.device;

import static com.wifi.obs.data.mysql.entity.device.DeviceEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
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
@Table(
		name = ENTITY_PREFIX + "_tb",
		indexes = {
			@Index(name = "idx_wifi_service_id_device_mac", columnList = "wifi_service_id, device_mac")
		})
@SQLDelete(sql = "UPDATE device_tb SET deleted=true WHERE id = ?")
public class DeviceEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "device";

	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_type", nullable = false, updatable = false)
	private DeviceType type;

	@Column(name = ENTITY_PREFIX + "_mac", nullable = false, updatable = false)
	private String mac;

	@Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = WifiServiceEntity.ENTITY_PREFIX + "_id",
			nullable = false,
			foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
			updatable = false)
	private WifiServiceEntity wifiService;
}
