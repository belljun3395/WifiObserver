package com.wifi.obs.data.mysql.entity.member;

import static com.wifi.obs.data.mysql.entity.member.MemberEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import javax.persistence.*;
import lombok.*;
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
		indexes = @Index(name = "idx_member_certification", columnList = "member_certification"))
@SQLDelete(sql = "UPDATE member_tb SET deleted=true WHERE id = ?")
public class MemberEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "member";

	@Column(name = ENTITY_PREFIX + "_certification", nullable = false)
	private String certification;

	@Column(name = ENTITY_PREFIX + "_password", nullable = false)
	private String password;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	private MemberStatus status = MemberStatus.NORMAL;
}
