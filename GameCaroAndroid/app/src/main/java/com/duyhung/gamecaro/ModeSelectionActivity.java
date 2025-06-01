package com.duyhung.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ModeSelectionActivity extends AppCompatActivity {

    private Button btnTwoPlayers, btnPlayWithAIEasy, btnPlayWithAIHard, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        btnTwoPlayers = findViewById(R.id.btnTwoPlayers);
        btnPlayWithAIEasy = findViewById(R.id.btnPlayWithAIEasy);
        btnPlayWithAIHard = findViewById(R.id.btnPlayWithAIHard);
        btnHistory = findViewById(R.id.btnHistory);

        // Hiển thị nút "Lịch sử chơi" nếu đã đăng nhập
        if (LoginActivity.isLoggedIn) {
            btnHistory.setVisibility(View.VISIBLE);
        } else {
            btnHistory.setVisibility(View.GONE);
        }

        btnTwoPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", GameActivity.MODE_TWO_PLAYERS);
            startActivity(intent);
        });

        btnPlayWithAIEasy.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", GameActivity.MODE_PLAY_WITH_AI);
            intent.putExtra("difficulty", "easy");
            startActivity(intent);
        });

        btnPlayWithAIHard.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", GameActivity.MODE_PLAY_WITH_AI);
            intent.putExtra("difficulty", "hard");
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            // TODO: Triển khai hoạt động lịch sử chơi
            // Intent intent = new Intent(this, PlayHistoryActivity.class);
            // startActivity(intent);
        });
    }
}