package com.arslans.buyandsell.Activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arslans.buyandsell.Models.AdModel;
import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAd extends AppCompatActivity {

    String key;
    DatabaseReference mDatabase;
    private AdModel model;
    ProgressBar progress;
    TextView title, price, description, location, time;
    ImageView image;
    Button sms, call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("");
        key = getIntent().getStringExtra("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progress = findViewById(R.id.progress);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        time = findViewById(R.id.time);
        sms = findViewById(R.id.sms);
        call = findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+model.getPhoneNumber()));
                startActivity(i);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", model.getPhoneNumber()
                        , null)));

            }
        });

        getDaraFromServer();
    }

    private void getDaraFromServer() {
        progress.setVisibility(View.VISIBLE);
        mDatabase.child("Ads").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(AdModel.class);
                progress.setVisibility(View.GONE);
                Glide.with(ViewAd.this).load(model.getPicUrl()).into(image);
                title.setText(model.getTitle());
                price.setText("Rs " + model.getPrice());
                location.setText(model.getCity());
                description.setText(model.getDescription());
                time.setText(CommonUtils.getFormattedDate(model.getTime()));
                ViewAd.this.setTitle(model.getTitle());

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