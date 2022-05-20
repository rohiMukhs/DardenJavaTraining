package com.darden.dash.capacity.util;

import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void testInstantToTime() {
		String date = DateUtil.instantToTime(Instant.now());
		assertNotNull(date);
	}

	@Test
	void testInstantToDate() {
		String date = DateUtil.instantToDate(new Date());
		assertNotNull(date);
	}

}
