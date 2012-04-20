package edu.uwp.cs.android.sco;

import android.app.Dialog;
import android.app.ListActivity;
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
import edu.uwp.cs.android.sco.entities.Course;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.view.SelectMultipleAdapter;

public class StudentSelectActivity extends ListActivity implements View.OnClickListener{
	
	// database management
	private DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Cursor cursor;
    private long courseId;
    private String courseName;
    private SelectMultipleAdapter adapter;
    
    // buttons
    private Button buttonAddStudent, buttonResetSearch, buttonBack;
    private EditText etSearchStudent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StudentSelectActivity", "onCreate() called");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("StudentSelectActivity", "onResume() called");
    	courseId = getIntent().getLongExtra("courseId", -1l);
    	courseName = getIntent().getStringExtra("courseName");
    	openStudentOverview();
    }
    
    @Override
    protected void onPause () {
    	super.onPause();
    	Log.d("StudentSelectActivity", "onPause() called");
    	releaseAllResources();
    }
    
    @Override
    protected void onStop () {
    	super.onStop();
    	Log.d("StudentSelectActivity", "onStop() called");
    	releaseAllResources();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d("StudentSelectActivity", "onRestart() called");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("StudentSelectActivity", "onDestroy() called");
    	releaseAllResources();
    }
    
    private void releaseAllResources() {
    	adapter = null;
    	studentDao = null;
    	daoMaster = null;
        daoSession = null;
        if (cursor != null) {
        	cursor.close();
        }
    	if (db != null) {
    		db.close();
    	}
    	if (helper != null) {
    		helper.close();
    	}
    }
    
    private void openStudentOverview() {
        setTitle("Student Classroom Observer - Add student to " + courseName);
    	
    	helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
    	// load view
    	if (courseId == -1l) {
    		/*
    		 * ERROR: Failed to receive course id from intent
    		 * It has to use intentName.putExtra("courseId", courseId); 
    		 */
    		Log.e("StudentSelectActivity", "ERROR: Failed to receive course id from intent.");
    		finish();
    	} else {
    		// loading list of students to add them to a course
    		System.out.println("display student list for adding it to course " + courseId);
    		showNonCourseStudents();
    	}
    }
    
    protected void showNonCourseStudents() {		
    	setContentView(R.layout.student_select_multiple);
    	
        String textColumn = StudentDao.Properties.FName.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        String where = "_id NOT IN (SELECT STUDENT_ID FROM RELATION_COURSE_STUDENT WHERE COURSE_ID = " + courseId + ")";
        cursor = db.query(studentDao.getTablename(), studentDao.getAllColumns(), where, null, null, null, orderBy);
        String[] from = { textColumn, StudentDao.Properties.LName.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        adapter = new SelectMultipleAdapter(this, R.layout.student_select_multiple_row, cursor, from, to);
        setListAdapter(adapter);

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bCreateStudent);
        buttonAddStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
        buttonBack = (Button) findViewById(R.id.student_overview_bBack);
        buttonBack.setOnClickListener(this);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setFocusable(true);
        registerForContextMenu(getListView());
    }
    

	@Override
	public void onClick(View v) {
		if (v == buttonAddStudent) {
			openCreateStudentDialog();
		}
		if (v == buttonResetSearch) {
            etSearchStudent.setText("");
        }
        if (v == buttonBack) {
            finish();
//            Intent i = new Intent(this, CourseOverviewActivity.class);    
//            startActivity(i);
        }
	}
	
	public void openCreateStudentDialog() {
		final Dialog createStudentDialog = new Dialog(StudentSelectActivity.this);
        
        createStudentDialog.setContentView(R.layout.student_dialog_create);
        createStudentDialog.setTitle("Create a new student");
        createStudentDialog.setCancelable(true);
        createStudentDialog.show();
        
        final EditText fNameText = (EditText) createStudentDialog.findViewById(R.id.studentFNameEditText);
        final EditText lNameText = (EditText) createStudentDialog.findViewById(R.id.studentLNameEditText);
        
        Button cancelButton = (Button) createStudentDialog.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) createStudentDialog.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        
        fNameText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				saveButton.setEnabled(validateAddStudentInput(createStudentDialog, fNameText, lNameText));
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
				saveButton.setEnabled(validateAddStudentInput(createStudentDialog, fNameText, lNameText));
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
                
                // if user selected a course - add course_student relation
                if (courseId != -1l) {
                    // insert student in current course
                    Course course = daoSession.getCourseDao().load(courseId);
                    
                    if (course == null) { // TEMP FOR DEVELOPMENT
                    	Log.e("StudentSelectActivity", "ERROR: COURSE IS NOT INITIALIZED");
                    }
                    
                    course.addStudent(student);
                    // TODO Output: Student was created and added to this course
                }
                
                studentDao.update(student);

                cursor.requery();
                createStudentDialog.dismiss();
            }
        });
        
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createStudentDialog.dismiss();
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
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long studentId) {
    	Course course = daoSession.getCourseDao().load(courseId);
    	course.addStudent(studentId);
    }
    
}