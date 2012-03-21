/**
 * 
 */
package edu.uwp.cs.android.sco;

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
import edu.uwp.cs.android.sco.sqllite.ClassesDB;

/**
 * @author M.Duettmann
 */
public class ClassListActivity extends Activity {

    private ListView lView;

    //    private ClassList clList;

    private EditText searchEdit;

    private ArrayAdapter<String> adapter;

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

        lView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String className = classesDB.getClassName(position);

                Intent i = new Intent(parent.getContext(), StudentListActivity.class);
                i.putExtra("Selected_Class", className);
                startActivity(i);
            }
        });

        String[] classes = classesDB.getAllClassNames();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classes);

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

                        classesDB.createStudentList(newClassText.getText().toString());

                        String[] classes = classesDB.getAllClassNames();
                        adapter = new ArrayAdapter<String>(ClassListActivity.this, android.R.layout.simple_list_item_1, classes);

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
