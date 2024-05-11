package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login1 extends AppCompatActivity {

    // SharedPreferences를 이용하여 사용자 데이터 저장을 위한 상수 정의
    private static final String PREF_NAME = "UserData";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // 이메일과 비밀번호를 입력 받을 EditText 위젯 변수 선언
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);

        // XML 레이아웃에서 정의한 EditText 위젯을 변수에 바인딩
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // 로그인 버튼에 대한 클릭 리스너 설정
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(); // 로그인 시도 메소드 호출
            }
        });

        // 회원가입 버튼에 대한 클릭 리스너 설정
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동하는 인텐트 생성 및 실행
                Intent intent = new Intent(login1.this, login2.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        ImageView imageView = findViewById(R.id.imageViewBottom1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 뒤로가기 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        imageView = findViewById(R.id.imageViewBottom5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(login1.this, mypage.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

    }

    // 사용자 로그인을 처리하는 메소드
    private void loginUser() {
        // EditText에서 사용자가 입력한 이메일과 비밀번호를 가져옴
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 이메일 또는 비밀번호가 비어있는지 확인
        if (email.isEmpty() || password.isEmpty()) {
            // 이메일 또는 비밀번호가 입력되지 않은 경우 사용자에게 메시지 표시
            Toast.makeText(login1.this, "이메일과 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            return; // 로그인 시도 중단
        }

        // 현재 액티비티의 참조를 저장
        login1 login1 = this;

        // Firebase 인증 객체를 사용하여 사용자 로그인 시도
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공 시
                            Toast.makeText(login1, "로그인 완료", Toast.LENGTH_LONG).show();
                            // 로그인이 성공한 경우 home 화면으로 이동하는 인텐트 생성 및 실행
                            Intent intent = new Intent(login1.this, mypage.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료

                        } else {
                            // 로그인 실패 시
                            Toast.makeText(login1, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
