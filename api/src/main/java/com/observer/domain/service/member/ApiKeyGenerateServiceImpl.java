package com.observer.domain.service.member;

import com.observer.domain.external.dao.member.MemberDao;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyGenerateServiceImpl implements ApiKeyGenerateService {

	private final MemberDao memberDao;

	@Override
	@Transactional(readOnly = true)
	public String generate() {
		boolean isExistApiKey = true;
		String apiKey = "";
		LocalTime now = LocalTime.now();
		// todo apikey 기준으로 락을 걸어 처리 해야 함
		while (isExistApiKey) {
			apiKey = doGenerate();
			isExistApiKey = memberDao.existsByApiKey(apiKey);
			if (LocalTime.now().isAfter(now.plusSeconds(10))) {
				log.warn("Too Long time to generate API Key");
				throw new RuntimeException("API Key generation is failed.");
			}
		}
		if (Objects.equals(apiKey, Strings.EMPTY)) {
			throw new RuntimeException("API Key generation is failed.");
		}
		return apiKey;
	}

	private String doGenerate() {
		Random random = new Random();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			list.add(String.valueOf(random.nextInt(10)));
		}
		for (int i = 0; i < 16; i++) {
			list.add(String.valueOf((char) (random.nextInt(26) + 65)));
		}

		Collections.shuffle(list);
		return list.toString().split("[\\[\\]]")[1].replaceAll(", ", "");
	}
}
