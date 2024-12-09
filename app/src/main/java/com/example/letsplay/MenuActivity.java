package com.example.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            onBackPressed(); // 이전 화면으로 이동
        });
        // 이미지 리스트 생성
        imageList = new ArrayList<>();
        imageList.add(R.drawable.another_roleplaying);
        imageList.add(R.drawable.finish_roleplaying);
        imageList.add(R.drawable.continue_roleplaying);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MenuAdapter(imageList, this::onImageClicked);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 스크롤 이벤트로 가운데 버튼 확대 및 끝에 도달 시 처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scaleCenterItem();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // 스크롤 멈췄을 때, 끝에 도달하면 첫 번째 버튼을 1.2배
                if (!recyclerView.canScrollHorizontally(-1)) { // 왼쪽 끝
                    resetItemScales();
                    scaleItemAtPosition(0); // 첫 번째 버튼 확대
                }
                if (!recyclerView.canScrollHorizontally(1)) { // 오른쪽 끝
                    resetItemScales();
                    scaleItemAtPosition(2); // 마지막 버튼 확대
                }
            }
        });
    }

    private void scaleCenterItem() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int centerPosition = layoutManager.findFirstVisibleItemPosition() + 1;

        resetItemScales();
        scaleItemAtPosition(centerPosition);
    }

    private void resetItemScales() {
        // 모든 아이템의 크기를 1배로 초기화
        for (int i = 0; i < imageList.size(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
        }
    }

    private void scaleItemAtPosition(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            viewHolder.itemView.setScaleX(1.2f);
            viewHolder.itemView.setScaleY(1.2f);
        }
    }

    private void onImageClicked(int position) {
        Intent intent;
        switch (position) {
            case 0: // Another Roleplaying
                intent = new Intent(MenuActivity.this, RewardsMainPageActivity.class);
                break;
            case 1: // Finish Roleplaying
                intent = new Intent(MenuActivity.this, EndingActivity.class);
                break;
            case 2: // Continue Roleplaying
                intent = new Intent(MenuActivity.this, RoleplayingBackgroundActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
