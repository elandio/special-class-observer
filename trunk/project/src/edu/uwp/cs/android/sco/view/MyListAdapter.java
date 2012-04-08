/**
 * 
 */
package edu.uwp.cs.android.sco.view;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SimpleCursorAdapter;

/**
 * @author Moritz
 */
public class MyListAdapter extends SimpleCursorAdapter {

    private int[] colors = new int[] { 0x30ffffff, 0x30808080 };

//    private Filter newFilter;

//    private final static String TAG = "MySFilter";

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
