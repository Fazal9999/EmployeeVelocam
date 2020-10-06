package com.example.velocam.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.R;



 class BranchCheckBoxViewHolder extends RecyclerView.ViewHolder {

    CheckBox checkBox;
    TextView meter;

    public BranchCheckBoxViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.ckb_topping);
        meter = (TextView) itemView.findViewById(R.id.br_m);
    }
}
