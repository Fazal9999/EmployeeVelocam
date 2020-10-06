package com.example.velocam.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.velocam.AddCompany;
import com.example.velocam.BranchMap;
import com.example.velocam.LoginActivity;
import com.example.velocam.R;
import com.example.velocam.UpdateBranchMap;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateBranch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateBranch extends Fragment {
    private Double map_lat=0.0;
 private  AppCompatEditText bra_name;
    private Double map_long=0.0;
    private String map_address="";
    private ImageButton close;
    AppCompatEditText bra_code;
    private  AppCompatEditText bra_radius;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
   private AppCompatEditText bra_address;
    private AppCompatEditText bra_lat;
    private ImageButton save;
    private Button delete;
    private AppCompatEditText bra_long;
    Uri selectedUri=null;
    public UpdateBranch() {
        super();
    }

    public static UpdateBranch newInstance(String param1, String param2) {
        UpdateBranch fragment = new UpdateBranch();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_update_branch, container,
                false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // getActivity().setTitle("Edit Profile");

        if (getArguments() != null) {
            map_lat = getArguments().getDouble("latitude");
            map_long = getArguments().getDouble("longitude");
            map_address=getArguments().getString("address");
        }






        bindView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @SuppressLint("SetTextI18n")
    private void bindView() {


           bra_code=(AppCompatEditText) getActivity().findViewById(R.id.bra_code);
           bra_code.setEnabled(false);
           bra_name=(AppCompatEditText) getActivity().findViewById(R.id.bra_name);
           bra_radius=(AppCompatEditText) getActivity().findViewById(R.id.bra_radius);
        bra_address=(AppCompatEditText) getActivity().findViewById(R.id.bra_address);
        bra_lat=(AppCompatEditText) getActivity().findViewById(R.id.bra_lat);
        bra_long=(AppCompatEditText) getActivity().findViewById(R.id.bra_long);
           save=(ImageButton) getActivity().findViewById(R.id.bt_save);
           close=(ImageButton) getActivity().findViewById(R.id.bt_close);
           delete=(Button) getActivity().findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog;
                /**
                 * Progress Dialog for User Interaction
                 */
                dialog = new ProgressDialog(getActivity());
                dialog.setTitle("Loding");
                dialog.setMessage("Please Wait...");
                dialog.show();

                //Creating an object of our api interface
                ApiInterface api = ApiClient.getApiService();

                UserPrefs userPrefs = new UserPrefs(getActivity());
                Intent intent = getActivity().getIntent();
                String admin_phone=intent.getStringExtra(Config.TAG_PHONE);
                if (admin_phone == null || admin_phone.equals("")) {
                    admin_phone = userPrefs.getUserPhone();
                }
                if (admin_phone == null || admin_phone.equals("")) {
                    admin_phone = userPrefs.getUserPhone();
                }
                if (admin_phone == null || admin_phone.equals("")) {
                    SharedPreferences result=getActivity().getSharedPreferences("login_id", Context.MODE_PRIVATE);

                    admin_phone = result.getString("login_id","");
                }

                compositeDisposable.add(api.deleteBranch(Config.currentBranch.getBranch_code(),admin_phone).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                                Config.currentBranch=null;
                            }
                        },Throwable->{
                            Toast.makeText(getActivity(),""+Throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }));
            }
        });
        final Button branch_map=(Button) getActivity().findViewById(R.id.branch_map);
        if(map_lat!=0.0 && map_long!=0.0)
        {
            bra_lat.setText("");
            bra_long.setText("");
            bra_address.setText("");
            bra_lat.setText(Double.toString(map_lat));
            bra_long.setText(Double.toString(map_long));
            bra_address.setText(map_address);
        }

  if (Config.currentBranch!=null)
        {
            bra_code.setText(Config.currentBranch.getBranch_code());
            bra_name.setText(Config.currentBranch.getBranch_name());
            bra_radius.setText(Config.currentBranch.getBranch_radius());
            bra_address.setText(Config.currentBranch.getBranch_address());
            bra_lat.setText(Config.currentBranch.getBranch_latitude());
            bra_long.setText(Config.currentBranch.getBranch_longitude());


        }



        branch_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bra_map=new Intent(getActivity(), UpdateBranchMap.class);
                startActivity(bra_map);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCompany.class);
                startActivity(intent);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(bra_code.getText()).toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Code cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_code.setError("Branch Code cannot be Blank");

                    return;

                }
                else  if (bra_name.getText().toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Name cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_name.setError("Branch Name cannot be Blank");

                    return;

                }
                else  if (bra_radius.getText().toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Radius cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_radius.setError("Branch Radius cannot be Blank");

                    return;

                }
                else if (Integer.valueOf(bra_radius.getText().toString())<150 && Integer.valueOf(bra_radius.getText().toString())>1000 )
                {
                    Toast.makeText(getActivity(), "Branch Radius cannot be less than 150 or more than 1000",
                            Toast.LENGTH_LONG).show();
                    bra_radius.setError("Branch Radius cannot be less than 150 or more than 1000");

                    return;
                }

                else if (bra_address.getText().toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Address cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_address.setError("Branch Address cannot be Blank");

                    return;

                }
                else if (bra_lat.getText().toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Latitude cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_lat.setError("Branch Latitude cannot be Blank");

                    return;

                }
                else if (bra_long.getText().toString().length() == 0) {

                    Toast.makeText(getActivity(), "Branch Longitude cannot be Blank",
                            Toast.LENGTH_LONG).show();
                    bra_long.setError("Branch Longitude  cannot be Blank");

                    return;

                }
                else
                {
                    final double branch_latitude;
                    final double branch_longitude;
                    final String branch_address;


                        branch_latitude= Double.parseDouble(bra_lat.getText().toString());



                        branch_longitude=Double.parseDouble(bra_long.getText().toString());



                        branch_address=bra_address.getText().toString();


                   // final String branch_code=bra_code.getText().toString().trim();
                    final String branch_name=bra_name.getText().toString().trim();
                    final String branch_radius=bra_radius.getText().toString().trim();
                    UserPrefs userPrefs = new UserPrefs(Objects.requireNonNull(getActivity()));
                    Intent intent = getActivity().getIntent();
                    String admin_phone=intent.getStringExtra(Config.TAG_PHONE);
                    if (admin_phone == null || admin_phone.equals("")) {
                        admin_phone = userPrefs.getUserPhone();
                    }
                    if (admin_phone == null || admin_phone.equals("")) {
                        admin_phone = userPrefs.getUserPhone();
                    }
                    if (admin_phone == null || admin_phone.equals("")) {
                        SharedPreferences result=getActivity().getSharedPreferences("login_id", Context.MODE_PRIVATE);

                        admin_phone = result.getString("login_id","");
                    }
                    updateBranch(Config.currentBranch.getBranch_code(),branch_name,branch_radius,branch_address,branch_latitude,branch_longitude,admin_phone);
                }

            }
        });
        //dialog.show();
        //dialog.getWindow().setAttributes(lp);
    }

    private void updateBranch(String branch_code, String branch_name, String branch_radius, String branch_address, double
            branch_latitude, double branch_longitude, String admin_phone) {
        final ProgressDialog dialog;
        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loding");
        dialog.setMessage("Please Wait...");
        dialog.show();

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();



        compositeDisposable.add(api.updateBranch(branch_code,branch_name,branch_radius,branch_address,
                branch_latitude,branch_longitude,admin_phone).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                        Config.currentBranch=null;
                    }
                },Throwable->{
                    dialog.dismiss();
                    Toast.makeText(getActivity(),""+Throwable.getMessage(),Toast.LENGTH_LONG).show();
                }));



    }

}
