package edu.uwp.cs.android.sco.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.uwp.cs.android.sco.model.StudentList;

public class ClassesDB {

    // Database fields
    private SQLiteDatabase sqlLiteDB;

    private SQLiteOpener sqlliteOpener;

    private String[] allColumns = { sqlliteOpener.CLASSES_ID, sqlliteOpener.CLASSES_NAME };

    public ClassesDB(Context context) {
        sqlliteOpener = new SQLiteOpener(context);

    }

    public void open() throws SQLException {
        sqlLiteDB = sqlliteOpener.getWritableDatabase();
        //        sqlliteOpener.onCreate(sqlLiteDB);
    }

    public void close() {
        sqlLiteDB.close();
    }

    public StudentList createStudentList(String name) {

        ContentValues values = new ContentValues();
        values.put(sqlliteOpener.CLASSES_NAME, name);

        System.out.println("Blablabla!" + sqlLiteDB.toString());

        long insertId = sqlLiteDB.insert(sqlliteOpener.CLASSES_TABLE, null, values);
        if (insertId == -1) {
            Log.e("Insert Data", "Error occurred with inserting data!");
        }
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.CLASSES_TABLE, allColumns, sqlliteOpener.CLASSES_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        StudentList stList = new StudentList(cursor.getString(1));
        cursor.close();
        return stList;
    }

    //TODO: Use for deleting classes
    //    public void deleteComment(Stu comment) {
    //        long id = comment.getId();
    //        System.out.println("Comment deleted with id: " + id);
    //        classesDB.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
    //                + " = " + id, null);
    //    }

    @Deprecated
    public List<StudentList> getAllClasses() {
        List<StudentList> classes = new ArrayList<StudentList>();

        Cursor cursor = sqlLiteDB.query(sqlliteOpener.CLASSES_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StudentList stList = new StudentList(cursor.getString(1));
            classes.add(stList);
            cursor.moveToNext();
        }
        cursor.close();
        return classes;
    }

    public String[] getAllClassNames() {
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.CLASSES_TABLE, allColumns, null, null, null, null, null);
        String[] classes = new String[cursor.getCount()];

        cursor.moveToFirst();

        int i = 0;
        while (!cursor.isAfterLast()) {
            classes[i] = cursor.getString(1);
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        return classes;
    }

    @Deprecated
    public StudentList getStudentList(int posi) {
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.CLASSES_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToPosition(posi);
        StudentList stList = new StudentList(cursor.getString(1));

        return stList;
    }

    public String getClassName(int posi) {
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.CLASSES_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToPosition(posi);

        return cursor.getString(1);
    }
}
