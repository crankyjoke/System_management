package com.example.demo.verification;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class RegisterVerification {

    private int generateNumberCode() {
        return 1000 + new Random().nextInt(9000);
    }

    public boolean verifyCode(int correctCode, int userInput) {
        return correctCode == userInput;
    }

    public boolean sendVerification(String recipientEmail) {
        int verificationCode = generateNumberCode();

        final String senderEmail = "your-email@example.com";
        final String senderPassword = "your-password";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Verification Code");
            message.setText("Your verification code is: " + verificationCode);


            Transport.send(message);

            System.out.println("📧 Verification code sent successfully to " + recipientEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
