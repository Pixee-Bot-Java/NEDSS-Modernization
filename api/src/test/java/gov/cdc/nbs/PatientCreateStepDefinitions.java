package gov.cdc.nbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import gov.cdc.nbs.controller.PatientController;
import gov.cdc.nbs.entity.Person;
import gov.cdc.nbs.graphql.PatientFilter;
import gov.cdc.nbs.graphql.PatientInput;
import gov.cdc.nbs.repository.PersonRepository;
import gov.cdc.nbs.support.PersonMother;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class PatientCreateStepDefinitions {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    private PatientController patientController;

    private Person person;

    @Given("A patient does not exist")
    public void a_patient_does_not_exist() {
        person = PersonMother.johnDoe();
        var filter = new PatientFilter();
        filter.setFirstName(person.getFirstNm());
        filter.setLastName(person.getLastNm());
        filter.setAddress(person.getHmStreetAddr1());
        filter.setPhoneNumber(person.getHmPhoneNbr());
        var existing = patientController.findPatientsByFilter(filter);
        if (existing.size() > 0) {
            personRepository.deleteAll(existing);
        }
    }

    @When("I send a create patient request")
    public void i_send_a_create_patient_request() {
        var input = new PatientInput();
        input.setLastName(person.getLastNm());
        input.setFirstName(person.getFirstNm());
        input.setSsn(person.getSsn());
        input.setPhoneNumber(person.getHmPhoneNbr());
        input.setDateOfBirth(person.getBirthTime());
        input.setGender(person.getBirthGenderCd());
        input.setDeceased(person.getDeceasedIndCd());
        input.setAddress(person.getHmStreetAddr1());
        input.setCity(person.getHmCityCd());
        input.setState(person.getHmStateCd());
        input.setCountry(person.getHmCntryCd());
        input.setZip(person.getHmZipCd());
        input.setEthnicity(person.getEthnicityGroupCd());
        patientController.createPatient(input);
    }

    @Then("The patient exists")
    public void the_patient_exists() {
        var filter = new PatientFilter();
        filter.setFirstName(person.getFirstNm());
        filter.setLastName(person.getLastNm());
        filter.setAddress(person.getHmStreetAddr1());
        filter.setPhoneNumber(person.getHmPhoneNbr());
        var patientSearch = patientController.findPatientsByFilter(filter);
        assertTrue(patientSearch.size() > 0);

        var patient = patientSearch.get(0);
        assertEquals(patient.getLastNm(), person.getLastNm());
        assertEquals(patient.getFirstNm(), person.getFirstNm());
        assertEquals(patient.getSsn(), person.getSsn());
        assertEquals(patient.getHmPhoneNbr(), person.getHmPhoneNbr());
        assertEquals(patient.getBirthTime(), person.getBirthTime());
        assertEquals(patient.getBirthGenderCd(), person.getBirthGenderCd());
        assertEquals(patient.getDeceasedIndCd(), person.getDeceasedIndCd());
        assertEquals(patient.getHmStreetAddr1(), person.getHmStreetAddr1());
        assertEquals(patient.getHmCityCd(), person.getHmCityCd());
        assertEquals(patient.getHmStateCd(), person.getHmStateCd());
        assertEquals(patient.getHmCntryCd(), person.getHmCntryCd());
        assertEquals(patient.getHmZipCd(), person.getHmZipCd());
        assertEquals(patient.getEthnicityGroupCd(), person.getEthnicityGroupCd());
    }
}
