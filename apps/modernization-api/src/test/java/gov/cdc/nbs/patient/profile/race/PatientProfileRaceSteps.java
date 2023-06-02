package gov.cdc.nbs.patient.profile.race;

import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.graphql.GraphQLPage;
import gov.cdc.nbs.message.patient.input.PatientInput;
import gov.cdc.nbs.patient.PatientAssertions;
import gov.cdc.nbs.patient.TestPatient;
import gov.cdc.nbs.patient.TestPatients;
import gov.cdc.nbs.patient.profile.PatientProfile;
import gov.cdc.nbs.support.TestActive;
import gov.cdc.nbs.support.util.RandomUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PatientProfileRaceSteps {

    @Autowired
    TestActive<PatientInput> input;

    @Autowired
    TestPatient patient;

    @Autowired
    TestPatients patients;

    @Autowired
    PatientRaceResolver resolver;

    @Given("the new patient's race is entered")
    public void the_new_patient_race_is_entered() {
        PatientInput active = this.input.active();

        active.getRaces().add(RandomUtil.getRandomString());
    }

    @Then("the new patient has the entered race")
    @Transactional
    public void the_new_patient_has_the_entered_race() {
        Person actual = patient.managed();

        if (!actual.getRaces().isEmpty()) {
            assertThat(actual.getRaces())
                .satisfiesExactly(PatientAssertions.containsRaceCategories(this.input.active().getRaces()));
        }
    }

    @Then("the profile has associated races")
    public void the_profile_has_associated_races() {
        long patient = this.patients.one();

        PatientProfile profile = new PatientProfile(patient, "local", (short) 1);

        GraphQLPage page = new GraphQLPage(1);

        Page<PatientRace> actual = this.resolver.resolve(profile, page);
        assertThat(actual).isNotEmpty();
    }

    @Then("the profile has associated no races")
    public void the_profile_has_no_associated_races() {
        long patient = this.patients.one();

        PatientProfile profile = new PatientProfile(patient, "local", (short) 1);

        GraphQLPage page = new GraphQLPage(1);

        Page<PatientRace> actual = this.resolver.resolve(profile, page);
        assertThat(actual).isEmpty();
    }

    @Then("the profile races are not accessible")
    public void the_profile_race_is_not_accessible() {
        long patient = this.patients.one();


        PatientProfile profile = new PatientProfile(patient, "local", (short) 1);

        GraphQLPage page = new GraphQLPage(1);

        assertThatThrownBy(
            () -> this.resolver.resolve(profile, page)
        )
            .isInstanceOf(AccessDeniedException.class);
    }
}
