/**
 * 
 */
package edu.uwp.cs.android.sco.model;


/**
 * @author M.Duettmann
 *
 */
public class ChecklistEntry {
    
    String name;
    int stars;
    String info;
    
    public ChecklistEntry(String name, int stars, String info){
        this.name = name;
        this.stars = stars;
        this.info = info;
    }
}
