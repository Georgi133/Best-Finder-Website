package softuni.WebFinderserver.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class ForgottenPasswordEmailMessageEvent extends ApplicationEvent {


    private String email;
    private String password;
    private Locale locale;

    public ForgottenPasswordEmailMessageEvent(Object source) {
        super(source);
    }

    public ForgottenPasswordEmailMessageEvent setEmail(String email) {
        this.email = email;
        return this;
    }

    public ForgottenPasswordEmailMessageEvent setPassword(String password) {
        this.password = password;
        return this;
    }

    public ForgottenPasswordEmailMessageEvent setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }


}
