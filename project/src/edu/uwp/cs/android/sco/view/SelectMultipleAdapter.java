package edu.uwp.cs.android.sco.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.entities.StudentDao;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SelectMultipleAdapter extends SimpleCursorAdapter {

	private int[] colors = new int[] { 0x55d9d9d9, 0x55eeeeee };
	
	Context context;
	Cursor cursor;
	

	public SelectMultipleAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
		this.cursor = c;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
	    View view = super.getView(position, convertView, parent);
	    int colorPos = position % colors.length;
	    view.findViewById(R.id.listViewRow).setBackgroundColor(colors[colorPos]);
	    
	    System.out.println("CURSOR: " + cursor.getString(0));
	    
	    long time = cursor.getLong(cursor.getColumnIndex(StudentDao.Properties.LastModified.columnName));

	    System.out.println(time);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        String format = "yyyy/MM/dd hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(cal.getTime());

        ((TextView) view.findViewById(R.id.student_select_lastModified)).setText("Last modification: " + dateString);

	    
	    return view;
	}
	
	public int[] getColors() {
		return colors;
	}
	
}
