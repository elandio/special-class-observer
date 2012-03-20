/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Student model
 * 
 * @author M.Duettmann
 */
public class Student implements Serializable {

	private static final long serialVersionUID = -6081462500274581471L;

	String lName; // Last Name

	String fName; // First Name
	public ArrayList<Disability> attrs;

	public Student(String fName, String lName) {
		this.fName = fName;
		this.lName = lName;
		attrs = new ArrayList<Disability>();
		attrs.add(new Disability("Disability 1", "Info 1"));
		attrs.add(new Disability("Disability 2", "Info 2"));
		attrs.add(new Disability("Disability 3", "Info 3"));
	}

	public String toString() {
		return (fName + " " + lName);

	}
	
	public void addDisability(String disName, String disInfo){
		attrs.add(new Disability(disName, disInfo));
	}

}
