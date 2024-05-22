package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        // 기존 하단 이미지뷰 클릭 리스너 설정
        View imageView = findViewById(R.id.imageViewBottom1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 액티비티로 돌아가는 인텐트
                finish();
            }
        });

        imageView = findViewById(R.id.imageViewBottom2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(home.this, googlemap.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(home.this, home.class);
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
                Intent intent = new Intent(home.this, mypage.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });



        // 추가된 ImageView 클릭 리스너 설정
        setImageViewClickListener(R.id.store1, "popup_info", "store1.png");
        setImageViewClickListener(R.id.store2, "popup_info1", "store2.png");
        setImageViewClickListener(R.id.store3, "popup_info2", "store3.png");

        // 추가된 LinearLayout 클릭 리스너 설정
        setLinearLayoutClickListener(R.id.store4, "popup_info3", "store4.png");
        setLinearLayoutClickListener(R.id.store5, "popup_info4", "store5.png");
        setLinearLayoutClickListener(R.id.store6, "popup_info5", "store6.png");
        setLinearLayoutClickListener(R.id.store7, "popup_info6", "store7.png");
        setLinearLayoutClickListener(R.id.store8, "popup_info7", "store8.png");
        setLinearLayoutClickListener(R.id.store9, "popup_info8", "store9.png");
        setLinearLayoutClickListener(R.id.store10, "popup_info9", "store10.png");
    }

    private void setImageViewClickListener(int imageViewId, final String tableName, final String imageFileName) {
        findViewById(imageViewId).setOnClickListener(view -> {
            Intent intent = new Intent(home.this, info.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("imageFileName", imageFileName);
            startActivity(intent);
        });
    }

    private void setLinearLayoutClickListener(int linearLayoutId, final String tableName, final String imageFileName) {
        findViewById(linearLayoutId).setOnClickListener(view -> {
            Intent intent = new Intent(home.this, info.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("imageFileName", imageFileName);
            startActivity(intent);
        });
    }
}


