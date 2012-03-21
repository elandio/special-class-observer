package edu.uwp.cs.android.sco.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.uwp.cs.android.sco.model.Student;

public class StudentsDB {

    // Database fields
    private SQLiteDatabase sqlLiteDB;

    private SQLiteOpener sqlliteOpener;

    private String[] allColumns = { sqlliteOpener.STUDENT_ID, sqlliteOpener.STUDENT_F_NAME, sqlliteOpener.STUDENT_L_NAME, sqlliteOpener.CLASSES_NAME };

    public StudentsDB(Context context) {
        sqlliteOpener = new SQLiteOpener(context);

    }

    public void open() throws SQLException {
        sqlLiteDB = sqlliteOpener.getWritableDatabase();
        //        sqlliteOpener.onCreate(sqlLiteDB);
    }

    public void close() {
        sqlLiteDB.close();
    }

    public Student createStudent(String f_name, String l_name, String className) {

        ContentValues values = new ContentValues();
        values.put(sqlliteOpener.STUDENT_F_NAME, f_name);
        values.put(sqlliteOpener.STUDENT_L_NAME, l_name);
        values.put(sqlliteOpener.CLASSES_NAME, className);

        long insertId = sqlLiteDB.insert(sqlliteOpener.STUDENT_TABLE, null, values);
        if (insertId == -1) {
            Log.e("Insert Data", "Error occurred with inserting data!");
        }
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.STUDENT_TABLE, allColumns, sqlliteOpener.STUDENT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Student st = new Student(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return st;
    }

    //TODO: Use for deleting students
    //    public void deleteComment(Stu comment) {
    //        long id = comment.getId();
    //        System.out.println("Comment deleted with id: " + id);
    //        classesDB.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
    //                + " = " + id, null);
    //    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<Student>();

        Cursor cursor = sqlLiteDB.query(sqlliteOpener.STUDENT_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Student st = new Student(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            students.add(st);
            cursor.moveToNext();
        }
        cursor.close();
        return students;
    }

    public Student getStudent(int posi) {
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.STUDENT_TABLE, allColumns, null, null, null, null, null);

        cursor.moveToPosition(posi);
        Student st = new Student(cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return st;
    }

    public String[] getStundesInClass(String className) {
        Cursor cursor = sqlLiteDB.query(sqlliteOpener.STUDENT_TABLE, allColumns, sqlliteOpener.CLASSES_NAME + " = \"" + className + "\"", null, null, null, null);

        String[] students = new String[cursor.getCount()];
        cursor.moveToFirst();

        int i = 0;
        while (!cursor.isAfterLast()) {
            students[i] = cursor.getString(1) + " " + cursor.getString(2);
            i++;
            cursor.moveToNext();
        }
        cursor.close();

        return students;
    }
}
