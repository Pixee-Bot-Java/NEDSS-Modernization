package gov.cdc.nbs.authorization;

import gov.cdc.nbs.authentication.SessionCookie;
import gov.cdc.nbs.testing.support.Active;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestActiveSessionCookieConfiguration {

    @Bean
    static Active<SessionCookie> sessionCookieTestActive() {
        return new Active<>();
    }

}
