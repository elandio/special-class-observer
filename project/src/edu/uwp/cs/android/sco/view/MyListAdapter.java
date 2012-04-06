/**
 * 
 */
package edu.uwp.cs.android.sco.view;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;


/**
 * @author Moritz
 *
 */
public class MyListAdapter extends SimpleCursorAdapter {

    private int[] colors = new int[] { 0x30ffffff, 0x30808080 };
    
    public MyListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);

      int colorPos = position % colors.length;
      view.setBackgroundColor(colors[colorPos]);
      return view;
    }
}
