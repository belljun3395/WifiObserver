package com.wifi.obs.data.mysql.entity.support;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.exception.NotLoadedException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DeviceEntitySupporter {

	private EntityManager em;

	@Autowired
	public void setEntityManager(
			@Qualifier(JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME) EntityManager entityManager) {
		this.em = entityManager;
	}

	public DeviceEntity getDeviceIdEntity(Long id) {
		return DeviceEntity.builder().id(id).build();
	}

	public DeviceEntity getReferenceEntity(Long id) {
		boolean isLoaded =
				em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(DeviceEntity.class);
		if (isLoaded) {
			return em.getReference(DeviceEntity.class, id);
		}

		throw new NotLoadedException("DeviceEntity is not loaded");
	}
}
