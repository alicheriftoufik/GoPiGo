package com.example.toufik.gopigo_master2_ise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by toufik on 06/01/2017.
 */

public class MeasureBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "sensor.db";

    private static final String TABLE_SENSOR = "sensor";
    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;
    private static final String COL_MEASURE = "measure";
    private static final int NUM_COL_MEASURE = 1;

    private SQLiteBase base;

    private SQLiteDatabase bdd;

    public MeasureBDD(Context context) {
        base = new SQLiteBase(context);
    }

    public void open(){
        bdd = base.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertMeasure(Measure measure){
        ContentValues values = new ContentValues();
        //values.put(COL_ID, measure.getId());
        values.put(COL_MEASURE, measure.getMeasure());
        return bdd.insert(TABLE_SENSOR, null, values);
    }

    public int updateMeasure(Measure measure){
        String id = String.valueOf(measure.getId());
        ContentValues values = new ContentValues();
        values.put(COL_ID, measure.getId());
        values.put(COL_MEASURE, measure.getMeasure());
        return bdd.update(TABLE_SENSOR, values, COL_ID + " = ?", new String[]{ id });
    }

    public int removeMeasure(int id){
        return bdd.delete(TABLE_SENSOR, COL_ID + " = " +id, null);
    }

    public boolean checkMeasure(Measure measure){
        String id = String.valueOf(measure.getId());
        Cursor cursor = bdd.rawQuery("SELECT * FROM "+ TABLE_SENSOR +" WHERE "+COL_ID+" = ?", new String[]{ id });
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void cleanTable(){
        base.onUpgrade(bdd,0,1);
    }
}