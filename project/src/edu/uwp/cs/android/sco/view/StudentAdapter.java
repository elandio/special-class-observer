package edu.uwp.cs.android.sco.view;

import java.util.List;

import edu.uwp.cs.android.sco.R;
import edu.uwp.cs.android.sco.StudentProfileActivity;
import edu.uwp.cs.android.sco.entities.Disability;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class StudentAdapter extends ArrayAdapter<Disability> {
	
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

    private Context context;
    private int layout;
    private int theme;
   
    public StudentAdapter(Context context, int layout, List<Disability> list){
    	super(context, layout, list);
    	this.context = context;
    	this.layout = layout;
    	this.theme = 0;
    }

    @Override
    public View getView(int position, View convertViewRow, ViewGroup parent) {
        ViewWrapper wrapper;
        RatingBar rate;
        
        int[] themeColors = getThemeColors();
		int colorPos = position % 2;
       
        if(convertViewRow == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertViewRow = inflater.inflate(layout, parent, false);
            convertViewRow.setBackgroundColor(themeColors[colorPos]);
            
            wrapper = new ViewWrapper(convertViewRow);
            convertViewRow.setTag(wrapper);
			rate = wrapper.getRatingBar();
			rate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
						Integer myPosition = (Integer) ratingBar.getTag();
						Disability disabiltiy = ((StudentProfileActivity)context).getDisability(myPosition);
						disabiltiy.setRating((int) rating);
					}
				});
        } else {
        	wrapper = (ViewWrapper) convertViewRow.getTag();
        	rate = wrapper.getRatingBar();
        }
       
        Disability disability = ((StudentProfileActivity)context).getDisability(position);
        wrapper.getDisInfo().setText(disability.getInfo());
        wrapper.getDisName().setText(disability.getName());
        rate.setTag(new Integer(position));
        rate.setRating(disability.getRating());
       
        return convertViewRow;
    }
    
	public int[] getThemeColors() {
		if (theme == 1) {
			return themeColorsLight;
		} else {
			return themeColorsDark;
		}
	}
    
    private class ViewWrapper {
		View view;

		ViewWrapper(View view) {
			this.view = view;
		}

		RatingBar getRatingBar() {
			return (RatingBar) view.findViewById(R.id.ratingbar);
		}

		TextView getDisName() {
			return (TextView) view.findViewById(R.id.tvDisName);
		}

		TextView getDisInfo() {
			return (TextView) view.findViewById(R.id.tvDisInfo);
		}
	}
}