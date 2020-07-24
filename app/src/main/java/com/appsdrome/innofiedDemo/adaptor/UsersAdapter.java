package com.appsdrome.innofiedDemo.adaptor;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsdrome.innofiedDemo.R;
import com.appsdrome.innofiedDemo.model.Data;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CountryViewHolder>{

private ArrayList<Data> userList;
Context context;


        public UsersAdapter(ArrayList<Data> userList, Context context) {
            this.userList = userList;
            this.context=context;
        }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.row_user_item,parent,false);

        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

         holder.countryNameTextView.setText(userList.get(position).getEmail());
         holder.firstName.setText(userList.get(position).getFirstName());
         holder.lastName.setText(userList.get(position).getLastName());
        Glide.with(context).load(userList.get(position).getAvatar()).placeholder(R.drawable.img_placeholder).into(holder.userImg);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class CountryViewHolder extends RecyclerView.ViewHolder{

            TextView countryNameTextView;
            TextView firstName;
            TextView lastName;
            ImageView userImg;

            public CountryViewHolder(View itemView) {
                super(itemView);

                countryNameTextView=itemView.findViewById(R.id.tv_email_id);
                firstName=itemView.findViewById(R.id.tv_first_name);
                lastName=itemView.findViewById(R.id.tv_last_name);
                userImg = itemView.findViewById(R.id.img_view);
            }
        }
}
