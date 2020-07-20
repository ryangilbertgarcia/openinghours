package com.wolt.openinghours.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class BusinessHourTest {

	@Test
	void testFormattingWithoutMinutes() {
		BusinessHour hour = new BusinessHour(DayOfWeek.MONDAY, LocalTime.of(9, 0), true);
		assertEquals("9 AM", hour.toString());
	}

	@Test
	void testFormattingWithMinutes() {
		BusinessHour hour = new BusinessHour(DayOfWeek.WEDNESDAY, LocalTime.of(12, 30), false);
		assertEquals("12:30 PM", hour.toString());
	}

}
