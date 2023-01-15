package com.arslans.buyandsell.Activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arslans.buyandsell.Models.AdModel;
import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.CommonUtils;
import com.arslans.buyandsell.Utils.CompressImage;
import com.arslans.buyandsell.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubmitAd extends AppCompatActivity {

    ImageView pickImage;
    EditText title, price, description;

    Button submitAd;
    Spinner categorySpinner, citySpinner;
    private String imgUrl;
    String categoryChosen, cityChosen;
    DatabaseReference mDatabase;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_ad);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Submit ad");
        pickImage = findViewById(R.id.pickImage);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        progress = findViewById(R.id.progress);
        description = findViewById(R.id.description);
        submitAd = findViewById(R.id.submitAd);
        citySpinner = findViewById(R.id.citySpinner);
        categorySpinner = findViewById(R.id.categorySpinner);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options options = Options.init()
                        .setRequestCode(23)                                           //Request code for activity results
                        .setCount(1)
                        .setExcludeVideos(true)
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                        ;                                       //Custom Path For media Storage

                Pix.start(SubmitAd.this, options);
            }
        });

        submitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() == 0) {
                    title.setError("Enter title");
                } else if (price.getText().length() == 0) {
                    price.setError("Enter price");
                } else if (description.getText().length() == 0) {
                    description.setError("Enter description");
                } else if (imgUrl == null) {
                    CommonUtils.showToast("Pick image");
                } else {
                    uploadImage();
                }
            }
        });

        setupCategorySpinner();
        setupCitySpinner();


    }

    private void uploadImage() {
        progress.setVisibility(View.VISIBLE);
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(imgUrl));

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content


                        riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                postAdNow("" + uri);

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(SubmitAd.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void postAdNow(String liveUrl) {
        progress.setVisibility(View.GONE);
        String key = "" + System.currentTimeMillis();

        AdModel model = new AdModel(
                key,
                title.getText().toString(),
                liveUrl,
                description.getText().toString(),
                cityChosen,
                categoryChosen, SharedPrefs.getUser().getPhone(),
                Integer.parseInt(price.getText().toString()),
                System.currentTimeMillis()
        );
        mDatabase.child("Ads").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CommonUtils.showToast("Ad succssfully posted");
                Intent i = new Intent(SubmitAd.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> list = new ArrayList<>();
        list.add("Choose Category");
        list.add("Vehicles");
        list.add("Mobiles");
        list.add("Bikes");
        list.add("Electronics");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                categoryChosen = list.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCitySpinner() {
        List<String> list = new ArrayList<>();
        list.add("Lahore");
        list.add("Karachi");
        list.add("Islamabad");
        list.add("Faisalabad");
        list.add("Multan");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(dataAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cityChosen = list.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && data != null) {
            ArrayList<String> mSelected = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            imgUrl = CompressImage.compressImage(mSelected.get(0), SubmitAd.this);

            Glide.with(SubmitAd.this).load(mSelected.get(0)).into(pickImage);

        }
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