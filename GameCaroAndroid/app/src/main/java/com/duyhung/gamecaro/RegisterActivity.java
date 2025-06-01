package com.duyhung.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etOTP, etConfirmOTP;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etOTP = findViewById(R.id.etOTP);
        etConfirmOTP = findViewById(R.id.etConfirmOTP);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String otp = etOTP.getText().toString().trim();
            String confirmOtp = etConfirmOTP.getText().toString().trim();

            // Kiểm tra email có hợp lệ không
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem OTP và xác nhận OTP có được nhập không
            if (otp.isEmpty() || confirmOtp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập OTP và xác nhận OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem OTP và xác nhận OTP có trùng khớp không
            if (!otp.equals(confirmOtp)) {
                Toast.makeText(this, "OTP và xác nhận OTP không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi email thông báo đăng ký thành công
            EmailSender.sendEmail(email, "Đăng ký thành công", "Bạn đã đăng ký tài khoản thành công game cờ Caro");
            Toast.makeText(this, "Đăng ký thành công, email thông báo đã được gửi", Toast.LENGTH_LONG).show();

            // Chuyển về LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("registeredEmail", email);
            startActivity(intent);
            finish();
        });
    }
}