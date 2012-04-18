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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.uwp.cs.android.sco.entities.Course;
import edu.uwp.cs.android.sco.entities.CourseDao;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.RelationCourseStudentDao;
import edu.uwp.cs.android.sco.view.MyListAdapter;

public class CourseOverviewActivity extends ListActivity implements View.OnClickListener {

    // database management
	private DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CourseDao courseDao;
    private Cursor cursor;
    private MyListAdapter adapter;

    // buttons
    private Button buttonAddCourse, buttonResetSearch;
    private EditText etSearchCourse;
    
    private static final int OPEN_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST +1;
    private static final int DELETE_ID = Menu.FIRST +2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CourseOverviewActivity", "onCreate() called");
//      openCourseOverview(); // will be executed by onResume()
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("CourseOverviewActivity", "onResume() called");
    	openCourseOverview();
    }
    
    @Override
    protected void onPause () {
    	super.onPause();
    	Log.d("CourseOverviewActivity", "onPause() called");
    	releaseAllResources();
    }
    
    @Override
    protected void onStop () {
    	super.onStop();
    	Log.d("CourseOverviewActivity", "onStop() called");
    	releaseAllResources();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d("CourseOverviewActivity", "onRestart() called");
//    	openCourseOverview();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("CourseOverviewActivity", "onDestroy() called");
    	releaseAllResources();
    }
    
    private void releaseAllResources() {
    	adapter = null;
    	courseDao = null;
        daoSession = null;
        daoMaster = null;
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
    
    private void openCourseOverview() {
    	Log.d("CourseOverviewActivity", "openCourseOverview() called");
    	setContentView(R.layout.course_overview);
    	setTitle("Student Classroom Observer -  Course List");
    	
        helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        courseDao = daoSession.getCourseDao();
        
        String textColumn = CourseDao.Properties.Name.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(courseDao.getTablename(), courseDao.getAllColumns(), null, null, null, null, orderBy);
        String[] from = { textColumn, CourseDao.Properties.Category.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };
        
        adapter = new MyListAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        setListAdapter(adapter);
        
        // initialize buttons and set listeners
        buttonAddCourse = (Button) findViewById(R.id.course_overview_bAddCourse);
        buttonAddCourse.setOnClickListener(this);

        buttonResetSearch = (Button) findViewById(R.id.course_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);
        
        getListView().setTextFilterEnabled(true);
        
        etSearchCourse = (EditText) findViewById(R.id.et_searchCourse);
        etSearchCourse.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerForContextMenu(getListView());
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddCourse) {
            openAddCourseDialog();
        }
        if (v == buttonResetSearch) {
            etSearchCourse.setText("");
        }
    }

    public void openAddCourseDialog() {
        final Dialog addCourseDialog = new Dialog(CourseOverviewActivity.this);

        addCourseDialog.setContentView(R.layout.course_dialog_add);
        addCourseDialog.setTitle("Add a course");
        addCourseDialog.setCancelable(true);
        addCourseDialog.show();

        final EditText courseNameText = (EditText) addCourseDialog.findViewById(R.id.courseNameEditText);
        final Spinner comboBoxCategory = (Spinner) addCourseDialog.findViewById(R.id.courseCategorySpinner);
        final Spinner comboBoxSemester = (Spinner) addCourseDialog.findViewById(R.id.courseSemesterSpinner);
        final EditText courseYearText = (EditText) addCourseDialog.findViewById(R.id.courseYearEditText);

        
        String arrayCategories[] = new String[]{"Math", "Languages", "Science", "Economics", "Others"};
        String arraySemesters[] = new String[]{"Spring", "Summer", "Winter"};
        
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayCategories);
        comboBoxCategory.setAdapter(categoriesAdapter);
        ArrayAdapter<String> semestersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySemesters);
        comboBoxSemester.setAdapter(semestersAdapter);

        Button cancelButton = (Button) addCourseDialog.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) addCourseDialog.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);

        courseNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(!courseNameText.getText().toString().equals(""));
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        
        courseYearText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(!courseYearText.getText().toString().equals(""));
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
                String courseName = courseNameText.getText().toString();               
                String courseCategory = comboBoxCategory.getSelectedItem().toString();               
                String courseSemester = comboBoxSemester.getSelectedItem().toString();
                int courseYear = 2012;
                try{
                    courseYear = Integer.parseInt(courseYearText.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(getParent(), "Error with year, year was set to 2012", 5);
                }             

                courseNameText.setText("");
                courseYearText.setText("");

                Course course = new Course(null, courseName, courseCategory, courseSemester, courseYear);
                courseDao.insert(course);

                Log.d("CourseOverviewActivity", "Inserted new course: [" + course.getId() + "] " + courseName + " " + courseCategory);

                cursor.requery();
                addCourseDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseDialog.dismiss();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long courseId) {
        openCourseListActivity(courseId);
    }
    
    private void openCourseListActivity(long courseId){
        Intent studentOverview = new Intent(this, StudentOverviewActivity.class);
        studentOverview.putExtra("courseId", courseId);
        studentOverview.putExtra("courseName", courseDao.load(courseId).getName());
        Log.d("CourseOverviewActivity", "--> CLICKED onListItemClick called Intent");
        startActivity(studentOverview);
    }

    /**
     * DELETE COURSE AND RELATIONS
     */
    protected void openDeleteDialog(long courseId) {
    	final Course course = courseDao.load(courseId);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CourseOverviewActivity.this);
        builder.setMessage("Are you sure you want to delete the course " 
        					+ course.getName() + " (category: " + course.getCategory() + ")?")
        		.setCancelable(false)
        		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                course.deleteRelation(getStudentRelations(course.getId()));
		                courseDao.deleteByKey(course.getId());
		                cursor.requery();
		                Log.d("CourseOverviewActivity", "Deleted course and relations, courseId: " + course.getId());
		            }
        		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
        		});
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private List<Long> getStudentRelations(long courseId) {
    	RelationCourseStudentDao relDao = daoSession.getRelationCourseStudentDao();
    	String where = "COURSE_ID = " + courseId;
        Cursor mCursor = db.query(relDao.getTablename(), relDao.getAllColumns(), where, null, null, null, null);
        
        List<Long> relationPKeys = new ArrayList<Long>();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
        	relationPKeys.add(mCursor.getLong(0));
        }
        mCursor.close();
        return relationPKeys;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, OPEN_ID, 0, "Open course");
        menu.add(0, EDIT_ID, 0, "Edit course");
        menu.add(0, DELETE_ID, 0, "Delete course");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        AdapterContextMenuInfo current = (AdapterContextMenuInfo) item.getMenuInfo();
        long courseId = current.id;
        switch (item.getItemId()) {
            case OPEN_ID:
                openCourseListActivity(courseId);
                break;
            case EDIT_ID:
                openEditDialog(courseId);
                break;
            case DELETE_ID:
                openDeleteDialog(courseId);
                break;
            default:
            	Log.e("CourseOverviewActivity", "UNKNOWN CASE IN ContextItemSelected(MenuItem item)");
        }
        return super.onContextItemSelected(item);
    }
    
    private void openEditDialog(final long courseId){
            final Dialog editCourseDialog = new Dialog(CourseOverviewActivity.this);

            editCourseDialog.setContentView(R.layout.course_dialog_add);
            editCourseDialog.setTitle("Edit a course");
            editCourseDialog.setCancelable(true);
            editCourseDialog.show();

            final EditText courseNameText = (EditText) editCourseDialog.findViewById(R.id.courseNameEditText);
            final Spinner comboBoxCategory = (Spinner) editCourseDialog.findViewById(R.id.courseCategorySpinner);
            final Spinner comboBoxSemester = (Spinner) editCourseDialog.findViewById(R.id.courseSemesterSpinner);
            final EditText courseYearText = (EditText) editCourseDialog.findViewById(R.id.courseYearEditText);
            
            final Course course = courseDao.load(courseId);
            courseNameText.setText(course.getName());
            int year = course.getYear();
            courseYearText.setText(Integer.toString(year));
            
            String arrayCategories[] = new String[]{"Math", "Languages", "Science", "Economics", "Others"};
            String arraySemesters[] = new String[]{"Spring", "Summer", "Winter"};
            
            ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayCategories);
            comboBoxCategory.setAdapter(categoriesAdapter);
            ArrayAdapter<String> semestersAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySemesters);
            comboBoxSemester.setAdapter(semestersAdapter);
            
            if(course.getCategory().equals("Math")){
                comboBoxCategory.setSelection(0);
            }
            if(course.getCategory().equals("Languages")){
                comboBoxCategory.setSelection(1);
            }
            if(course.getCategory().equals("Science")){
                comboBoxCategory.setSelection(2);
            }
            if(course.getCategory().equals("Economics")){
                comboBoxCategory.setSelection(3);
            }
            if(course.getCategory().equals("Others")){
                comboBoxCategory.setSelection(4);
            }
            
            if(course.getSemester().equals("Spring")){
                comboBoxSemester.setSelection(0);
            }
            if(course.getSemester().equals("Summer")){
                comboBoxSemester.setSelection(1);
            }
            if(course.getSemester().equals("Winter")){
                comboBoxSemester.setSelection(2);
            }
            
            Button cancelButton = (Button) editCourseDialog.findViewById(R.id.cancelButton);
            final Button saveButton = (Button) editCourseDialog.findViewById(R.id.saveButton);
            saveButton.setEnabled(false);
            
            saveButton.setEnabled(!courseNameText.getText().toString().equals(""));
            saveButton.setEnabled(!courseYearText.getText().toString().equals(""));

            courseNameText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    saveButton.setEnabled(!courseNameText.getText().toString().equals(""));
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            
            courseYearText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    saveButton.setEnabled(!courseYearText.getText().toString().equals(""));
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
//                    Course thisCourse = courseDao.load(courseId)
                    String courseName = courseNameText.getText().toString();               
                    String courseCategory = comboBoxCategory.getSelectedItem().toString();               
                    String courseSemester = comboBoxSemester.getSelectedItem().toString();
                    int courseYear = 2012;
                    try{
                        courseYear = Integer.parseInt(courseYearText.getText().toString());
                    }catch (NumberFormatException e){
                        Toast.makeText(getParent(), "Error with year, year was set to 2012", 5);
                    }             

                    courseNameText.setText("");
                    courseYearText.setText("");

                    Course course = new Course(courseId, courseName, courseCategory, courseSemester, courseYear);
                    courseDao.update(course);

                    Log.d("CourseOverviewActivity", "Inserted new course: [" + course.getId() + "] " + courseName + " " + courseCategory);

                    cursor.requery();
                    editCourseDialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCourseDialog.dismiss();
                }
            });
        }

}
