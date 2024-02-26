package gov.cdc.nbs.patient.search.indexing;

import gov.cdc.nbs.patient.search.SearchablePatient;
import gov.cdc.nbs.patient.search.indexing.address.SearchablePatientAddressFinder;
import gov.cdc.nbs.patient.search.indexing.identification.SearchablePatientIdentificationFinder;
import gov.cdc.nbs.patient.search.indexing.name.SearchablePatientNameFinder;
import gov.cdc.nbs.patient.search.indexing.race.SearchablePatientRaceFinder;
import gov.cdc.nbs.patient.search.indexing.telecom.SearchablePatientTelecom;
import gov.cdc.nbs.patient.search.indexing.telecom.SearchablePatientTelecomFinder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class SearchablePatientResolver {

  private final SearchablePatientFinder patientFinder;
  private final SearchablePatientNameFinder nameFinder;
  private final SearchablePatientAddressFinder addressFinder;
  private final SearchablePatientRaceFinder raceFinder;
  private final SearchablePatientIdentificationFinder identificationFinder;
  private final SearchablePatientTelecomFinder telecomFinder;

  SearchablePatientResolver(
      final SearchablePatientFinder patientFinder,
      final SearchablePatientNameFinder nameFinder,
      final SearchablePatientAddressFinder addressFinder,
      final SearchablePatientRaceFinder raceFinder,
      final SearchablePatientIdentificationFinder identificationFinder,
      final SearchablePatientTelecomFinder telecomFinder
  ) {
    this.patientFinder = patientFinder;
    this.nameFinder = nameFinder;
    this.addressFinder = addressFinder;
    this.raceFinder = raceFinder;
    this.identificationFinder = identificationFinder;
    this.telecomFinder = telecomFinder;
  }

  Optional<SearchablePatient> resolve(final long identifier) {
    return this.patientFinder.find(identifier).map(this::withDemographics);
  }

  private SearchablePatient withDemographics(final SearchablePatient patient) {

    List<SearchablePatient.Name> names = this.nameFinder.find(patient.identifier());
    List<SearchablePatient.Address> addresses = this.addressFinder.find(patient.identifier());
    List<SearchablePatient.Race> races = this.raceFinder.find(patient.identifier());
    List<SearchablePatient.Identification> identifications = identificationFinder.find(patient.identifier());

    SearchablePatientTelecom telecom = this.telecomFinder.find(patient.identifier());

    List<SearchablePatient.Phone> phones = telecom.phones();
    List<SearchablePatient.Email> emails = telecom.emails();

    return new SearchablePatient(
        patient.identifier(),
        patient.local(),
        patient.status(),
        patient.birthday(),
        patient.deceased(),
        patient.gender(),
        patient.ethnicity(),
        names,
        addresses,
        phones,
        emails,
        races,
        identifications
    );
  }
}
