/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

/**
 * Holds a list of students
 * 
 * @author M.Duettmann
 */
public class StudentList implements Serializable {

    private static final long serialVersionUID = 2124855436805916877L;

    String name;

    List<Student> stList;

    /**
     * Constructor
     * 
     * @param name Name of the list
     */
    public StudentList(String name) {
        this.name = name;
        stList = new ArrayList<Student>();
    }

    /**
     * 
     * @param st Student which should be added
     * @return Status if import was successfull
     */
    public boolean add(Student st) {
        stList.add(st);
        return true;
    }

    public Integer size() {
        return stList.size();
    }

    /**
     * 
     * @return A String array with all the student names
     */
    public String[] getStudentsNames() {

        int size = stList.size();

        String[] str;

        int i = 0;

        if (size != 0) {

            str = new String[size];
            for (Student st : stList) {
                str[i] = st.toString();
                i++;
            }
        }
        else {
            str = new String[]{"No students"};
        }

        return str;
    }

    public String toString() {
        return name;
    }

}
