/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Student model
 * 
 * @author M.Duettmann
 */
public class Student implements Serializable {

    private static final long serialVersionUID = -6081462500274581471L;

    String lName; // Last Name

    String fName; // First Name

    Map<String, Integer> attrs = new HashMap<String, Integer>(); // Attributes

    /**
     * Constructor
     * 
     * @param fName First name 
     * @param lName Last name
     */
    public Student(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }
    
    /**
     *@return ("First name" "Last name") 
     */
    public String toString() {
        return (fName + " " + lName);

    }

}
