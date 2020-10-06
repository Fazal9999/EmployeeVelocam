package com.example.velocam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.velocam.Common.Common;
import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.User;
import com.example.velocam.fragment.AddBranchDialog;
import com.example.velocam.fragment.Add_new_employee;
import com.example.velocam.fragment.ViewUserFragment;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private ImageView dash_image;
    private TextView dash_name;
    public static final int DIALOG_QUEST_CODE = 300;
    private FirebaseFirestore fStore;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;
CardView company;
LinearLayout linearLayout,employee,add_employee;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
  //      initToolbar();
        fAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
dash_image=(ImageView) findViewById(R.id.dash_image);
dash_name=(TextView)findViewById(R.id.dash_name);
        linearLayout=findViewById(R.id.company);
        employee=findViewById(R.id.add_employee);
        add_employee=findViewById(R.id.add_new_employee);

        add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Add_new_employee newFragment = new Add_new_employee();
                newFragment.setRequestCode(DIALOG_QUEST_CODE);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

            }
        });
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,AddEmployee.class);
                startActivity(intent);
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,AddCompany.class);
                startActivity(intent);
                finish();
            }
        });


getUser();
        forgotLogin();
        //sessionLogin();
    }

    @Override
    protected void onStart() {
        if(listener!=null)
            fAuth.addAuthStateListener(listener);
        super.onStart();
    }


    @Override
    protected void onStop() {
        if(listener!=null)
            fAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    private void getUser() {
        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait...");
        //user already auth
        dialog.show();
        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();

        /**
         * Calling JSON
         */
        Intent intent = getIntent();
        String id = intent.getStringExtra(Config.TAG_PHONE);
        UserPrefs userPrefs = new UserPrefs(getApplicationContext());

        if (id == null || id.equals("")) {
            id = userPrefs.getUserPhone();
        }

//        if (id == null || id.equals("")) {
//            SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);
//
//            id = result.getString("login_id","");
//
//        }

        Call<User> call = api.getUserById(id);

        String finalId = id;
        call.enqueue(new Callback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {


                    Config.currentUser=response.body();
                    dash_name.setText("Hi!"+Config.currentUser.getFirst_name());
                    Picasso.with(getApplicationContext())
                            .load(Config.URL_IMAGES + response.body().getImage())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).into(dash_image);

                    UserPrefs userPrefs = new UserPrefs(getApplicationContext());
                    if (finalId != null) {
                        userPrefs.setUserPhone(finalId);
                    }

                    userPrefs.setUserFirstName(response.body().getFirst_name());
                    userPrefs.setUserLastName(response.body().getLast_name());
                    userPrefs.setUserDesignation(response.body().getDesignation());
                    userPrefs.setUserEmail(response.body().getEmail());
                    userPrefs.setUserImage(response.body().getImage());
                    userPrefs.setUserPassword(response.body().getPassword());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this,""+t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
  /**/  private void sessionLogin() {
        {
            Intent intent = getIntent();
            String id = intent.getStringExtra(Config.TAG_PHONE);
            UserPrefs userPrefs = new UserPrefs(getApplicationContext());

            if (id == null || id.equals("")) {
                id = userPrefs.getUserPhone();
            }

            if (id == null || id.equals("")) {
                SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);

                id = result.getString("login_id","");

            }

            Toast.makeText(DashboardActivity.this,"Dash Login_id"+id,Toast.LENGTH_LONG).show();


            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(DashboardActivity.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please Wait...");
            //user already auth
            dialog.show();
            ApiInterface api = ApiClient.getApiService();
            Call<CheckUserResponse> call = api.checkUser(id);

            call.enqueue(new Callback<CheckUserResponse>() {
                @Override
                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                    CheckUserResponse userResponse=response.body();
                    assert userResponse != null;
                    if(userResponse.isExists())
                    {
                        Intent intent = getIntent();
                        String phone = intent.getStringExtra(Config.TAG_PHONE);
                        UserPrefs userPrefs = new UserPrefs(getApplicationContext());

                        if (phone == null || phone.equals("")) {
                            phone = userPrefs.getUserPhone();
                        }

                        if (phone == null || phone.equals("")) {
                            SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);

                            phone = result.getString("login_id","");

                        }




                        Call<User> user_call = api.getUserById(phone);

                        String finalPhone = phone;
                        user_call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                dialog.dismiss();
                                Config.currentUser=response.body();
                                dash_name.setText("Hi!"+Config.currentUser.getFirst_name());
                                Picasso.with(getApplicationContext())
                                        .load(Config.URL_IMAGES + response.body().getImage())
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE).into(dash_image);
                                UserPrefs userPrefs = new UserPrefs(getApplicationContext());
                                if (finalPhone != null) {
                                    userPrefs.setUserPhone(finalPhone);
                                }

                                userPrefs.setUserFirstName(response.body().getFirst_name());
                                userPrefs.setUserLastName(response.body().getLast_name());
                                userPrefs.setUserDesignation(response.body().getDesignation());
                                userPrefs.setUserEmail(response.body().getEmail());
                                userPrefs.setUserImage(response.body().getImage());
                                userPrefs.setUserPassword(response.body().getPassword());

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(DashboardActivity.this,""+t.getMessage().toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }

                @Override
                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                    Toast.makeText(DashboardActivity.this,""+t.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void forgotLogin() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            listener=firebaseAuth -> {
                //            Intent intent = getIntent();
//            String id = intent.getStringExtra(Config.TAG_PHONE);
//            UserPrefs userPrefs = new UserPrefs(getApplicationContext());
//
//            if (id == null || id.equals("")) {
//                id = userPrefs.getUserPhone();
//            }
//            if (id == null || id.equals(""))
//            {
//                id = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
//            }
//            if (id == null || id.equals("")) {
//                SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);
//
//                id = result.getString("login_id","");
//
//            }
//
//            Toast.makeText(DashboardActivity.this,"Dash Login_id"+id,Toast.LENGTH_LONG).show();


                final ProgressDialog dialog;
                /**
                 * Progress Dialog for User Interaction
                 */
                dialog = new ProgressDialog(DashboardActivity.this);
                dialog.setTitle("Loading");
                dialog.setMessage("Please Wait...");
                //user already auth
                dialog.show();
                ApiInterface api = ApiClient.getApiService();
                //            Call<CheckUserResponse> call = api.checkUser(id);
//
//            call.enqueue(new Callback<CheckUserResponse>() {
//                @Override
//                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
//                    CheckUserResponse userResponse=response.body();
//                    if(userResponse.isExists())
//                    {
//                        Intent intent = getIntent();
//                        String phone = intent.getStringExtra(Config.TAG_PHONE);
//                        UserPrefs userPrefs = new UserPrefs(getApplicationContext());
//
//                        if (phone == null || phone.equals("")) {
//                            phone = userPrefs.getUserPhone();
//                        }
//                        if (phone == null || phone.equals(""))
//                        {
                String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
//                        }
//                        if (phone == null || phone.equals("")) {
//                            SharedPreferences result=getSharedPreferences("login_id", Context.MODE_PRIVATE);
//
//                            phone = result.getString("login_id","");
//
//                        }
//
//                        Toast.makeText(DashboardActivity.this,"Dash Login_id"+phone,Toast.LENGTH_LONG).show();
//
                Call<User> user_call = api.getUserById(phone);

                String finalPhone = phone;
                user_call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        dialog.dismiss();
                        Config.currentUser=response.body();
                        dash_name.setText("Hi!"+Config.currentUser.getFirst_name());
                        Picasso.with(getApplicationContext())
                                .load(Config.URL_IMAGES + response.body().getImage())
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(dash_image);
                        UserPrefs userPrefs = new UserPrefs(getApplicationContext());
                        if (finalPhone != null) {
                            userPrefs.setUserPhone(finalPhone);
                        }

                        userPrefs.setUserFirstName(response.body().getFirst_name());
                        userPrefs.setUserLastName(response.body().getLast_name());
                        userPrefs.setUserDesignation(response.body().getDesignation());
                        userPrefs.setUserEmail(response.body().getEmail());
                        userPrefs.setUserImage(response.body().getImage());
                        userPrefs.setUserPassword(response.body().getPassword());

                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(DashboardActivity.this,""+t.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
                // }
//                    else {
//                        FirebaseAuth.getInstance().signOut();
//
//                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
//                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Config.currentUser=null;
//                        startActivity(intent);
//                        finish();
//                    }
                //}

//                @Override
//                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
//
//                }
//            });
            };

            };





    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_person_24);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.lime_600);
    }




}