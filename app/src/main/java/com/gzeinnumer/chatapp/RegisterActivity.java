package com.gzeinnumer.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gzeinnumer.chatapp.databinding.ActivityRegisterBinding;

import java.util.HashMap;

//todo 5 buat activity
public class RegisterActivity extends AppCompatActivity {

    //todo 8 view binding
    ActivityRegisterBinding binding;

    //todo 10 set firebase
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 9 set view Binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //todo 14 set toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App Java Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //todo 11
        auth = FirebaseAuth.getInstance();

        //todo 13
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.username.getText().toString()) || TextUtils.isEmpty(binding.email.getText().toString()) || TextUtils.isEmpty(binding.pass.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else if (binding.pass.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                } else {
                    register(binding.username.getText().toString(), binding.email.getText().toString(), binding.pass.getText().toString());
                }
            }
        });
    }

    //todo 12 buat function register
    private void register(final String username, String email, String password) {
        Log.d("MyZein", "register");
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    //todo 93
                    hashMap.put("status", "offline");
                    //end todo 93
                    //todo 99
                    hashMap.put("search", username.toLowerCase());
                    //end todo 99
                    //todo 100 perbaiki struktur di firebase dan tambahkan search
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Gagal register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
