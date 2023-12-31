package softuni.WebFinderserver.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class CorsConfig {

    @Bean
    public FilterRegistrationBean corsFilter () {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000","http://localhost:8080", "https://web-finder.azurewebsites.net", "https://lb-spring-app-webfinder.azuremicroservices.io"));
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        configuration.addAllowedHeader("X-Forwarded-For");
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name()
        ));
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        bean.setOrder(-102);
        return bean;
    }

}
