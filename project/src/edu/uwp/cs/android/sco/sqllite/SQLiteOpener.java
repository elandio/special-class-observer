package edu.uwp.cs.android.sco.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@Deprecated
public class SQLiteOpener extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sco.db";

    private static final int DATABASE_VERSION = 1;

    public static final String CLASSES_TABLE = "classes";

    public static final String CLASSES_ID = "c_id";

    public static final String CLASSES_NAME = "c_name";

    public static final String STUDENT_TABLE = "students";

    public static final String STUDENT_ID = "s_id";

    public static final String STUDENT_F_NAME = "s_f_name";

    public static final String STUDENT_L_NAME = "s_l_name";

    // Database creation sql statement
    private static final String CLASSES_CREATE = "create table " + CLASSES_TABLE + "( " + CLASSES_ID + " integer primary key autoincrement, " + CLASSES_NAME + " text not null);";

    private static final String STUDENTS_CREATE = "create table " + STUDENT_TABLE + "( " + STUDENT_ID + " integer primary key autoincrement, " + STUDENT_F_NAME + " text not null, " + STUDENT_L_NAME + " text not null, " + CLASSES_NAME + " text not null);";

    public SQLiteOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //        database.execSQL(CLASSES_CREATE);
        //        database.execSQL(STUDENTS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteOpener.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CLASSES_TABLE);
        onCreate(db);
    }

}
