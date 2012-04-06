package edu.uwp.cs.android.sco;

import java.util.Arrays;

import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Disability;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.model.StudentAdapter;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class StudentProfileActivity extends ListActivity {
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Button bsave;
    private RatingBar rbdis;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_student);
        
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "student-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
	    Student student = studentDao.load(1l);
	    
	    // TEMP CREATING STUDENT
		if (student == null) {
			// student is not in database - create a test student
			student = new Student(1l, "Test", "Driver");
			studentDao.insert(student);
			student.addDefaultDisabilities();
			studentDao.update(student);
		}
		
		student.addDisability(new Disability(null, "Disability 4", "Info 4", 2, "Math", student.getId()));
		studentDao.update(student);
		Disability[] disabilities = new Disability[student.getDisabilities().size()];
	    int i=0;
	    for (Disability disability : student.getDisabilities()) {
			disabilities[i]= disability;
			i++;
		}
	    StudentAdapter adapter = new StudentAdapter(this,
        R.layout.row_student, disabilities);
	    
	    ListView listView = getListView();
	    View header = (View)getLayoutInflater().inflate(R.layout.student_header, null);
        listView.addHeaderView(header);
        TextView tvheader = (TextView)findViewById(R.id.tvhead);
	    tvheader.setText(student.getFName() + " " +student.getLName());
	    Log.v("edit", student.getDisabilities().get(0).getRating().toString());
	    
//	    rbdis = (RatingBar)findViewById(R.id.ratingbar);
//	    rbdis.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
//			
//			@Override
//			public void onRatingChanged(RatingBar ratingBar, float rating,
//					boolean fromUser) {
//				Log.v("edit", "" + ratingBar.getTag());			
//			}
//		});

	    bsave = (Button)findViewById(R.id.bsave);
	    bsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ListView listView = getListView();
				for(int i=1; i<listView.getChildCount();i++){
					Log.v("edit", listView.getChildAt(i).toString());
				}
//				Log.v("edit", student.getDisabilities().get(0).getRating().toString());
			}
		});

        setListAdapter(adapter);
	}

}