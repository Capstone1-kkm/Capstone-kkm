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

        // AAA 이미지뷰를 찾습니다.
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
        });
    }
}

