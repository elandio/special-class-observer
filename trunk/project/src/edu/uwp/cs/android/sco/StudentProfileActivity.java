package edu.uwp.cs.android.sco;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.uwp.cs.android.sco.entities.DaoMaster;
import edu.uwp.cs.android.sco.entities.DaoMaster.DevOpenHelper;
import edu.uwp.cs.android.sco.entities.DaoSession;
import edu.uwp.cs.android.sco.entities.Disability;
import edu.uwp.cs.android.sco.entities.Student;
import edu.uwp.cs.android.sco.entities.StudentDao;
import edu.uwp.cs.android.sco.view.StudentAdapter;

public class StudentProfileActivity extends ListActivity {
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
    private Student student;
    private Button bsave;
    private StudentAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_edit);
        setTitle("Student Classroom Observer - Student Profil");
        
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "sco-v1.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();
        
	    student = studentDao.load(1l);
	    
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
	    ListView listView = getListView();
	    View header = (View)getLayoutInflater().inflate(R.layout.student_profile_header, null);
        listView.addHeaderView(header);
        TextView tvheader = (TextView)findViewById(R.id.tvhead);
	    tvheader.setText(student.getFName() + " " +student.getLName());
	    
	    bsave = (Button)findViewById(R.id.bsave);
	    bsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Disability> disUp = new ArrayList<Disability>();
				for (int i=0; i<adapter.getCount(); i++){
					disUp.add(adapter.getItem(i));
				}
				student.updateDisabilities(disUp);
			}
		});

        setListAdapter(adapter);
	}
	
	public Disability getDisability(int position) {
		return(((StudentAdapter)getListAdapter()).getItem(position));
	}

}