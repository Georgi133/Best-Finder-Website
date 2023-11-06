package softuni.WebFinderserver.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;

    @Value("${mail.username}")
    private String fromMail;

    public EmailService(JavaMailSender javaMailSender, MessageSource messageSource) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
    }

    public void sendEmail(String userEmail, String newPassword, Locale language)  {

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(fromMail);
//        message.setTo(userEmail);
//        message.setSubject("Forgotten Password");
//        message.setText("Your new password is :" + newPassword);
//        javaMailSender.send(message);
        MimeMessage message = javaMailSender.createMimeMessage();
        Locale locale = Locale.ENGLISH;
        if (language.getLanguage().equals("bg")) {
            locale = Locale.forLanguageTag("bg");
        }
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(fromMail, setMessageLang(locale, "app_name"));
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject(setMessageLang(locale, "app_subject"));
            mimeMessageHelper.setText(generateMessageContent(locale, newPassword), true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateMessageContent(Locale locale, String password) {
        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<h2 style=\"color: darkgreen\" text-align=\"center\" border=\"2px solid grey\">" + setMessageLang(locale,"best_wishes_hello") + "</h2>");
        mailContent.append("<h3>" + setMessageLang(locale,"app_newPassword") + "<strong style=\"color: darkblue\">" + password  + "</strong> </h3>");
        mailContent.append("</p> </tr> ");
        mailContent.append("<strong>" + setMessageLang(locale,"best_wishes") + "</strong>");

        return mailContent.toString();
    }
    private String setMessageLang(Locale locale, String code) {
        return messageSource.getMessage(
                code,
                new Object[0],
                locale);
    }


}
