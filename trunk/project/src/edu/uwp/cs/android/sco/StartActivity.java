package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
	Button navToConvertToPDF, navToStudentProfile, navToStudentOverview, navToCourseOverview;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        // find buttons
//        navToConvertToPDF = (Button) findViewById(R.id.navToConvertToPDF);
        navToStudentProfile = (Button) findViewById(R.id.navToStudentProfile);
        navToStudentOverview = (Button) findViewById(R.id.navToStudentOverview);
        navToCourseOverview = (Button) findViewById(R.id.navToCourseOverview);
        
        // set OnClickListeners
//        navToConvertToPDF.setOnClickListener(this);
        navToStudentProfile.setOnClickListener(this);
        navToStudentOverview.setOnClickListener(this);
        navToCourseOverview.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		
		if (v == navToConvertToPDF) {
//			Intent i = new Intent(this, ConvertToPDFActivity.class);    
//	        startActivity(i);
		}
		if (v == navToStudentProfile) {
			Intent i = new Intent(this, StudentProfileActivity.class);    
	        startActivity(i);
		}
		if (v == navToStudentOverview) {
			Intent i = new Intent(this, StudentOverviewActivity.class);    
	        startActivity(i);
		}
		if (v == navToCourseOverview) {
			Intent i = new Intent(this, CourseOverviewActivity.class);    
	        startActivity(i);
		}
		
	}
}