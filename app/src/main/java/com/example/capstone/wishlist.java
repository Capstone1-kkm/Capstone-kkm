package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class wishlist extends AppCompatActivity {
    private LinearLayout wishlistContainer;
    private static final String TAG = "WishlistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);

        wishlistContainer = findViewById(R.id.wishlistLinearLayout);

        // Firebase에서 위시리스트 항목을 로드
        loadWishlistItems();

        // 하단 이미지뷰 클릭 이벤트 처리
        findViewById(R.id.imageViewBottom3).setOnClickListener(v -> startActivity(new Intent(wishlist.this, home.class)));
        findViewById(R.id.imageViewBottom4).setOnClickListener(v -> startActivity(new Intent(wishlist.this, chat.class)));
        findViewById(R.id.imageViewBottom5).setOnClickListener(v -> startActivity(new Intent(wishlist.this, mypage.class)));
    }

    private void loadWishlistItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");

        userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "No wishlist data found for user.");
                    return;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    Log.d(TAG, "Wishlist item: " + title + ", " + location + ", " + imageUrl);
                    addWishlistItem(title, location, imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(wishlist.this, "위시리스트를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void addWishlistItem(String title, String location, String imageUrl) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout wishlistItemLayout = (RelativeLayout) inflater.inflate(R.layout.wishlist_item, null);

        ImageView wishImgView = wishlistItemLayout.findViewById(R.id.wishimg);
        TextView storeNameTextView = wishlistItemLayout.findViewById(R.id.popup_store_name);
        TextView locationTextView = wishlistItemLayout.findViewById(R.id.wish_location);

        // 가져온 데이터를 각 요소에 설정
        storeNameTextView.setText(title);
        locationTextView.setText(location);

        // 위시리스트 항목 클릭 이벤트 설정
        wishlistItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");
                userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String wishlistTitle = snapshot.child("title").getValue(String.class);
                                if (wishlistTitle.equals(title)) {
                                    Log.d(TAG, "Matching wishlist item found: " + wishlistTitle);
                                    // 위시리스트의 제목과 동일한 title을 가진 데이터를 popup_info 테이블에서 찾음
                                    DatabaseReference popupRef = FirebaseDatabase.getInstance().getReference("popup_info");
                                    Query query = popupRef.orderByChild("title").equalTo(title);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Log.d(TAG, "Popup info found for title: " + title);
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    // 해당 팝업 스토어의 정보를 가져와서 Bundle에 담기
                                                    String date = snapshot.child("date").getValue(String.class);
                                                    String description = snapshot.child("description").getValue(String.class);
                                                    String hoursWeekday = snapshot.child("hours_weekday").getValue(String.class);
                                                    String hoursWeekend = snapshot.child("hours_weekend").getValue(String.class);
                                                    String website = snapshot.child("website").getValue(String.class);
                                                    String instagram = snapshot.child("instagram").getValue(String.class);
                                                    String instagramWebsite = snapshot.child("instagram_website").getValue(String.class);
                                                    String popupWebsite = snapshot.child("popup_website").getValue(String.class);

                                                    // 가져온 정보를 Bundle에 담아 info 액티비티로 전달
                                                    Intent intent = new Intent(wishlist.this, info.class);
                                                    intent.putExtra("title", title);
                                                    intent.putExtra("location", location);
                                                    intent.putExtra("date", date);
                                                    intent.putExtra("description", description);
                                                    intent.putExtra("hoursWeekday", hoursWeekday);
                                                    intent.putExtra("hoursWeekend", hoursWeekend);
                                                    intent.putExtra("website", website);
                                                    intent.putExtra("instagram", instagram);
                                                    intent.putExtra("instagramWebsite", instagramWebsite);
                                                    intent.putExtra("popupWebsite", popupWebsite);
                                                    startActivity(intent); // 인텐트를 시작합니다.
                                                    return;
                                                }
                                            } else {
                                                // 팝업 정보를 찾을 수 없는 경우 사용자에게 메시지 표시
                                                Toast.makeText(wishlist.this, "해당 팝업 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Popup info not found for title: " + title);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(wishlist.this, "Failed to load popup info.", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
                                        }
                                    });
                                    return; // 이미 매칭된 title을 찾았으므로 더 이상 반복할 필요가 없음
                                }
                            }
                        }
                        // 위시리스트에 해당 title이 없는 경우 사용자에게 메시지 표시
                        Toast.makeText(wishlist.this, "해당 위시리스트 아이템을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Matching wishlist item not found: " + title);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(wishlist.this, "Failed to load wishlist data", Toast.LENGTH_SHORT);
                                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
                    }
                });
            }
        });


        // 이미지 URL이 유효한 경우에만 이미지를 로드
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(wishImgView);
        } else {
            wishImgView.setVisibility(View.GONE); // 이미지가 없는 경우 이미지뷰를 숨김
        }

        wishlistContainer.addView(wishlistItemLayout);
    }
}

