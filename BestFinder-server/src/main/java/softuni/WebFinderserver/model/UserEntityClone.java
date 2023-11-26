package softuni.WebFinderserver.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import softuni.WebFinderserver.model.entities.UserEntity;
import softuni.WebFinderserver.model.views.BaseView;
import softuni.WebFinderserver.model.views.FullNameView;


public class UserEntityClone {


    public static String getUserEmail () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails principal1 = (UserDetails) principal;
        return principal1.getUsername();
    }

    public static FullNameView getFullName () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserEntity principal1 = (UserEntity) principal;
        FullNameView view = new FullNameView();
        view.setFullName(principal1.getFullName());
        return view;
    }

//        public static String getUserEmail () {
//            return null;
//    }


}
