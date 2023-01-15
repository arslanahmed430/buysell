package com.arslans.buyandsell.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.arslans.buyandsell.Adapter.AdsAdapter;
import com.arslans.buyandsell.Models.AdModel;
import com.arslans.buyandsell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    ImageView submitAd;
    AdsAdapter adapter;
    private List<AdModel> adsList=new ArrayList<>();
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler=findViewById(R.id.recycler);
        submitAd=findViewById(R.id.submitAd);
        recycler.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        adapter=new AdsAdapter(this,adsList);
        recycler.setAdapter(adapter);
        mDatabase= FirebaseDatabase.getInstance().getReference();



        submitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(MainActivity.this,SubmitAd.class));
            }
        });
    }

    private void getDataFromServer() {
        adsList.clear();
        mDatabase.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    AdModel model=snapshot1.getValue(AdModel.class);
                    adsList.add(model);
                }
                Collections.reverse(adsList);
                if(adsList.size()>0){
                    adapter.setItemList(adsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile){
            startActivity(new Intent(MainActivity.this,MyAccount.class));
            // do something
        }
        return super.onOptionsItemSelected(item);
    }
}