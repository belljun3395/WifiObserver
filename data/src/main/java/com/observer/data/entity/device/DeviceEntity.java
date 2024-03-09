package com.observer.data.entity.device;

import com.observer.data.entity.RouterAndMemberFKBaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = "device")
@Table(name = "device_tb")
@SQLDelete(sql = "UPDATE device_tb SET deleted=true WHERE id = ?")
public class DeviceEntity extends RouterAndMemberFKBaseEntity {

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "device_type", nullable = false, updatable = false)
	private DeviceType type = DeviceType.NOTEBOOK;

	@Column(name = "mac", nullable = false, updatable = false)
	private String mac;

	@Builder.Default
	@Column(name = "info", columnDefinition = "json")
	private String info = "{}";
}
