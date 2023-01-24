package gov.cdc.nbs.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import gov.cdc.nbs.RunCucumberTest;

@SpringBootTest(classes = ElasticSearchTest.class, properties = { "spring.profiles.active:test" })
public class ElasticSearchTest {
	@Test
    void testElasticSearchIsRunning() {
        assertThat(RunCucumberTest.ELASTICSEARCH_CONTAINER.isRunning()).isTrue();
    }

	@Test
    void testElasticSearchHasCorrectPlugins() throws UnsupportedOperationException, IOException, InterruptedException  {
        String output = RunCucumberTest.ELASTICSEARCH_CONTAINER.execInContainer(
            "/usr/share/elasticsearch/bin/elasticsearch-plugin", "list").getStdout();
        assertEquals(output,"analysis-phonetic\n");
    }
}