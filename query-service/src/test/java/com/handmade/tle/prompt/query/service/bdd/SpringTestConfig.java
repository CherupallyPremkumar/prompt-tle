package com.handmade.tle.prompt.query.service.bdd;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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

@SpringBootApplication(scanBasePackages = {
        "org.chenile.configuration",
        "com.handmade.tle.**.configuration",
        "com.handmade.tle.auth",
        "com.handmade.tle.auth.security"
})
@EnableJpaRepositories(basePackages = "com.handmade.tle.shared.repository")
@EntityScan(basePackages = "com.handmade.tle.shared.model")
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
    public com.handmade.tle.upload.service.UploadOrchestrationService uploadOrchestrationService() {
        return Mockito.mock(com.handmade.tle.upload.service.UploadOrchestrationService.class);
    }

    @Bean
    @Primary
    public com.handmade.tle.quota.QuotaManagementService quotaManagementService() {
        return Mockito.mock(com.handmade.tle.quota.QuotaManagementService.class);
    }

}
