package com.handmade.tle.auth.security.oauth2;

import com.handmade.tle.shared.repository.RoleRepository;
import com.handmade.tle.shared.repository.UserRepository;
import com.handmade.tle.shared.model.AuthProvider;
import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.RoleName;
import com.handmade.tle.shared.model.User;
import com.handmade.tle.auth.security.CustomUserDetails;
import com.handmade.tle.auth.security.oauth2.OAuth2UserInfo;
import com.handmade.tle.auth.security.oauth2.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(new OAuth2Error("processing_error"), ex.getMessage(), ex);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauth2User.getAttributes());

        if (!StringUtils.hasText(userInfo.getEmail())) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"),
                    "Email not found from OAuth2 provider");
        }

        if (!userInfo.isEmailVerified()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_verified"),
                    "Email not verified by provider");
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

        return CustomUserDetails.create(user, oauth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RoleName defaultRoleName = RoleName.ROLE_USER;

        if (AuthProvider.GOOGLE.toString().equalsIgnoreCase(registrationId)) {
            defaultRoleName = RoleName.ROLE_GOOGLE;
        }

        final RoleName roleName = defaultRoleName;

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " not found"));

        User user = User.builder()
                .fullName(userInfo.getName())
                .email(userInfo.getEmail())
                .pictureUrl(userInfo.getImageUrl())
                .provider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .providerId(userInfo.getId())
                .emailVerified(true)
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