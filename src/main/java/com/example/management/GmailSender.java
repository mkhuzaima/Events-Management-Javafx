package com.example.management;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.Properties;

public class GmailSender {
    private String email;
    private String password;
    private Session session;

    public GmailSender() {
        // generate app password by
        // enabling 2-step verification
        // then going to https://myaccount.google.com/apppasswords
        this.email = "mkhuzaima78@gmail.com"; // update email accordingly
        this.password = "***********";

        // Set up Gmail SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Authenticate with Gmail and create a new email session
        session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
    }

    public void sendEmail(ArrayList<String> recipients, String subject, String message) throws MessagingException {
        // Create a new email message
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress(email));
        System.out.println("inside send email");

        // Add recipients to the email message
        for (String recipient : recipients) {
            System.out.println("recipient = " + recipient);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));
        }

        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        // Send the email
        Transport.send(emailMessage);

        System.out.println("Email sent successfully.");
    }
}
