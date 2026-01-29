package com.handmade.tle.auth.configuration;

import com.handmade.tle.auth.security.jwt.JwtAuthenticationFilter;
import com.handmade.tle.auth.security.jwt.JwtTokenProvider;
import com.handmade.tle.auth.security.oauth2.CustomOAuth2UserService;
import com.handmade.tle.auth.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.handmade.tle.auth.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.handmade.tle.auth.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.handmade.tle.auth.security.oauth2.CustomOidcUserService;
import com.handmade.tle.shared.repository.RoleRepository;
import com.handmade.tle.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${app.oauth2.authorized-redirect-uris}")
    private List<String> authorizedRedirectUris;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtSecret, accessTokenExpiration, refreshTokenExpiration);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                jwtTokenProvider(),
                cookieAuthorizationRequestRepository(),
                authorizedRedirectUris);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository());
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository, roleRepository);
    }

    @Bean
    public CustomOidcUserService customOidcUserService() {
        return new CustomOidcUserService(userRepository, roleRepository);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider(), userRepository);
    }
}
