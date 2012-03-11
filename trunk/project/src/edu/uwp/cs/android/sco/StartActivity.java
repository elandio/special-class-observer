package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        Intent i = new Intent(this,ClassListActivity.class);    
        startActivity(i); 
    }
}