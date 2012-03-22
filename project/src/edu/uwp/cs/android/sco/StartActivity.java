package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
	Button navToClassList, navToConvertToPDF, navToStudentEdit, navToStudentOverview;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        // find buttons
        navToClassList = (Button) findViewById(R.id.navToClassList);
        navToConvertToPDF = (Button) findViewById(R.id.navToConvertToPDF);
        navToStudentEdit = (Button) findViewById(R.id.navToStudentEdit);
        navToStudentOverview = (Button) findViewById(R.id.navToStudentOverview);
        
        // set OnClickListeners
        navToClassList.setOnClickListener(this);
        navToConvertToPDF.setOnClickListener(this);
        navToStudentEdit.setOnClickListener(this);
        navToStudentOverview.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		
		if (v == navToClassList) {
			Intent i = new Intent(this, ClassListActivity.class);    
	        startActivity(i);
		}
		if (v == navToConvertToPDF) {
			Intent i = new Intent(this, ConvertToPDFActivity.class);    
	        startActivity(i);
		}
		if (v == navToStudentEdit) {
			Intent i = new Intent(this, StudentEditActivity.class);    
	        startActivity(i);
		}
		if (v == navToStudentOverview) {
			Intent i = new Intent(this, StudentOverviewActivity.class);    
	        startActivity(i);
		}
		
	}
}