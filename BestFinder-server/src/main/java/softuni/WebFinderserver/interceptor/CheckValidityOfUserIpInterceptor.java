package softuni.WebFinderserver.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import softuni.WebFinderserver.services.businessServicesInt.BlackListService;
import softuni.WebFinderserver.services.exceptions.user.UserException;


@Component
@Slf4j
public class CheckValidityOfUserIpInterceptor implements HandlerInterceptor {

    private final BlackListService blackListService;

    public CheckValidityOfUserIpInterceptor(BlackListService blackListService) {
        this.blackListService = blackListService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ipAddress = getIpAddress(request);
        if(blackListService.isBlackListed(ipAddress)) {
            log.info("Blacklisted user tried to make request!!");
            throw new UserException("You are black listed from the website", HttpStatus.NOT_ACCEPTABLE);
        }
        return true;
    }
    public String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if(xForwardedForHeader != null && !xForwardedForHeader.equals("unknown")) {
            ipAddress = xForwardedForHeader;
        }
        if(ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
