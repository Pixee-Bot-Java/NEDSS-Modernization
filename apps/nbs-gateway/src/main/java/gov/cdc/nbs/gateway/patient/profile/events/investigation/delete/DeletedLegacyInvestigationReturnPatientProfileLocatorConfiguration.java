package gov.cdc.nbs.gateway.patient.profile.events.investigation.delete;

import gov.cdc.nbs.gateway.patient.profile.PatientProfileService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configures the Patient Profile routes to the {@code /nbs/redirect/patient/investigation/delete} path of the
 * {@code nbs.gateway.patient.profile.service} when the {@code routes.patient.profile.enabled} property is {@code true}
 * and the following criteria is satisfied;
 *
 * <ul>
 *     <li>Path equal to {@code /nbs/ViewInvestigation1.do}</li>
 *     <li>Query Parameter {@code delete} equal to {@code true}</li>
 *     <li>Query Parameter {@code ContextAction} equal to {@code ReturnToFileSummary}</li>
 * </ul>
 *
 * OR
 *
 * <ul>
 *     <li>Path equal to {@code /nbs/ViewInvestigation3.do}</li>
 *     <li>Query Parameter {@code delete} equal to {@code true}</li>
 *     <li>Query Parameter {@code ContextAction} equal to {@code ReturnToFileEvents}</li>
 * </ul>
 */
@Configuration
@ConditionalOnProperty(prefix = "nbs.gateway.patient.profile", name = "enabled", havingValue = "true")
class DeletedLegacyInvestigationReturnPatientProfileLocatorConfiguration {

    @Bean
    RouteLocator deletedLegacyInvestigationPatientProfileReturn(
        final RouteLocatorBuilder builder,
        @Qualifier("default") final GatewayFilter globalFilter,
        final PatientProfileService service
    ) {
        return builder.routes()
            .route(
                "deleted-legacy-investigation-patient-profile-return",
                route -> route.order(Ordered.HIGHEST_PRECEDENCE)
                    .path("/nbs/ViewInvestigation1.do", "/nbs/ViewInvestigation3.do")
                    .and()
                    .query("delete", "true")
                    .and()
                    .query("ContextAction", "ReturnToFile(:?Summary|Events)")
                    .filters(
                        filter -> filter.setPath("/nbs/redirect/patient/investigation/delete")
                            .filter(globalFilter)
                    )
                    .uri(service.uri())
            )
            .build();
    }

}
