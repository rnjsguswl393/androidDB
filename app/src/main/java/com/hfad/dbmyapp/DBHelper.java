package com.hfad.dbmyapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME="mydb.db";
    public DBHelper(Context context, int version) {
        super(context,DB_NAME,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE testtable (pos TEXT,lati INTEGER,longi INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldver, int newver){
        db.execSQL("DROP TABLE IF EXISTS testtable");
        onCreate(db);
    }
}
