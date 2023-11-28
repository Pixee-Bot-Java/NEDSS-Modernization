package gov.cdc.nbs.gateway.patient.profile.events.observation;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "nbs.gateway.classic=http://localhost:10000",
                "nbs.gateway.patient.profile.enabled=true"
        }
)
class ViewObservationRouteLocatorConfigurationTest {

    @RegisterExtension
    static WireMockExtension classic = WireMockExtension.newInstance()
            .options(wireMockConfig().port(10000))
            .build();

    @Autowired
    WebTestClient webClient;

    @Test
    void should_add_Patient_Action_cookie_when_viewing_a_morbidity_observation() {

        classic.stubFor(get(urlPathMatching("/nbs/NewLabReview1.do\\\\?.*")).willReturn(ok()));

        webClient
                .get().uri(
                        builder -> builder
                                .path("/nbs/NewLabReview1.do")
                                .queryParam("ContextAction", "ViewMorb")
                                .queryParam("observationUID", "7841")
                                .build()
                )
                .exchange()
                .expectHeader()
                .value("Set-Cookie", containsString("Patient-Action=7841"));

    }

    @Test
    void should_add_Patient_Action_cookie_when_viewing_a_lab_report_observation() {

        classic.stubFor(get(urlPathMatching("/nbs/NewLabReview1.do\\\\?.*")).willReturn(ok()));

        webClient
            .get().uri(
                builder -> builder
                    .path("/nbs/NewLabReview1.do")
                    .queryParam("ContextAction", "ViewLab")
                    .queryParam("observationUID", "7841")
                    .build()
            )
            .exchange()
            .expectHeader()
            .value("Set-Cookie", containsString("Patient-Action=7841"));

    }
}
