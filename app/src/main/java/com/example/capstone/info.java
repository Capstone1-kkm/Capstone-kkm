package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    private ImageView wishheart;
    private boolean isWished = false;
    private String itemId = "popup_info";
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
    public static final String KEY_WISHHEART_STATE = "wishheart_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        wishheart= findViewById(R.id.wishheart);
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
        // 현재 로그인한 사용자의 UID 가져오기
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");

        // 팝업 정보를 가져와서 데이터베이스에 저장할 준비
        mDatabase.child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 팝업 정보에서 title과 location을 가져옴
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String imageFileName = getIntent().getStringExtra("imageFileName"); // 인텐트에서 직접 이미지 파일 이름을 가져옴

                    if (imageFileName != null && !imageFileName.isEmpty()) {
                        // Firebase Storage에서 이미지 URL을 가져옴
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageFileName);
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                // 해당 사용자의 위시리스트를 확인하여 팝업 스토어의 존재 여부를 결정
                                userWishlistRef.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // wishlist에서 팝업 스토어를 제거
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                childSnapshot.getRef().removeValue();
                                            }
                                            // wishheart 상태를 wishheart로 변경
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    wishhearttImageView.setImageResource(R.drawable.wishheart);
                                                    wishhearttImageView.setTag("wishheart");
                                                    // wishheart 상태를 영구적으로 저장
                                                    saveWishheartState(false);
                                                    Toast.makeText(info.this, "Item removed from your wishlist", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            // wishlist에 팝업 스토어를 추가
                                            String key = userWishlistRef.push().getKey();
                                            userWishlistRef.child(key).child("title").setValue(title);
                                            userWishlistRef.child(key).child("location").setValue(location);
                                            userWishlistRef.child(key).child("imageUrl").setValue(imageUrl);

                                            // wishheart 상태를 wishheart2로 변경
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    wishhearttImageView.setImageResource(R.drawable.wishheart2);
                                                    wishhearttImageView.setTag("wishheart2");
                                                    // wishheart 상태를 영구적으로 저장
                                                    saveWishheartState(true);
                                                    Toast.makeText(info.this, "Wishlist item added to your list", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(info.this, "Failed to check your wishlist", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(info.this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(info.this, "Image file name is missing.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(info.this, "Failed to load popup info.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }




    // wishheart 상태를 영구적으로 저장
    private void saveWishheartState(boolean isWished) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_WISHHEART_STATE, isWished);
        editor.apply();

        Log.d("WishheartState", "Wishheart state saved: " + isWished);
    }

    // wishheart 상태를 불러오기
    private boolean loadWishheartState() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_WISHHEART_STATE, false);
    }


    private void addToWishlist(String itemId) {
        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("wishlist")
                .child(itemId);
        wishlistRef.setValue(true);
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




