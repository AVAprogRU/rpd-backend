package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.suai.department43.loutsker.rpddrafter.exception.business.NotificationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final String author;

    @Autowired
    public EmailService(@Value("${spring.mail.username}") String author,
                        JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.author = author;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(author);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new NotificationFailedException(to);
        }

    }
}
