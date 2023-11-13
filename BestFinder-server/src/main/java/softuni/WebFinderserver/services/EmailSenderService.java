package softuni.WebFinderserver.services;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softuni.WebFinderserver.model.entities.ForgottenPasswordEmailMessageEvent;
import softuni.WebFinderserver.services.exceptions.user.InvalidEmailDataException;

import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderService {

    private final EmailService emailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    public EmailSenderService(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener(ForgottenPasswordEmailMessageEvent.class)
    public void onForgottenPasswordSendEmail(ForgottenPasswordEmailMessageEvent evt) {
        try {
            emailService.sendEmail(evt.getEmail(),evt.getPassword(),evt.getLocale());
            LOGGER.info("Email was send to " + evt.getEmail());
        } catch (MessagingException e) {
            LOGGER.info("Email error");
            throw new InvalidEmailDataException("Error when sending new password, try again", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedEncodingException e) {
            LOGGER.info("Email error");
            throw new InvalidEmailDataException("Error when sending new password, try again", HttpStatus.BAD_REQUEST);
        }

    }


}
