package com.gzeinnumer.chatapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapp.R;
import com.gzeinnumer.chatapp.adapter.UserAdapter;
import com.gzeinnumer.chatapp.databinding.FragmentUsersBinding;
import com.gzeinnumer.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersFragment extends Fragment {

    //todo 40
    private FragmentUsersBinding binding;
    private UserAdapter myAdapter;
    private List<User> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //todo 41
        binding = FragmentUsersBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    //todo 42
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readData();
    }

    //todo 43
    private void readData() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_chat_app");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if(!Objects.equals(user.getId(), firebaseUser.getUid())){
                        list.add(user);
                    }
                }
                Log.d("MyZein", String.valueOf(list.size()));

                myAdapter = new UserAdapter(list);
                binding.rvData.setAdapter(myAdapter);
                binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
