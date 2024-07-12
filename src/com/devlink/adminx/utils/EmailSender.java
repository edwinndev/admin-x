package com.devlink.adminx.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private final Session session;
    private final String from;

    public EmailSender() {
        Properties envs = PropsUtils.loadProperties(Var.PROPS_EMAIL);

        final String username = envs.getProperty("username");
        final String password = envs.getProperty("password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        from = username;
    }

    public boolean sendEmail(String to, String subject, String content) {
        try {
            String htmlTemplate = "<html>" +
                    "<body>" +
                    "<p>Revisa tu reporte mensual.</p>" +
                    content +
                    "</body>" +
                    "</html>";


            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(htmlTemplate, "text/html;charset=utf-8");

            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}

