package com.gzeinnumer.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gzeinnumer.chatapp.MessageActivity;
import com.gzeinnumer.chatapp.R;
import com.gzeinnumer.chatapp.databinding.ChatItemLeftBinding;
import com.gzeinnumer.chatapp.databinding.ChatItemRightBinding;
import com.gzeinnumer.chatapp.databinding.UserItemBinding;
import com.gzeinnumer.chatapp.model.Chat;
import com.gzeinnumer.chatapp.model.User;

import java.util.List;

//todo 56
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    public Context context;
    private List<Chat> list;
    private String image;
    FirebaseUser firebaseUser;

    public MessageAdapter(List<Chat> list, String image) {
        this.list = list;
        this.image = image;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if(viewType == MSG_TYPE_RIGHT){
            View bindingR = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MyHolder(bindingR);
        } else {
            View bindingL = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MyHolder(bindingL);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(list.get(position), image, context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView showMessage;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            showMessage = itemView.findViewById(R.id.show_message);
        }

        void bind(final Chat chat, String image, Context context) {
            showMessage.setText(chat.getMessage());
            if(image.equals("default")){
                profileImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(image).into(profileImage);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}