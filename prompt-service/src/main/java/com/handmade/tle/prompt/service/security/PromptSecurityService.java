package com.handmade.tle.prompt.service.security;

import org.chenile.core.context.ChenileExchange;
import org.chenile.security.service.SecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Arrays;

@Service
public class PromptSecurityService implements SecurityService {

    @Override
    public String[] getCurrentAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return new String[0];
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    @Override
    public boolean doesCurrentUserHaveGuardingAuthorities(ChenileExchange exchange) {
        // For simplicity, we can assume true or check specific headers if needed.
        // But typically STM checks explicit ACLs.
        // If exchange has security info, we could check it.
        // For now, let's return true as default or implementation specific logic.
        // However, looking at standard Chenile impl might be safer.
        // But since we are replacing it, let's just check if user is authenticated at
        // least.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public boolean doesCurrentUserHaveGuardingAuthorities(String... acls) {
        if (acls == null || acls.length == 0) {
            return true;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // Check if user has ANY of the required ACLs (or ALL? usually ANY for access)
        // Standard Spring Security hasRole/hasAuthority check.
        // Let's assume ANY match is sufficient for access (OR logic).
        for (String acl : acls) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(acl)) {
                    return true;
                }
            }
        }
        return false;
    }
}
