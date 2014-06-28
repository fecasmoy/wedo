package com.wedo.businessserver.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class DateUtils {
	public static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy";
	private static final String DEFAULTDATEPATTERN = "yyyy-MM-dd";

	public static String getDatePattern() {
		/* 34 */return "yyyy-MM-dd";
	}

	public static String getToday() {
		/* 44 */return format(now());
	}

	public static Date now() {
		/* 54 */return nowCal().getTime();
	}

	public static Calendar nowCal() {
		/* 64 */return Calendar.getInstance();
	}

	public static Calendar date2Cal(Date date) {
		/* 75 */Calendar c = Calendar.getInstance();
		/* 76 */c.setTime(date);
		/* 77 */return c;
	}

	public static Calendar nextDay() {
		/* 87 */return nextDay(nowCal());
	}

	public static Calendar nextMonth() {
		/* 97 */return nextMonth(nowCal());
	}

	public static Calendar nextYear() {
		return nextMonth(nowCal());
	}

	public static Calendar nextDay(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return afterDays(cal, 1);
	}

	public static Calendar nextMonth(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return afterMonths(cal, 1);
	}

	public static Calendar nextYear(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return afterYesrs(cal, 1);
	}

	public static Calendar beforeDays(Calendar cal, int n) {
		if (cal == null) {
			return null;
		}
		Calendar c = (Calendar) cal.clone();
		c.set(6, cal.get(6) - n);
		return c;
	}

	public static Calendar afterDays(Calendar cal, int n) {
		if (cal == null) {
			return null;
		}
		Calendar c = (Calendar) cal.clone();
		c.set(6, cal.get(6) + n);
		return c;
	}

	public static Calendar afterMonths(Calendar cal, int n) {
		if (cal == null) {
			return null;
		}
		Calendar c = (Calendar) cal.clone();
		c.set(2, cal.get(2) + n);
		return c;
	}

	public static Calendar afterYesrs(Calendar cal, int n) {
		if (cal == null) {
			return null;
		}
		Calendar c = (Calendar) cal.clone();
		c.set(1, cal.get(1) + n);
		return c;
	}

	public static String format(Date date) {
		return date == null ? "" : format(date, getDatePattern());
	}

	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}

	public static String format(Calendar cal, String pattern) {
		return cal == null ? "" : new SimpleDateFormat(pattern).format(cal
				.getTime());
	}

	public static Date parse(String strDate) throws ParseException {
		return StringUtils.isBlank(strDate) ? null : parse(strDate,
				getDatePattern());
	}

	public static Date parse(String strDate, String pattern)
			throws ParseException {
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}

	public static Calendar parseCalendar(String strDate, String pattern)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date date = StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
		if (date != null) {
			cal.setTime(date);
			return cal;
		}

		throw new ParseException(strDate, 0);
	}

	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(2, n);
		return cal.getTime();
	}
}
