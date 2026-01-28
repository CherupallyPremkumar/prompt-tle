package com.handmade.tle.prompt.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;


@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "classpath:com.handmade.tle/prompt/bdd",
        "classpath:org/chenile/cucumber/rest",
        "classpath:org/chenile/cucumber/workflow" },
        plugin = { "pretty" })
@ActiveProfiles("unittest")
public class CukesRestTest {

}
