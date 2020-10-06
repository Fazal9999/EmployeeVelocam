package com.example.velocam.utils;


import com.example.velocam.Model.Branch;
import com.example.velocam.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Config {


	public static final String URL_IMAGES = "https://fazaldrinkshop.000webhostapp.com/images/";

	// Keys that will be used to send the request to php scripts
	public static User currentUser=null;
	public static Branch currentBranch;
	public static final String KEY_PHONE = "phone";
	public static final String KEY_FIRST_NAME="first_name";
	public static final String KEY_LAST_NAME="last_name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_COMPANY_CODE = "company_code";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_DESIGNATION = "designation";
	public static final String KEY_COMPANY_START = "company_start";
	public static final String KEY_IMAGE = "image";
public static List<Branch> branchList=new ArrayList<>();
	public static List<String> branchadded=new ArrayList<>();
	public static int meter=0;


	// JSON Tags
	
	public static final String TAG_JSON_ARRAY = "result";
	public static final String TAG_PHONE = "phone";
	public static final String TAG_FIRST_NAME = "first_name";
	public static final String TAG_LAST_NAME = "last_name";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_COMPANY_CODE = "company_code";
	public static final String TAG_PASSWORD = "password";
	public static final String TAG_DESIGNATION = "designation";
	public static final String TAG_COMPANY_START = "company_start";
	public static final String TAG_IMAGE = "image";
}