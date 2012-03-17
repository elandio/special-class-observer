/**
 * 
 */
package edu.uwp.cs.android.sco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import edu.uwp.cs.android.sco.model.ClassList;
import edu.uwp.cs.android.sco.model.Student;
import edu.uwp.cs.android.sco.model.StudentList;

/**
 * @author M.Duettmann
 */
public class ClassListActivity extends Activity {

    private ListView lView;

    private ClassList clList;

    private EditText searchEdit;

    private ArrayAdapter<String> adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);

        lView = (ListView) findViewById(R.id.clList);
        searchEdit = (EditText) findViewById(R.id.searchClassList);

        // Test Data
        clList = new ClassList();
        Student me = new Student("Moritz", "Duettmann");
        Student you = new Student("Matthias", "Duettmann");
        Student he = new Student("Michael", "Duettmann");
        StudentList math_stList = new StudentList("Math-Class");
        StudentList eng_stList = new StudentList("Eng-Class");
        math_stList.add(me);
        math_stList.add(you);
        math_stList.add(he);

        clList.add(math_stList);

        clList.add(eng_stList);

        lView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StudentList stList = clList.get(position);
                System.out.println(stList.size());

                Intent i = new Intent(parent.getContext(), StudentListActivity.class);
                i.putExtra("Selected_Class", stList);
                startActivity(i);
            }
        });

        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, clList.getClassesNames());

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
