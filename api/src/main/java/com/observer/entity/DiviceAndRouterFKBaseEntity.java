package com.observer.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class DiviceAndRouterFKBaseEntity extends BaseEntity {

	@Column(name = "device_fk", nullable = false)
	private Long deviceId;

	@Column(name = "router_fk", nullable = false)
	private Long routerId;
}
