package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;
import com.example.capstone.R;

public class wishlist extends AppCompatActivity {
    private LinearLayout wishlistContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist); // wishlist_item.xml이 아닌 wishlist.xml로 변경

        wishlistContainer = findViewById(R.id.wishlistLinearLayout);

        // Firebase에서 위시리스트 항목을 로드
        loadWishlistItems();

        // 하단 이미지들에 대한 클릭 이벤트 처리 추가
        View imageView = findViewById(R.id.imageViewBottom3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(wishlist.this, home.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wishlist.this, chat.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wishlist.this, mypage.class);
                startActivity(intent);
            }
        });
    }

    private void loadWishlistItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");

        userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    addWishlistItem(title, location, imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(wishlist.this, "위시리스트를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addWishlistItem(String title, String location, String imageUrl) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout wishlistItemLayout = (RelativeLayout) inflater.inflate(R.layout.wishlist, null);

        ImageView wishImgView = wishlistItemLayout.findViewById(R.id.wishimg);
        TextView storeNameTextView = wishlistItemLayout.findViewById(R.id.popup_store_name);
        TextView locationTextView = wishlistItemLayout.findViewById(R.id.wish_location);

        // 가져온 데이터를 각 요소에 설정
        storeNameTextView.setText(title);
        locationTextView.setText(location);
        wishImgView.setImageURI(Uri.parse(imageUrl));

        // 이미지 로드 라이브러리를 사용하여 이미지 설정
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(wishImgView);
        }

        wishlistContainer.addView(wishlistItemLayout);
    }
}
