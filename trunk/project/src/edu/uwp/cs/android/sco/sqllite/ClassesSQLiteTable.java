package edu.uwp.cs.android.sco.sqllite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClassesSQLiteTable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sco.db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_NAME = "classes";
    public static final String ID = "id";
    public static final String NAME = "name";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + ID
            + " integer primary key autoincrement, " + NAME
            + " text not null);";

    public ClassesSQLiteTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ClassesSQLiteTable.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}