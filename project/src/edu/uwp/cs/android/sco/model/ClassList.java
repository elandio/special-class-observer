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
 * Holds a list of classes
 * 
 * @author M.Duettmann
 */
public class ClassList implements Serializable {

    private static final long serialVersionUID = 736367943607885903L;

    List<StudentList> clList;

    public ClassList() {
        clList = new ArrayList<StudentList>();
    }

    public boolean add(StudentList stList) {
        clList.add(stList);
        return true;
    }

    public StudentList get(int position) {
        return clList.get(position);
    }

    public Integer size() {
        return clList.size();
    }

    public String[] getClassesNames() {
        int size = clList.size();
        String[] str;

        int i = 0;

        if (size != 0) {
            
            str  = new String[size];
            
            for (StudentList stList : clList) {
                str[i] = stList.toString();
                i++;
            }            
        }
        else {
            str = new String[]{"No classes"};
        }
        return str;
    }

}
