package com.handmade.tle.prompt;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@PropertySource("classpath:com/handmade/tle/prompt/TestService.properties")
@SpringBootApplication(scanBasePackages = { "org.chenile.configuration", "com.handmade.tle" })
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.handmade.tle")
@org.springframework.boot.persistence.autoconfigure.EntityScan(basePackages = "com.handmade.tle")
@ActiveProfiles("unittest")
public class SpringTestConfig extends SpringBootServletInitializer {

    @Bean
    public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
        return MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }
}
