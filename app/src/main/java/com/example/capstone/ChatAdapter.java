package com.example.capstone;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

//채팅화면 recyclerview 어댑터
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Map<String, String>> mChatList;
    private String mNick;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView nicknameView;
        public TextView messageView;

        @SuppressLint("WrongViewCast")
        public ChatViewHolder(View v) {
            super(v);
            nicknameView = v.findViewById(R.id.nicknameTextView);
            messageView = v.findViewById(R.id.messageTextView);
        }
    }

    public ChatAdapter(List<Map<String, String>> chatList, String nick) {
        mChatList = chatList;
        mNick = nick;
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Map<String, String> chat = mChatList.get(position);
        holder.nicknameView.setText(chat.get("nickname"));
        holder.messageView.setText(chat.get("message"));
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
