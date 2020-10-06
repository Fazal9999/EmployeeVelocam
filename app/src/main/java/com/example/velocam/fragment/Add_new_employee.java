package com.example.velocam.fragment;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.velocam.AddEmployee;
import com.example.velocam.EmployeeList;
import com.example.velocam.Model.Branch;
import com.example.velocam.Model.BranchList;
import com.example.velocam.Model.CheckUserResponse;
import com.example.velocam.Model.User;
import com.example.velocam.Model.UserList;
import com.example.velocam.R;
import com.example.velocam.Tools;
import com.example.velocam.adapter.BranchCheckBoxAdapter;
import com.example.velocam.adapter.BranchListAdapter;
import com.example.velocam.adapter.SpinnerAdapter;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instabug.library.Instabug.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_new_employee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_new_employee<CallbackResult> extends DialogFragment implements AdapterView.OnItemSelectedListener {
    AppCompatEditText join_date, dob;
    // List<String> branchList;
    ArrayList<Branch> dataModels;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<Branch> branchList;
    private RecyclerView branh_recycler;
    Spinner gender, status;
    String selectedGender, selectedStatus;
    private ArrayList<Branch> branchModelArrayList;
    private ArrayList<String> branch_name = new ArrayList<String>();

    private Spinner spinner;
    private int request_code = 0;
    public CallbackResult callbackResult;

    public Add_new_employee() {
        super();
        // Required empty public constructor
    }

    public static Add_new_employee newInstance(String param1, String param2) {
        Add_new_employee fragment = new Add_new_employee();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // getActivity().setTitle("Edit Profile");
        spinner = getActivity().findViewById(R.id.spBranch);
        gender = getActivity().findViewById(R.id.gender);


        dataModels = new ArrayList<>();
        status = getActivity().findViewById(R.id.status);
        ArrayAdapter<String> gen = new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.gender)
        );
        ArrayAdapter<String> stat = new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.status)
        );

        gen.setDropDownViewResource(R.layout.custom_spinner);
        gender.setAdapter(gen);

        stat.setDropDownViewResource(R.layout.custom_spinner);
        status.setAdapter(stat);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedGender = gender.getItemAtPosition(position).toString();
                Toast.makeText(getActivity().getApplicationContext(), selectedGender, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedStatus = status.getItemAtPosition(position).toString();
                Toast.makeText(getActivity().getApplicationContext(), selectedStatus, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        bindView();
        branchList = new ArrayList<>();
        inserUser();
        getBranchList();
        spinner.setOnItemSelectedListener(this);


    }

    private void getBranchList() {
        Intent intent = requireActivity().getIntent();
        String id = intent.getStringExtra(Config.TAG_PHONE);
        UserPrefs userPrefs = new UserPrefs(getActivity().getApplicationContext());

        if (id == null || id.equals("")) {
            id = userPrefs.getUserPhone();
        }

        if (id == null || id.equals("")) {
            SharedPreferences result = requireActivity().getSharedPreferences("login_id", Context.MODE_PRIVATE);

            id = result.getString("login_id", "");

        }
        ApiInterface api = ApiClient.getApiService();


        compositeDisposable.add(api.getBranches(id).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ArrayList<Branch>>() {
                    @Override
                    public void accept(ArrayList<Branch> branches) throws Exception {
                        //Config.branchList=branches;
                        Toast.makeText(getActivity(), "adapter" + branches, Toast.LENGTH_LONG).show();
                        branh_recycler = getActivity().findViewById(R.id.recycler);
                        branh_recycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        branh_recycler.setHasFixedSize(true);
                        BranchCheckBoxAdapter adapter = new BranchCheckBoxAdapter(getActivity().getApplicationContext(), branches);
                        ((BranchCheckBoxAdapter)adapter).notifyDataSetChanged();
                        branh_recycler.setAdapter(adapter);
                    }
                }, Throwable -> {
                    Toast.makeText(getActivity(), "" + Throwable.getMessage(), Toast.LENGTH_LONG).show();
                }));

    }

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private void inserUser() {
        Intent intent = requireActivity().getIntent();
        String id = intent.getStringExtra(Config.TAG_PHONE);
        UserPrefs userPrefs = new UserPrefs(getActivity().getApplicationContext());

        if (id == null || id.equals("")) {
            id = userPrefs.getUserPhone();
        }

        if (id == null || id.equals("")) {
            SharedPreferences result = requireActivity().getSharedPreferences("login_id", Context.MODE_PRIVATE);

            id = result.getString("login_id", "");

        }
        //  ApiInterface api = ApiClient.getApiService();

        // ArrayList<Branch>branch=new ArrayList<>();
        //  ArrayAdapter<Branch> adapter=new ArrayAdapter<Branch>(getActivity(),R.layout.custom_spinner);
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

                        SpinnerAdapter adapter = new SpinnerAdapter(branches, getActivity());
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                String nm = spinner.getItemAtPosition(position).toString();
                                Toast.makeText(getActivity().getApplicationContext(), nm, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

//                        SpinnerAdapter adapter = new SpinnerAdapter(branches, getActivity());
//                        spinner.setAdapter(adapter);

//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                Config.currentBranch= branches.get(i);
//                                Intent intent=new Intent(getApplicationContext(),EmployeeList.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                dialog.dismiss();
////getApplicationContext().startActivity(new Intent());
//                                Snackbar.make(view, Config.currentBranch.getBranch_name()+"\n"+Config.currentBranch.getBranch_code(), Snackbar.LENGTH_LONG)
//                                        .setAction("No action", null).show();
//                            }
//                        });
                    }
                }));
    }


    @Override
    public void onStop() {
      compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void bindView() {
        join_date=(AppCompatEditText) getActivity().findViewById(R.id.emp_join_date);
        dob=(AppCompatEditText) getActivity().findViewById(R.id.emp_dob);

        join_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerDark((AppCompatEditText) v);
            }

        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDobPickerDark((AppCompatEditText) v);
            }

        });


    }

    private void dialogDobPickerDark(AppCompatEditText v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year,





                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        ((AppCompatEditText) getActivity().findViewById(R.id.emp_dob)).setText(Tools.getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getActivity().getFragmentManager(), "Join Date");

    }

    private void dialogDatePickerDark(AppCompatEditText v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        ((AppCompatEditText) getActivity().findViewById(R.id.emp_join_date)).setText(Tools.getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getActivity().getFragmentManager(), "Join Date");


    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_employye, container, false);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

}