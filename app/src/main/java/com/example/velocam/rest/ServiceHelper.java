package com.example.velocam.rest;


import android.content.Context;

import com.example.velocam.Model.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class ServiceHelper {


    final String email="";
    final String password="";

    private static final String ENDPOINT = "https://fazaldrinkshop.000webhostapp.com/";

    private static OkHttpClient httpClient = new OkHttpClient();
    private static ServiceHelper instance = new ServiceHelper();
    private ApiInterface service;


    private ServiceHelper() {

        Retrofit retrofit = createAdapter().build();
        service = retrofit.create(ApiInterface.class);
    }

    public static ServiceHelper getInstance() {
        return instance;
    }

    private Retrofit.Builder createAdapter() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);
        Retrofit retrofit = createAdapter().build();
        service = retrofit.create(ApiInterface.class);
        return new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }







}