package edu.uwp.cs.android.sco.model;

import edu.uwp.cs.android.sco.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

@Deprecated
public class StudentAdapter extends ArrayAdapter<Disability>{

    Context context;
    int layoutResourceId;   
    Disability[] dis = null;
   
    public StudentAdapter(Context context, int layoutResourceId, Disability[] dis) {
        super(context, layoutResourceId, dis);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dis = dis;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DisabilityHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new DisabilityHolder();
            holder.ratingBar = (RatingBar)row.findViewById(R.id.ratingbar);
            holder.tvName = (TextView)row.findViewById(R.id.tvattribute);
            holder.tvInfo = (TextView)row.findViewById(R.id.tvinfo);
           
            row.setTag(holder);
        }
        else
        {
            holder = (DisabilityHolder)row.getTag();
        }
       
        Disability disability = dis[position];
        holder.tvName.setText(disability.name);
        holder.ratingBar.setRating(disability.rating);
        holder.tvInfo.setText(disability.info);
       
        return row;
    }
   
    static class DisabilityHolder
    {
    	
    	RatingBar ratingBar;
        TextView tvName;
        TextView tvInfo;
    }
}