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

    public StudentList(String name) {
        this.name = name;
        stList = new ArrayList<Student>();
    }

    public boolean add(Student st) {
        stList.add(st);
        return true;
    }

    public Integer size() {
        return stList.size();
    }

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
