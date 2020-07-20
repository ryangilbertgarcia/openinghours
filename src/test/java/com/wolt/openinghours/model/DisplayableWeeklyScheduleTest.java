package com.wolt.openinghours.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class DisplayableWeeklyScheduleTest {

	@Test
	void testWeekClosed() {
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(Collections.emptyList());
		assertEquals("MONDAY: CLOSED\n" + "TUESDAY: CLOSED\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
				+ "FRIDAY: CLOSED\n" + "SATURDAY: CLOSED\n" + "SUNDAY: CLOSED\n", weeklySchedule.toString());
	}

	@Test
	void testSingleOpeningHourADay() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(18, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals("MONDAY: CLOSED\n" + "TUESDAY: 10 AM - 6 PM\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
				+ "FRIDAY: CLOSED\n" + "SATURDAY: CLOSED\n" + "SUNDAY: CLOSED\n", weeklySchedule.toString());
	}

	@Test
	void testMultipleOpeningHoursADay() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(11, 30), false),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(12, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(18, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals(
				"MONDAY: CLOSED\n" + "TUESDAY: 10 AM - 11:30 AM, 12 PM - 6 PM\n" + "WEDNESDAY: CLOSED\n"
						+ "THURSDAY: CLOSED\n" + "FRIDAY: CLOSED\n" + "SATURDAY: CLOSED\n" + "SUNDAY: CLOSED\n",
				weeklySchedule.toString());
	}

	@Test
	void tesClosingHourMissingOnNextDay() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.FRIDAY, LocalTime.of(12, 0), true),
				new BusinessHour(DayOfWeek.SATURDAY, LocalTime.of(12, 0), true),
				new BusinessHour(DayOfWeek.SATURDAY, LocalTime.of(18, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals(
				"MONDAY: CLOSED\n" + "TUESDAY: CLOSED\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
						+ "FRIDAY: 12 PM - 11:59 PM\n" + "SATURDAY: 12 PM - 6 PM\n" + "SUNDAY: CLOSED\n",
				weeklySchedule.toString());
	}

	@Test
	void testClosingHourTheNextDay() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.FRIDAY, LocalTime.of(12, 0), true),
				new BusinessHour(DayOfWeek.SATURDAY, LocalTime.of(1, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals(
				"MONDAY: CLOSED\n" + "TUESDAY: CLOSED\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
						+ "FRIDAY: 12 PM - 1 AM\n" + "SATURDAY: CLOSED\n" + "SUNDAY: CLOSED\n",
				weeklySchedule.toString());
	}

	@Test
	void testClosingHourTheNextWeek() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.SUNDAY, LocalTime.of(12, 0), true),
				new BusinessHour(DayOfWeek.MONDAY, LocalTime.of(1, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals(
				"MONDAY: CLOSED\n" + "TUESDAY: CLOSED\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
						+ "FRIDAY: CLOSED\n" + "SATURDAY: CLOSED\n" + "SUNDAY: 12 PM - 1 AM\n",
				weeklySchedule.toString());
	}

	@Test
	void testShouldIgnoreUnpairedClosingHours() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(1, 0), false),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(9, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(18, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals("MONDAY: CLOSED\n" + "TUESDAY: 9 AM - 6 PM\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: CLOSED\n"
				+ "FRIDAY: CLOSED\n" + "SATURDAY: CLOSED\n" + "SUNDAY: CLOSED\n", weeklySchedule.toString());
	}

	@Test
	void testShouldThrowExceptionOnUnpairedOpeningHours() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(1, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(9, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(18, 0), false));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertThrows(IllegalArgumentException.class, () -> weeklySchedule.toString());
	}

	@Test
	void testFullWeek() {
		List<BusinessHour> hours = Arrays.asList(new BusinessHour(DayOfWeek.MONDAY, LocalTime.of(1, 0), false),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(18, 0), false),
				new BusinessHour(DayOfWeek.THURSDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.THURSDAY, LocalTime.of(18, 0), false),
				new BusinessHour(DayOfWeek.FRIDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.SATURDAY, LocalTime.of(1, 0), false),
				new BusinessHour(DayOfWeek.SATURDAY, LocalTime.of(10, 0), true),
				new BusinessHour(DayOfWeek.SUNDAY, LocalTime.of(1, 0), false),
				new BusinessHour(DayOfWeek.SUNDAY, LocalTime.of(12, 0), true));
		DisplayableWeeklySchedule weeklySchedule = new DisplayableWeeklySchedule(hours);
		assertEquals(
				"MONDAY: CLOSED\n" + "TUESDAY: 10 AM - 6 PM\n" + "WEDNESDAY: CLOSED\n" + "THURSDAY: 10 AM - 6 PM\n"
						+ "FRIDAY: 10 AM - 1 AM\n" + "SATURDAY: 10 AM - 1 AM\n" + "SUNDAY: 12 PM - 1 AM\n",
				weeklySchedule.toString());
	}
}
