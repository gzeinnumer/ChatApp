package com.gzeinnumer.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gzeinnumer.chatapp.MessageActivity;
import com.gzeinnumer.chatapp.R;
import com.gzeinnumer.chatapp.databinding.UserItemBinding;
import com.gzeinnumer.chatapp.model.User;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

//todo 39
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {
    private Context context;
    private List<User> list;
    //todo 84 tambah variable untuk status dan perbaiki contructor
    private boolean isChat;

//    public UserAdapter(List<User> list) {
//        this.list = list;
//    }
    //todo 85 komentarkan costruktor diatas
    public UserAdapter(List<User> list, boolean isChat) {
        this.list = list;
        this.isChat = isChat;
    }
    //end todo 85

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding =  UserItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        context = parent.getContext();
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//        holder.bind(list.get(position));
        //todo 86 komentarkan yang diatas
        holder.bind(list.get(position), isChat);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;
        MyHolder(@NonNull UserItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

//        void bind(final User user) {
        //todo 87 komentarkan yang diatas
        void bind(final User user, boolean isChat) {
            binding.username.setText(user.getUsername());
            if(user.getImageURL().equals("default")){
                binding.profileImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(user.getImageURL()).into(binding.profileImage);
            }
            //todo 47
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("id", user.getId());
                    context.startActivity(intent);
                }
            });

            //todo 88
            if (isChat){
                if(user.getStatus().equals("online")){
                    binding.imgOn.setVisibility(View.VISIBLE);
                    binding.imgOff.setVisibility(View.GONE);
                } else {
                    binding.imgOn.setVisibility(View.GONE);
                    binding.imgOff.setVisibility(View.VISIBLE);
                }
            } else {
                binding.imgOn.setVisibility(View.GONE);
                binding.imgOff.setVisibility(View.GONE);
            }
            //end todo 88
        }
    }
}
