/**
 * 
 */
package edu.uwp.cs.android.sco.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;


/**
 * Holds a list of classes
 * 
 * @author  M.Duettmann
 *
 */
public class ClassList extends 
ArrayList<StudentList> {

    private List<StudentList> clList = new ArrayList<StudentList>();    

    @Override
    public boolean add(StudentList stList){
        
        clList.add(stList);
        return true;
    }
    
    /**
     * @return the clList
     */
    public List<StudentList> getClList() {
        return clList;
    }
    
    public String[] getStudentNames(){
        
        int size = clList.size();
        
        String[] str = new String[size];
        
        Iterator iterator = clList.iterator();
        int i = 0;
        
        while(iterator.hasNext()){
            String s = iterator.next().toString();
            Log.i("Test", s);
            str[i] = s;
            
            i++;
        }
        
        return str;
    }
    
}
