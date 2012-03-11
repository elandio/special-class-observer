/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.util.ArrayList;
import java.util.List;


/** 
 * Holds a list of students
 * 
 * @author M.Duettmann
 *
 */
public class StudentList extends ArrayList<Student>{
    
    private List<Student> stList = new ArrayList<Student>();

    public boolean add(Student st){
        stList.add(st);
        return true;
    }
    
    /**
     * @return the stList
     */
    public List<Student> getStList() {
        return stList;
    }

    
}
