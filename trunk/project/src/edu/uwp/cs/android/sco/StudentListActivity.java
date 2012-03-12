/**
 * 
 */
package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import edu.uwp.cs.android.sco.model.StudentList;


/**
 * @author M.Duettmann
 *
 */
public class StudentListActivity extends Activity {
    
    private ListView lView;
    private TextView tView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        
        lView = (ListView) findViewById(R.id.stList);
        tView = (TextView) findViewById(R.id.className);

        System.out.println(getIntent().getSerializableExtra("Selected_Class"));
        
        StudentList stList = (StudentList) getIntent().getSerializableExtra("Selected_Class");
        tView.setText(stList.toString());
        
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, stList.getStudentsNames());

        // Assign adapter to ListView
        lView.setAdapter(adapter);
        
        
    }
}
