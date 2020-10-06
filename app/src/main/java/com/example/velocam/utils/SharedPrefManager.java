package com.example.velocam.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.velocam.Model.User;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "velocam";

    private static final String KEY_USER_PHONE = "keyuserphone";
    private static final String KEY_USER_FNAME = "keyuserfname";
    private static final String KEY_USER_LNAME = "keyuserlname";
    private static final String KEY_USER_ADDRESS = "keyuseraddress";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_DESIGNATION = "keyuserdesignation";
    private static final String KEY_USER_IMAGE = "keyuserimage";
    private static final String KEY_USER_CODE = "keyusercode";
    private static final String KEY_USER_CO_star = "keyuserco_start";
    private static final String KEY_USER_PASSWORD = "keyuserpassword";



    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_FNAME, user.getFirst_name());
        editor.putString(KEY_USER_LNAME, user.getLast_name());
        editor.putString(KEY_USER_ADDRESS, user.getAddress());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_DESIGNATION, user.getDesignation());
        editor.putString(KEY_USER_CODE, user.getCompany_code());
        editor.putString(KEY_USER_CO_star, user.getCompany_start());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_USER_IMAGE, user.getImage());







        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(

                sharedPreferences.getString(KEY_USER_PHONE,null),
                sharedPreferences.getString(KEY_USER_FNAME, null),
                sharedPreferences.getString(KEY_USER_LNAME, null),
                sharedPreferences.getString(KEY_USER_ADDRESS, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_DESIGNATION, null),
                sharedPreferences.getString(KEY_USER_CODE, null),
                sharedPreferences.getString(KEY_USER_CO_star, null),
                sharedPreferences.getString(KEY_USER_PASSWORD, null),
                sharedPreferences.getString(KEY_USER_IMAGE, null)







        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}