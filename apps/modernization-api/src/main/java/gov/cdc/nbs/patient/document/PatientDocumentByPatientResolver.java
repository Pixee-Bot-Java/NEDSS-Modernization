package gov.cdc.nbs.patient.document;

import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.graphql.GraphQLPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
class PatientDocumentByPatientResolver {

    private final int maxPageSize;
    private final PatientDocumentFinder finder;

    PatientDocumentByPatientResolver(
        @Value("${nbs.max-page-size}") final int maxPageSize,
        final PatientDocumentFinder finder
    ) {
        this.maxPageSize = maxPageSize;
        this.finder = finder;
    }

    @QueryMapping(name = "findDocumentsForPatient")
    @PreAuthorize("hasAuthority('FIND-PATIENT') and hasAuthority('VIEW-DOCUMENT')")
    Page<PatientDocument> find(
        @Argument("patient") final long patient,
        @Argument final GraphQLPage page
    ) {
        Pageable pageable = GraphQLPage.toPageable(page, maxPageSize);
        return this.finder.find(
            patient,
            pageable
        );
    }

    @SchemaMapping("documents")
    @PreAuthorize("hasAuthority('FIND-PATIENT') and hasAuthority('VIEW-DOCUMENT')")
    List<PatientDocument> resolve(final Person patient) {
        return this.finder.find(patient.getId());
    }
}
