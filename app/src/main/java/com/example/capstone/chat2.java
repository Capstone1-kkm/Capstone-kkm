package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat2 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Map<String, String>> chatList;
    private String nick; // 사용자 이름

    private EditText EditText_chat;
    private Button send;
    private DatabaseReference myRef;
    private TextView storeNameTextView;

    private static final String PREF_NAME = "login_pref";
    private static final String KEY_USER_NAME = "user_name"; // 사용자 이름을 저장할 키

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat2);

        //오피스 팝업스토어 클릭시 전환
        ImageView backimageView = findViewById(R.id.chattingroom);
        backimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 액티비티로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(chat2.this, chat.class);
                // Intent를 사용하여 새로운 액티비티로 이동합니다.
                startActivity(intent);
            }
        });

        storeNameTextView = findViewById(R.id.popup_store_name);
        send = findViewById(R.id.sendText);
        EditText_chat = findViewById(R.id.messageEditText);

        // Get the popup store name from the Intent
        String popupStoreName = getIntent().getStringExtra("popup_store_name");
        storeNameTextView.setText(popupStoreName);

        // SharedPreferences에서 사용자 이름을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        nick = sharedPreferences.getString(KEY_USER_NAME, "Unknown User"); // 기본값은 "Unknown User"

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize chat list and adapter
        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, nick);
        mRecyclerView.setAdapter(mAdapter);

        // Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chats").child(popupStoreName);

        // Send button click listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = EditText_chat.getText().toString().trim(); // Get message and trim whitespace
                if (!msg.isEmpty()) {
                    Map<String, String> chat = new HashMap<>();
                    chat.put("nickname", nick);
                    chat.put("message", msg);
                    myRef.push().setValue(chat); // Push message to Firebase
                    EditText_chat.setText(""); // Clear input field
                }
            }
        });

        // Firebase ChildEventListener to listen for new messages
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> chat = (Map<String, String>) dataSnapshot.getValue();
                if (chat != null) {
                    chatList.add(chat);
                    mAdapter.notifyItemInserted(chatList.size() - 1); // Notify adapter of new item
                    mRecyclerView.scrollToPosition(chatList.size() - 1); // Scroll to bottom
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Handle changes to messages if necessary
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle message removal if necessary
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Handle message moves if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors
            }
        });
    }
}
