/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.util.HashMap;
import java.util.Map;


/**
 * Student model
 * 
 * @author  M.Duettmann
 *
 */
public class Student {
    
    private String lName;  // Last Name
    private String fName;  // First Name
    
    private Map<String, Integer> attrs = new HashMap<String, Integer>();  // Attributes
    
    
    public Student(String fName, String lName){
        this.fName = fName;
        this.lName = lName;
        
    }


    
    /**
     * @return the lName
     */
    public String getlName() {
        return lName;
    }


    
    /**
     * @param lName the lName to set
     */
    public void setlName(String lName) {
        this.lName = lName;
    }


    
    /**
     * @return the fName
     */
    public String getfName() {
        return fName;
    }


    
    /**
     * @param fName the fName to set
     */
    public void setfName(String fName) {
        this.fName = fName;
    }

}
