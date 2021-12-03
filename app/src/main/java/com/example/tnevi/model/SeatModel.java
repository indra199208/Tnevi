package com.example.tnevi.model;

public class SeatModel {

    private  String row_name, row_id;

    public SeatModel(String row_name,String row_id) {
        this.row_name = row_name;
        this.row_id = row_id;
    }

    public String getRow_name() {
        return row_name;
    }

    public void setRow_name(String row_name) {
        this.row_name = row_name;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

}
