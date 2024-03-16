package com.observer.batch.job.browse.iptime;

import com.observer.batch.job.browse.exception.AbstractRouterException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

@RequiredArgsConstructor
public class IptimeBrowseSkipPolicy implements SkipPolicy {

	private final int skipLimit;

	@Override
	public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
		if (throwable instanceof AbstractRouterException && i < skipLimit) {
			return true;
		}
		return false;
	}
}
