package softuni.WebFinderserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softuni.WebFinderserver.interceptor.CheckValidityOfUserIpInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    private final CheckValidityOfUserIpInterceptor interceptorTest;

    public InterceptorConfig(CheckValidityOfUserIpInterceptor interceptorTest) {
        this.interceptorTest = interceptorTest;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorTest);
        WebMvcConfigurer.super.addInterceptors(registry);
    }


}
