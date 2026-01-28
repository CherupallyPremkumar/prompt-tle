package com.handmade.tle.prompt.query.service.bdd;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import com.handmade.tle.auth.security.jwt.JwtTokenProvider;
import com.handmade.tle.auth.security.CustomUserDetails;
import com.handmade.tle.shared.model.User;
import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.RoleName;
import com.handmade.tle.shared.repository.UserRepository;
import java.util.Set;
import org.chenile.cucumber.CukesContext;
import java.util.HashMap;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import java.util.Optional;
import java.util.Collections;

public class SecuritySteps {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Given("the security is enabled")
    public void the_security_is_enabled() {
        System.setProperty("chenile.security.ignore", "false");
    }

    @Given("I am logged in as {string} with role {string}")
    public void i_am_logged_in_as_with_role(String username, String roleName) {
        User user = new User();
        user.setId("user1");
        user.setEmail(username);
        user.setFullName(username);

        Role role = new Role();
        role.setName(RoleName.valueOf(roleName));
        role.setAcls(Collections.emptySet());
        user.setRoles(Set.of(role));

        // Stub the repository
        Mockito.when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

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
    }
}
