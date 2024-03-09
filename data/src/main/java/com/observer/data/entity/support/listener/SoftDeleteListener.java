package com.observer.data.entity.support.listener;

import com.observer.data.entity.BaseEntity;
import javax.persistence.PreRemove;

/** 엔티티에 삭제되었다는 정보를 반영하기 위해서 리스너가 필요하다. */
public class SoftDeleteListener {

	@PreRemove
	private void preRemove(BaseEntity entity) {
		entity.delete();
	}
}
