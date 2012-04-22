package edu.uwp.cs.android.sco.view;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.entities.StudentDao;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class StudentListViewAdapter extends SimpleCursorAdapter {
        
	private static int[] themeColorsDark = new int[] { 
        0x55666666, // row 1 background  
        0x55353535, // row 2 background
        0xfff3f3f3, // row 1 text color  
        0xfff9f9f9  // row 2 text color
        };
	private static int[] themeColorsLight = new int[] { 
        0x55d9d9d9, // row 1 background  
        0x55eeeeee, // row 2 background
        0xff000000, // row 1 text color
        0xff101010  // row 2 text color
        };

	protected Cursor cursor;
	protected int theme;

	public StudentListViewAdapter(Context context, int layout, int theme,
			Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.cursor = c;
		this.theme = theme;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int[] themeColors = getThemeColors();
		int colorPos = position % 2;

		view.findViewById(R.id.listViewRow).setBackgroundColor(themeColors[colorPos]);

		// firstName color
		TextView firstName = (TextView) view
				.findViewById(R.id.student_select_firstName);
		firstName.setTextColor(themeColors[colorPos + 2]);

		// lastName color
		TextView lastName = (TextView) view.findViewById(R.id.student_select_lastName);
		lastName.setTextColor(themeColors[colorPos + 2]);

		// lastModified format timestamp
		long time = cursor.getLong(cursor.getColumnIndex(StudentDao.Properties.LastModified.columnName));
		TextView lastModified = (TextView) view.findViewById(R.id.student_select_lastModified);
		lastModified.setText(LayoutFormatter.lastModification(time));

		return view;
	}
	
	public int[] getThemeColors() {
	    if (theme == 1) {
	            return themeColorsLight;
	    } else {
	            return themeColorsDark;
	    }
	}    

}
