/**
 * 
 */
package edu.uwp.cs.android.sco;

import edu.uwp.cs.android.sco.model.ClassList;
import edu.uwp.cs.android.sco.model.Student;
import edu.uwp.cs.android.sco.model.StudentList;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * @author M.Duettmann
 *
 */
public class ClassListActivity extends Activity {
    
    private ListView lView;
    private ClassList clList = new ClassList();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);
        
        lView = (ListView) findViewById(R.id.clList);
        
        String[] values = new String[] {"First Name", "Last Name"};
        
        Student me = new Student("Moritz", "Duettmann");
        StudentList stList = new StudentList();
        stList.add(me);
        clList.add(stList);
        
        
        System.out.println(clList.toString());
        
        
     // First paramenter - Context
     // Second parameter - Layout for the row
     // Third parameter - ID of the View to which the data is written
     // Forth - the Array of data
     ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
         android.R.layout.simple_list_item_1, android.R.id.text1, clList.getStudentNames());

     // Assign adapter to ListView
     lView.setAdapter(adapter);
        
    }
}
