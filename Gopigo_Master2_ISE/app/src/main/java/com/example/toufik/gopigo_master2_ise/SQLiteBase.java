package com.example.toufik.gopigo_master2_ise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by toufik on 06/01/2017.
 */

public class SQLiteBase extends SQLiteOpenHelper {

    private static final String CREATE_BDD = "CREATE TABLE sensor (id INTEGER primary key, measure INTEGER);";

    private static String name = "sensor.db";
    private static int version = 1;


    public SQLiteBase(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE sensor;");
        onCreate(db);
    }
}
