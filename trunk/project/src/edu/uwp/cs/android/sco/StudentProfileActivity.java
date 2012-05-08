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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Disability;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.view.StudentAdapter;

/**
 * This prototype was developed for the Mobile Device Programming class
 * in the Spring Semester 2012 at the University of Wisconsin Parkside.
 * 
 * @author Michael Tiede, Matthias Kleinert, Moritz Duettmann
 * @since May 2012
 */
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
    private final int criticalRatingSum = 30;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    	backButtonDialog();
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
	    View footer = (View)getLayoutInflater().inflate(R.layout.student_profile_footer, null);
	    getListView().addFooterView(footer);
	    comment = (EditText)findViewById(R.id.student_profile_comment);
	    comment.setText(student.getNote());
	    
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
	protected void backButtonDialog(){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Want to save changes on this students profile? ")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    					Integer ratingSum = updateStudentAndRating();
    					if (ratingSum>criticalRatingSum){
    						informUserRating(true);
    					}else{
    						finish();
    					}
    					
    	           }

    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   finish();
    	           }
    	       }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	private Integer updateStudentAndRating() {
		List<Disability> disUp = new ArrayList<Disability>();
		Integer ratingSum=0;
		for (int i=0; i<adapter.getCount(); i++){
			Disability tempDis = adapter.getItem(i);
			ratingSum=ratingSum+tempDis.getRating();
			disUp.add(tempDis);
		}
		
		student.updateDisabilities(disUp, comment.getText().toString(), ratingSum);
		return ratingSum;
	}
    protected void openSaveConfirmDialog() {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Want to save changes on this students profile? ")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    	    		   	Integer ratingSum = updateStudentAndRating();
    					if (ratingSum>criticalRatingSum){
    						informUserRating(false);
    					}
    					dialog.cancel();
    					
    	           }
    	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        		   dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    protected void informUserRating(final boolean backButtonPressed) {
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("The students total rating is above " +  criticalRatingSum + ". You maybe should consult a Psychologist")
    	       .setCancelable(false)
    	       .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
    	    	   public void onClick(DialogInterface dialog, int id) {
    	    		   if(backButtonPressed){
    	    			   finish();
    	    		   }else{
    	    			   dialog.cancel();   
    	    		   }
    	    	   }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }

}