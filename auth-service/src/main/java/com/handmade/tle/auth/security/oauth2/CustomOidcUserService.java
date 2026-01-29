package com.handmade.tle.auth.security.oauth2;

import com.handmade.tle.shared.repository.RoleRepository;
import com.handmade.tle.shared.repository.UserRepository;
import com.handmade.tle.shared.model.AuthProvider;
import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.RoleName;
import com.handmade.tle.shared.model.User;
import com.handmade.tle.auth.security.CustomUserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

import java.util.Set;

public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOidcUserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(new OAuth2Error("processing_error"), ex.getMessage(), ex);
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oidcUser.getAttributes());

        if (!StringUtils.hasText(userInfo.getEmail())) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"),
                    "Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser -> {
                    if (!existingUser.getProvider().equals(AuthProvider.valueOf(registrationId.toUpperCase()))) {
                        throw new OAuth2AuthenticationException(new OAuth2Error("provider_mismatch"),
                                "Please use " + existingUser.getProvider() + " to login.");
                    }
                    return updateExistingUser(existingUser, userInfo);
                })
                .orElseGet(() -> registerNewUser(userRequest, userInfo));

        return CustomUserDetails.create(user, oidcUser.getAttributes(), oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    private User registerNewUser(OidcUserRequest userRequest, OAuth2UserInfo userInfo) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RoleName defaultRoleName = AuthProvider.GOOGLE.toString().equalsIgnoreCase(registrationId)
                ? RoleName.ROLE_GOOGLE
                : RoleName.ROLE_USER;

        Role role = roleRepository.findByName(defaultRoleName)
                .orElseThrow(() -> new RuntimeException(defaultRoleName + " not found"));

        User user = User.builder()
                .fullName(userInfo.getName())
                .email(userInfo.getEmail())
                .pictureUrl(userInfo.getImageUrl())
                .provider(AuthProvider.valueOf(registrationId.toUpperCase()))
                .providerId(userInfo.getId())
                .emailVerified(true)
                .roles(Set.of(role))
                .username(userInfo.getEmail())
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo userInfo) {
        existingUser.setFullName(userInfo.getName());
        existingUser.setPictureUrl(userInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
