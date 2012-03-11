package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity implements View.OnClickListener{
    /** Called when the activity is first created. */
	Button navToClassList, navToConvertToPDF;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        // find buttons
        navToClassList = (Button) findViewById(R.id.navToClassList);
        navToConvertToPDF = (Button) findViewById(R.id.navToConvertToPDF);
        
        // set OnClickListeners
        navToClassList.setOnClickListener(this);
        navToConvertToPDF.setOnClickListener(this);
        
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
		
	}
}