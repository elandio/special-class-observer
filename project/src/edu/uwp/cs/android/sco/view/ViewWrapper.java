package edu.uwp.cs.android.sco.view;
import edu.uwp.cs.android.sco.R;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

class ViewWrapper {
	View base;
	RatingBar rate=null;
    TextView disName=null;
    TextView disInfo=null;
	ViewWrapper(View base) {
		this.base=base;
	}
	
	RatingBar getRatingBar() {
		if (rate==null) {
			rate=(RatingBar)base.findViewById(R.id.ratingbar);
		}
		return(rate);
	}
	
	TextView getDisName() {
		if (disName==null) {
			disName=(TextView)base.findViewById(R.id.tvDisName);
		}
		return(disName);
	}
	
	TextView getDisInfo() {
		if (disInfo==null) {
			disInfo=(TextView)base.findViewById(R.id.tvDisInfo);
		}
		return(disInfo);
	}
	
}
