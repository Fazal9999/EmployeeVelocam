package com.example.velocam.rest;


import android.widget.ArrayAdapter;

import com.example.velocam.Model.Branch;
import com.example.velocam.Model.BranchList;
import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.User;
import com.example.velocam.Model.UserList;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    /*
  Retrofit get annotation with our URL
  And our method that will return us the List of User List
  */
    @FormUrlEncoded
    @POST("getAllBranch.php")
    Observable<List<Branch>> getBranch(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("getAllBranch.php")
    Observable<ArrayList<Branch>> getBranches(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("getAllBranch.php")
    Observable<List<Branch>>get(@Field("phone") String phone);



    @GET("getAllBranch.php")
    public void getBran(Callback<JsonArray> callback,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("getAllBranch.php")
    Call<List<Branch>> spinBranch(@Field("phone") String phone);
    @GET("getAllBranch.php")
    Call<ArrayList<Branch>> getBranchString(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("getAllBranch.php")
    Call<BranchList> spin(@Field("phone") String phone);



    @GET("getAllUser.php")
    Call<UserList> getAllUser();


    @FormUrlEncoded
    @POST("login.php")
    Call<User>userLogin(
            @Field("email") String email,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse>checkUser(
            @Field("phone") String phone);




    // (fname,lname,addr,em, co,pass,des,com_s,phone,image);

    @FormUrlEncoded
    @POST("addUser.php")
    public Call<ResponseBody> registerUser(@Field("fname") String fname,
                                           @Field("lname") String lname,
                                           @Field("addr") String addr,
                                           @Field("em") String em,
                                           @Field("co") String co,
                                           @Field("pass") String pass,
                                           @Field("des") String des,
                                           @Field("com_s") String com_s,
                                           @Field("phone") String phone,
                                           @Field("image") String image);
    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUserById(@Field("phone") String phone);

  //  phone,fname,lname,desig,co_co,ema,image
    @FormUrlEncoded
    @POST("updateUser.php")
    public Call<ResponseBody> updateUser(@Field("phone") String phone,
                                         @Field("fname") String fname,
                                         @Field("lname") String lname,
                                         @Field("addr") String addr,
                                         @Field("desig") String desig,
                                         @Field("co_co") String co_co,
                                         @Field("em") String em,
                                         @Field("image") String image);

    @FormUrlEncoded
    @POST("updatePasswordUser.php")
    public Call<ResponseBody> updatePasswordUser(@Field("phone") String phone,
                                                 @Field("password") String password
                                                 );

    @GET("deleteUser.php")
    Call<ResponseBody> deleteUser(@Query("id") String id);


    @FormUrlEncoded
    @POST("addBranch.php")
    Call<String> registerBranch( @Field("branch_code") String branch_code,
                                       @Field("branch_name") String branch_name,
                                       @Field("branch_radius") String branch_radius,
                                       @Field("branch_address")  String branch_address,
                                       @Field("branch_latitude") double branch_latitude,
                                       @Field("branch_longitude") double branch_longitude,
                                       @Field("admin_phone") String admin_phone);


    @FormUrlEncoded
    @POST("UpdateBranch.php")
    Observable<String> updateBranch( @Field("branch_code") String branch_code,
                                     @Field("branch_name") String branch_name,
                                       @Field("branch_radius") String branch_radius,
                                       @Field("branch_address")  String branch_address,
                                       @Field("branch_latitude") double branch_latitude,
                                       @Field("branch_longitude") double branch_longitude,
                                       @Field("admin_phone") String admin_phone);



    @FormUrlEncoded
    @POST("DeleteBranch.php")
    Observable<String> deleteBranch(@Field("branch_code") String branch_code,
                                       @Field("admin_phone") String admin_phone);










}
