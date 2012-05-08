package edu.uwp.cs.android.sco.view;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.entities.StudentDao;
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
public class StudentListViewAdapter extends ListViewAdapter {

    private static final int[] highlightLevelColors = new int[] { 0xff009cff, // Level 1
            0xffffc000, // Level 2
            0xffff0000 // Level 3
    };

    public StudentListViewAdapter(Context context, int layout, int theme, Cursor c, String[] from, int[] to) {
        super(context, layout, theme, c, from, to);
        this.cursor = c;
        this.theme = theme;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int[] themeColors = getThemeColors();
        int colorPos = position % 2;

        view.findViewById(R.id.listViewRow).setBackgroundColor(themeColors[colorPos]);

        // get disability level and change text color if necessary
        int disabilityLevel = cursor.getInt(cursor.getColumnIndex(StudentDao.Properties.DisabilityLevel.columnName));
        int fontColor;
        if (disabilityLevel != 0) {
            fontColor = highlightLevelColors[disabilityLevel - 1];
        }
        else {
            fontColor = themeColors[colorPos + 2];
        }

        // firstName color
        TextView firstName = (TextView) view.findViewById(R.id.student_select_firstName);
        firstName.setTextColor(fontColor);

        // lastName color
        TextView lastName = (TextView) view.findViewById(R.id.student_select_lastName);
        lastName.setTextColor(fontColor);

        // lastModified format timestamp
        long time = cursor.getLong(cursor.getColumnIndex(StudentDao.Properties.LastModified.columnName));
        TextView lastModified = (TextView) view.findViewById(R.id.student_select_lastModified);
        lastModified.setText(LayoutFormatter.lastModification(time));

        return view;
    }

}
