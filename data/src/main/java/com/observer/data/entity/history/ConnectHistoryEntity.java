package com.observer.data.entity.history;

import com.observer.data.entity.DiviceAndRouterFKBaseEntity;
import java.time.LocalDateTime;
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
@Entity(name = "connect_history")
@Table(name = "connect_history_tb")
@SQLDelete(sql = "UPDATE connect_history_tb SET deleted=true WHERE id = ?")
public class ConnectHistoryEntity extends DiviceAndRouterFKBaseEntity {

	@Column(name = "connect_time", nullable = false, updatable = false)
	private LocalDateTime connectTime;

	@Column(name = "check_time", nullable = false)
	private LocalDateTime checkTime;

	@Column(name = "disconnect_time", nullable = false, updatable = false)
	private LocalDateTime disConnectTime;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "connect_status", nullable = false)
	private ConnectStatus connectStatus = ConnectStatus.CONNECTED;

	@Builder.Default
	@Column(name = "record", columnDefinition = "json")
	private String record = "{}";
}
