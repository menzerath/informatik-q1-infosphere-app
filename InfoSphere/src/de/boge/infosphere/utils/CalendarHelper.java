package de.boge.infosphere.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarHelper {

	public static long parseFeedDate(String date) {
		SimpleDateFormat formatter;
		if (date.contains(" - ")) {
			date = date.split(" - ")[0].trim();
		}
		formatter = new SimpleDateFormat("d. MMMM yyyy");
		
		Date d = null;
		try {
			d = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d.getTime();
	}
}