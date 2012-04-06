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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.uwp.cs.android.sco.entities.Course;
import edu.uwp.cs.android.sco.entities.CourseDao;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.view.MyListAdapter;

public class CourseOverviewActivity extends ListActivity implements View.OnClickListener {

    // database management
    private SQLiteDatabase db;

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    private CourseDao courseDao;

    private Cursor cursor;

    private MyListAdapter adapter;

    // buttons
    private Button buttonAddCourse, buttonResetSearch;

    private EditText etSearchCourse;
    
    private static final int DELETE_ID = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_overview);

        // TODO refactor "student-db" in all classes to "sco-db"
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "student-db", null);
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

        etSearchCourse = (EditText) findViewById(R.id.et_searchCourse);

        // initialize buttons and set onclicklisteners
        buttonAddCourse = (Button) findViewById(R.id.course_overview_bAddCourse);
        buttonAddCourse.setOnClickListener(this);

        buttonResetSearch = (Button) findViewById(R.id.course_overview_bResetSearch);
        buttonResetSearch.setOnClickListener(this);

        etSearchCourse.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
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
        final EditText courseCategoryText = (EditText) addCourseDialog.findViewById(R.id.courseCategoryEditText);

        Button cancelButton = (Button) addCourseDialog.findViewById(R.id.cancelButton);
        final Button saveButton = (Button) addCourseDialog.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);

        courseNameText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(validateAddCourseInput(addCourseDialog, courseNameText, courseCategoryText));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        courseCategoryText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(validateAddCourseInput(addCourseDialog, courseNameText, courseCategoryText));
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
                String courseCategory = courseCategoryText.getText().toString();

                // clear fields for first and last name
                courseNameText.setText("");
                courseCategoryText.setText("");

                Course course = new Course(null, courseName, courseCategory);
                courseDao.insert(course);

                Log.d("SCO-Project", "Inserted new course: [" + course.getId() + "] " + courseName + " " + courseCategory);

                // TEST AREA
                //                Course course = new Course(null, "Discrete Structures", "Math");
                //                daoSession.getCourseDao().insert(course);
                //                
                //                student.addCourse(course);
                //                courseDao.update(student);
                // TEST AREA END

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

    public boolean validateAddCourseInput(Dialog dialog, EditText courseName, EditText courseCategory) {
        if (!courseName.getText().toString().equals("") && !courseCategory.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * DELETE COURSE AND RELATIONS
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long courseId) {
        Course course = courseDao.load(courseId);

        // open dialog for delete confirmation
        openDeleteDialog(course);
    }

    protected void openDeleteDialog(final Course course) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CourseOverviewActivity.this);
        builder.setMessage("Are you sure you want to delete the course " + course.getName() + " (category: " + course.getCategory() + ")?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                // TODO CLEAN RELATIONSHIP
                //course.deleteRelation(courseId);
                courseDao.deleteByKey(course.getId());
                cursor.requery();

                Log.d("SCO-Project", "Deleted course, courseId: " + course.getId());
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

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
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                Course course = courseDao.load(info.id);
                openDeleteDialog(course);
                return true;
        }
        return super.onContextItemSelected(item);
    }
}