package com.example.velocam;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.UserList;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verify extends AppCompatActivity {
    FirebaseAuth fAuth;
    String phoneNumber = "+923061625562";
    String otpCode = "123456";
    String verificationId;
    EditText phone,optEnter;
    Button next;
    CountryCodePicker countryCodePicker;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false;
    ProgressBar progressBar;
    TextView state,resend;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        phone = findViewById(R.id.phone);
        optEnter = findViewById(R.id.codeEnter);
        countryCodePicker = findViewById(R.id.ccp);
        next = findViewById(R.id.nextBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        resend = findViewById(R.id.resendOtpBtn);


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {
                    if(!verificationOnProgress){
                        next.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setVisibility(View.VISIBLE);
                        String phoneNum = "+"+countryCodePicker.getSelectedCountryCode()+phone.getText().toString();
                        Log.d("phone", "Phone No.: " + phoneNum);
                        requestPhoneAuth(phoneNum);
                    }else {
                        next.setEnabled(false);
                        optEnter.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Wait Please...");
                        state.setVisibility(View.VISIBLE);
                        otpCode = optEnter.getText().toString();
                        if(otpCode.isEmpty()){
                            optEnter.setError("Required");
                            return;
                        }

                        credential = PhoneAuthProvider.getCredential(verificationId,otpCode);
                        verifyAuth(credential);
                    }

                }else {
                    phone.setError("Valid Phone Required");
                }
            }
        });
    }

    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(Verify.this, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(Verify.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }




    private void verifyAuth(com.google.firebase.auth.PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Verify.this, "Phone Verified."+fAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                    checkUserProfile();
                }else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(Verify.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            state.setText("Checking..");
            state.setVisibility(View.VISIBLE);
            checkUserProfile();
        }
    }

    private void checkUserProfile() {

        ApiInterface api = ApiClient.getApiService();
//        Intent intent = getIntent();
//        String id = intent.getStringExtra(Config.TAG_PHONE);
//        UserPrefs userPrefs = new UserPrefs(getApplicationContext());
//        if (id == null || id.equals("")) {
//            id = userPrefs.getUserPhone();
//        }
//        if (id == null || id.equals(""))
//        {
        String  id =  Objects.requireNonNull(fAuth.getCurrentUser()).getPhoneNumber();
       // }


        Call<CheckUserResponse> call = api.checkUser(id);
      call.enqueue(new Callback<CheckUserResponse>() {
          @Override
          public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
              CheckUserResponse userResponse=response.body();
              if (userResponse.isExists())
              {
                  final Dialog dialog = new Dialog(Verify.this);
                  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                  dialog.setContentView(R.layout.warning);
                  dialog.setCancelable(true);

                  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                  lp.copyFrom(dialog.getWindow().getAttributes());
                  lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                  ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                          dialog.dismiss();
                          Intent intent1=new Intent(Verify.this,LoginActivity.class);
                          startActivity(intent1);
                          finish();
                      }
                  });

                  dialog.show();
                  dialog.getWindow().setAttributes(lp);


              }
              else
              {
                  Toast.makeText(Verify.this,"Profile do not exists!",Toast.LENGTH_LONG).show();
                  Intent intent1=new Intent(Verify.this,DetailsActivity.class);
                  startActivity(intent1);
                  finish();
              }
          }

          @Override
          public void onFailure(Call<CheckUserResponse> call, Throwable t) {
              Toast.makeText(Verify.this,""+t.getMessage().toString(),Toast.LENGTH_LONG).show();
          }
      });



    }
}