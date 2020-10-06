package com.example.velocam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.Interface.ItemClickListener;
import com.example.velocam.Model.Branch;
import com.example.velocam.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BranchListAdapter  extends ArrayAdapter<Branch>  {
    private ArrayList<Branch> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtCode;

    }
    public BranchListAdapter(ArrayList<Branch> data, Context context)
    {
        super(context,R.layout.branch_list_item,data);
        this.dataSet = data;
        this.mContext=context;

    }

    public void onClick(@NotNull View v) {

//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        Branch dataModel=(Branch) object;
//
//        switch (v.getId())
//        {
////            case R.id.item_info:
////                Snackbar.make(v, "Release date " +dataModel.getBranch_name(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
////                break;
//        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Branch dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.branch_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.br_name);
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.br_code);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        assert dataModel != null;
        viewHolder.txtName.setText(dataModel.getBranch_name());
        viewHolder.txtCode.setText(dataModel.getBranch_code());

        // Return the completed view to render on screen
        return convertView;
    }





}
