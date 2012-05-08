package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This prototype was developed for the Mobile Device Programming class
 * in the Spring Semester 2012 at the University of Wisconsin Parkside.
 * 
 * @author Michael Tiede, Matthias Kleinert, Moritz Duettmann
 * @since May 2012
 */
public class StartActivity extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
	Button faqButton, navToStudentOverview, navToCourseOverview;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        // find buttons
        faqButton = (Button) findViewById(R.id.mainViewFaq);
        navToStudentOverview = (Button) findViewById(R.id.navToStudentOverview);
        navToCourseOverview = (Button) findViewById(R.id.navToCourseOverview);
        
        // set OnClickListeners
        faqButton.setOnClickListener(this);
        navToStudentOverview.setOnClickListener(this);
        navToCourseOverview.setOnClickListener(this);
    }
    
    @Override
	public void onBackPressed() {
		closeConfirmDialog();
	}

	@Override
	public void onClick(View v) {
		if (v == faqButton) {
			Intent i = new Intent(this, FaqAndAboutActivity.class);    
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
	
	protected void closeConfirmDialog() {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Are you sure you want to exit the program?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    					finish();
    	           }
    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}