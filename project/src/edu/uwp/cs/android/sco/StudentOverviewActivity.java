/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.uwp.cs.android.sco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;

public class StudentOverviewActivity extends ListActivity implements View.OnClickListener{
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Cursor cursor;
    
    // buttons
    Button buttonAddStudent;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_overview);

        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "student-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();

        String textColumn = StudentDao.Properties.FName.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(studentDao.getTablename(), studentDao.getAllColumns(), null, null, null, null, orderBy);
        String[] from = { textColumn, StudentDao.Properties.LName.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);

        editText = (EditText) findViewById(R.id.et_searchStudent);
        addUiListeners();
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bAddStudent);
        buttonAddStudent.setOnClickListener(this);
    }

    protected void addUiListeners() {
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    addStudent();
                    return true;
                }
                return false;
            }
        });

        final View button = findViewById(R.id.bAddStudent);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                button.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onMyButtonClick(View view) {
        //addStudent();
    }

    /**
     * DELETE STUDENT AND RELATIONS
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, final long studentId) {
    	final Student student = studentDao.load(studentId);
    	final AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverviewActivity.this);
    	builder.setMessage("Are you sure you want to delete student " + student.getFName() + " " 
    							+ student.getLName() + "?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   
    	               student.deleteRelation(studentId);
    	               studentDao.deleteByKey(studentId);
    	               
    	               Log.d("SCO-Project", "Deleted student, studentId: " + studentId);
    	               cursor.requery();
    	           }
    	       })
    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

	@Override
	public void onClick(View v) {
		if (v == buttonAddStudent) {
			openAddStudentDialog();
		}
	}
	
	public void openAddStudentDialog() {
		final Dialog addClassDialog = new Dialog(StudentOverviewActivity.this);
        
        addClassDialog.setContentView(R.layout.dialog_add_student);
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
            	String firstName = fNameText.getText().toString();
            	String lastName = lNameText.getText().toString();
            	// clear 
                fNameText.setText("");
                lNameText.setText("");

                Student student = new Student(null, firstName, lastName);
                studentDao.insert(student);
                student.addDefaultDisabilities();

                Log.d("SCO-Project", "Inserted new student: [" + student.getId() + "] " + firstName + " " + lastName);

                cursor.requery();
            	
                addClassDialog.dismiss();
            }
        });
	}

}