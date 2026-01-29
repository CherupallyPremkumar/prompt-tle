package com.handmade.tle.auth.security.jwt;

import com.handmade.tle.shared.model.User;
import com.handmade.tle.shared.repository.UserRepository;
import com.handmade.tle.auth.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String userId = tokenProvider.getUserIdFromToken(jwt);
                User user = userRepository.findById(userId)
                        .orElse(null);
                if (user != null) {
                    CustomUserDetails userDetails = CustomUserDetails.create(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 1. Try Authorization Header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.debug("Found JWT in Authorization header");
            return bearerToken.substring(7);
        }

        // 2. Try access_token Cookie (for browser)
        String cookieToken = com.handmade.tle.auth.security.util.CookieUtils.getCookie(request, "access_token")
                .map(jakarta.servlet.http.Cookie::getValue)
                .orElse(null);
        if (StringUtils.hasText(cookieToken)) {
            log.debug("Found JWT in access_token cookie");
            return cookieToken;
        }

        // 3. Try token Query Parameter (for initial redirects)
        String queryToken = request.getParameter("token");
        if (StringUtils.hasText(queryToken)) {
            log.debug("Found JWT in query parameter");
            return queryToken;
        }

        return null;
    }
}
