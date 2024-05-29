package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashSet;
import java.util.Set;

public class info extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String tableName;
    private String imageFileName;
    private String websiteLink;
    private String instagramLink;
    private TextView storeNameTextView;
    private static final String PREFS_NAME = "wishlist_prefs";
    private static final String KEY_CHAT_LIST_ITEMS = "chat_list_items";
    private String popupStoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableName = getIntent().getStringExtra("tableName");
        imageFileName = getIntent().getStringExtra("imageFileName");

        loadPopupInfo();
        loadCloudImage(); // 사진 로딩 메소드 호출

        View imageView = findViewById(R.id.backst);
        // 이전으로 돌아가는 인텐트
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 채팅 이미지뷰 클릭 리스너 설정
        ImageView chatImageView = findViewById(R.id.message);
        chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupStoreName != null) {
                    // SharedPreferences에 팝업 스토어 이름을 저장
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    Set<String> chatListItems = sharedPreferences.getStringSet(KEY_CHAT_LIST_ITEMS, new HashSet<String>());
                    chatListItems.add(popupStoreName);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet(KEY_CHAT_LIST_ITEMS, chatListItems);
                    editor.apply();

                    // 채팅 화면으로 이동하는 Intent를 생성합니다.
                    Intent intent = new Intent(info.this, chat2.class);
                    intent.putExtra("popup_store_name", popupStoreName);
                    startActivity(intent);
                } else {
                    Toast.makeText(info.this, "Popup store name is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 팝업 홈페이지 클릭 이벤트 설정
        TextView websiteTextView = findViewById(R.id.websiteTextView);
        websiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteLink != null && !websiteLink.isEmpty()) {
                    // 해당 링크로 이동
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink)));
                } else {
                    Toast.makeText(info.this, "Website link is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 인스타그램 클릭 이벤트 설정
        TextView instagramTextView = findViewById(R.id.instagramTextView);
        instagramTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instagramLink != null && !instagramLink.isEmpty()) {
                    // 해당 링크로 이동
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink)));
                } else {
                    Toast.makeText(info.this, "Instagram link is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    websiteLink = dataSnapshot.child("popup_website").getValue(String.class);
                    instagramLink = dataSnapshot.child("instagram_website").getValue(String.class);

                    popupStoreName = title; // 제목을 팝업 스토어 이름으로 설정

                    ((TextView) findViewById(R.id.titleTextView)).setText(title);
                    ((TextView) findViewById(R.id.dateTextView)).setText(date);
                    ((TextView) findViewById(R.id.locationTextView)).setText(location);
                    ((TextView) findViewById(R.id.descriptionTextView)).setText(description);
                    ((TextView) findViewById(R.id.hoursWeekdayTextView)).setText(hoursWeekday);
                    ((TextView) findViewById(R.id.hoursWeekendTextView)).setText(hoursWeekend);
                    ((TextView) findViewById(R.id.websiteTextView)).setText("팝업 홈페이지");
                    ((TextView) findViewById(R.id.instagramTextView)).setText("인스타그램");
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
