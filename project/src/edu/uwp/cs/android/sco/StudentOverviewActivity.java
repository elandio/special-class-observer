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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import edu.uwp.cs.android.sco.entities.Course;
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
    private Button buttonAddStudent, buttonResetSearch;
    private EditText etSearchStudent;
    
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

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bAddStudent);
        buttonAddStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if (v == buttonAddStudent) {
			openAddStudentDialog();
		}
		if (v == buttonResetSearch) {
			etSearchStudent.setText("");
		}
	}
	
	public void openAddStudentDialog() {
		final Dialog addStudentDialog = new Dialog(StudentOverviewActivity.this);
        
        addStudentDialog.setContentView(R.layout.dialog_add_student);
        addStudentDialog.setTitle("Add a student");
        addStudentDialog.setCancelable(true);
        addStudentDialog.show();
        
        final EditText fNameText = (EditText) addStudentDialog.findViewById(R.id.studentFNameEditText);
        final EditText lNameText = (EditText) addStudentDialog.findViewById(R.id.studentLNameEditText);
        
        Button cancelButton = (Button) addStudentDialog.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) addStudentDialog.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        
        fNameText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				saveButton.setEnabled(validateAddStudentInput(addStudentDialog, fNameText, lNameText));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
        });
        
        lNameText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				saveButton.setEnabled(validateAddStudentInput(addStudentDialog, fNameText, lNameText));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
        });
        
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	String firstName = fNameText.getText().toString();
            	String lastName = lNameText.getText().toString();
            	
            	// clear fields for first and last name
                fNameText.setText("");
                lNameText.setText("");

                Student student = new Student(null, firstName, lastName);
                studentDao.insert(student);
                student.addDefaultDisabilities();
                Log.d("SCO-Project", "Inserted new student: [" + student.getId() + "] " + firstName + " " + lastName);
                
                // TEST AREA
                Course course = new Course(null, "Discrete Structures", "Math");
                daoSession.getCourseDao().insert(course);
                
                student.addCourse(course);
                studentDao.update(student);
                // TEST AREA END

                cursor.requery();
                addStudentDialog.dismiss();
            }
        });
        
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentDialog.dismiss();
            }
        });
	}
	
	public boolean validateAddStudentInput(Dialog dialog, EditText firstName, EditText lastName) {
    	if (!firstName.getText().toString().equals("") 
    			&& !lastName.getText().toString().equals("")) {
    		return true;
    	}            	
    	return false;
    }
    
    /**
     * DELETE STUDENT AND RELATIONS
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, final long studentId) {
    	final Student student = studentDao.load(studentId);
    	final AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverviewActivity.this);
    	builder.setMessage("Are you sure you want to delete student " 
    						+ student.getFName() + " " + student.getLName() + "?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               student.deleteRelation(studentId);
    	               studentDao.deleteByKey(studentId);
    	               cursor.requery();
    	               
    	               Log.d("SCO-Project", "Deleted student, studentId: " + studentId);
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

}