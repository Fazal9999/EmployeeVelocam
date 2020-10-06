package com.example.velocam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.Model.Branch;
import com.example.velocam.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpinnerAdapter  extends ArrayAdapter<Branch>  {
    private ArrayList<Branch> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView spinner_name;


    }
    public SpinnerAdapter(ArrayList<Branch> data, Context context)
    {
        super(context,R.layout.simple_spinner_item,data);
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
        SpinnerAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new SpinnerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
            viewHolder.spinner_name = (TextView) convertView.findViewById(R.id.spinner_name);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpinnerAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

       // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
      //  lastPosition = position;

        assert dataModel != null;
        viewHolder.spinner_name.setText(dataModel.getBranch_name());


        // Return the completed view to render on screen
        return convertView;
    }





}
