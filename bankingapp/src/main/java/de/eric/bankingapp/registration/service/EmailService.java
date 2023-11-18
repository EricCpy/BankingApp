package de.eric.bankingapp.registration.service;

import de.eric.bankingapp.user.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Async
    public void sendVerificationMail(User user, String verificationUrl)  throws MessagingException, UnsupportedEncodingException {
        String subject = "Account Verification";
        String senderName = "Banking App";
        String mailContent = "<p> Hello Mr. " + user.getLastName() + ", </p>"+
                "Please, click on the link below to complete your registration.</p>"+
                "<a href=\"" +verificationUrl+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Banking App";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("banking.app@bank.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
