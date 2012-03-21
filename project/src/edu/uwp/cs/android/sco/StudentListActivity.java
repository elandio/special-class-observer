/**
 * 
 */
package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import edu.uwp.cs.android.sco.sqllite.StudentsDB;


/**
 * @author M.Duettmann
 *
 */
public class StudentListActivity extends Activity {
    
    private ListView lView;
    private TextView tView;
    private EditText searchEdit;
    private ArrayAdapter<String> adapter;
    private Button addStundentButton;
    private StudentsDB studentsDB;
    private String className;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        
        lView = (ListView) findViewById(R.id.stList);
        tView = (TextView) findViewById(R.id.className);
        searchEdit = (EditText) findViewById(R.id.searchStudentList);
        addStundentButton = (Button) findViewById(R.id.addStudentButton);
        
        studentsDB = new StudentsDB(this);
        studentsDB.open();
        

        className = (String) getIntent().getSerializableExtra("Selected_Class");
        tView.setText(className);
        
        String[] students = studentsDB.getStundesInClass(className);
        
        adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, students);
        
        lView.setAdapter(adapter);
        
        searchEdit.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { 
                Filter filter = adapter.getFilter();
                filter.filter(s);
                adapter.notifyDataSetChanged();                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                
            }
        });
        
        searchEdit.setOnClickListener( new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
            }
        });
        
        
        
        addStundentButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final Dialog addClassDialog = new Dialog(StudentListActivity.this);
               
                addClassDialog.setContentView(R.layout.add_student);
                addClassDialog.setTitle("Add a student");
                addClassDialog.setCancelable(true);
                
                addClassDialog.show();
                
                
                final EditText fNameText = (EditText) addClassDialog.findViewById(R.id.studentFNameEditText);
                final EditText lNameText = (EditText) addClassDialog.findViewById(R.id.studentLNameEditText);
                Button cancelButton = (Button) addClassDialog.findViewById(R.id.cancelButton);
                Button saveButton = (Button) addClassDialog.findViewById(R.id.saveButton);
                
                cancelButton.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        addClassDialog.dismiss();
                    }
                });
                
                saveButton.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        
                        studentsDB.createStudent(fNameText.getText().toString(), lNameText.getText().toString(), className);
                        
                        String[] students = studentsDB.getStundesInClass(className);
                        
                        adapter = new ArrayAdapter<String>(StudentListActivity.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, students);

                        lView.setAdapter(adapter);
                        
                        addClassDialog.dismiss();
                    }
                });
                
                
            }
        }); 
    }
}
