package com.gzeinnumer.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapp.adapter.MessageAdapter;
import com.gzeinnumer.chatapp.databinding.ActivityMessageBinding;
import com.gzeinnumer.chatapp.model.Chat;
import com.gzeinnumer.chatapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//todo 44
public class MessageActivity extends AppCompatActivity {

    //todo 45
    ActivityMessageBinding binding;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    //todo 58
    MessageAdapter myAdapter;
    List<Chat> listChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 46
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(binding.getRoot());

        //todo 48
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                //todo 92 komentarkan yang diatas
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //end todo 92
            }
        });

        intent = getIntent();
        final String userId = intent.getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                binding.username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    binding.profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(binding.profileImage);
                }

                //todo 60
                readMessage(firebaseUser.getUid(), userId, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //todo 51
        binding.btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.textSend.getText().toString();

                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), userId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "pesan jangan kosong", Toast.LENGTH_SHORT).show();
                }
                binding.textSend.setText("");
            }
        });


    }

    //todo 50
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chats_app").push().setValue(hashMap);
    }

    //todo 59
    private void readMessage(final String myId, final String userId, final String imageUrl){
        binding.rvData.setLayoutManager(new LinearLayoutManager(this));
        binding.rvData.setHasFixedSize(true);

        listChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats_app");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        listChat.add(chat);
                    }

                    myAdapter = new MessageAdapter(listChat, imageUrl);
                    binding.rvData.setAdapter(myAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
