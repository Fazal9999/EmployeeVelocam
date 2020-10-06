package com.example.velocam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.velocam.Model.Branch;
import com.example.velocam.R;
import com.example.velocam.utils.Config;

import java.util.ArrayList;
import java.util.List;

public class BranchCheckBoxAdapter extends RecyclerView.Adapter<BranchCheckBoxViewHolder> {
    private ArrayList<Integer> selectCheck=new ArrayList<Integer>();

    private int selectedPosition =-1;// no selection by default
    Context context;
   List<Branch> optionsList;

    public BranchCheckBoxAdapter(Context context, ArrayList<Branch> optionsList) {
        this.context = context;
        this.optionsList = optionsList;
    }

    @NonNull
    @Override
    public BranchCheckBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.branch_list_layout,parent, false);
        return new BranchCheckBoxViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BranchCheckBoxViewHolder holder, int position) {
        holder.checkBox.setTag(position); // This line is important.
       // holder.checkBox.setChecked(selectedPosition == position);







            holder.checkBox.setChecked(true);
            holder.checkBox.setText( optionsList.get(position).getBranch_name());
            holder.meter.setText("("+optionsList.get(position).getBranch_radius()+""+""+"M"+")");
        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
        } else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));


  /*      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                        selectedPosition = position;
                        Config.branchadded.add(compoundButton.getText().toString());
                        Config.meter += Integer.parseInt(optionsList.get(position).getBranch_radius());

                        selectedPosition = -1;

                   // selectedPosition = holder.getAdapterPosition();


                } else {
                    Config.branchadded.remove(compoundButton.getText().toString());
                    Config.meter = Integer.parseInt(optionsList.get(position).getBranch_radius());
                }
            }
        });*/

           // final int finalPosition = position;


    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount () {
        return optionsList.size();
    }


}


