package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class chat extends AppCompatActivity {

    private static final String PREFS_NAME = "wishlist_prefs";
    private static final String KEY_CHAT_LIST_ITEMS = "chat_list_items";
    private ListView chatListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> chatListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

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
                Intent intent = new Intent(chat.this, googlemap.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(chat.this, home.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        // 채팅 버튼 눌렀을때 이전 스택에 쌓인 액티비티로 이동하게 됨
        imageView = findViewById(R.id.imageViewBottom4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(chat.this, chat.class);
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
                Intent intent = new Intent(chat.this, mypage.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        chatListView = findViewById(R.id.chat_list_view);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭된 아이템의 정보를 가져옵니다.
                String popupStoreName = chatListItems.get(position); // 예시: 팝업스토어 이름이라고 가정

                // 채팅 화면으로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(chat.this, chat2.class);
                // 팝업스토어 이름을 Intent에 추가합니다.
                intent.putExtra("popup_store_name", popupStoreName);
                // Intent를 사용하여 채팅 화면으로 이동합니다.
                startActivity(intent);
            }
        });

        chatListItems = new ArrayList<>();

        // Load chat list from SharedPreferences
        loadChatList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatListItems);
        chatListView.setAdapter(adapter);
    }

    private void loadChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> chatList = sharedPreferences.getStringSet(KEY_CHAT_LIST_ITEMS, null);

        if (chatList != null) {
            chatListItems.addAll(chatList);
        }
    }
}
