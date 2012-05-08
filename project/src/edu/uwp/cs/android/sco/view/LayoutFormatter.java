package edu.uwp.cs.android.sco.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This prototype was developed for the Mobile Device Programming class
 * in the Spring Semester 2012 at the University of Wisconsin Parkside.
 * 
 * @author Michael Tiede, Matthias Kleinert, Moritz Duettmann
 * @since May 2012
 */
public class LayoutFormatter {
	
	public static String timestampToString(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        String format = "yyyy/MM/dd hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
	}
	
	public static String lastModification(long time) {
		return "Last modification: " + timestampToString(time);
	}

}
