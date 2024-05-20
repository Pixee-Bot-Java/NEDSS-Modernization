package gov.cdc.nbs.gateway.patient.profile.events.report.lab;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "nbs.gateway.classic=http://localhost:10000",
        "nbs.gateway.patient.profile.service=localhost:10001",
        "nbs.gateway.patient.profile.enabled=true"
    }
)
class DeletedLabReportLocatorConfigurationTest {

  @RegisterExtension
  static WireMockExtension service = WireMockExtension.newInstance()
      .options(wireMockConfig().port(10001))
      .build();

  @Autowired
  WebTestClient webClient;

  @Test
  void should_route_to_service_when_Lab_Report_is_deleted() {
    service.stubFor(get(urlPathMatching("/nbs/redirect/patientProfile/events/return\\\\?.*")).willReturn(ok()));

    webClient
        .get().uri(
            builder -> builder
                .path("/nbs/LoadViewFile1.do")
                .queryParam("ContextAction", "Delete")
                .build()
        )
        .exchange()
        .expectStatus()
        .isOk();

  }

}
