package edu.uwp.cs.android.sco.view;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.entities.StudentDao;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StudentListViewAdapter extends ListViewAdapter {
	
	public StudentListViewAdapter(Context context, int layout, int theme, Cursor c, String[] from, int[] to) {
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
	    TextView firstName = (TextView) view.findViewById(R.id.student_select_firstName);
	    firstName.setTextColor(themeColors[colorPos+2]);
	    
	    // lastName color
	    TextView lastName = (TextView) view.findViewById(R.id.student_select_lastName);
	    lastName.setTextColor(themeColors[colorPos+2]);

	    // lastModified format timestamp
	    long time = cursor.getLong(cursor.getColumnIndex(StudentDao.Properties.LastModified.columnName));	    
	    TextView lastModified = (TextView) view.findViewById(R.id.student_select_lastModified);
	    lastModified.setText(LayoutFormatter.lastModification(time));
	    
	    return view;
	}
	
}
