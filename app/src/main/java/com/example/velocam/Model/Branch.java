package com.example.velocam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Branch {
    @SerializedName("branch_code")
    @Expose
    private String branch_code ;
    @SerializedName("branch_name")
    @Expose
    private String branch_name;
    @SerializedName("branch_radius")
    @Expose
    private String branch_radius;
    @SerializedName("branch_address")
    @Expose
    private String branch_address ;
    @SerializedName("branch_latitude")
    @Expose
    private String branch_latitude ;
    @SerializedName("branch_longitude")
    @Expose
    private String branch_longitude;
    @SerializedName("admin_phone")
    @Expose
    private String admin_phone ;

//    public Branch(String branch_code, String branch_name, String branch_radius, String branch_address, String branch_latitude, String branch_longitude, String admin_phone) {
//        this.branch_code = branch_code;
//        this.branch_name = branch_name;
//        this.branch_radius = branch_radius;
//        this.branch_address = branch_address;
//        this.branch_latitude = branch_latitude;
//        this.branch_longitude = branch_longitude;
//        this.admin_phone = admin_phone;
//    }


    public Branch(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_radius() {
        return branch_radius;
    }

    public void setBranch_radius(String branch_radius) {
        this.branch_radius = branch_radius;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getBranch_latitude() {
        return branch_latitude;
    }

    public void setBranch_latitude(String branch_latitude) {
        this.branch_latitude = branch_latitude;
    }

    public String getBranch_longitude() {
        return branch_longitude;
    }

    public void setBranch_longitude(String branch_longitude) {
        this.branch_longitude = branch_longitude;
    }

    public String getAdmin_phone() {
        return admin_phone;
    }

    public void setAdmin_phone(String admin_phone) {
        this.admin_phone = admin_phone;
    }
    @Override
    public String toString() {

        return getBranch_name() ; // You can add anything else like maybe getDrinkType()
    }

}
