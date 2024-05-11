package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class mypage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        TextView textView = findViewById(R.id.login1);
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
}
