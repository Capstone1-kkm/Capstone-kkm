package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class mypage extends Activity {

    TextView greetingTextView;
    TextView loginTextView;

    boolean isLoggedIn = false;
    String loggedInUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        TextView textView = findViewById(R.id.login1);
        greetingTextView = findViewById(R.id.login2);
        loginTextView = findViewById(R.id.login1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 이벤트 처리 코드
                // 예를 들어, 새로운 화면으로 이동하는 코드를 작성할 수 있습니다.
                Intent intent = new Intent(mypage.this, login1.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                loggedInUsername = data.getStringExtra("username");
                isLoggedIn = true;
                updateGreeting();
            }
        }
    }
    private void updateGreeting(){
        if(isLoggedIn){
            greetingTextView.setText(loggedInUsername + "님, 안녕하세요");
        }
        else{
            greetingTextView.setText(("비회원님, 안녕하세요"));
        }
    }
}
