package com.gzeinnumer.chatapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.gzeinnumer.chatapp.adapter.UserAdapter;
import com.gzeinnumer.chatapp.databinding.FragmentChatsBinding;
import com.gzeinnumer.chatapp.model.Chat;
import com.gzeinnumer.chatapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    //todo 61
    private FragmentChatsBinding binding;
    private UserAdapter myUserAdapter;
    private List<User> users = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private List<String> userList = new ArrayList<>();
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //todo 62
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //todo 63
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats_app");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid())){
                        userList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid())){
                        userList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //todo 64
    private void readChats() {
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    for (String id: userList){
                        if (user.getId().equals(id)){
                            if(users.size() != 0){
                                for (User user1: users){
                                    if(!user.getId().equals(user1.getId())){
                                        users.add(user);
                                    }
                                }
                            } else {
                                users.add(user);
                            }
                        }
                    }
                }


                binding.rvData.setHasFixedSize(true);
                binding.rvData.setLayoutManager(new LinearLayoutManager(context));
                myUserAdapter = new UserAdapter(users);
                binding.rvData.setAdapter(myUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
