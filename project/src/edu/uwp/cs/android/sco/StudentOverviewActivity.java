package edu.uwp.cs.android.sco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.uwp.cs.android.sco.entities.Course;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.CourseDao;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.view.MyListAdapter;

public class StudentOverviewActivity extends ListActivity implements View.OnClickListener{
	
	// database management
	private DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Cursor cursor;
    private long courseId;
    
    private static final int OPEN_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST +1;
    private static final int PRINT_ID = Menu.FIRST +2;
    private static final int DELETE_ID = Menu.FIRST +3;   
    
    // buttons
    private Button buttonAddStudent, buttonResetSearch, buttonBack;
    private EditText etSearchStudent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("StudentOverviewActivity", "onCreate() called");
        courseId = getIntent().getLongExtra("courseId", -1l);
//    	openStudentOverview();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i("StudentOverviewActivity", "onResume() called");
//    	courseId = getIntent().getLongExtra("courseId", -1l);
    	openStudentOverview();
    	
    }
    
    @Override
    protected void onPause () {
    	super.onPause();
    	Log.i("StudentOverviewActivity", "onPause() called");
    	releaseAllResources();
    }
    
    @Override
    protected void onStop () {
    	super.onStop();
    	Log.i("StudentOverviewActivity", "onStop() called");
    	releaseAllResources();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.i("StudentOverviewActivity", "onRestart() called");
    	courseId = getIntent().getLongExtra("courseId", -1l);
    	openStudentOverview();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i("StudentOverviewActivity", "onDestroy() called");
    	releaseAllResources();
    }
    
    private void releaseAllResources() {
    	daoMaster = null;
        daoSession = null;
        studentDao = null;
    	cursor.close();
    	db.close();
    	helper.close();
    }
    
    private void openStudentOverview() {
    	
    	helper = new DaoMaster.DevOpenHelper(this, "student-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
    	// load view - show all students or show students  
    	if (courseId == -1l) {
    		// display all students
    		System.out.println("display all students");
    		showAllStudents();
    	} else {
    		// display student depending on courseId
    		System.out.println("display student " + courseId);
    		showCourseStudents();
    	}
        
    }
    
    protected void showAllStudents() {		
		setContentView(R.layout.student_overview);

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
        
        buttonBack = (Button) findViewById(R.id.student_overview_bBack);
        buttonBack.setOnClickListener(this);
        
        registerForContextMenu(getListView());
    }
    
    protected void showCourseStudents() {		
		setContentView(R.layout.student_overview);

        String textColumn = StudentDao.Properties.FName.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        String where = "_id IN (SELECT STUDENT_ID FROM RELATION_COURSE_STUDENT WHERE COURSE_ID = " + courseId + ")";
        cursor = db.query(studentDao.getTablename(), studentDao.getAllColumns(), where, null, null, null, orderBy);
        String[] from = { textColumn, StudentDao.Properties.LName.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };

//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        MyListAdapter adapter = new MyListAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bAddStudent);
        buttonAddStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
        buttonBack = (Button) findViewById(R.id.student_overview_bBack);
        buttonBack.setOnClickListener(this);
        
        registerForContextMenu(getListView());
    }

	@Override
	public void onClick(View v) {
		if (v == buttonAddStudent) {
			openAddStudentDialog();
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
	
	public void openAddStudentDialog() {
		final Dialog addStudentDialog = new Dialog(StudentOverviewActivity.this);
        
        addStudentDialog.setContentView(R.layout.student_dialog_add);
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
                
                // if user selected a course - add course_student relation
                if (courseId != -1l) {
                    // insert student in current course
                    Course course = daoSession.getCourseDao().load(courseId);
                    
                    if (course == null) { // TEMP FOR DEVELOPMENT
                    	Log.e("StudentOverviewActivity", "ERROR: COURSE IS NOT INITIALIZED");
                    }
                    
                    course.addStudent(student);
                }
                
                studentDao.update(student);

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
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long studentId) {
    	Student student = studentDao.load(studentId);
    	
    	// open dialog for delete confirmation
    	openDeleteDialog(student);
    }

    /**
     * DELETE STUDENT AND RELATIONS
     */
    protected void openDeleteDialog(final Student student) {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverviewActivity.this);
    	builder.setMessage("Are you sure you want to delete student " 
    						+ student.getFName() + " " + student.getLName() + "?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               student.deleteRelation(student.getId());
    	               studentDao.deleteByKey(student.getId());
    	               cursor.requery();
    	               
    	               Log.d("SCO-Project", "Deleted student, studentId: " + student.getId());
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);       
        menu.add(0, OPEN_ID, 0, "Open");
        menu.add(0, EDIT_ID, 0, "Edit");
        menu.add(0, PRINT_ID, 0, "Print PDF");
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Student student = studentDao.load(info.id);
        switch(item.getItemId()) {            
            case OPEN_ID:
                //TODO: implement open students profil
                break;
            case EDIT_ID:
                //TODO: implement edit students profil
                break;
            case PRINT_ID:
                Intent i = new Intent(this, ConvertToPDFActivity.class);
                i.putExtra("studentId", info.id);
                i.putExtra("courseId", courseId);
                startActivity(i);
               break;    
            case DELETE_ID:
                openDeleteDialog(student);
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

}