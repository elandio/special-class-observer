package edu.uwp.cs.android.sco;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Disability;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.view.StudentAdapter;

public class StudentProfileActivity extends ListActivity implements View.OnClickListener {
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Student student;
    private StudentAdapter adapter;
    
    // buttons
    private Button buttonSaveChanges, buttonCancelOperation; 
    
    // editText
    private EditText comment;
    
    //global variables
    private boolean backButtonPressed;
    private final int criticalRatingSum = 15;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backButtonPressed=false;
        Log.d("StudentProfileActivity", "onCreate() called");
//      openStudentProfile(); // will be executed by onResume()
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("StudentProfileActivity", "onResume() called");
    	openStudentProfile();
    }
    
    @Override
    protected void onPause () {
    	super.onPause();
    	Log.d("StudentProfileActivity", "onPause() called");
    	releaseAllResources();
    }
    
    @Override
    protected void onStop () {
    	super.onStop();
    	Log.d("StudentProfileActivity", "onStop() called");
    	releaseAllResources();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Log.d("StudentProfileActivity", "onRestart() called");
//    	openStudentProfile();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("StudentProfileActivity", "onDestroy() called");
    	releaseAllResources();
    }
    
    @Override
	public void onBackPressed() {
    	backButtonPressed=true;
    	openSaveConfirmDialog();
	}
    
    private void releaseAllResources() {
    	adapter = null;
    	studentDao = null;
        daoSession = null;
        daoMaster = null;
    	if (db != null) {
    		db.close();
    	}
    }
	
	public void openStudentProfile() {
        setContentView(R.layout.student_profile_edit);
        setTitle("Student Classroom Observer - Student Profil");
        
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
	    student = studentDao.load(getIntent().getLongExtra("studentId", 1l));
	    
	    // TEMP CREATING STUDENT
		if (student == null) {
			// student is not in database - create a test student
			student = new Student(1l, "Test", "Driver");
			studentDao.insert(student);
			student.addDefaultDisabilities();
			studentDao.update(student);
		}
	    adapter = new StudentAdapter(this, R.layout.student_profile_row, student.getDisabilities());
	    
	    //Adding the header
	    TextView tvStudentName = (TextView) findViewById(R.id.student_profile_name);
	    tvStudentName.setText(student.getFName() + " " + student.getLName());
	    
	    //Adding the footer
//	    View footer = (View)getLayoutInflater().inflate(R.layout.studnet_profile_footer, null);
//	    listView.addFooterView(footer);
//	    comment = (EditText)findViewById(R.id.et_profile_comment);
	    
	    // initialize buttons and set onclicklisteners
        buttonSaveChanges = (Button) findViewById(R.id.student_profile_bSaveChanges);
        buttonSaveChanges.setOnClickListener(this);
        
        buttonCancelOperation = (Button) findViewById(R.id.student_profile_bCancelOperation);
        buttonCancelOperation.setOnClickListener(this);

        setListAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		if (v == buttonSaveChanges) {
			openSaveConfirmDialog();
		}
		if (v == buttonCancelOperation) {
			finish();
		}
	}
	
	public Disability getDisability(int position) {
		return(((StudentAdapter)getListAdapter()).getItem(position));
	}
	
    protected void openSaveConfirmDialog() {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Want to save changes on this students profile? ")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    					List<Disability> disUp = new ArrayList<Disability>();
    					Integer ratingSum=0;
    					for (int i=0; i<adapter.getCount(); i++){
    						Disability tempDis = adapter.getItem(i);
    						ratingSum=ratingSum+tempDis.getRating();
    						disUp.add(tempDis);
    					}
    					student.updateDisabilities(disUp);
    					if (ratingSum>criticalRatingSum){
    						informUserRating();
    					}else{
    						finish();
    					}
    					
    	           }
    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   if (backButtonPressed){
    	        		   finish();
    	        	   }else {
    	        		   dialog.cancel();
    	        	   }
    	           }
    	       });
    	backButtonPressed=false;
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    protected void informUserRating() {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("The students total rating is above 15. You maybe want to consult a Psychologist")
    	       .setCancelable(false)
    	       .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    	    		   finish();
    	    	   }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

}