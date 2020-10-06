package com.example.velocam;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.velocam.Model.UserObject;

import static com.example.velocam.Login_Activity.mGson;
import static com.example.velocam.Login_Activity.mPref;


@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
    private static final String TAG = FingerprintHandler.class.getSimpleName();
    private static UserObject mUser;
    private static String userString;
    private Context context;
    public FingerprintHandler(Context context){
        this.context = context;
    }
    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Log.d(TAG, "Error message " + errorCode + ": " + errString);
        Toast.makeText(context, context.getString(R.string.authenticate_fingerprint), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        Toast.makeText(context, R.string.auth_successful, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        userString = mPref.getUserData();
        mUser = mGson.fromJson(userString, UserObject.class);
        if(mUser != null){
            Toast.makeText(context, context.getString(R.string.auth_successful), Toast.LENGTH_LONG).show();
            if(mUser.isLoginOption()){
                // login with fingerprint and password
                showPasswordAuthentication(context);
            }
            else{
                // login with only fingerprint
                Intent userIntent = new Intent(context, UserProfileActivity.class);
                userIntent.putExtra("USER_BIO", userString);
                context.startActivity(userIntent);
            }
        }else{
            Toast.makeText(context, "You must register before login with fingerprint", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }
    public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            }
        }catch (SecurityException ex) {
            Log.d(TAG, "An error occurred:\n" + ex.getMessage());
        } catch (Exception ex) {
            Log.d(TAG, "An error occurred\n" + ex.getMessage());
        }
    }

    private static void showPasswordAuthentication(Context context){
        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.password_layout);
        openDialog.setTitle("Enter Password");
        final EditText passwordDialog = (EditText)openDialog.findViewById(R.id.password);
        Button loginWithPasswordButton = (Button)openDialog.findViewById(R.id.login_button);
        loginWithPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authPassword = passwordDialog.getText().toString();
                if(TextUtils.isEmpty(authPassword)){
                    Toast.makeText(view.getContext(), "Password field must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(mUser.getPassword().equals(authPassword)){
                    Intent userIntent = new Intent(view.getContext(), UserProfileActivity.class);
                    userIntent.putExtra("USER_BIO", userString);
                    view.getContext().startActivity(userIntent);
                }else{
                    Toast.makeText(view.getContext(), "Incorrect password! Try again", Toast.LENGTH_LONG).show();
                    return;
                }
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }}