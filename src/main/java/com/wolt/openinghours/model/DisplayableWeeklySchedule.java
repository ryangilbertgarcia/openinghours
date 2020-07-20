package com.wolt.openinghours.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class DisplayableWeeklySchedule {

	private TreeSet<BusinessHour> weeklyHours;

	public DisplayableWeeklySchedule(TreeSet<BusinessHour> hours) {
		weeklyHours = hours;
	}

	public DisplayableWeeklySchedule(Collection<BusinessHour> hours) {
		weeklyHours = new TreeSet<BusinessHour>(hours);
	}

	public Set<BusinessHour> getWeeklyHours() {
		return weeklyHours;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<DayOfWeek, List<BusinessHour>> currentEntry : getFormattedModel().entrySet()) {
			String daySchedule = formatHoursForDay(currentEntry.getValue());
			buffer.append(String.format("%s: %s\n", currentEntry.getKey(), daySchedule));
		}
		return buffer.toString();
	}

	/**
	 * Return a formatted string of all opening and closing times given a list of hours for a day.
	 * 
	 * @param hours Assumes a list of business hours for a specific day
	 * @return A formatted String listing the opening and closing hours provided in one line
	 */
	private String formatHoursForDay(List<BusinessHour> hours) {
		Stack<BusinessHour> openingHours = new Stack<>();
		StringBuffer buffer = new StringBuffer();
		if (hours.stream().map(BusinessHour::isOpen).noneMatch(isOpen -> isOpen)) {
			return "CLOSED";
		} else {
			for (BusinessHour currentHour : hours) {
				if (currentHour.isOpen()) {
					if (openingHours.isEmpty()) {
						openingHours.push(currentHour);
					} else {
						throw new IllegalArgumentException(
								String.format("Opening hour [%s, %s] has no closing time set.",
										currentHour.getDayOfWeek(), currentHour));
					}
				} else if (hours.indexOf(currentHour) > 0) {
					buffer.append(String.format("%s - %s", openingHours.pop(), currentHour));
					if (hours.lastIndexOf(currentHour) + 1 < hours.size()) {
						buffer.append(", ");
					}
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Produce a map to aid in displaying opening and closing hours in a readable format
	 * 
	 * @return A mapping of each day in a week to a list of all relevant opening and closing hours 
	 */
	private Map<DayOfWeek, List<BusinessHour>> getFormattedModel() {
		LinkedHashMap<DayOfWeek, List<BusinessHour>> formattedModel = new LinkedHashMap<>();
		Arrays.asList(DayOfWeek.values()).stream()
				.forEach(day -> formattedModel.put(day, new ArrayList<BusinessHour>()));
		for (BusinessHour current : weeklyHours) {
			formattedModel.get(current.getDayOfWeek()).add(current);
			addClosingHourIfMissing(current, formattedModel);
		}
		return formattedModel;
	}

	/**
	 * Obtain closing hour from the succeeding day when applicable.
	 * If current open business hour is not closed in the succeeding day,
	 *     then add a displayable entry that closes business hours at the end of the current day.
	 */
	private void addClosingHourIfMissing(final BusinessHour current,
			final LinkedHashMap<DayOfWeek, List<BusinessHour>> formattedModel) {
		BusinessHour peek = Optional.ofNullable(weeklyHours.higher(current)).orElse(weeklyHours.first());
		if (!peek.getDayOfWeek().equals(current.getDayOfWeek()) && current.isOpen()) {
			formattedModel.get(current.getDayOfWeek())
					.add(peek.isOpen() ? new BusinessHour(current.getDayOfWeek(), LocalTime.MAX, false) : peek);
		}
	}
}
