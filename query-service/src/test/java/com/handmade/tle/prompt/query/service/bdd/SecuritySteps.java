package com.handmade.tle.prompt.query.service.bdd;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import com.handmade.tle.auth.security.jwt.JwtTokenProvider;
import com.handmade.tle.auth.security.CustomUserDetails;
import com.handmade.tle.shared.model.User;
import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.RoleName;
import com.handmade.tle.shared.repository.UserRepository;
import com.handmade.tle.shared.repository.RoleRepository;
import java.util.Set;
import org.chenile.cucumber.CukesContext;
import java.util.HashMap;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.util.Collections;

public class SecuritySteps {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Given("the security is enabled")
    public void the_security_is_enabled() {
        System.setProperty("chenile.security.ignore", "false");
    }

    @Given("I am logged in as {string} with role {string}")
    public void i_am_logged_in_as_with_role(String username, String roleName) {
        User user = userRepository.findById(username).orElseGet(() -> {
            User u = new User();
            u.setId(username);
            return u;
        });
        user.setEmail(username);
        user.setFullName(username);

        RoleName roleNameEnum = RoleName.valueOf(roleName);
        Role role = roleRepository.findByName(roleNameEnum)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(roleNameEnum);
                    r.setAcls(Collections.emptySet());
                    return roleRepository.save(r);
                });
        user.setRoles(Set.of(role));

        // Save the user to the database instead of mocking
        userRepository.save(user);

        CustomUserDetails userDetails = CustomUserDetails.create(user);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = tokenProvider.generateAccessToken(authentication);

        CukesContext context = CukesContext.CONTEXT;
        Map<String, String> headers = context.get("headers");
        if (headers == null) {
            headers = new HashMap<>();
            context.set("headers", headers);
        }
        headers.put("Authorization", "Bearer " + token);
        headers.put("x-tenant-id", "Handmade");
    }
}
