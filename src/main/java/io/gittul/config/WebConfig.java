package io.gittul.config;

import io.gittul.infra.auth.aop.JWTAuthenticator;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JWTAuthenticator jwtAuthenticator;

    public WebConfig(JWTAuthenticator jwtAuthenticator) {
        this.jwtAuthenticator = jwtAuthenticator;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthenticator);
    }
}
