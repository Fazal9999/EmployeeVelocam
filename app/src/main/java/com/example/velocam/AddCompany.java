package com.example.velocam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.velocam.Model.Branch;
import com.example.velocam.Model.BranchList;
import com.example.velocam.Model.Result;
import com.example.velocam.Model.User;
import com.example.velocam.Model.UserList;
import com.example.velocam.adapter.BranchAdapter;
import com.example.velocam.fragment.AddBranchDialog;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompany extends AppCompatActivity {
RecyclerView recycler_branch;
CompositeDisposable compositeDisposable=new CompositeDisposable();

    FloatingActionButton add;
    Toolbar mToolbar;
    List<Branch> dataList;




    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       toolbar();
        setContentView(R.layout.activity_add_company);

        add=(FloatingActionButton) findViewById(R.id.addBra);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        AddCompany.this.setTitle("Add a Branch");
         dataList = new ArrayList<>();
        add.setVisibility(View.VISIBLE);
         // adapter = new BranchAdapter(this, dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_branch=findViewById(R.id.recycler_branch);
        recycler_branch.setLayoutManager(layoutManager);
        recycler_branch.setHasFixedSize(true);
        //recycler_branch.setAdapter(adapter);
        add.setOnClickListener(view -> {
//                Intent intent = new Intent(AddCompany.this, AddBranchDialog.class);
//                startActivity(intent);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            AddBranchDialog dia = new AddBranchDialog();
            fragmentTransaction.replace(R.id.main_content, dia);
            fragmentTransaction.commit();
            add.setVisibility(View.INVISIBLE);
        });


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Add Branch");

        getBranch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBranch();
    }

    private void getBranch() {
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
        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(AddCompany.this);
        dialog.setMessage("Please Wait...");
        dialog.show();
        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();

        /**
         * Calling JSON
         */


       compositeDisposable.add(api.getBranch(id).
               observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribe(new Consumer<List<Branch>>() {
                   @Override
                   public void accept(List<Branch> branches) throws Exception {
                   displayBranch(branches);
                   dialog.dismiss();
                   }
               },Throwable->{
                   Toast.makeText(AddCompany.this,""+Throwable.getMessage(),Toast.LENGTH_LONG).show();
               }));}



    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void insertBranch(String branch_code, String branch_name, String branch_radius, String branch_address, double branch_latitude, double branch_longitude, String admin_phone) {
        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(AddCompany.this);
        dialog.setTitle("Loding");
        dialog.setMessage("Please Wait...");
        dialog.show();

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();

        Call<String> call = api.registerBranch(branch_code, branch_name, branch_radius, branch_address, branch_latitude, branch_longitude, admin_phone);

call.enqueue(new Callback<String>() {
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            //Dismiss Dialog
            dialog.dismiss();


            assert response.body() != null;

                    Toast.makeText(AddCompany.this, response.body(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddCompany.this,
                            LoginActivity.class);
                    startActivity(i);

                    finish();




        }}

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }
});
    }

              private void toolbar() {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Custom");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Tools.setSystemBarColor(this);

    }
    private void displayBranch(List<Branch> branches) {
        BranchAdapter adapter=new BranchAdapter(this,branches);
        recycler_branch.setAdapter(adapter);
    }

}

