package com.wifi.obs.data.mysql.entity.support;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.exception.NotLoadedException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WifiServiceEntitySupporter {

	private EntityManager em;

	@Autowired
	public void setEntityManager(
			@Qualifier(JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME) EntityManager entityManager) {
		this.em = entityManager;
	}

	public WifiServiceEntity getWifiServiceIdEntity(Long id) {
		return WifiServiceEntity.builder().id(id).build();
	}

	public WifiServiceEntity getReferenceEntity(Long id) {
		boolean isLoaded =
				em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(WifiServiceEntity.class);
		if (isLoaded) {
			return em.getReference(WifiServiceEntity.class, id);
		}

		throw new NotLoadedException("WifiServiceEntity is not loaded");
	}
}
