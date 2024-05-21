package com.example.capstone;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class info extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String tableName;
    private String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableName = getIntent().getStringExtra("tableName");
        imageFileName = getIntent().getStringExtra("imageFileName");

        loadPopupInfo();
        loadCloudImage(); // 사진 로딩 메소드 호출
    }

    private void loadPopupInfo() {
        mDatabase.child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String hoursWeekday = dataSnapshot.child("hours_weekday").getValue(String.class);
                    String hoursWeekend = dataSnapshot.child("hours_weekend").getValue(String.class);
                    String website = dataSnapshot.child("website").getValue(String.class);
                    String instagram = dataSnapshot.child("instagram").getValue(String.class);

                    ((TextView) findViewById(R.id.titleTextView)).setText(title);
                    ((TextView) findViewById(R.id.dateTextView)).setText(date);
                    ((TextView) findViewById(R.id.locationTextView)).setText(location);
                    ((TextView) findViewById(R.id.descriptionTextView)).setText(description);
                    ((TextView) findViewById(R.id.hoursWeekdayTextView)).setText(hoursWeekday);
                    ((TextView) findViewById(R.id.hoursWeekendTextView)).setText(hoursWeekend);
                    ((TextView) findViewById(R.id.websiteTextView)).setText(website);
                    ((TextView) findViewById(R.id.instagramTextView)).setText(instagram);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(info.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCloudImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageFileName);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                ImageView imageView = findViewById(R.id.cloudImageView);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(info.this, "Failed to load image.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
