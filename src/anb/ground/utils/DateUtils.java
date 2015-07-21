package anb.ground.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	public static final SimpleDateFormat format = new SimpleDateFormat("MM.dd", Locale.getDefault());
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
	public static int currentYear = Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR);

	public static String getDateString(long stamp) {
		Date date = new Date(stamp);
		return format.format(date);
	}

	public static String getDateString(Date date) {
		return format.format(date);
	}

	public static String getTimeString(int hour, int min) {
		return ((hour < 10) ? "0" + hour : "" + hour) + ":" + ((min < 10) ? "0" + min : "" + min);
	}

	public static String getDay(long stamp) {
		StringBuilder sb = new StringBuilder();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(stamp);

		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			sb.append("MON");
			break;
		case Calendar.TUESDAY:
			sb.append("TUE");
			break;
		case Calendar.WEDNESDAY:
			sb.append("WED");
			break;
		case Calendar.THURSDAY:
			sb.append("THU");
			break;
		case Calendar.FRIDAY:
			sb.append("FRI");
			break;
		case Calendar.SATURDAY:
			sb.append("SAT");
			break;
		case Calendar.SUNDAY:
			sb.append("SUN");
			break;
		}

		return sb.toString();
	}

	public static Date getDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.set(year, month, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static long getStamp(int year, int month, int day, int hour, int min) {
		if (hour == 24 && min == 0) {
			hour = 23;
			min = 59;
		}

		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.set(year, month, day, hour, min);
		return c.getTimeInMillis();
	}

	public static long getStamp(String date, int hour, int min) {
		int year = Integer.valueOf(date.substring(0, 4));
		int month = Integer.valueOf(date.substring(5, 7)) - 1;
		int day = Integer.valueOf(date.substring(8));

		return getStamp(year, month, day, hour, min);
	}

	public static String getMatchTimeString(long startTime, long endTime) {
		StringBuilder sb = new StringBuilder(timeFormat.format(new Date(startTime)));
		sb.append(" ~ " + timeFormat.format(new Date(endTime)));
		return sb.toString();
	}

	public static String getTimeFromStamp(long stamp) {
		return timeFormat.format(new Date(stamp));
	}

	public static long getToday() {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		return c.getTimeInMillis();
	}
}
