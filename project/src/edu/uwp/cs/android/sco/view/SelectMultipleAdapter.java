package edu.uwp.cs.android.sco.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class SelectMultipleAdapter extends SimpleCursorAdapter {

	private int[] colors = new int[] { 0xff0000ff, 0xff00ff00 };
	
	Context context;

	public SelectMultipleAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = super.getView(position, convertView, parent);
	    int colorPos = position % colors.length;
	    view.setBackgroundColor(colors[colorPos]);
	    return view;
	}
	
}
