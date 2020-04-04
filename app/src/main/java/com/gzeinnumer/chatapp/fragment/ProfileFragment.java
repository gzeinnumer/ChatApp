package com.gzeinnumer.chatapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapp.R;
import com.gzeinnumer.chatapp.databinding.FragmentProfileBinding;
import com.gzeinnumer.chatapp.model.User;

//todo 65 part 10
public class ProfileFragment extends Fragment {

    //todo 67
    FragmentProfileBinding binding;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //todo 68
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //todo 69
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                binding.username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    binding.profileImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(view.getContext()).load(user.getImageURL()).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
