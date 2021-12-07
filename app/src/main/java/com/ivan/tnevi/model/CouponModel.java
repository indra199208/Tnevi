package com.ivan.tnevi.model;

public class CouponModel {

    private String id, coupon_code, coupon_amount, expire_time, dis_type, coupon_status, start_time, multiple_applicable, applicable_number;


    public CouponModel(String coupon_code, String coupon_amount) {
        this.coupon_code = coupon_code;
        this.coupon_amount = coupon_amount;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getMultiple_applicable() {
        return multiple_applicable;
    }

    public void setMultiple_applicable(String multiple_applicable) {
        this.multiple_applicable = multiple_applicable;
    }

    public String getApplicable_number() {
        return applicable_number;
    }

    public void setApplicable_number(String applicable_number) {
        this.applicable_number = applicable_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getDis_type() {
        return dis_type;
    }

    public void setDis_type(String dis_type) {
        this.dis_type = dis_type;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }
}
