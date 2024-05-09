package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class login1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1); // 여기에는 현재 화면의 레이아웃 파일명을 넣어야 합니다.

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼이 클릭되었을 때 실행되는 코드
                Intent intent = new Intent(login1.this, login2.class); // Login2Activity는 다음 화면의 액티비티 클래스명입니다.
                startActivity(intent);
            }
        });
    }
}
