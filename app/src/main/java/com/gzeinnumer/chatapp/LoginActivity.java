package com.gzeinnumer.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.gzeinnumer.chatapp.databinding.ActivityLoginBinding;

//todo 15 buat activity
public class LoginActivity extends AppCompatActivity {

    //todo 16
    ActivityLoginBinding binding;

    //todo 19
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 17
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //todo 18
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App Java Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo 20
        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.email.getText().toString()) || TextUtils.isEmpty(binding.pass.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else if (binding.pass.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                } else {
                    login(binding.email.getText().toString(), binding.pass.getText().toString());
                }
            }
        });
    }

    //todo 21
    private void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
