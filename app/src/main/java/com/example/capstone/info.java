package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class info extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String tableName;
    private String imageFileName;
    private String websiteLink;
    private String instagramLink;
    private TextView storeNameTextView;
    public static final String PREFS_NAME = "wishlist_prefs";
    public static final String KEY_WISHLIST_ITEMS = "wishlist_items";
    public static final String LISTPREFS_NAME = "chat_list_pref";
    public static final String KEY_CHAT_LIST = "chat_list";
    private String popupStoreName;
    private ImageView wishhearttImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableName = getIntent().getStringExtra("tableName");
        imageFileName = getIntent().getStringExtra("imageFileName");

        loadPopupInfo();
        loadCloudImage(); // 사진 로딩 메소드 호출

        wishhearttImageView = findViewById(R.id.wishheart);

        // info.java

// wishhearttImageView 클릭 이벤트 설정
        wishhearttImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭한 정보를 데이터베이스에 저장하는 코드 추가
                saveToDatabase();

                // Check the current image resource and switch to the other one
                if (wishhearttImageView.getTag() != null && wishhearttImageView.getTag().toString().equals("wishheart")) {
                    wishhearttImageView.setImageResource(R.drawable.wishheart2);
                    wishhearttImageView.setTag("wishheart2");
                } else {
                    wishhearttImageView.setImageResource(R.drawable.wishheart);
                    wishhearttImageView.setTag("wishheart");
                }
            }
        });


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
                    // 채팅 목록에 채팅방 추가
                    addChatRoomToPreferences(popupStoreName);

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
    private void addChatRoomToPreferences(String chatRoomName) {
        SharedPreferences sharedPreferences = getSharedPreferences(LISTPREFS_NAME, Context.MODE_PRIVATE);
        // 이전에 저장된 채팅 목록을 불러옵니다.
        Set<String> chatList = sharedPreferences.getStringSet(KEY_CHAT_LIST, new HashSet<>());

        // 새로운 채팅방을 목록에 추가합니다.
        chatList.add(chatRoomName);

        // 수정된 목록을 다시 SharedPreferences에 저장합니다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_CHAT_LIST, chatList);
        editor.apply(); // 변경 사항을 적용합니다.

        // 추가 완료 메시지 표시
        Toast.makeText(info.this, "Added to chat list", Toast.LENGTH_SHORT).show();
    }


    private void saveToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // 정보를 저장할 새로운 데이터베이스 참조 생성
        String key = databaseReference.child("wishlist").push().getKey();

        // 데이터베이스에 저장할 정보 설정 (예: 팝업 스토어 이름)
        String popupStoreName = "YourPopupStoreName";

        // 데이터베이스에 정보 저장
        databaseReference.child("wishlist").child(key).setValue(popupStoreName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 저장 성공 시 실행할 코드 (예: 토스트 메시지 표시)
                        Toast.makeText(info.this, "Wishlist item added to database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 저장 실패 시 실행할 코드 (예: 토스트 메시지 표시)
                        Toast.makeText(info.this, "Failed to add wishlist item to database", Toast.LENGTH_SHORT).show();
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

    private void addToWishlist() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> wishlistItems = sharedPreferences.getStringSet(KEY_WISHLIST_ITEMS, new HashSet<String>());
        wishlistItems.add(popupStoreName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_WISHLIST_ITEMS, wishlistItems);
        editor.apply();

        Toast.makeText(info.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
    }

    private void removeFromWishlist() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> wishlistItems = sharedPreferences.getStringSet(KEY_WISHLIST_ITEMS, new HashSet<String>());
        wishlistItems.remove(popupStoreName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_WISHLIST_ITEMS, wishlistItems);
        editor.apply();

        Toast.makeText(info.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
    }
}
