package com.example.letsplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<Integer> imageList;
    private final OnImageClickListener listener;

    public MenuAdapter(List<Integer> imageList, OnImageClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.imageView.setImageResource(imageList.get(position));
        holder.imageView.setOnClickListener(v -> listener.onImageClicked(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size(); // 아이템 개수를 3개로 유지
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menuImage);
        }
    }

    public interface OnImageClickListener {
        void onImageClicked(int position);
    }
}
