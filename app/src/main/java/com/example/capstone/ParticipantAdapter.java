package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

//참여자 명단 어댑터
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private List<String> participantsList;

    public ParticipantAdapter(List<String> participantsList) {
        this.participantsList = participantsList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        holder.bind(participantsList.get(position));
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder {

        private TextView participantTextView;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            participantTextView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String participant) {
            participantTextView.setText(participant);
        }
    }
}


