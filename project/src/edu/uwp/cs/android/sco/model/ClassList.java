/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of classes
 * 
 * @author M.Duettmann
 */
@Deprecated
public class ClassList implements Serializable {

    private static final long serialVersionUID = 736367943607885903L;

    List<StudentList> clList;

    /**
     * Constructor
     */
    public ClassList() {
        clList = new ArrayList<StudentList>();
    }

    /**
     * 
     * @param stList Student which should be added
     * @return Status if import was successfull 
     */
    public boolean add(StudentList stList) {
        clList.add(stList);
        return true;
    }
    
    /**
     * 
     * @param position The position in the List
     * @return The element at the given position
     */
   
    public StudentList get(int position) {
        return clList.get(position);
    }

    public Integer size() {
        return clList.size();
    }
    
    /**
     * 
     * @return String array with all the classnames
     */
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
