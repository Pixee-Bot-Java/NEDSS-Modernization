package gov.cdc.nbs;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import javax.transaction.Transactional;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("gov/cdc/nbs")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:build/reports/tests/test/cucumber-report.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "gov.cdc.nbs")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources")
@ContextConfiguration(classes = Application.class, loader = SpringBootContextLoader.class)
@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Rollback(false)
class RunCucumberTest {

}
