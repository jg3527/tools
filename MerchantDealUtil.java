import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.TimeZone;

public class MerchantDealUtil {
	private static long weekDiffInMills = 604800000l;
	private static Calendar calendar = Calendar.getInstance();

	private static LocalTime localTimeFromTimestamp(Long timestamp) {
		calendar.setTimeInMillis(timestamp);
		return LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));
	}

	private static int getDayOfMonth(long timestamp, TimeZone timeZone){
		calendar.setTimeInMillis(timestamp);
		calendar.setTimeZone(timeZone);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	private static int weekDifference(long ts1, long ts2){
		Instant d1 = Instant.ofEpochMilli(ts1);
		Instant d2 = Instant.ofEpochMilli(ts2);

		LocalDateTime startDate = LocalDateTime.ofInstant(d1, ZoneId.systemDefault());
		LocalDateTime endDate = LocalDateTime.ofInstant(d2, ZoneId.systemDefault());
		return Math.abs((int) ChronoUnit.WEEKS.between(startDate, endDate));
	}

	/**
	 * Takes into account the recurrence value while validating a deal
	 * 
	 * Recurrence - only accounts for days - timings are retained from the
	 * original
	 * 
	 * D1 - everyday
	 * 
	 * W1 - same day of the week as original day
	 * 
	 * WD12345 - weekdays - On days 1,2,3,4,5 of the week at startTime
	 * 
	 * WD67 - weekdays - On days 6, 7 of the week at startTime
	 * 
	 * M1 - once every month on same date as original
	 * 
	 * @param deal
	 * @param timestamp
	 * @return
	 */
	public static boolean isDealValidForTimestamp(Long startTimeMills,
			Long endTimeTMills,
			Long timestamp, String recurring) {
//		System.out.println("deal.startTime " + deal.merchant.ID);
//
//		System.out.println("deal.startTime " + deal.startTime);
//		System.out.println("deal.endTime " + deal.endTime);
//		System.out.println("timestamp " + timestamp);
//		//long startTimeMills = deal.startTime * 1000;
		//long endTimeTMills = deal.endTime * 1000;
		//timestamp *= 1000;
			TimeZone timeZone = TimeZone.getDefault();
		if (startTimeMills < timestamp && endTimeTMills > timestamp) {
			return true;
		} else if (startTimeMills < timestamp){
			boolean isTimeValid = isTimeBetween(startTimeMills, endTimeTMills, timestamp);
			// need to check for recurrences
			//System.out.println(recurring);
			switch (recurring) {
			case "D1":
				return isTimeValid;
			case "W1":
				return isWeeklyDealValid(startTimeMills, endTimeTMills, timestamp);
			case "WD12345":
				return isWeekdays(timestamp, timeZone) && isTimeValid;
			case "WD67":
				return isWeekends(timestamp, timeZone) && isTimeValid;
			case "M1":
				return isTimeValid &&
						isDateBetween(startTimeMills, endTimeTMills, timestamp, timeZone);
			default:
				break;
			}
		}
		return false;
	}
	private static boolean isWeeklyDealValid(long startTime, long endTime, long timestamp){
		int weekDifference = weekDifference(startTime, timestamp);
		long newStartTime = startTime + weekDiffInMills * weekDifference;
		long newEndTime = endTime + weekDiffInMills * weekDifference;
		return timestamp > newStartTime && timestamp < newEndTime;
	}

	private static int getDayOfWeek(long timestamp, TimeZone timeZone){
		calendar.setTimeInMillis(timestamp);
		calendar.setTimeZone(timeZone);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	private static boolean isWeekdays(long timestamp, TimeZone timeZone){
		int dayOfWeek = getDayOfWeek(timestamp, timeZone);
		return dayOfWeek > 1 && dayOfWeek < 7;
	}

	private static boolean isWeekends(long timestamp, TimeZone timeZone){
		
		int dayOfWeek = getDayOfWeek(timestamp, timeZone);
		//System.out.println(dayOfWeek);
		return dayOfWeek == 1 || dayOfWeek == 7;
	}
	private static boolean isTimeBetween(long startTimeMills, long endTimeTMills,
									long timestamp){
		LocalTime startTime = localTimeFromTimestamp(startTimeMills),
				endTime = localTimeFromTimestamp(endTimeTMills),
				currentTime = localTimeFromTimestamp(timestamp);
		/**
		 * 2 Cases:
		 * 1. Start time is smaller than end time, eg. 13:00 - 18:00;
		 * 2. Start time is bigger than end time, eg. 23:00 - 03:00;
		 * */
		if(startTime.compareTo(endTime) <= 0){
			return currentTime.compareTo(startTime) > 0 &&
				currentTime.compareTo(endTime) < 0;
		} else{
			return currentTime.compareTo(startTime) > 0 || currentTime.compareTo(endTime) < 0;
		}
	}

	private static boolean isDateBetween(long startTimeMills, long endTimeTMills,
										 long timestamp, TimeZone timeZone){
		long day = getDayOfMonth(timestamp, timeZone);
		return day >= getDayOfMonth(startTimeMills, timeZone) &&
				day <= getDayOfMonth(endTimeTMills, timeZone);
	}

}
