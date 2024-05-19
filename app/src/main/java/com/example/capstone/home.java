package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);



        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        ImageView imageView = findViewById(R.id.imageViewBottom5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(home.this, mypage.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
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

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        imageView = findViewById(R.id.imageViewBottom4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(home.this, info.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

       /* // AAA 이미지뷰를 찾습니다.
        ImageView imageView = findViewById(R.id.imageView);

        // AAA 이미지뷰에 OnClickListener를 설정합니다.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(home.this, store1.class);

                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });*/
    }
}

