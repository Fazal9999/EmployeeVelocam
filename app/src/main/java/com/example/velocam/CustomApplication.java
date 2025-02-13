package com.example.velocam;

import android.app.Application;

import com.example.velocam.utils.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomApplication extends Application {
    private Gson gson;
    private GsonBuilder builder;
    private CustomSharedPreference shared;
    @Override
    public void onCreate() {
        super.onCreate();
        builder = new GsonBuilder();
        gson = builder.create();
        shared = new CustomSharedPreference(getApplicationContext());
    }
    public CustomSharedPreference getShared(){
        return shared;
    }
    public Gson getGsonObject(){
        return gson;
    }
}

