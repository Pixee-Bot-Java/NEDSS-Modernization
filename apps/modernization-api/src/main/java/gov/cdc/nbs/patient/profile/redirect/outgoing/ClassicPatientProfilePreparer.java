package gov.cdc.nbs.patient.profile.redirect.outgoing;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ClassicPatientProfilePreparer {

    private static final String LOCATION = "/PatientSearchResults1.do";

    private final RestTemplate template;

    public ClassicPatientProfilePreparer(
        @Qualifier("classic") final RestTemplate template
    ) {
        this.template = template;
    }

    public void prepare(final long patient) {

        String profileLocation = UriComponentsBuilder.fromPath(LOCATION)
            .queryParam("ContextAction", "ViewFile")
            .queryParam("uid", patient)
            .build()
            .toUriString();

        RequestEntity<Void> profileRequest = RequestEntity
            .get(profileLocation)
            .build();

        this.template.exchange(profileRequest, Void.class);
    }
}
