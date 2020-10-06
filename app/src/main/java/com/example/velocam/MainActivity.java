package com.example.velocam;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.User;
import com.example.velocam.Model.UserList;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 6000;
    private static int LOGIN_TIME_OUT = 4000;
    private String forgot_id;
    SharedPreferences sp;


    private String id="";
    @Nullable
    String phone="";
    private List<User> userList;

    //Hooks
    View first, second,third,fourth,fifth,sixth;
    TextView slogan;
    ImageView img;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);
        img = findViewById(R.id.img);
        slogan = findViewById(R.id.tagLine);

        //Animation Calls
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        //-----------Setting Animations to the elements of Splash
       first.setAnimation(topAnimantion);
        second.setAnimation(topAnimantion);
        third.setAnimation(topAnimantion);
        fourth.setAnimation(topAnimantion);
        fifth.setAnimation(topAnimantion);
        sixth.setAnimation(topAnimantion);
        img.setAnimation(middleAnimation);
        slogan.setAnimation(bottomAnimation);







        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);


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

    private void autologin() {

        ApiInterface api = ApiClient.getApiService();
        Intent intent = getIntent();
        id = intent.getStringExtra(Config.TAG_PHONE);
        UserPrefs userPrefs = new UserPrefs(getApplicationContext());

        if (id == null || id.equals("")) {
            id = userPrefs.getUserPhone();
        }
        if (id == null || id.equals("")) {
            SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);

            id = result.getString("login_id","");

        }


        Call<CheckUserResponse> call = api.checkUser(id);
        call.enqueue(new Callback<CheckUserResponse>() {
            @Override
            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                CheckUserResponse userResponse=response.body();
                if (userResponse.isExists())
                {
                    final ProgressDialog dialog;

                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("Gathering User Information");
                    dialog.setMessage("Please Wait...");
                    //user already auth
                    dialog.show();

                    ApiInterface api = ApiClient.getApiService();

                    /**
                     * Calling JSON
                     */
                    Call<UserList> call_forgot = api.getAllUser();

                    call_forgot.enqueue(new Callback<UserList>() {
                        @Override
                        public void onResponse(Call<UserList> call, Response<UserList> response) {
                            //Dismiss Dialog

                            Intent intent = getIntent();

                            phone = intent.getStringExtra(Config.TAG_PHONE);
                            UserPrefs userPrefs = new UserPrefs(getApplicationContext());

                            if (phone == null || phone.equals("")) {
                                phone = userPrefs.getUserPhone();
                            }
                            if (phone == null || phone.equals("")) {
                                SharedPreferences result=getSharedPreferences("login_id",Context.MODE_PRIVATE);

                                phone = result.getString("login_id","");

                            }


                            if (response.isSuccessful()) {
                                /**
                                 * Got Successfully
                                 */
                                dialog.dismiss();
                                userList = response.body().getResult();

                                Boolean login = false;
                                for (int i = 0; i < userList.size(); i++) {
                                    assert phone != null;
                                    if (phone.equals(userList.get(i).getPhone())) {
                                        //here is the point
                                        forgot_id = userList.get(i).getPhone();


                                        login = true;

                                    }
                                }

                                if (login) {

                                    Intent forgot_intent = new Intent(MainActivity.this, DashboardActivity.class);
                                    intent.putExtra(Config.TAG_PHONE, forgot_id);
                                    startActivity(forgot_intent);
                                    finish();


                                    Toast.makeText(MainActivity.this,
                                            "Sucess.....", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                } else {

                                    Toast.makeText(MainActivity.this,
                                            "Profile does not exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            } else {

                                Toast.makeText(MainActivity.this,
                                        "Some Thing Wrong", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserList> call, Throwable t) {

                            Toast.makeText(MainActivity.this,
                                    ""+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });




                }

            }

            @Override
            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,""+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



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
}


