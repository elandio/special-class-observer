package edu.uwp.cs.android.sco.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
