package gov.cdc.nbs.questionbank;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import gov.cdc.nbs.questionbank.questionnaire.QuestionnaireRepository;
import io.cucumber.spring.CucumberContextConfiguration;



@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("gov/cdc/nbs/questionbank")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "gov.cdc.nbs.questionbank")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:build/reports/tests/test/cucumber-report.html")
@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
class CucumberTests {

    @Autowired
    private QuestionnaireRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(repository);
    }

}