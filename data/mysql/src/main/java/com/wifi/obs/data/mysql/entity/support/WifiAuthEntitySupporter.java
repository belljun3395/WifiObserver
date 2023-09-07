package com.wifi.obs.data.mysql.entity.support;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.exception.NotLoadedException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WifiAuthEntitySupporter {

	private EntityManager em;

	@Autowired
	public void setEntityManager(
			@Qualifier(JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME) EntityManager entityManager) {
		this.em = entityManager;
	}

	public WifiAuthEntity getWifiAuthIdEntity(Long id) {
		return WifiAuthEntity.builder().id(id).build();
	}

	public WifiAuthEntity getReferenceEntity(Long id) {
		boolean isLoaded =
				em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(WifiAuthEntity.class);
		if (isLoaded) {
			return em.getReference(WifiAuthEntity.class, id);
		}

		throw new NotLoadedException("WifiAuthEntity is not loaded");
	}
}
