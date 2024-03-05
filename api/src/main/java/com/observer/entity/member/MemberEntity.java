package com.observer.entity.member;

import com.observer.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
		name = "member",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "uk_member_api_key",
					columnNames = {"api_key"}),
		})
@SQLDelete(sql = "UPDATE member SET deleted=true where id=?")
public class MemberEntity extends BaseEntity {

	@Convert(converter = CertificationDataConverter.class)
	@Column(name = "certification", nullable = false)
	private CertificationData certification;

	@Convert(converter = MemberPasswordDataConverter.class)
	@Column(name = "password", nullable = false)
	private MemberPasswordData password;

	@Column(name = "api_key", nullable = false)
	private String apiKey;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(name = "member_st", nullable = false)
	private MemberStatus status = MemberStatus.REGULAR;

	@Builder.Default
	@Column(name = "resource", columnDefinition = "json")
	private String resource = "{}";
}
