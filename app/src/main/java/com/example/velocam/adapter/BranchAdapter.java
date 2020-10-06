package com.example.velocam.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.AddCompany;
import com.example.velocam.BranchMap;
import com.example.velocam.Interface.ItemClickListener;
import com.example.velocam.Model.Branch;
import com.example.velocam.Model.BranchList;
import com.example.velocam.Model.Result;
import com.example.velocam.R;
import com.example.velocam.fragment.UpdateBranch;
import com.example.velocam.utils.Config;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.instabug.library.Instabug.getApplicationContext;

public class BranchAdapter extends RecyclerView.Adapter<BranchViewHolder> {
    Context context;

    //List<BranchList> branchListList;
    List<Branch> list;
    public BranchAdapter(Context context, List<Branch> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_branch_item, parent, false);
        return new BranchViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {


        holder.branch_name.setText("Branch Name:"+""+list.get(position).getBranch_name());
        holder.branch_code.setText("Branch Code:"+""+list.get(position).getBranch_code());
        holder.mapView.setVisibility(View.VISIBLE);
        holder.initializeMapView();
     /*   holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {


               if (mMap != null) {
                   mMap = googleMap;
                   //   LatLngBounds.Builder builder = new LatLngBounds.Builder();


                   LatLng Branch_location = new LatLng(Double.parseDouble(list.get(position)
                           .getBranch_latitude()), Double.parseDouble(list.get(position).getBranch_longitude()));

                   Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                   List<Address> addresses = null;
                   try {
                       addresses = geocoder.getFromLocation(Double.parseDouble(list.get(position)
                               .getBranch_latitude()), Double.parseDouble(list.get(position).getBranch_longitude()), 1);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   assert addresses != null;
                   String addr = addresses.get(0).getAddressLine(0);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(Branch_location);
                     markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                   mMap.addMarker(new MarkerOptions().position(Branch_location).title(addr));
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(Branch_location));
                   mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
               }
            }
        //}
        });*/


        //if (holder.googleMap != null) {coordinatescoordinates
            holder.mapView.onCreate(null);
            holder.mapView.onResume();
            holder.mapView.getMapAsync(googleMap -> {
                MapsInitializer.initialize(context.getApplicationContext());
                holder.googleMap = googleMap;


                    LatLng coordinates = new LatLng(Double.parseDouble(list.get(position).getBranch_longitude()), Double.parseDouble(list.get(position).getBranch_longitude()));
                    googleMap.addMarker(new MarkerOptions().position(coordinates)
                            .title(list.get(position).getBranch_name()));
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                List<Address> addresses = null;
//                try {
//                    addresses = geocoder.getFromLocation(Double.parseDouble(list.get(position)
//                            .getBranch_latitude()), Double.parseDouble(list.get(position).getBranch_longitude()), 1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                assert addresses != null;
//                String addr = addresses.get(0).getAddressLine(0);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(coordinates);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("Branch Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 19));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(coordinates).zoom(19f).tilt(0).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


            });
       // }

        holder.setItemClickListener(view -> {
            Config.currentBranch=list.get(position);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            UpdateBranch myFragment = new UpdateBranch();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, myFragment).addToBackStack(null).commit();
        });

 }


    public void onViewRecycled(BranchViewHolder holder) {
        if (holder.googleMap != null) {
            holder.googleMap.clear();
            holder.googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }
    public void startRotationAnimation(ImageView imageView) {
        if (imageView != null) {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation);
            anim.setRepeatCount(Animation.INFINITE);
            imageView.startAnimation(anim);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
