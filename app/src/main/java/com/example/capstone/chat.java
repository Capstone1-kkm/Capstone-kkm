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

    private static final String PREFS_NAME = "chat_list_pref";
    private static final String KEY_CHAT_LIST = "chat_list";
    private static final int CHAT_REQUEST_CODE = 1;
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
                startActivityForResult(intent, CHAT_REQUEST_CODE);
            }
        });

        chatListItems = new ArrayList<>();
        loadChatList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatListItems);
        chatListView.setAdapter(adapter);

        // 인텐트를 통해 전달된 새로운 채팅 항목 추가
        Intent intent = getIntent();
        if (intent.hasExtra("new_chat_item")) {
            String newChatItem = intent.getStringExtra("new_chat_item");
            addChatList(newChatItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHAT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("remove_chat_item")) {
                String removeChatItem = data.getStringExtra("remove_chat_item");
                removeChatList(removeChatItem);
            }
        }
    }

    // 새로운 채팅을 채팅 목록에 추가하는 메서드
    public void addChatList(String chat) {
        if (!chatListItems.contains(chat)) {
            chatListItems.add(chat);
            adapter.notifyDataSetChanged();
            saveChatList();
        }
    }

    // 채팅 목록에서 채팅을 제거하는 메서드
    private void removeChatList(String chat) {
        if (chatListItems.contains(chat)) {
            chatListItems.remove(chat);
            adapter.notifyDataSetChanged();
            saveChatList();
        }
    }

    private void loadChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> chatList = sharedPreferences.getStringSet(KEY_CHAT_LIST, null);

        if (chatList != null) {
            chatListItems.addAll(chatList);
        }
    }

    private void saveChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> chatListSet = new HashSet<>(chatListItems);
        editor.putStringSet(KEY_CHAT_LIST, chatListSet);
        editor.apply();
    }
}
