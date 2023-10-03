package gov.cdc.nbs.patient;

import gov.cdc.nbs.patient.identifier.PatientIdentifier;
import gov.cdc.nbs.support.TestActive;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
public class PatientSteps {

  @Autowired
  TestActive<PatientIdentifier> patient;

  @Autowired
  PatientMother mother;

  //  Make sure that patients are cleaned up after everything else
  @Before(order = 15000)
  public void clean() {
    mother.reset();
  }

  @Given("I have a(nother) patient")
  public void i_have_a_patient() {
    mother.create();
  }

  @Given("the patient is inactive")
  public void the_patient_is_inactive() {
    mother.deleted(patient.active());
  }

  @Given("the patient has a(n) {string} of {string}")
  public void the_patient_has_a_field_with_a_value_of(
      final String field,
      final String value
  ) {

    PatientIdentifier identifier = this.patient.active();

    switch (field.toLowerCase()) {
      case "first name" -> mother.withName(
          identifier,
          "L",
          value,
          null
      );

      case "last name" -> mother.withName(
          identifier,
          "L",
          null,
          value
      );

      case "race" -> mother.withRace(
          identifier,
          resolveRace(value)
      );

      case "birthday" -> mother.withBirthday(
          identifier,
          LocalDate.parse(value)
      );

      case "phone number" -> mother.withPhone(
          identifier,
          value
      );

      case "address" -> mother.withAddress(
          identifier,
          value,
          null,
          null,
          null
      );

      case "city" -> mother.withAddress(
          identifier,
          null,
          value,
          null,
          null
      );
    }

  }

  private String resolveRace(final String value) {
    return switch (value.toLowerCase()) {
      case "american indian or alaska native", "american indian", "alaska native" -> "1002-5";
      case "asian" -> "2028-9";
      case "black or african american", "black", "african american" -> "2054-5";
      case "native Hawaiian or other pacific Islander", "native Hawaiian", "pacific Islander" -> "2076-8";
      case "white" -> "2106-3";
      case "other race", "other" -> "2131-1";
      case "not asked" -> "NASK";
      case "refused to answer", "refused" -> "PHC1175";
      case "multi-race", "multi" -> "M";
      default -> "U";
    };
  }
}
