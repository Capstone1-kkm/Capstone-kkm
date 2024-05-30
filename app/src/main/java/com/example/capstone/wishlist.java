package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;
import com.example.capstone.R;

public class wishlist extends AppCompatActivity {

    private LinearLayout wishlistLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);

        wishlistLayout = findViewById(R.id.wishlist);

        // Firebase에서 위시리스트 항목을 로드
        loadWishlistItems();

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

        // 채팅 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        imageView = findViewById(R.id.imageViewBottom4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(wishlist.this, chat.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        imageView = findViewById(R.id.imageViewBottom5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(wishlist.this, mypage.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });
    }

    // wishlist.java

    private void loadWishlistItems() {
        SharedPreferences sharedPreferences = getSharedPreferences(info.PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> wishlistItems = sharedPreferences.getStringSet(info.KEY_WISHLIST_ITEMS, new HashSet<String>());

        for (String item : wishlistItems) {
            addWishlistItem(item);
        }
    }



    private void addWishlistItem(String title) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String location = snapshot.child("location").getValue(String.class);
                    if (location != null) {
                        View wishlistItemView = getLayoutInflater().inflate(R.layout.wishlist, null);

                        TextView titleTextView = wishlistItemView.findViewById(R.id.popup_store_name);
                        TextView locationTextView = wishlistItemView.findViewById(R.id.wish_location);

                        titleTextView.setText(title);
                        locationTextView.setText(location);

                        wishlistLayout.addView(wishlistItemView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });
    }

}
