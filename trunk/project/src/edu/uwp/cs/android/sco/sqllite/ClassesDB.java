package edu.uwp.cs.android.sco.sqllite;


import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import edu.uwp.cs.android.sco.model.StudentList;

public class ClassesDB {

    // Database fields
    private SQLiteDatabase classesDB;
    private ClassesSQLiteTable classesTable;
    private String[] allColumns = { classesTable.ID,
            classesTable.NAME };


    public ClassesDB(Context context) {
        classesTable = new ClassesSQLiteTable(context);

    }

    public void open() throws SQLException {
        classesDB = classesTable.getWritableDatabase();
    }

    public void close() {
        classesDB.close();
    }

    public StudentList createStudentList(String name) {
        ContentValues values = new ContentValues();
        values.put(classesTable.NAME, name);
        long insertId = classesDB.insert(classesTable.TABLE_NAME, null,
                values);
        Cursor cursor = classesDB.query(classesTable.TABLE_NAME,
                allColumns, classesTable.ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        StudentList stList = new StudentList(cursor.getString(1));
        cursor.close();
        return stList;
    }

//    public void deleteComment(Stu comment) {
//        long id = comment.getId();
//        System.out.println("Comment deleted with id: " + id);
//        classesDB.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
//                + " = " + id, null);
//    }

    public List<StudentList> getAllClasses() {
        List<StudentList> classes = new ArrayList<StudentList>();

        Cursor cursor = classesDB.query(classesTable.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StudentList stList = new StudentList(cursor.getString(1));
            classes.add(stList);
            cursor.moveToNext();
        }
        cursor.close();
        return classes;
    }
    
    public StudentList getStudentList(int posi) {
        Cursor cursor = classesDB.query(classesTable.TABLE_NAME, allColumns, classesTable.ID + " = " + posi, null, null, null, null);
        StudentList stList = new StudentList(cursor.getString(1));
        
        return stList;
    }
}