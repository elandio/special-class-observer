package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FaqAndAboutActivity extends Activity{

	Button closeFaqButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_and_about);
        
        closeFaqButton = (Button) findViewById(R.id.faq_close);
    
        closeFaqButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }
}