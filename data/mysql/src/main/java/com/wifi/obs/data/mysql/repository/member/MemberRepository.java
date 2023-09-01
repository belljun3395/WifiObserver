package com.wifi.obs.data.mysql.repository.member;

import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = MemberEntity.class, idClass = Long.class)
public interface MemberRepository extends MemberJpaRepository, MemberCustomRepository {}
