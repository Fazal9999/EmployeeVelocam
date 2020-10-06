package com.example.velocam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.velocam.Model.Branch;
import com.example.velocam.adapter.BranchListAdapter;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddEmployee extends AppCompatActivity {
    ArrayList<Branch> dataModels;
    ListView listView;
    private static BranchListAdapter adapter;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        listView = (ListView) findViewById(R.id.branch_list);
        dataModels= new ArrayList<>();
getbrachList();
    }

    private void getbrachList() {

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
            dialog = new ProgressDialog(AddEmployee.this);
            dialog.setMessage("Please Wait...");
            dialog.show();
            //Creating an object of our api interface
            ApiInterface api = ApiClient.getApiService();

            /**
             * Calling JSON
             */


            compositeDisposable.add(api.getBranches(id).
                    observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<ArrayList<Branch>>() {
                        @Override
                        public void accept(ArrayList<Branch> branches) throws Exception {
                            BranchListAdapter adapter=new BranchListAdapter(branches, AddEmployee.this);
                            listView .setAdapter(adapter);
                            dialog.dismiss();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Config.currentBranch= branches.get(i);
                                    Intent intent=new Intent(getApplicationContext(),EmployeeList.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    dialog.dismiss();
//getApplicationContext().startActivity(new Intent());
                                    Snackbar.make(view, Config.currentBranch.getBranch_name()+"\n"+Config.currentBranch.getBranch_code(), Snackbar.LENGTH_LONG)
                                            .setAction("No action", null).show();
                                }
                            });
                        }
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

    @Override
    protected void onResume() {
        getbrachList();
        super.onResume();
    }
}