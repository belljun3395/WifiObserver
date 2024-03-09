package com.observer.data.entity.router;

import com.observer.data.entity.MemberFKBaseEntity;
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
@AllArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = "router")
@Table(name = "router_tb")
@SQLDelete(sql = "UPDATE router_tb SET deleted=true WHERE id = ?")
public class RouterEntity extends MemberFKBaseEntity {

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private RouterServiceType serviceType;

	@Builder.Default
	@Column(name = "cycle", nullable = false)
	private Long cycle = 10L;

	@Builder.Default
	@Column(name = "standard_time", nullable = false)
	private Long standardTime = 9L;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private RouterStatus status = RouterStatus.ON;

	@Column(name = "host", nullable = false)
	private String host;

	@Column(name = "certification", nullable = false)
	private String certification;

	@Column(name = "password", nullable = false)
	private String password;
}
