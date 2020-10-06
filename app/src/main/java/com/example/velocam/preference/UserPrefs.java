package com.example.velocam.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.velocam.utils.Config;


public class UserPrefs {

	private static final String USER_PREFS = "USER_PREFS";
	private SharedPreferences userSharedPrefs;
	private SharedPreferences.Editor prefsEditor;

	public UserPrefs(Context context) {
		this.userSharedPrefs = context.getSharedPreferences(USER_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = userSharedPrefs.edit();
	}

	public String getUserPhone() {
		return userSharedPrefs.getString(Config.KEY_PHONE, "");
	}

	public void setUserPhone(String userPhone) {
		prefsEditor.putString(Config.KEY_PHONE, userPhone).commit();
	}

	public String getUserFirtName() {
		return userSharedPrefs.getString(Config.KEY_FIRST_NAME, "");
	}


	public void setUserFirstName(String userFirstName) {
		prefsEditor.putString(Config.KEY_FIRST_NAME, userFirstName).commit();
	}

	public String getUserLastName() {
		return userSharedPrefs.getString(Config.KEY_LAST_NAME, "");
	}


	public void setUserLastName(String userLastName) {
		prefsEditor.putString(Config.KEY_LAST_NAME, userLastName).commit();
	}

	public String getUserAddress() {
		return userSharedPrefs.getString(Config.KEY_ADDRESS, "");
	}


	public void setUserAddress(String userAddress) {
		prefsEditor.putString(Config.KEY_ADDRESS, userAddress).commit();
	}

	public String getUserEmail() {
		return userSharedPrefs.getString(Config.KEY_EMAIL, "");
	}

	public void setUserEmail(String userEmail) {
		prefsEditor.putString(Config.KEY_EMAIL, userEmail).commit();
	}


	public String getCompanyCode() {
		return userSharedPrefs.getString(Config.KEY_COMPANY_CODE, "");
	}

	public void setUserCompanyCode(String userCompanyCode) {
		prefsEditor.putString(Config.KEY_COMPANY_CODE, userCompanyCode).commit();
	}


	public String getUserPassword() {
		return userSharedPrefs.getString(Config.KEY_PASSWORD, "");
	}

	public void setUserPassword(String userPassword) {
		prefsEditor.putString(Config.KEY_PASSWORD, userPassword).commit();
	}



	public String getUserDesignation() {
		return userSharedPrefs.getString(Config.KEY_DESIGNATION, "");
	}

	public void setUserDesignation(String userDesignation) {
		prefsEditor.putString(Config.KEY_DESIGNATION, userDesignation).commit();
	}

	public String getUserCompanyStart() {
		return userSharedPrefs.getString(Config.KEY_COMPANY_START, "");
	}

	public void setUserCompanyStart(String userCompanyStart) {
		prefsEditor.putString(Config.KEY_COMPANY_START, userCompanyStart).commit();
	}








	public String getUserImage() {
		return userSharedPrefs.getString(Config.KEY_IMAGE, "");
	}

	public void setUserImage(String userImage) {
		prefsEditor.putString(Config.KEY_IMAGE, userImage).commit();
	}





}
