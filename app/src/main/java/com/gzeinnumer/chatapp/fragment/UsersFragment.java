package com.gzeinnumer.chatapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapp.adapter.UserAdapter;
import com.gzeinnumer.chatapp.databinding.FragmentUsersBinding;
import com.gzeinnumer.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersFragment extends Fragment {

    //todo 40
    private FragmentUsersBinding binding;
    private UserAdapter myUserAdapter;
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

        //todo 96
        binding.searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchUser(s.toString());
                //todo 104 komentarkan yang diatas
                searchUser(s.toString().toLowerCase());
                //end todo 104
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchUser.getText().clear();
                binding.searchUser.clearFocus();
            }
        });

    }

    //todo 43
    private void readData() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_chat_app");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //todo 98-1 tambahkan if
                if(binding.searchUser.getText().toString().equals("")){
                //end todo 98-1

                    list.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);

                        if(!Objects.equals(user.getId(), firebaseUser.getUid())){
                            list.add(user);
                        }
                    }
                    Log.d("MyZein", String.valueOf(list.size()));

                    //myAdapter =     new UserAdapter(list);
                    //todo 94 komentari yang diatas
                    myUserAdapter =     new UserAdapter(list, true);
                    //end todo 94
                    binding.rvData.setAdapter(myUserAdapter);
                    binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));

                //todo 98-2
                }
                //end todo 98-2

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //todo 97
    private void searchUser(String s) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Query query = FirebaseDatabase.getInstance().getReference("Users_chat_app").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        //todo 103 komentarkan yg di atas
        Query query = FirebaseDatabase.getInstance().getReference("Users_chat_app").orderByChild("search").startAt(s).endAt(s+"\uf8ff");
        //end todo 103
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        list.add(user);
                    }
                }

                myUserAdapter = new UserAdapter(list, true);
                binding.rvData.setAdapter(myUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
