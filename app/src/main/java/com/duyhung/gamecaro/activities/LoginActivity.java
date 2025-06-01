package com.duyhung.gamecaro.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duyhung.gamecaro.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etOTP;
    private Button btnLogin, btnGuest;
    public static boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etOTP = findViewById(R.id.etOTP);
        btnLogin = findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);

        // Thiết lập TextView cho thông báo
        TextView tvRegisterPrompt = findViewById(R.id.tvRegisterPrompt);
        String fullText = "Lưu ý: Bạn chỉ có thể lưu bàn cờ và điểm số với người chơi khi đăng nhập game. Nếu chưa có tài khoản, đăng ký ngay.";
        SpannableString spannableString = new SpannableString(fullText);

        // Áp dụng kiểu in đậm cho "Lưu ý:"
        int startBold = fullText.indexOf("Lưu ý:");
        int endBold = startBold + "Lưu ý:".length();
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startBold, endBold, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Tìm vị trí của từ "đăng ký"
        int startLink = fullText.indexOf("đăng ký");
        int endLink = startLink + "đăng ký".length();

        // Tạo ClickableSpan cho "đăng ký"
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        };

        // Áp dụng ClickableSpan
        spannableString.setSpan(clickableSpan, startLink, endLink, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set spannable string cho TextView
        tvRegisterPrompt.setText(spannableString);
        tvRegisterPrompt.setMovementMethod(LinkMovementMethod.getInstance());

        // Nhận email và OTP từ RegisterActivity (nếu có)
        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("registeredEmail");
            if (email != null) {
                etEmail.setText(email);
            }
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String otp = etOTP.getText().toString().trim();

            if (email.isEmpty() || otp.length() != 6) {
                Toast.makeText(this, "Vui lòng nhập email và OTP hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra OTP (giả lập, thực tế nên dùng backend)
            String expectedOTP = intent.getStringExtra("otp");
            if (otp.equals(expectedOTP)) {
                isLoggedIn = true;
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ModeSelectionActivity.class));
                finish();
            } else {
                Toast.makeText(this, "OTP không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        btnGuest.setOnClickListener(v -> {
            isLoggedIn = false;
            Toast.makeText(this, "Chơi với tư cách Guest", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ModeSelectionActivity.class));
            finish();
        });
    }
}