package edu.uwp.cs.android.sco.view;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.entities.CourseDao;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This prototype was developed for the Mobile Device Programming class
 * in the Spring Semester 2012 at the University of Wisconsin Parkside.
 * 
 * @author Michael Tiede, Matthias Kleinert, Moritz Duettmann
 * @since May 2012
 */
public class CourseListViewAdapter extends ListViewAdapter {

	public CourseListViewAdapter(Context context, int layout, int theme, Cursor c, String[] from, int[] to) {
		super(context, layout, theme, c, from, to);
		this.cursor = c;
		this.theme = theme;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int[] themeColors = getThemeColors();
		int colorPos = position % 2;

		view.setBackgroundColor(themeColors[colorPos]);

		// course name color
		TextView courseName = (TextView) view.findViewById(R.id.course_row_name);
		courseName.setTextColor(themeColors[colorPos + 2]);

		// course category color
		TextView courseCategory = (TextView) view.findViewById(R.id.course_row_category);
		courseCategory.setTextColor(themeColors[colorPos + 2]);

		// semester and year color
		TextView semesterAndYear = (TextView) view.findViewById(R.id.course_row_semesterYear);
		int year = cursor.getInt(cursor.getColumnIndex(CourseDao.Properties.Year.columnName));
		String semester = cursor.getString(cursor.getColumnIndex(CourseDao.Properties.Semester.columnName));
		semesterAndYear.setText(semester + " " + year);

		return view;
	}  

}
