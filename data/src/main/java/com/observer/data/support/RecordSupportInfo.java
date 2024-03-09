package com.observer.data.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RecordSupportInfo {
	@Builder.Default private Long month = 0L;
	@Builder.Default private Long day = 0L;

	public void accumulate(Long time) {
		this.day += time;
		this.month += time;
	}

	public void resetDay() {
		this.day = 0L;
	}

	public void resetMonth() {
		this.month = 0L;
		this.day = 0L;
	}
}
