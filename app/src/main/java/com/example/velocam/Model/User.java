
package com.example.velocam.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("phone")
    @Expose
    private String phone ;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("address")
    @Expose
    private String address ;
    @SerializedName("email")
    @Expose
    private String email ;
    @SerializedName("company_code")
    @Expose
    private String company_code;
    @SerializedName("password")
    @Expose
    private String password ;
    @SerializedName("designation")
    @Expose
    private String designation ;
    @SerializedName("company_start")
    @Expose
    private String company_start;
    @SerializedName("image")
    @Expose
    private String image ;


    public User(String phone, String first_name, String last_name, String address, String email, String company_code, String password, String designation, String company_start, String image) {
        this.phone = phone;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.email = email;
        this.company_code = company_code;
        this.password = password;
        this.designation = designation;
        this.company_start = company_start;
        this.image = image;

    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany_start() {
        return company_start;
    }

    public void setCompany_start(String company_start) {
        this.company_start = company_start;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
