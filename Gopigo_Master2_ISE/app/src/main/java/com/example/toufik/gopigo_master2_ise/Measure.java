package com.example.toufik.gopigo_master2_ise;

/**
 * Created by toufik on 06/01/2017.
 */

public class Measure {
    int id, measure;

    public Measure(int id, int measure) {
        this.id = id;
        this.measure = measure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }
}
