package gov.cdc.nbs.questionbank;

import gov.cdc.nbs.questionbank.container.EmbeddedNbsDatabase;
import gov.cdc.nbs.questionbank.kafka.config.RequestProperties;
import gov.cdc.nbs.testing.interaction.http.EnableAuthenticatedInteractions;
import gov.cdc.nbs.testing.support.EnableSupport;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertNotNull;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("gov/cdc/nbs/questionbank")
@CucumberOptions(
    glue = {"gov.cdc.nbs.questionbank", "gov.cdc.nbs.testing.authorization"},
    features = "src/test/resources/features",
    plugin = {"pretty", "html:build/reports/tests/test/cucumber-report.html"}
)
@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
@Testcontainers
@EmbeddedNbsDatabase
@SpringBootTest
@Transactional
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
@EnableSupport
@EnableAuthenticatedInteractions
class CucumberTests {
  @Autowired
  private RequestProperties properties;

  @Test
  void should_autowire() {
    assertNotNull(properties);
  }
}
