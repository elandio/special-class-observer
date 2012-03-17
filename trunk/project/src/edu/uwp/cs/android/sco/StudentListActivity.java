/**
 * 
 */
package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.database.DataSetObserver;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.uwp.cs.android.sco.model.StudentList;


/**
 * @author M.Duettmann
 *
 */
public class StudentListActivity extends Activity {
    
    private ListView lView;
    private TextView tView;
    private EditText searchEdit;
    private ArrayAdapter<String> adapter;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        
        lView = (ListView) findViewById(R.id.stList);
        tView = (TextView) findViewById(R.id.className);
        searchEdit = (EditText) findViewById(R.id.searchStudentList);

        System.out.println(getIntent().getSerializableExtra("Selected_Class"));
        
        StudentList stList = (StudentList) getIntent().getSerializableExtra("Selected_Class");
        tView.setText(stList.toString());
        
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data
        adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, stList.getStudentsNames());

        // Assign adapter to ListView
        lView.setAdapter(adapter);
        
        TextWatcher changeListener = new TextWatcher() {
            
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
        };
        
        OnClickListener onClickListener = new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
            }
        };
        searchEdit.setOnClickListener(onClickListener);
        searchEdit.addTextChangedListener(changeListener);
    }
}
