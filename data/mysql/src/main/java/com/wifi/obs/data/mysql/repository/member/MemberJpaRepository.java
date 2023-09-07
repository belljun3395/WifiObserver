package com.wifi.obs.data.mysql.repository.member;

import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

	Optional<MemberEntity> findByIdAndDeletedFalse(Long id);

	Optional<MemberEntity> findByCertificationAndDeletedFalse(String email);
}
