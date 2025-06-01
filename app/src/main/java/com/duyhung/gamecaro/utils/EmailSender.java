package com.duyhung.gamecaro.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private static final String EMAIL = "4hprotein@gmail.com"; // Thay bằng email của bạn
    private static final String PASSWORD = "fgjl mwmv xgvj ywij"; // Thay bằng mật khẩu ứng dụng (App Password) của bạn

    public static void sendEmail(String to, String subject, String body) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL, PASSWORD);
                    }
                });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(EMAIL));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                    message.setSubject(subject);
                    message.setText(body);
                    Transport.send(message);
                    Log.d("EmailSender", "Email sent successfully to " + to);
                } catch (MessagingException e) {
                    Log.e("EmailSender", "Error sending email", e);
                }
                return null;
            }
        }.execute();
    }
}