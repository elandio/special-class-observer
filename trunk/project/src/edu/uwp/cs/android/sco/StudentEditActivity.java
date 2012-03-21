package edu.uwp.cs.android.sco;

import edu.uwp.cs.android.sco.model.Disability;
import edu.uwp.cs.android.sco.model.Student;
import edu.uwp.cs.android.sco.model.StudentAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StudentEditActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_student);
	    Student test = new Student("Matthias", "Kleinert", "Schwul");
	    test.addDisability("Test1", "Test1");
	    Disability[] disabilities = new Disability[test.attrs.size()];
	    int i=0;
	    for (Disability disability : test.attrs) {
			disabilities[i]= disability;
			i++;
		}
	    StudentAdapter adapter = new StudentAdapter(this,
        R.layout.row_student, disabilities);
	    
	    ListView listView = getListView();
	    View header = (View)getLayoutInflater().inflate(R.layout.student_header, null);
        listView.addHeaderView(header);
        TextView tvheader = (TextView)findViewById(R.id.tvhead);
	    tvheader.setText(test.toString());

        setListAdapter(adapter);
	}
}