package com.wolt.openinghours.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class BusinessHour implements Comparable<BusinessHour> {

	private LocalTime timeOfDay;
	private DayOfWeek dayOfWeek;
	private boolean isOpen;

	public BusinessHour(DayOfWeek dayOfWeek, LocalTime timeOfDay, boolean isOpen) {
		this.dayOfWeek = dayOfWeek;
		this.timeOfDay = timeOfDay;
		this.isOpen = isOpen;
	}

	public BusinessHour(String dayOfWeek, long localTime, String type) {
		this(DayOfWeek.valueOf(dayOfWeek.trim().toUpperCase()),
				LocalTime.ofSecondOfDay(localTime),
				type.trim().equals("open"));
	}

	public LocalTime getTimeOfDay() {
		return timeOfDay;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public int compareTo(BusinessHour comparableHour) {
		return Comparator.comparing(BusinessHour::getDayOfWeek)
				.thenComparing(BusinessHour::getTimeOfDay)
				.compare(this, comparableHour);
	}

	@Override
	public String toString() {
		String timePattern = timeOfDay.getMinute() == 0 ? "h a" : "h:mm a";
		return timeOfDay.format(DateTimeFormatter.ofPattern(timePattern));
	}

}
