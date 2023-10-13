//package softuni.WebFinderserver.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import softuni.WebFinderserver.model.dtos.ErrorDto;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//
//    private static final ObjectMapper MAPPER = new ObjectMapper();
//
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException)
//            throws IOException, ServletException {
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//
//        MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Unauthotized path"));
//    }
//}
