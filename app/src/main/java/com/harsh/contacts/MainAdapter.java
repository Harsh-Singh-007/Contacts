package com.harsh.contacts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Viewholder> {

    Activity activity;
    ArrayList<ContactModel> arrayList;

    //Creating a constructor
    public MainAdapter(Activity activity,ArrayList<ContactModel> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialise view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);

        //Return view
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        //Initialise Contact model
        ContactModel model = arrayList.get(position);

        //Set name
        holder.tvName.setText(model.getName());

        //Set number
        holder.tvNumber.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {

        //Return array list size
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        //Intiailzing variables
        TextView tvName, tvNumber;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);

        }
    }
}
