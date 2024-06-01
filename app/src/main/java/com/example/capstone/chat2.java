package com.example.capstone;

import android.app.Dialog;
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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat2 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Map<String, String>> chatList;
    private String nick;

    private EditText EditText_chat;
    private Button send;
    private DatabaseReference myRef;
    private TextView storeNameTextView;
    private static final String PREF_NAME = "login_pref";
    private static final String KEY_USER_NAME = "user_name";
    private boolean isLeaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat2);

        ImageView backimageView = findViewById(R.id.chattingroom);
        backimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat2.this, chat.class);
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
        String sanitizedPopupStoreName = sanitizeFirebasePath(popupStoreName);  // 경로를 정리
        myRef = database.getReference("chats").child(sanitizedPopupStoreName);

        // Send button click listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = EditText_chat.getText().toString().trim();
                if (!msg.isEmpty()) {
                    Map<String, String> chat = new HashMap<>();
                    chat.put("nickname", nick);
                    chat.put("message", msg);
                    myRef.child("messages").push().setValue(chat);
                    EditText_chat.setText("");
                }
            }
        });

        // Firebase ChildEventListener to listen for new messages
        myRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> chat = (Map<String, String>) dataSnapshot.getValue();
                if (chat != null) {
                    chatList.add(chat);
                    mAdapter.notifyItemInserted(chatList.size() - 1);
                    mRecyclerView.scrollToPosition(chatList.size() - 1);
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

        // Add user to chat room
        addUserToChatRoom();

        // Set up listener for the participant list image
        ImageView participantListImageView = findViewById(R.id.list);
        participantListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantDialog();
            }
        });
    }

    //파이어베이스 데이터베이스에서 허용되지 않는 문자를 "_"로 대체
    private String sanitizeFirebasePath(String path) {
        return path.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }

    private void showParticipantDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_participants);

        RecyclerView participantsRecyclerView = dialog.findViewById(R.id.participants2);
        participantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> participantList = new ArrayList<>();
        ParticipantAdapter participantAdapter = new ParticipantAdapter(participantList);
        participantsRecyclerView.setAdapter(participantAdapter);

        // Firebase에서 참여자 목록을 가져와서 RecyclerView에 업데이트
        myRef.child("participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String participantName = snapshot.getKey();
                    participantList.add(participantName);
                }
                participantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors
            }
        });

        Button leaveButton = dialog.findViewById(R.id.leave);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLeaving = true;
                removeUserFromChatRoom();
                dialog.dismiss();

                // 채팅 목록에서 항목 제거를 위해 인텐트 설정
                Intent resultIntent = new Intent();
                resultIntent.putExtra("remove_chat_item", storeNameTextView.getText().toString());
                setResult(RESULT_OK, resultIntent);

                finish(); // 채팅방 나가기
            }
        });

        dialog.show();
    }

    private void addUserToChatRoom() {
        // Add the user to the participants node
        myRef.child("participants").child(nick).setValue(true);
    }

    private void removeUserFromChatRoom() {
        // Remove the user from the participants node
        myRef.child("participants").child(nick).removeValue();

        // 채팅방을 삭제하는 부분을 제거하여 메시지가 남아있도록 합니다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLeaving) {
            removeUserFromChatRoom();
        }
    }
}
