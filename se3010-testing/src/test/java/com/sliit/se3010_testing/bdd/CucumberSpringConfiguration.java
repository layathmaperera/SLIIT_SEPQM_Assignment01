package com.sliit.se3010_testing.bdd;

import com.sliit.se3010_testing.Se3010TestingApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = Se3010TestingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {
    // This class configures the Spring context for all Cucumber steps
}
