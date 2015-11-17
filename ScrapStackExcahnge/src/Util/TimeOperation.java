package Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeOperation {
	public static long GetEpochTimeOfThreeMonthAgo(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		Date result = cal.getTime();

		long epochTime = result.getTime()/1000;
		return epochTime;
	}
	
	public static String GetTimeFromEpoch(long unixSeconds){
		Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
		String result = sdf.format(date);
		return result;
	}
}
