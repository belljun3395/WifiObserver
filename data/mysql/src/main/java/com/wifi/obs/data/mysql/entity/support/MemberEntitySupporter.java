package com.wifi.obs.data.mysql.entity.support;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.exception.NotLoadedException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MemberEntitySupporter {

	private EntityManager em;

	@Autowired
	public void setEntityManager(
			@Qualifier(JpaDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME) EntityManager entityManager) {
		this.em = entityManager;
	}

	public MemberEntity getMemberIdEntity(Long id) {
		return MemberEntity.builder().id(id).build();
	}

	public MemberEntity getReferenceEntity(Long id) {
		boolean isLoaded =
				em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(MemberEntity.class);
		if (isLoaded) {
			return em.getReference(MemberEntity.class, id);
		}

		throw new NotLoadedException("MemberEntity is not loaded");
	}
}
