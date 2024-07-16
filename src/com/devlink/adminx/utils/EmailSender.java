package com.devlink.adminx.utils;

import com.devlink.adminx.model.Employee;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private final Session session;
    private final String from;

    public EmailSender() {
        Properties envs = PropsUtils.loadProperties(Environment.PROPS_EMAIL_PATH);

        final String username = envs.getProperty("username");
        final String password = envs.getProperty("password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        from = username;
    }

    public boolean sendEmail(Employee employee, String subject, String content) {
        try {
            String styles = """
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        width: 80%;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #ffffff;
                    }
                    h1, p {
                        margin-bottom: 20px;
                    }
                    .footer {
                        margin-top: 20px;
                        padding-top: 10px;
                        border-top: 1px solid #f5efef;
                        text-align: center;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-bottom: 20px;
                    }
                    th, td {
                        padding: 10px;
                        text-align: left;
                        border: 1.6px solid #338ed7;
                    }
                    th {
                        background-color: #338ed7;
                        font-weight: bold;
                        color: white;
                        text-align: center;
                    }
                    td {
                        background-color: #ffffff;
                        text-align: center;
                    }
                    """;

            String htmlTemplate = String.format(
                    """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>%s</style>
                    </head>
                    <body>
                        <div class="container">
                            <header>
                                <h1>!Hola %s!</h1>
                            </header>
                            <main>
                                <p>
                                    Adjunto encontrarás los detalles de tus pagos correspondientes.
                                    Por favor, revisa la información y contáctanos si tienes alguna pregunta o necesitas 
                                    asistencia adicional.
                                </p>
                                %s
                            </main>
                            <footer class="footer">
                                <p>Gracias.</p>
                            </footer>
                        </div>
                    </body>
                    </html>
                    """, styles, employee.getNames(), content);

            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(employee.getEmail()));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(htmlTemplate, "text/html;charset=utf-8");

            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}

