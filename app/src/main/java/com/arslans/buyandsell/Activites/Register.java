package com.arslans.buyandsell.Activites;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText name, phone, password;
    Button login, register;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (phone.getText().length() == 0) {
                    phone.setError("Enter phone");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else {
                    registeNow();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registeNow() {
        mDatabase.child("Users").child(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    CommonUtils.showToast("Phone number already taken");
                } else {
                    User user = new User(name.getText().toString(),
                            phone.getText().toString(),
                            password.getText().toString());
                    mDatabase.child("Users").child(phone.getText().toString()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CommonUtils.showToast("Registered successfully");
                            SharedPrefs.setUser(user);
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}