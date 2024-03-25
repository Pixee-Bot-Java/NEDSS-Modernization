package gov.cdc.nbs.event.investigation;

import gov.cdc.nbs.patient.identifier.PatientIdentifier;
import gov.cdc.nbs.support.jurisdiction.JurisdictionIdentifier;
import gov.cdc.nbs.support.programarea.ProgramAreaIdentifier;
import gov.cdc.nbs.support.provider.ProviderIdentifier;
import gov.cdc.nbs.testing.authorization.ActiveUser;
import gov.cdc.nbs.testing.support.Active;
import gov.cdc.nbs.testing.support.concept.ConceptParameterResolver;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
public class InvestigationSteps {

  private final Active<PatientIdentifier> activePatient;
  private final Active<JurisdictionIdentifier> activeJurisdiction;
  private final Active<ProgramAreaIdentifier> activeProgramArea;
  private final Active<ProviderIdentifier> activeProvider;
  private final Active<InvestigationIdentifier> investigation;
  private final InvestigationMother mother;
  private final ConceptParameterResolver resolver;

  public InvestigationSteps(
      final Active<PatientIdentifier> activePatient,
      final Active<JurisdictionIdentifier> activeJurisdiction,
      final Active<ProgramAreaIdentifier> activeProgramArea,
      final Active<ProviderIdentifier> activeProvider,
      final Active<InvestigationIdentifier> investigation,
      final InvestigationMother mother,
      final ConceptParameterResolver resolver
  ) {
    this.activePatient = activePatient;
    this.activeJurisdiction = activeJurisdiction;
    this.activeProgramArea = activeProgramArea;
    this.activeProvider = activeProvider;
    this.investigation = investigation;
    this.mother = mother;
    this.resolver = resolver;
  }

  @Before
  public void clean() {
    mother.reset();
  }

  @Given("the patient is a subject of an investigation")
  public void the_patient_is_a_subject_of_an_investigation() {
    activePatient.maybeActive().ifPresent(
        p -> mother.create(
            p,
            activeJurisdiction.active(),
            activeProgramArea.active()
        )
    );
  }

  @Given("the patient is a subject of {int} investigations")
  public void the_patient_is_a_subject_N_investigation(final int n) {
    PatientIdentifier patient = this.activePatient.active();
    JurisdictionIdentifier jurisdiction = this.activeJurisdiction.active();
    ProgramAreaIdentifier programArea = this.activeProgramArea.active();

    for (int i = 0; i < n; i++) {
      mother.create(
          patient,
          jurisdiction,
          programArea
      );
    }
  }

  @Given("the investigation is for {programArea} within {jurisdiction}")
  public void the_investigation_is_within(
      final ProgramAreaIdentifier programArea,
      final JurisdictionIdentifier jurisdiction
  ) {
    investigation.maybeActive().ifPresent(
        investigation -> mother.within(
            investigation,
            programArea,
            jurisdiction
        )
    );
  }

  @Given("the investigation is for the {condition} condition")
  public void the_investigation_is_for_the_condition(final String condition) {
    investigation.maybeActive().ifPresent(investigation -> mother.withCondition(investigation, condition));
  }

  @Given("the investigation is for a patient that is pregnant")
  public void the_investigation_is_for_a_pregnant_patient() {
    investigation.maybeActive().ifPresent(mother::forPregnantPatient);
  }

  @Given("the investigation is for a patient that is not pregnant")
  public void the_investigation_is_for_a_non_pregnant_patient() {
    investigation.maybeActive().ifPresent(mother::forNonPregnantPatient);
  }

  @Given("the investigation is for a patient that does not know if they are pregnant")
  public void the_investigation_is_for_an_unknown_pregnancy() {
    investigation.maybeActive().ifPresent(mother::forPregnancyUnknownPatient);
  }

  @Given("the investigation was created by {user} on {date}")
  public void the_investigation_was_created_on(final ActiveUser user, final Instant date) {
    investigation.maybeActive().ifPresent(investigation -> this.mother.created(investigation, user.id(), date));
  }

  @Given("the investigation was updated by {user} on {date}")
  public void the_investigation_was_updated_on(final ActiveUser user, final Instant date) {
    investigation.maybeActive().ifPresent(investigation -> this.mother.updated(investigation, user.id(), date));
  }

  @Given("the investigation has been closed")
  public void the_investigation_has_been_closed() {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.closed(active, Instant.now()));
  }

  @Given("the investigation was closed on {date}")
  public void the_investigation_was_closed_on(final Instant on) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.closed(active, on));
  }

  @Given("the investigation was started on {date}")
  public void the_investigation_started_on(final Instant on) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.started(active, on));
  }

  @Given("the investigation was reported on {date}")
  public void the_investigation_reported_on(final Instant on) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.reported(active, on));
  }

  @Given("the investigation has a processing status of {processingStatus}")
  public void the_investigation_has_a_processing_status_of(final String status) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.processing(active, status));
  }

  @ParameterType(name = "processingStatus", value = ".*")
  public String processingStatus(final String status) {
    return resolver.resolve("CM_PROCESS_STAGE", status)
        .orElse(null);
  }

  @Given("the investigation has a case status of {caseStatus}")
  public void the_investigation_has_a_case_status_of(final String status) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.caseStatus(active, status));
  }

  @ParameterType(name = "caseStatus", value = ".*")
  public String caseStatus(final String value) {
    return resolver.resolve("PHC_CLASS", value)
        .orElse(null);
  }

  @Given("the investigation is related to State Case {string}")
  public void the_investigation_is_related_to_state_case(final String number) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.relatedToStateCase(active, number));
  }

  @Given("the investigation is related to ABCS Case {string}")
  public void the_investigation_is_related_to_ABCS_case(final String number) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.relatedToABCSCase(active, number));
  }

  @Given("the investigation is related to County Case {string}")
  public void the_investigation_is_related_to_county_case(final String number) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.relatedToCountyCase(active, number));
  }

  @Given("the investigation was investigated by the provider")
  public void the_investigation_was_investigated_by_the_provider() {
    this.investigation.maybeActive()
        .ifPresent(
            active -> mother.investigatedBy(
                active,
                activeProvider.active()
            )
        );
  }

  @Given("the investigation was reported by the {organization} facility")
  public void the_lab_report_was_ordered_by_the_organization(final long organization) {
    this.investigation.maybeActive()
        .ifPresent(active -> mother.reportedBy(active, organization));
  }

  @Given("the investigation was reported by the provider")
  public void the_investigation_was_reported_by_the_provider() {
    this.investigation.maybeActive()
        .ifPresent(
            active -> mother.reportedBy(
                active,
                activeProvider.active()
            )
        );
  }

  @Given("the investigation is related to the {outbreak} outbreak")
  public void the_investigation_is_related_to_the_outbreak(final String outbreak) {
    this.investigation.maybeActive().ifPresent(
        active -> mother.relatedToOutbreak(active, outbreak)
    );
  }
}
