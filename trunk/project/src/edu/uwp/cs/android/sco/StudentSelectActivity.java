package edu.uwp.cs.android.sco;

import java.util.ArrayList;
import java.util.List;

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
import edu.uwp.cs.android.sco.view.StudentListViewAdapter;

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
    private StudentListViewAdapter adapter;
    
    private List<Long> selectedStudents;
    
    // buttons
    private Button buttonCreateStudent, buttonResetSearch;
    private Button buttonAddStudentsToCourse, buttonCancelOperation;
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
        
        selectedStudents = new ArrayList<Long>();
        
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
    	setContentView(R.layout.student_select);
    	
        String textColumn = StudentDao.Properties.FName.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        String where = "_id NOT IN (SELECT STUDENT_ID FROM RELATION_COURSE_STUDENT WHERE COURSE_ID = " + courseId + ")";
        cursor = db.query(studentDao.getTablename(), studentDao.getAllColumns(), where, null, null, null, orderBy);
        String[] from = { textColumn, 
        					StudentDao.Properties.LName.columnName, 
        						StudentDao.Properties.LastModified.columnName,
        							StudentDao.Properties.DisabilityLevel.columnName};
        int[] to = { R.id.student_select_firstName, 
        				R.id.student_select_lastName, 
        					R.id.student_select_lastModified };

        adapter = new StudentListViewAdapter(this, R.layout.student_row, 1, cursor, from, to);
        setListAdapter(adapter);

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonCreateStudent = (Button) findViewById(R.id.student_overview_bCreateStudent);
        buttonCreateStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
        buttonAddStudentsToCourse = (Button) findViewById(R.id.student_select_bAddStudentsToCourse);
        buttonAddStudentsToCourse.setOnClickListener(this);
        
        buttonCancelOperation = (Button) findViewById(R.id.student_select_bCancelOperation);
        buttonCancelOperation.setOnClickListener(this);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setFocusable(true);
        registerForContextMenu(getListView());
    }
    

	@Override
	public void onClick(View v) {
		if (v == buttonCreateStudent) {
			openCreateStudentDialog();
		}
		if (v == buttonResetSearch) {
            etSearchStudent.setText("");
        }
		if (v == buttonAddStudentsToCourse) {
			addStudentsToCourse();
			finish();
		}
		if (v == buttonCancelOperation) {
			// TODO maybe setting a confirm dialog first
			finish();
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
                    
                    // TODO student could be added to the course immediately 
                    // course.addStudent(student);
                }
                
                studentDao.update(student);

                cursor.requery();
                getListView();
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
    	if (selectedStudents.contains(studentId)) {
    		selectedStudents.remove(studentId);
    		int[] colors = adapter.getThemeColors();
        	int colorPos = position % 2;
    	    //v.findViewById(R.id.listViewRow).setBackgroundColor(colors[colorPos]);
    		v.setBackgroundColor(colors[colorPos]);    		
    	} else {
    		selectedStudents.add(studentId);
    		v.setBackgroundColor(getResources().getColor(R.color.listView_selected_row));
    	}
    	System.out.println(selectedStudents);
    }
    
    private void addStudentsToCourse() {
    	Course course = daoSession.getCourseDao().load(courseId);
    	for (Long studentId : selectedStudents) {
    		course.addStudent(studentId);
		}
    }
    
}