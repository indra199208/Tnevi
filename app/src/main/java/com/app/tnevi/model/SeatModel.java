package com.app.tnevi.model;

public class SeatModel {

    private  String row_name, row_id, seat_price;

    public SeatModel(String row_name,String row_id, String seat_price ) {
        this.row_name = row_name;
        this.row_id = row_id;
        this.seat_price = seat_price;
    }


    public String getSeat_price() {
        return seat_price;
    }

    public void setSeat_price(String seat_price) {
        this.seat_price = seat_price;
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
