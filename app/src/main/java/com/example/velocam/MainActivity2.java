package com.example.velocam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity2  extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }
        Button signInButton = (Button)findViewById(R.id.sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity2.this, Login_Activity.class);
                startActivity(loginIntent);
            }
        });
        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(MainActivity2.this, SignUpActivity.class);
                startActivity(signInIntent);
            }
        });
    }
}