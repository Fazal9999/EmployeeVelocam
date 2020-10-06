package com.example.velocam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.velocam.Model.Branch;
import com.example.velocam.R;
import com.example.velocam.utils.Config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BranchChechAdapter  extends ArrayAdapter<Branch> {
    private ArrayList<Branch> dataSet;
    Context mContext;

    // View lookup cache

    private static class ViewHolder {
        CheckBox checkBox;
        TextView meter;


    }
    public BranchChechAdapter(ArrayList<Branch> data, Context context)
    {
        super(context, R.layout.branch_list_layout,data);
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
        BranchChechAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new BranchChechAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.branch_list_layout, parent, false);
            viewHolder.meter = (TextView) convertView.findViewById(R.id.br_m);
            viewHolder.checkBox=(CheckBox)convertView.findViewById(R.id.ckb_topping);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BranchChechAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        //  lastPosition = position;

        assert dataModel != null;
        viewHolder.meter.setText(dataModel.getBranch_radius());
        viewHolder.checkBox.setText(dataModel.getBranch_name());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    Config.branchadded.add(compoundButton.getText().toString());
                    //Config.meter+=Integer.parseInt(optionsList.get(position).getBranch_radius());
                }
                else
                {
                    Config.branchadded.remove(compoundButton.getText().toString());
                    //Config.meter=Integer.parseInt(optionsList.get(position).getBranch_radius());
                }
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }





}

