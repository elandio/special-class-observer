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
 * This prototype was developed for the Mobile Device Programming class
 * in the Spring Semester 2012 at the University of Wisconsin Parkside.
 * 
 * @author Michael Tiede, Matthias Kleinert, Moritz Duettmann
 * @since May 2012
 */
public class ListViewAdapter extends SimpleCursorAdapter {

    private static final int[] themeColorsDark = new int[] { 
        0x55666666, // row 1 background  
        0x55353535, // row 2 background
        0xfff3f3f3, // row 1 text color  
        0xfff9f9f9  // row 2 text color
        };
    private static final int[] themeColorsLight = new int[] { 
        0x55d9d9d9, // row 1 background  
        0x55eeeeee, // row 2 background
        0xff000000, // row 1 text color
        0xff101010  // row 2 text color
        };
    
    protected Cursor cursor;
    protected int theme;

	public ListViewAdapter(Context context, int layout, int theme, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.cursor = c;
		this.theme = theme;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}

	public int[] getThemeColors() {
		if (theme == 1) {
			return themeColorsLight;
		} else {
			return themeColorsDark;
		}
	}

//    @Override
//    public Filter getFilter() {
//        Log.d(TAG, "begin getFilter");
//        if (newFilter == null) {
//            newFilter = new Filter() {
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    // TODO Auto-generated method stub
//                    Log.d(TAG, "publishResults");
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    Log.d(TAG, "performFiltering");
//
//                    constraint = constraint.toString().toLowerCase();
//                    Log.d(TAG, "constraint : " + constraint);
//
//                    List<Object> filteredList = new LinkedList<Object>();
//
//                    for (int i = 0; i < getCount(); i++) {
//                        Object newObj = getItem(i);
//
//                        if (newObj.equals(constraint)) {
//
//                            filteredList.add(newObj);
//                        }
//                    }
//
//                    FilterResults newFilterResults = new FilterResults();
//                    newFilterResults.count = filteredList.size();
//                    newFilterResults.values = filteredList;
//                    return newFilterResults;
//                }
//            };
//        }
//        return newFilter;
//    }
}