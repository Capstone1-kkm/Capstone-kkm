package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mypage extends Activity {

    TextView greetingTextView;
    TextView loginTextView;
    boolean isLoggedIn = false;
    private static final int LOGIN_REQUEST_CODE = 1;
    private static final String PREF_NAME = "login_pref";
    private static final String KEY_LOGGED_IN = "logged_in";

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

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        View imageView = findViewById(R.id.imageViewBottom3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(mypage.this, home.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        // 앱 시작 시 로그아웃 상태로 초기화
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
        updateGreeting();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 로그인 성공 시 사용자 이름을 받아와 환영 메시지를 업데이트
            updateGreeting();

            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(KEY_LOGGED_IN, true);
            editor.apply();
        }
    }

    private void updateGreeting() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                greetingTextView.setText(displayName + "님, 안녕하세요");
                isLoggedIn = true;
                loginTextView.setText("로그아웃"); // "로그아웃"으로 텍스트 설정
            }
        } else {
            // 사용자가 로그인하지 않은 경우
            greetingTextView.setText("비회원님, 안녕하세요");
            isLoggedIn = false;
            loginTextView.setText("로그인 하려면 클릭하세요"); // "로그인 하려면 클릭하세요"로 텍스트 설정
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 앱이 재개되면 로그인 상태를 다시 확인
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
        if (!isLoggedIn) {
            updateGreeting();
        }
    }
}
