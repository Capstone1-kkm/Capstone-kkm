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
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
            public void onClick(View view) {
                // 클릭한 뷰에서 제목 추출
                TextView storeNameTextView = view.findViewById(R.id.popup_store_name);
                String title = storeNameTextView.getText().toString();

                Intent intent = new Intent(wishlist.this, info.class);

                // 제목에 따라 정보 전달
                if ("A Cloud Traveler : 구름 위를 걷는 기분".equals(title)) {
                    intent.putExtra("tableName", "popup_info");
                    intent.putExtra("imageFileName", "store1.png");
                } else if ("It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!".equals(title)) {
                    intent.putExtra("tableName", "popup_info1");
                    intent.putExtra("imageFileName", "store2.png");
                } else if ("담곰이 카페".equals(title)) {
                    intent.putExtra("tableName", "popup_info2");
                    intent.putExtra("imageFileName", "store3.png");
                } else if ("담곰이 팝업스토어 <봄날의 담곰이>".equals(title)) {
                    intent.putExtra("tableName", "popup_info3");
                    intent.putExtra("imageFileName", "store4.png");
                } else if ("로에베 퍼퓸 팝업스토어".equals(title)) {
                    intent.putExtra("tableName", "popup_info4");
                    intent.putExtra("imageFileName", "store5.png");
                } else if ("IKEA 팝업스토어 더현대 서울".equals(title)) {
                    intent.putExtra("tableName", "popup_info5");
                    intent.putExtra("imageFileName", "store6.png");
                }else if ("엄브로 100주년 <MR.UM's CLEANERS>".equals(title)) {
                    intent.putExtra("tableName", "popup_info6");
                    intent.putExtra("imageFileName", "store7.png");
                }else if ("블레스문 팝업스토어".equals(title)) {
                    intent.putExtra("tableName", "popup_info7");
                    intent.putExtra("imageFileName", "store8.png");
                }else if ("요물, 우리를 홀린 고양이".equals(title)) {
                    intent.putExtra("tableName", "popup_info8");
                    intent.putExtra("imageFileName", "store9.png");
                }else if ("달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다".equals(title)) {
                    intent.putExtra("tableName", "popup_info9");
                    intent.putExtra("imageFileName", "store10.png");
                }
                // 필요한 만큼 else if 블록을 추가하여 다른 마커 제목에 맞는 테이블 이름을 설정할 수 있음

                startActivity(intent);
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
