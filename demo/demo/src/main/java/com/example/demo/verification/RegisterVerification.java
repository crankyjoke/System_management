package com.example.demo.verification;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class RegisterVerification {

    // Generate a 4-digit random number (ensuring it's always 4 digits)
    private int generateNumberCode() {
        return 1000 + new Random().nextInt(9000); // Ensures number is between 1000-9999
    }

    // Method to verify the entered code
    public boolean verifyCode(int correctCode, int userInput) {
        return correctCode == userInput;
    }

    // Method to send verification email
    public boolean sendVerification(String recipientEmail) {
        int verificationCode = generateNumberCode();

        // SMTP Server Configuration
        final String senderEmail = "your-email@example.com"; // Replace with your email
        final String senderPassword = "your-password"; // Replace with your email password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change if using another provider
        props.put("mail.smtp.port", "587"); // Port for TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS security

        // Creating a new email session
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
