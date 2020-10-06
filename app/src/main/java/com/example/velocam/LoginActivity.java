package com.example.velocam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.User;
import com.example.velocam.Model.UserList;
import com.example.velocam.fragment.Dashboard;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.example.velocam.utils.InternetConnection;
import com.example.velocam.utils.SharedPrefManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION = 22222;
    private static final int REQUEST_CODE = 1111;
    private static int SPLASH_TIME_OUT = 5000;
    private String forgot_id;
    SharedPreferences sp;
    SharedPreferences pref;
    Intent intent;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;

     TextView sign_up,sign_in,forgot;
    Button btnLogin;
    EditText edtEmail, edtPassword;
    private String JSON_STRING;
    private String id="";
    @Nullable
    String phone="";
    private List<User> userList;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        // Set the Toolbar as the activity's app bar (instead of the default ActionBar)
        isStoragePermissionGranted();
        init();
        bindView();
        addListener();
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(LoginActivity.this, DashboardActivity.class);
        if(pref.contains("username") && pref.contains("password")){
            startActivity(intent);
        }
//        if (!new PrefManager(this).isUserLogedOut()) {
//            //user's email and password both are saved in preferences
//            startHomeActivity();
//        }



    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE)
        {
            IdpResponse idpResponse=IdpResponse.fromResultIntent(data);

        }
        else
        {
            Toast.makeText(LoginActivity.this,"Failed To sign In",Toast.LENGTH_LONG).show();
        }
    }



    @SuppressLint("NewApi")
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat
                        .requestPermissions(
                                this,
                                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                1);
                return false;
            }
        } else { // permission is automatically granted on sdk<23 upon
            // installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    /* PagerAdapter for supplying the ViewPager with the pages (fragments) to display. */
    private void init() {
        userList  = new ArrayList<>();

    }

    private void bindView() {
       // btnLinkToRegisterScreen = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        sign_up=(TextView) findViewById(R.id.sign_up);
        sign_in=(TextView) findViewById(R.id.sign_in);
        forgot=(TextView) findViewById(R.id.forgot_password);
    }

    private void addListener() {
        btnLogin.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        forgot.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                if (edtEmail.getText().toString().length() == 0) {

                    Toast.makeText(getApplicationContext(),
                            "Email id cannot be Blank", Toast.LENGTH_LONG).show();
                    edtEmail.setError("Email cannot be Blank");

                    return;

                }
//                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
//                        edtEmail.getText().toString()).matches()) {
//
//                    Toast.makeText(getApplicationContext(), "Invalid Email",
//                            Toast.LENGTH_LONG).show();
//                    edtEmail.setError("Invalid Email");
//
//                }
//
                else if (edtPassword.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Password cannot be Blank", Toast.LENGTH_LONG).show();
                    edtPassword.setError("Password cannot be Blank");

                    return;
                } else {
                    showUsers();
                }

                break;

           case R.id.sign_up:

                Intent i = new Intent(LoginActivity.this, Verify.class);
                startActivity(i);

                break;

            case R.id.sign_in:

                Intent in = new Intent(LoginActivity.this, EmployeeLogin.class);
                startActivity(in);

                break;
            case R.id.forgot_password:

                Intent forgot = new Intent(LoginActivity.this, Forgot.class);
                startActivity(forgot);

                break;

            default:
                break;
        }
    }

    private void showUsers() {


        if (InternetConnection.checkConnection(LoginActivity.this)) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Login");
            dialog.setMessage("Please Wait...");
            dialog.show();

            //Creating an object of our api interface
            ApiInterface api = ApiClient.getApiService();

            /**
             * Calling JSON
             */
            Call<UserList> call = api.getAllUser();

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(Call<UserList> call, Response<UserList> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        userList = response.body().getResult();

                        Boolean login = false;
                        for (int i = 0; i < userList.size(); i++) {
                            if (edtEmail.getText().toString()
                                    .equals(userList.get(i).getEmail())
                                    ||edtEmail.getText().toString().equals(userList.get(i).getPhone())
                                    && edtPassword.getText().toString()
                                    .equals(userList.get(i).getPassword())) {
                                //here is the point
                                id = userList.get(i).getPhone();


                                login = true;

                            }
                        }

                        if (login) {
                            SharedPreferences.Editor editor1 = pref.edit();
                            editor1.putString("username",edtEmail.getText().toString());
                            editor1.putString("password",edtPassword.getText().toString());
                            editor1.commit();
                            sp=getSharedPreferences("login_id", Context.MODE_PRIVATE);
                            @SuppressLint("CommitPrefEdits")
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("login_id",id);
                            editor.apply();
                            //saveLoginDetails(edtEmail.getText().toString(), edtPassword.getText().toString());
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.putExtra(Config.TAG_PHONE, id);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this,
                                    "Sucess.....", Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this,
                                    ""+id, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Invalid Email Id of Password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Some Thing Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserList> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Toast.makeText(LoginActivity.this,
                    "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginDetails(String toString, String toString1) {
        new PrefManager(this).saveLoginDetails(toString, toString1);
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission: " + permissions[0] + "was "
                    + grantResults[0]);
            // resume tasks needing this permission
        } else {
            ActivityCompat
                    .requestPermissions(
                            this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            1);
        }
    }

    //usefulcode
//    prf = getSharedPreferences("user_details",MODE_PRIVATE);
    //SharedPreferences prf;
    //Intent intent;
//    intent = new Intent(DetailsActivity.this,MainActivity.class);
//        result.setText("Hello, "+prf.getString("username",null));
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            SharedPreferences.Editor editor = prf.edit();
//            editor.clear();
//            editor.commit();
//            startActivity(intent);
//        }
//    });

}