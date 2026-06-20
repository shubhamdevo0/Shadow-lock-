package service;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendOTPService {
    public static void sendOTP(String email, String genOTP) {
        // Recipient's email ID needs to be mentioned.
        String to = email;

        // Wahi email jise tumne SendGrid par single sender verify kiya hai
        // ⚠️ FOR GITHUB PUBLISHING: USE ENVIRONMENT VARIABLES
        String from = System.getenv("SENDGRID_FROM_EMAIL");
        if (from == null || from.isEmpty()) {
            from = "YOUR_SENDER_EMAIL_HERE"; // Fallback for local development only
        }

        // Google ki jagah SendGrid ka live host relay server
        String host = "smtp.sendgrid.net";

        // 🛠️ JavaFX aur standalone execution ke liye standalone instance use karna safe hai
        Properties properties = new Properties();

        // MAIL SERVER KO SET KARNA (SendGrid Specific Setup)
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Clean dynamic inbox delivery ke liye Port 587
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Mandatory security layer for SendGrid
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Handshake fix for compile environment


        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // SendGrid standard integration parameter: Username hamesha "apikey" hi rahega
                // Password box mein apni original SG. waali key paste karo
                String sendGridApiKey = System.getenv("SENDGRID_API_KEY");
                if (sendGridApiKey == null || sendGridApiKey.isEmpty()) {
                    sendGridApiKey = "YOUR_SENDGRID_API_KEY_HERE"; // Fallback for local development only
                }
                return new PasswordAuthentication("apikey", sendGridApiKey);
            }
        });

        // Used to debug SMTP issues in IntelliJ console
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header (Sender Display Name)
            message.setFrom(new InternetAddress(from, "S.H.U.B.H.A.M Security"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field (Cleaned to bypass Google Inbound filters)
            message.setSubject("S.H.U.B.H.A.M Verification Code", "UTF-8");

            // Now set the actual message (Anti-spam generic text format)
            String emailContent = "Hello,\n\n"
                    + "Your application passkey token is: " + genOTP + "\n\n"
                    + "Please enter this code in your window workspace to continue.\n\n"
                    + "Regards,\n"
                    + "Support Desk";

            message.setText(emailContent, "UTF-8");

            System.out.println("sending...");

            // Critical Fix: Thread context handling taaki jpackage packaging runtime mein mail dependency load ho sake
            Thread.currentThread().setContextClassLoader(javax.mail.Session.class.getClassLoader());

            // Send message
            Transport.send(message);
            System.out.println("✔ AUTH CODE SENT :: READY FOR ACCESS");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}