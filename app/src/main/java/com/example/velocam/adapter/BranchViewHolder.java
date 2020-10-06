package com.example.velocam.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.Interface.ItemClickListener;
import com.example.velocam.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Map;
import java.util.Objects;

import static com.instabug.library.Instabug.getApplicationContext;

public class BranchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener    {
    public GoogleMap googleMap;
    MapView mapView;
    ItemClickListener itemClickListener;
    TextView branch_name;
    TextView branch_code;

Context context;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private FragmentManager fragmentmanager;
    public BranchViewHolder(@NonNull View itemView) {
        super(itemView);

        mapView = itemView.findViewById(R.id.lite_listrow_map);
        branch_name=(TextView) itemView.findViewById(R.id.branch_name);
        branch_code=(TextView) itemView.findViewById(R.id.branch_code);
        itemView.setOnClickListener(this);
    }


    public void initializeMapView() {
        if (mapView != null) {
            // Initialise the MapView
            mapView.onCreate(null);


        }
    }


    @Override
    public void onClick(View view) {
itemClickListener.onClick(view);
    }
}
