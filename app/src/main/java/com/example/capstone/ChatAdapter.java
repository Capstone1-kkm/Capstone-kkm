package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

//채팅방 어댑터
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Map<String, String>> mChatList;
    private String mNick;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout leftChatBubble;
        public TextView leftNicknameTextView;
        public TextView leftMessageTextView;
        public LinearLayout rightChatBubble;
        public TextView rightNicknameTextView;
        public TextView rightMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            leftChatBubble = itemView.findViewById(R.id.left_chat_bubble);
            leftNicknameTextView = itemView.findViewById(R.id.left_nickname);
            leftMessageTextView = itemView.findViewById(R.id.left_message);
            rightChatBubble = itemView.findViewById(R.id.right_chat_bubble);
            rightNicknameTextView = itemView.findViewById(R.id.right_nickname);
            rightMessageTextView = itemView.findViewById(R.id.right_message);
        }
    }

    public ChatAdapter(List<Map<String, String>> chatList, String nick) {
        mChatList = chatList;
        mNick = nick;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Map<String, String> chat = mChatList.get(position);
        String nickname = chat.get("nickname");
        String message = chat.get("message");

        if (nickname.equals(mNick)) {
            // 메시지가 내 것인 경우
            holder.rightChatBubble.setVisibility(View.VISIBLE);
            holder.leftChatBubble.setVisibility(View.GONE);
            holder.rightNicknameTextView.setText(nickname);
            holder.rightMessageTextView.setText(message);
        } else {
            // 메시지가 상대방의 것인 경우
            holder.leftChatBubble.setVisibility(View.VISIBLE);
            holder.rightChatBubble.setVisibility(View.GONE);
            holder.leftNicknameTextView.setText(nickname);
            holder.leftMessageTextView.setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}