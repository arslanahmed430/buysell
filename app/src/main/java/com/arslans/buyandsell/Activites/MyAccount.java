package com.arslans.buyandsell.Activites;

import android.content.Intent;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arslans.buyandsell.Adapter.AdsAdapter;
import com.arslans.buyandsell.Models.AdModel;
import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAccount extends AppCompatActivity {


    Button logout;
    EditText name, phone;
    RecyclerView recycler;
    DatabaseReference mDatabase;
    AdsAdapter adapter;
    private List<AdModel> adsList = new ArrayList<>();
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("My Account");
        name = findViewById(R.id.name);
        progress = findViewById(R.id.progress);
        phone = findViewById(R.id.phone);
        recycler = findViewById(R.id.recycler);
        logout = findViewById(R.id.logout);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name.setText(SharedPrefs.getUser().getName());
        phone.setText(SharedPrefs.getUser().getPhone());
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new AdsAdapter(this, adsList);
        recycler.setAdapter(adapter);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefs.logout();
                startActivity(new Intent(MyAccount.this, Splash.class));
                finish();
            }
        });
        getDataFromServer();
    }

    private void getDataFromServer() {
        progress.setVisibility(View.VISIBLE);
        adsList.clear();
        mDatabase.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    AdModel model = snapshot1.getValue(AdModel.class);
                    if (model.getPhoneNumber().equalsIgnoreCase(SharedPrefs.getUser().getPhone())) {
                        adsList.add(model);
                    }
                }
                Collections.reverse(adsList);
                if (adsList.size() > 0) {
                    adapter.setItemList(adsList);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}