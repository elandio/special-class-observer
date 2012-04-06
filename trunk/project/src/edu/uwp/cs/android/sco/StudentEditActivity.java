package edu.uwp.cs.android.sco;

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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StudentEditActivity extends ListActivity {
	
	// database management
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;
	
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
	    tvheader.setText(student.toString());

        setListAdapter(adapter);
	}
}