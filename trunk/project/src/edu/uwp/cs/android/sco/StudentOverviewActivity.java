package edu.uwp.cs.android.sco;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.uwp.cs.android.sco.entities.Course;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.RelationCourseStudentDao;
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
    private String courseName;
    private MyListAdapter adapter;
    
    private static final int OPEN_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int PRINT_ID = Menu.FIRST + 2;
    private static final int REMOVE_ID = Menu.FIRST + 3;   
    
    // buttons
    private Button buttonAddStudent, buttonResetSearch; // buttonBack;
    private EditText etSearchStudent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StudentOverviewActivity", "onCreate() called");
//      courseId = getIntent().getLongExtra("courseId", -1l); // will be executed by onResume()
//      courseName = getIntent().getStringExtra("courseName"); // will be executed by onResume()
//    	openStudentOverview(); // will be executed by onResume()
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("StudentOverviewActivity", "onResume() called");
    	courseId = getIntent().getLongExtra("courseId", -1l);
    	courseName = getIntent().getStringExtra("courseName");
    	openStudentOverview();
    }
    
    @Override
    protected void onPause () {
    	super.onPause();
    	Log.d("StudentOverviewActivity", "onPause() called");
    	releaseAllResources();
    }
    
    @Override
    protected void onStop () {
    	super.onStop();
    	Log.d("StudentOverviewActivity", "onStop() called");
    	releaseAllResources();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d("StudentOverviewActivity", "onRestart() called");
//    	courseId = getIntent().getLongExtra("courseId", -1l);
//    	courseName = getIntent().getStringExtra("courseName");
//    	openStudentOverview();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("StudentOverviewActivity", "onDestroy() called");
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
        setTitle("Student Classroom Observer - Student List");
    	
    	helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
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
    		System.out.println("display course " + courseId);
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

        MyListAdapter adapter = new MyListAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bAddStudent);
        buttonAddStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
//        buttonBack = (Button) findViewById(R.id.student_overview_bBack);
//        buttonBack.setOnClickListener(this);
        
        registerForContextMenu(getListView());
    }
    
    protected void showCourseStudents() {		
    	setContentView(R.layout.student_overview);

//		if (adapter == null && getListView().getHeaderViewsCount() == 0) {
//		    // Adding the header
//		    View header = (View) getLayoutInflater().inflate(R.layout.student_overview_header, null);
//		    getListView().addHeaderView(header);
//		    TextView tvheader = (TextView) findViewById(R.id.tvHeader);
//		    tvheader.setText(courseName);
//        }
    	
        String textColumn = StudentDao.Properties.FName.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        String where = "_id IN (SELECT STUDENT_ID FROM RELATION_COURSE_STUDENT WHERE COURSE_ID = " + courseId + ")";
        cursor = db.query(studentDao.getTablename(), studentDao.getAllColumns(), where, null, null, null, orderBy);
        String[] from = { textColumn, StudentDao.Properties.LName.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        adapter = new MyListAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);

        etSearchStudent = (EditText) findViewById(R.id.et_searchStudent);
        
        // initialize buttons and set onclicklisteners
        buttonAddStudent = (Button) findViewById(R.id.student_overview_bAddStudent);
        buttonAddStudent.setOnClickListener(this);
        
        buttonResetSearch = (Button) findViewById(R.id.student_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
//        buttonBack = (Button) findViewById(R.id.student_overview_bBack);
//        buttonBack.setOnClickListener(this);
        
        registerForContextMenu(getListView());
    }
    

	@Override
	public void onClick(View v) {
		if (v == buttonAddStudent) {
			if (courseId == -1l) {
				openCreateStudentDialog();
			} else {
				Intent selectStudents = new Intent(this, StudentSelectActivity.class);
				selectStudents.putExtra("courseId", courseId);
				selectStudents.putExtra("courseName", courseName);
		        startActivity(selectStudents);
			}
		}
		if (v == buttonResetSearch) {
            etSearchStudent.setText("");
        }
//        if (v == buttonBack) {
//            finish();
//            Intent i = new Intent(this, CourseOverviewActivity.class);    
//            startActivity(i);
//        }
	}
	
	public void openCreateStudentDialog() {
		final Dialog createStudentDialog = new Dialog(StudentOverviewActivity.this);
        
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
                    	Log.e("StudentOverviewActivity", "ERROR: COURSE IS NOT INITIALIZED");
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
    	openStudentProfile(studentId);
    }
    
    protected void openStudentProfile(long studentId) {
        Intent studentProfile = new Intent(this, StudentProfileActivity.class);
        studentProfile.putExtra("studentId", studentId);
        startActivity(studentProfile);
	}

    /**
     * DELETE STUDENT AND RELATIONS
     */
    protected void openDeleteDialog(long studentId) {
    	final Student student = studentDao.load(studentId);
    	final AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverviewActivity.this);
    	builder.setMessage("Are you sure you want to delete student " 
    						+ student.getFName() + " " + student.getLName() + "?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    	               student.deleteRelation(getCourseRelations(student.getId()));
    	               studentDao.deleteByKey(student.getId());
    	               cursor.requery();
    	               Log.d("StudentOverviewActivity", "Deleted student and relations, studentId: " + student.getId());
    	           }
    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    /**
     * Remove student from a course
     */
    protected void removeStudentFromCourseDialog(long studentId) {
    	final Student student = studentDao.load(studentId);
    	final AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverviewActivity.this);
    	builder.setMessage("Are you sure you want to remove the student " 
    						+ student.getFName() + " " + student.getLName() 
    						+ " from your course?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    	               student.deleteRelation(getSingleRelationKey(student.getId()));
    	               cursor.requery();
    	               Log.d("StudentOverviewActivity", "Removed student from course, studentId: " + student.getId());
    	           }
    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    private List<Long> getCourseRelations(long studentId) {
    	RelationCourseStudentDao relDao = daoSession.getRelationCourseStudentDao();
    	String where = "STUDENT_ID = " + studentId;
        Cursor mCursor = db.query(relDao.getTablename(), relDao.getAllColumns(), where, null, null, null, null);
        
        List<Long> relationPKeys = new ArrayList<Long>();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
        	relationPKeys.add(mCursor.getLong(0));
        }
        mCursor.close();
        return relationPKeys;
    }
    
    private long getSingleRelationKey(long studentId) {
    	RelationCourseStudentDao relDao = daoSession.getRelationCourseStudentDao();
    	String where = "STUDENT_ID = " + studentId + " AND COURSE_ID = " + courseId;
        Cursor mCursor = db.query(relDao.getTablename(), relDao.getAllColumns(), where, null, null, null, null);
        
        long relationKey = -1l;
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
        	relationKey = mCursor.getLong(0);
        }
        mCursor.close();
        return relationKey;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);       
        menu.add(0, OPEN_ID, 0, "Open student's profil");
        menu.add(0, EDIT_ID, 0, "Edit student's profil");
        menu.add(0, PRINT_ID, 0, "View PDF / Send via E-Mail");
        menu.add(0, REMOVE_ID, 0, "Remove from course");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo current = (AdapterContextMenuInfo) item.getMenuInfo();
        long studentId = current.id;
        
        switch(item.getItemId()) {            
            case OPEN_ID:
            	openStudentProfile(studentId);
                break;
            case EDIT_ID:
                openEditDialog(studentId);
                break;
            case PRINT_ID:
                Intent i = new Intent(this, ConvertToPDFActivity.class);
                i.putExtra("studentId", studentId);
                startActivity(i);
               break;    
            case REMOVE_ID:
                //openDeleteDialog(studentId);
            	removeStudentFromCourseDialog(studentId);
                break;
            default:
            	Log.e("StudentOverviewActivity", "UNKNOWN CASE IN ContextItemSelected(MenuItem item)");
        }
        return super.onContextItemSelected(item);
    }
    
    private void openEditDialog(final long studentId) {
        final Dialog createStudentDialog = new Dialog(StudentOverviewActivity.this);
        
        final Student student = studentDao.load(studentId);
        createStudentDialog.setContentView(R.layout.student_dialog_create);
        createStudentDialog.setTitle("Edit a student");
        createStudentDialog.setCancelable(true);
        createStudentDialog.show();
        
        final EditText fNameText = (EditText) createStudentDialog.findViewById(R.id.studentFNameEditText);
        final EditText lNameText = (EditText) createStudentDialog.findViewById(R.id.studentLNameEditText);
        
        fNameText.setText(student.getFName());
        lNameText.setText(student.getLName());
        
        Button cancelButton = (Button) createStudentDialog.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) createStudentDialog.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        
        saveButton.setEnabled(validateAddStudentInput(createStudentDialog, fNameText, lNameText));
        
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

                Student student = new Student(studentId, firstName, lastName);
                studentDao.update(student);
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
}