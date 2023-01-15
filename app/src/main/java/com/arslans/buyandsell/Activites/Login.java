package com.arslans.buyandsell.Activites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arslans.buyandsell.Models.User;
import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.CommonUtils;
import com.arslans.buyandsell.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText phone, password;
    Button login,register;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().length() == 0) {
                    phone.setError("Enter phone");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else {
                    logiNNow();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
    }

    private void logiNNow() {
        mDatabase.child("Users").child(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    CommonUtils.showToast("User does not exists");
                } else {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getPassword().equals(password.getText().toString())) {
                            CommonUtils.showToast("Logged in successfully");
                            SharedPrefs.setUser(user);
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        } else {
                            CommonUtils.showToast("Wrng password");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}