package edu.uwp.cs.android.sco.view;

import java.util.List;
import edu.uwp.cs.android.sco.StudentProfileActivity;
import edu.uwp.cs.android.sco.entities.Disability;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class StudentAdapter extends ArrayAdapter<Disability>{

    Context context;
    int layoutResourceId;
   
    public StudentAdapter(Context context, int layoutResourceId, List<Disability> list){
    	super(context, layoutResourceId, list);
    	this.context=context;
    	this.layoutResourceId=layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewWrapper wrapper;
        RatingBar rate;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            wrapper = new ViewWrapper(row);
            row.setTag(wrapper);
			rate=wrapper.getRatingBar();
			rate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					Integer myPosition=(Integer)ratingBar.getTag();
					Disability disabiltiy=((StudentProfileActivity)context).getDisability(myPosition);
					disabiltiy.setRating((int) rating);
				}
			});
        }
        else
        {
        	wrapper=(ViewWrapper)row.getTag();
        	rate=wrapper.getRatingBar();
        }
       
        Disability disability = ((StudentProfileActivity)context).getDisability(position);
        wrapper.getDisInfo().setText(disability.getInfo());
        wrapper.getDisName().setText(disability.getName());
        rate.setTag(new Integer(position));
        rate.setRating(disability.getRating());
       
        return row;
    }
}