package com.observer.domain.service.router;

import com.observer.domain.external.dao.router.RouterDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckMemberCanPostRouterService {

	private final RouterDao routerDao;

	public boolean execute(Long memberId) {
		return !routerDao.existsByMemberIdAndDeletedFalse(memberId);
	}
}
