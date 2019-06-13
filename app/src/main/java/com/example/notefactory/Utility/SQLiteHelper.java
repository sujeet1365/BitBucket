package com.example.notefactory.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "NOTES";
    public static final String Column_TITLE = "Title";
    public static final String Column_Number = "Number";
    public static final String Column_BODY = "Body";
    public static final String Column_Timestamp = "Timestamp";
    public static final String Column_Image = "Image";

    public SQLiteHelper(Context context){
        super(context,"NOTESDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("+ Column_Number + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Column_TITLE + " TEXT," + Column_BODY + " TEXT," + Column_Image + " TEXT," + Column_Timestamp +
                " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}
