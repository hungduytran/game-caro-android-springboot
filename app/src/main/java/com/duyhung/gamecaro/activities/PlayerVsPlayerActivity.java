package com.duyhung.gamecaro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duyhung.gamecaro.R;

public class PlayerVsPlayerActivity extends AppCompatActivity {

    private EditText etPlayer1, etPlayer2;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_player);

        etPlayer1 = findViewById(R.id.etPlayer1);
        etPlayer2 = findViewById(R.id.etPlayer2);
        btnStartGame = findViewById(R.id.btnStartGame);

        // Điền sẵn tên người chơi 1 nếu đã đăng nhập
        if (LoginActivity.isLoggedIn) {
            String displayName = "TênNgườiDùng"; // Thay bằng logic lấy tên từ DB hoặc SharedPreferences
            etPlayer1.setText(displayName);
        }

        btnStartGame.setOnClickListener(v -> {
            String player1 = etPlayer1.getText().toString().trim();
            String player2 = etPlayer2.getText().toString().trim();
            if (player1.isEmpty() || player2.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên cả hai người chơi", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", "two_players");
            intent.putExtra("player1", player1);
            intent.putExtra("player2", player2);
            startActivity(intent);
        });
    }
}