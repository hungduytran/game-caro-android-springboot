package com.duyhung.gamecaro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        if (LoginActivity.isLoggedIn) {
            startActivity(new Intent(MainActivity.this, ModeSelectionActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish(); // Kết thúc MainActivity để không quay lại
    }
}