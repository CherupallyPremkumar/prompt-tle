package com.handmade.tle.prompt.query.service.bdd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.mockito.Mockito;

import com.handmade.tle.shared.repository.PromptRepository;
import com.handmade.tle.shared.repository.UserRepository;
import com.handmade.tle.shared.repository.RoleRepository;
import com.handmade.tle.shared.repository.RefreshTokenRepository;
import com.handmade.tle.shared.repository.GoogleAccountRepository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.chenile.configuration",
        "org.chenile.configuration.controller",
        "org.chenile.configuration.query.service",
        "com.handmade.tle.**.configuration",
        "com.handmade.tle.auth",
        "com.handmade.tle.auth.security"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*ChenileSecurityConfiguration"))
@ActiveProfiles(value = "unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

    @Bean
    public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Bean
    @Primary
    public PromptRepository promptRepository() {
        return Mockito.mock(PromptRepository.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public RoleRepository roleRepository() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    @Primary
    public RefreshTokenRepository refreshTokenRepository() {
        return Mockito.mock(RefreshTokenRepository.class);
    }

    @Bean
    @Primary
    public GoogleAccountRepository googleAccountRepository() {
        return Mockito.mock(GoogleAccountRepository.class);
    }

    @Bean
    @Primary
    public com.handmade.tle.storage.StorageProvider storageProvider() {
        return Mockito.mock(com.handmade.tle.storage.StorageProvider.class);
    }

    @Bean
    @Primary
    public com.handmade.tle.upload.service.UploadOrchestrationService uploadOrchestrationService() {
        return Mockito.mock(com.handmade.tle.upload.service.UploadOrchestrationService.class);
    }

    @Bean
    @Primary
    public com.handmade.tle.quota.QuotaManagementService quotaManagementService() {
        return Mockito.mock(com.handmade.tle.quota.QuotaManagementService.class);
    }

}
