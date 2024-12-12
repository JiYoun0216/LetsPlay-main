package com.example.letsplay;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.os.Handler;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;  // 사용자 말풍선 (오른쪽)
    private static final int VIEW_TYPE_GPT = 2;   // GPT 말풍선 (왼쪽)

    private final List<ChatMessage> messages;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser() ? VIEW_TYPE_USER : VIEW_TYPE_GPT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_gpt, parent, false);
            return new GptViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
            if (message.isPending()) {
                startTypingAnimation((UserViewHolder) holder);
            }
        } else if (holder instanceof GptViewHolder) {
            ((GptViewHolder) holder).bind(message);
        }
    }

    private void startTypingAnimation(UserViewHolder holder) {
        holder.stopTypingAnimation(); // 기존 애니메이션 중지

        Runnable typingRunnable = new Runnable() {
            int dotCount = 0;

            @Override
            public void run() {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    StringBuilder dots = new StringBuilder();
                    for (int i = 0; i < dotCount; i++) {
                        dots.append(".");
                    }
                    holder.messageText.setText(dots.toString());
                    dotCount = (dotCount + 1) % 4;
                    handler.postDelayed(this, 1000); // 1초 간격
                }
            }
        };
        holder.setTypingRunnable(typingRunnable);
        handler.post(typingRunnable);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView messageTime;
        private Runnable typingRunnable;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            messageTime.setText(message.getTimestamp());
        }

        void setTypingRunnable(Runnable typingRunnable) {
            this.typingRunnable = typingRunnable;
        }

        void stopTypingAnimation() {
            if (typingRunnable != null) {
                messageText.removeCallbacks(typingRunnable);
                typingRunnable = null;
            }
        }
    }

    static class GptViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView messageTime;

        GptViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            messageTime.setText(message.getTimestamp());
        }
    }
}
