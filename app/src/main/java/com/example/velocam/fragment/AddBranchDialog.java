package com.example.velocam.fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.velocam.DetailsActivity;
import com.example.velocam.LoginActivity;
import com.example.velocam.R;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddBranchDialog extends Fragment {
private Double map_lat=0.0;
private Double map_long=0.0;
private String map_address="";
    AppCompatEditText bra_address;
private AppCompatEditText bra_lat;
private AppCompatEditText bra_long;
    public AddBranchDialog() {
        super();
    }

    public static AddBranchDialog newInstance(String param1, String param2) {
        AddBranchDialog fragment = new AddBranchDialog();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return inflater.inflate(R.layout.fragment_dialog_add_branch, container,
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

    @SuppressLint("SetTextI18n")
    private void bindView() {

        final AppCompatEditText bra_code=(AppCompatEditText) getActivity().findViewById(R.id.bra_code);
        final AppCompatEditText bra_name=(AppCompatEditText) getActivity().findViewById(R.id.bra_name);
        final AppCompatEditText bra_radius=(AppCompatEditText) getActivity().findViewById(R.id.bra_radius);
            bra_address=(AppCompatEditText) getActivity().findViewById(R.id.bra_address);
          bra_lat=(AppCompatEditText) getActivity().findViewById(R.id.bra_lat);
         bra_long=(AppCompatEditText) getActivity().findViewById(R.id.bra_long);
        final ImageButton save=(ImageButton) getActivity().findViewById(R.id.bt_save);
        final ImageButton close=(ImageButton) getActivity().findViewById(R.id.bt_close);
        final Button branch_map=(Button) getActivity().findViewById(R.id.branch_map);
        if(map_lat!=0.0 && map_long!=0.0)
        {
            bra_lat.setText(Double.toString(map_lat));
            bra_long.setText(Double.toString(map_long));
            bra_address.setText(map_address);
        }



        branch_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bra_map=new Intent(getActivity(), BranchMap.class);
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
        save.setOnClickListener(view -> {
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


                final String branch_code=bra_code.getText().toString().trim();
                final String branch_name=bra_name.getText().toString().trim();
                final String branch_radius=bra_radius.getText().toString().trim();
                UserPrefs userPrefs = new UserPrefs(getActivity());
                Intent intent = getActivity().getIntent();
                  String admin_phone=intent.getStringExtra(Config.TAG_PHONE);
                if (admin_phone == null || admin_phone.equals("")) {
                    admin_phone = userPrefs.getUserPhone();
                }
                insertBranch(branch_code,branch_name,branch_radius,branch_address,branch_latitude,branch_longitude,admin_phone);
            }

        });
        //dialog.show();
        //dialog.getWindow().setAttributes(lp);
    }

    private void insertBranch(String branch_code, String branch_name, String branch_radius, String branch_address, double
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

        Call<String> call = api.registerBranch(branch_code,branch_name,branch_radius,branch_address,
                branch_latitude,branch_longitude,admin_phone);
       call.enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {
               if (response.body()!=null)
               {
                   Toast.makeText(getActivity(), response.body(), Toast.LENGTH_LONG).show();
                   Intent i = new Intent(getActivity(),
                           AddCompany.class);
                   startActivity(i);

               }



           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
               Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_LONG).show();
           }
       });



    }



}
