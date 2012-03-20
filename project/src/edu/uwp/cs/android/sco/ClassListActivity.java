/**
 * 
 */
package edu.uwp.cs.android.sco;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import edu.uwp.cs.android.sco.model.ClassList;
import edu.uwp.cs.android.sco.model.Student;
import edu.uwp.cs.android.sco.model.StudentList;
import edu.uwp.cs.android.sco.sqllite.ClassesDB;

/**
 * @author M.Duettmann
 */
public class ClassListActivity extends Activity {

    private ListView lView;

    private ClassList clList;

    private EditText searchEdit;

//    private ArrayAdapter<String> adapter;
    private ArrayAdapter<StudentList> adapter;
    
    private Button addClass;
    
    private ClassesDB classesDB;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);

        lView = (ListView) findViewById(R.id.clList);
        searchEdit = (EditText) findViewById(R.id.searchClassList);
        addClass = (Button) findViewById(R.id.addClassButton);
        
        classesDB = new ClassesDB(this);
        classesDB.open();
        


        // Test Data 
        clList = new ClassList();
        Student me = new Student("Moritz", "Duettmann");
        Student you = new Student("Matthias", "Duettmann");
        Student he = new Student("Michael", "Duettmann");
        StudentList math_stList = new StudentList("Math-Class");
        StudentList eng_stList = new StudentList("Eng-Class");
        
//        StudentList test = classesDB.createStudentList("test");
//        test.add(me);
//        test.add(you);
//        test.add(he);
        
        math_stList.add(me);
        math_stList.add(you);
        math_stList.add(he);
        
        clList.add(math_stList);

        clList.add(eng_stList);

        List<StudentList> stuList = classesDB.getAllClasses();
        
        lView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StudentList stList = clList.get(position);
                
                
                Intent i = new Intent(parent.getContext(), StudentListActivity.class);
                i.putExtra("Selected_Class", stList);
                startActivity(i);
            }
        });

        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data        
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, clList.getClassesNames());
        
        adapter = new ArrayAdapter<StudentList>(this,
                android.R.layout.simple_list_item_1, stuList);

        // Assign adapter to ListView
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

        searchEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchEdit.setText("");
            }
        });
        
        
        addClass.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final Dialog addClassDialog = new Dialog(ClassListActivity.this);
               
                addClassDialog.setContentView(R.layout.add_class);
                addClassDialog.setTitle("Add a class");
                addClassDialog.setCancelable(true);
                
                addClassDialog.show();
                
                
                final EditText newClassText = (EditText) addClassDialog.findViewById(R.id.classNameEditText);
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
                        clList.add(new StudentList(newClassText.getText().toString()));
                        
                        StudentList test = classesDB.createStudentList(newClassText.getText().toString());
                        
//                        adapter.notifyDataSetChanged();
//                        lView.invalidateViews();
                        
                        //TODO: Not perfect better update then creating a new adapter but notofiyDataSetChanged() is not working now....
//                        adapter = new ArrayAdapter<String>(ClassListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, clList.getClassesNames());
                        List<StudentList> stuList = classesDB.getAllClasses();
                        adapter = new ArrayAdapter<StudentList>(ClassListActivity.this,
                                android.R.layout.simple_list_item_1, stuList);
                        // Assign adapter to ListView
                        lView.setAdapter(adapter);
                        
                        addClassDialog.dismiss();
                    }
                });
                

                
            }
        }); 

    }
    
    @Override
    protected void onResume() {
        classesDB.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        classesDB.close();
        super.onPause();
    }
}
