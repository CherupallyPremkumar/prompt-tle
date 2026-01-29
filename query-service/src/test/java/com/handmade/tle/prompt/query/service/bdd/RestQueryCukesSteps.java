package com.handmade.tle.prompt.query.service.bdd;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;

@EnableAutoConfiguration
@EnableWebSecurity
@ComponentScan(basePackages = {
		"org.chenile.configuration",
		"org.chenile.configuration.controller",
		"org.chenile.configuration.query.service",
		"com.handmade.tle.configuration",
		"com.handmade.tle.upload.security",
		"com.handmade.tle.auth.security",
		"com.handmade.tle.storage"
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringTestConfig.class, properties = {
		"spring.profiles.active=unittest" })
@CucumberContextConfiguration
@ActiveProfiles("unittest")
public class RestQueryCukesSteps {
	// Create a dummy method so that Cucumber thinks of this as a steps
	// implementation.
	@Given("dummy")
	public void dummy() {

	}
}
