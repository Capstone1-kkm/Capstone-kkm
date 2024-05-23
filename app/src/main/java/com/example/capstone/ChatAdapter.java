package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//리사이클러뷰 어댑터
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<chatdata> chatList;
    private String currentUser;

    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    public ChatAdapter(List<chatdata> chatList, String currentUser) {
        this.chatList = chatList;
        this.currentUser = currentUser;
    }

    @Override

    public int getItemViewType(int position) {
        chatdata chat = chatList.get(position);
        if (chat.getNickname().equals(currentUser)) {
            return VIEW_TYPE_MY_MESSAGE;
        } else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
            return new OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatdata chat = chatList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_MY_MESSAGE) {
            ((MyMessageViewHolder) holder).bind(chat);
        } else {
            ((OtherMessageViewHolder) holder).bind(chat);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private TextView nicknameTextView;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
        }

        public void bind(chatdata chat) {
            messageTextView.setText(chat.getMsg());
            nicknameTextView.setText(chat.getNickname());
        }
    }

    public class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private TextView nicknameTextView;

        public OtherMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
        }

        public void bind(chatdata chat) {
            messageTextView.setText(chat.getMsg());
            nicknameTextView.setText(chat.getNickname());
        }
    }
}
