package com.example.toufik.gopigo_master2_ise;

import java.util.List;

/**
 * Created by toufik on 06/01/2017.
 */

public class DataHolder {

    List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance(){return holder;}
}