package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login1 extends AppCompatActivity {

    private static final String PREF_NAME = "UserData";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login1.this, login2.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            // 이메일 또는 비밀번호가 입력되지 않은 경우
            Toast.makeText(login1.this, "이메일과 비밀번호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            return; // 로그인 시도 중단
        }

        login1 login1 = this;

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(login1, "로그인 완료", Toast.LENGTH_LONG).show();
                            // 여기서 다음 화면으로 이동하도록 코드를 작성하면 됩니다.

                        } else {
                            // 로그인 실패
                            Toast.makeText(login1, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
