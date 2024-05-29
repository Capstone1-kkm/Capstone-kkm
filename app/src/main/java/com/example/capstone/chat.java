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
import java.util.HashSet;
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

        View imageView = findViewById(R.id.imageViewBottom1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = findViewById(R.id.imageViewBottom2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat.this, googlemap.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat.this, home.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat.this, chat.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageViewBottom5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat.this, mypage.class);
                startActivity(intent);
            }
        });

        chatListView = findViewById(R.id.chat_list_view);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String popupStoreName = chatListItems.get(position);

                Intent intent = new Intent(chat.this, chat2.class);
                intent.putExtra("popup_store_name", popupStoreName);
                startActivity(intent);
            }
        });

        chatListItems = new ArrayList<>();
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

    private void saveChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> chatListSet = new HashSet<>(chatListItems);
        editor.putStringSet(KEY_CHAT_LIST_ITEMS, chatListSet);
        editor.apply();
    }

    // 새로운 스토어 이름을 추가하고 저장하는 메서드 예시
    private void addStoreToChatList(String storeName) {
        if (!chatListItems.contains(storeName)) {
            chatListItems.add(storeName);
            adapter.notifyDataSetChanged();
            saveChatList();
        }
    }
}
