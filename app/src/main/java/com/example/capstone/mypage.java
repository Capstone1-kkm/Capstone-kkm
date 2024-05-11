package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mypage extends Activity {

    TextView greetingTextView;
    TextView loginTextView;
    boolean isLoggedIn = false;
    private static final int LOGIN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        greetingTextView = findViewById(R.id.login1);
        loginTextView = findViewById(R.id.login2);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 화면으로 이동하는 인텐트 생성 및 실행
                Intent intent = new Intent(mypage.this, login1.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });
        updateGreeting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 로그인 성공 시 사용자 이름을 받아와 환영 메시지를 업데이트
            updateGreeting();
        }
    }

    private void updateGreeting() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                greetingTextView.setText(displayName + "님, 안녕하세요");
                isLoggedIn = true;
                loginTextView.setText("");
            }
        } else{
            greetingTextView.setText("비회원님, 안녕하세요");
            isLoggedIn = false;
        }
    }
}

