package com.wifi.obs.data.mysql.entity.wifi.service;

import static com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import javax.persistence.*;
import lombok.*;
import lombok.ToString.Exclude;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = ENTITY_PREFIX + "_entity")
@EntityListeners({AuditingEntityListener.class})
@Table(
		name = ENTITY_PREFIX + "_tb",
		indexes = {
			@Index(name = "idx_member_id", columnList = "member_id"),
			@Index(name = "idx_wifi_status", columnList = "wifi_service_status")
		})
@SQLDelete(sql = "UPDATE wifi_service_tb SET deleted=true WHERE id = ?")
public class WifiServiceEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "wifi_service";

	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_type", nullable = false)
	private WifiServiceType serviceType;

	@Builder.Default
	@Column(name = ENTITY_PREFIX + "_cycle", nullable = false)
	private Long cycle = 10L;

	@Builder.Default
	@Column(name = ENTITY_PREFIX + "_standard_time", nullable = false)
	private Long standardTime = 9L;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	private WifiStatus status = WifiStatus.ON;

	@Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = MemberEntity.ENTITY_PREFIX + "_id",
			nullable = false,
			foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT),
			updatable = false)
	private MemberEntity member;

	@Exclude
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(
			name = WifiAuthEntity.ENTITY_PREFIX + "_id",
			nullable = false,
			foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private WifiAuthEntity wifiAuthEntity;
}
