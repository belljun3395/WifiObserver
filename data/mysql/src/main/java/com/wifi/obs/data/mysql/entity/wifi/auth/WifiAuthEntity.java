package com.wifi.obs.data.mysql.entity.wifi.auth;

import static com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity.ENTITY_PREFIX;

import com.wifi.obs.data.mysql.entity.BaseEntity;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity(name = ENTITY_PREFIX + "_entity")
@Table(name = ENTITY_PREFIX + "_tb")
@SQLDelete(sql = "UPDATE wifi_auth_tb SET deleted=true WHERE id = ?")
public class WifiAuthEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "wifi_auth";

	@Column(name = ENTITY_PREFIX + "_host", nullable = false)
	private String host;

	@Column(name = ENTITY_PREFIX + "_certification", nullable = false)
	private String certification;

	@Column(name = ENTITY_PREFIX + "_password", nullable = false)
	private String password;
}
