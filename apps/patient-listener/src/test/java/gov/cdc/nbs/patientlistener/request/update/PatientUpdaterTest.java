package gov.cdc.nbs.patientlistener.request.update;

import gov.cdc.nbs.entity.enums.RecordStatus;
import gov.cdc.nbs.entity.odse.EntityLocatorParticipationId;
import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.odse.PostalEntityLocatorParticipation;
import gov.cdc.nbs.entity.odse.PostalLocator;
import gov.cdc.nbs.id.IdGeneratorService;
import gov.cdc.nbs.id.IdGeneratorService.GeneratedId;
import gov.cdc.nbs.message.enums.Deceased;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.message.patient.event.DeleteMortalityData;
import gov.cdc.nbs.message.patient.event.UpdateAdministrativeData;
import gov.cdc.nbs.message.patient.event.UpdateMortalityData;
import gov.cdc.nbs.message.patient.event.UpdateSexAndBirthData;
import gov.cdc.nbs.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientUpdaterTest {

    @Mock
    private IdGeneratorService idGenerator;
    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @InjectMocks
    private PatientUpdater patientUpdater;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("squid:S5961")
        // Allow more than 25 assertions
    void should_update_mortality_info_new_locator() {
        when(idGenerator.getNextValidId(Mockito.any())).thenReturn(new GeneratedId(999L, "prefix", "suffix"));

        var data = getMortalityData();
        var person = new Person(123L, "localId");

        patientUpdater.update(person, data);

        verify(personRepository).save(personCaptor.capture());
        var postalParticipation = (PostalEntityLocatorParticipation) personCaptor.getValue()
            .getNbsEntity()
            .getEntityLocatorParticipations()
            .get(0);

        // validate entityLocatorParticipation
        var now = Instant.now();
        assertEquals(Long.valueOf(123L), postalParticipation.getId().getEntityUid());
        assertEquals(Long.valueOf(999L), postalParticipation.getId().getLocatorUid());
        assertEquals(Long.valueOf(data.updatedBy()), postalParticipation.getAddUserId());
        assertTrue(postalParticipation.getAddTime().until(now, ChronoUnit.SECONDS) < 5);
        assertTrue(postalParticipation.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(Long.valueOf(data.updatedBy()), postalParticipation.getLastChgUserId());
        assertEquals(RecordStatus.ACTIVE.toString(), postalParticipation.getRecordStatusCd());
        assertTrue(postalParticipation.getRecordStatusTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(Character.valueOf('A'), postalParticipation.getStatusCd());
        assertTrue(postalParticipation.getStatusTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(data.asOf().getEpochSecond(), postalParticipation.getAsOfDate().getEpochSecond());
        assertEquals(Short.valueOf((short) 1), postalParticipation.getVersionCtrlNbr());
        assertEquals("U", postalParticipation.getCd());
        assertEquals("DTH", postalParticipation.getUseCd());

        // validate locator
        var locator = postalParticipation.getLocator();
        assertEquals(Long.valueOf(999L), locator.getId());
        assertEquals(data.cityOfDeath(), locator.getCityDescTxt());
        assertEquals(data.countryOfDeath(), locator.getCntryCd());
        assertEquals(data.countyOfDeath(), locator.getCntyCd());
        assertEquals(data.stateOfDeath(), locator.getStateCd());
        assertEquals(Long.valueOf(data.updatedBy()), locator.getAddUserId());
        assertTrue(locator.getAddTime().until(now, ChronoUnit.SECONDS) < 5);
        assertTrue(locator.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(Long.valueOf(data.updatedBy()), locator.getLastChgUserId());
        assertEquals(RecordStatus.ACTIVE.toString(), locator.getRecordStatusCd());
        assertTrue(locator.getRecordStatusTime().until(now, ChronoUnit.SECONDS) < 5);
    }

    @Test
    void should_update_mortality_info_existing_locator() {
        var data = getMortalityData();
        var person = new Person(123L, "localId");
        // Create EntityLocatorParticipation
        var elp = new PostalEntityLocatorParticipation();
        elp.setId(new EntityLocatorParticipationId(person.getId(), 456L));
        elp.setNbsEntity(person.getNbsEntity());
        elp.setUseCd("DTH");
        elp.setVersionCtrlNbr((short) 1);

        // Create PostalLocator
        var postalLocator = new PostalLocator();
        postalLocator.setId(456L);

        elp.setLocator(postalLocator);
        person.getNbsEntity().setEntityLocatorParticipations(Collections.singletonList(elp));

        patientUpdater.update(person, data);

        verify(personRepository).save(personCaptor.capture());
        var postalParticipation = (PostalEntityLocatorParticipation) personCaptor.getValue()
                .getNbsEntity()
                .getEntityLocatorParticipations()
                .get(0);

        // validate entityLocatorParticipation
        var now = Instant.now();
        assertEquals(Long.valueOf(123L), postalParticipation.getId().getEntityUid());
        assertEquals(Long.valueOf(456L), postalParticipation.getId().getLocatorUid());
        assertTrue(postalParticipation.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(Long.valueOf(data.updatedBy()), postalParticipation.getLastChgUserId());
        assertEquals(data.asOf().getEpochSecond(), postalParticipation.getAsOfDate().getEpochSecond());
        assertEquals(Short.valueOf((short) 2), postalParticipation.getVersionCtrlNbr());

        // validate locator
        var locator = postalParticipation.getLocator();
        assertEquals(Long.valueOf(456L), locator.getId());
        assertEquals(data.cityOfDeath(), locator.getCityDescTxt());
        assertEquals(data.countryOfDeath(), locator.getCntryCd());
        assertEquals(data.countyOfDeath(), locator.getCntyCd());
        assertEquals(data.stateOfDeath(), locator.getStateCd());
        assertTrue(locator.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
        assertEquals(Long.valueOf(data.updatedBy()), locator.getLastChgUserId());
    }

    private UpdateMortalityData getMortalityData() {
        return new UpdateMortalityData(123L,
                "RequestId",
                321L,
                Instant.now(),
                Deceased.UNK,
                Instant.now(),
                "CityOfDeath",
                "State of Death",
                "County of death",
                "Country of death");
    }

    @Test
    void should_delete_mortality_info() {
        var data = getDeleteMortalityData();
        var person = new Person(123L, "localId");
        // Create EntityLocatorParticipation
        var elp = new PostalEntityLocatorParticipation();
        elp.setId(new EntityLocatorParticipationId(person.getId(), 456L));
        elp.setNbsEntity(person.getNbsEntity());
        elp.setUseCd("DTH");
        elp.setVersionCtrlNbr((short) 1);

        // Create PostalLocator
        var postalLocator = new PostalLocator();
        postalLocator.setId(456L);

        elp.setLocator(postalLocator);
        person.getNbsEntity().setEntityLocatorParticipations(Collections.singletonList(elp));
        assertEquals(1, person.getNbsEntity().getEntityLocatorParticipations().size());
        patientUpdater.update(person, data);

        verify(personRepository).save(personCaptor.capture());
        assertEquals(0, personCaptor.getValue().getNbsEntity().getEntityLocatorParticipations().size());
    }

    private DeleteMortalityData getDeleteMortalityData() {
        return new DeleteMortalityData(123L,
                (short) 456,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void should_update_sex_and_birth_info() {
        var data = getSexAndBirthData();
        var person = new Person(123L, "localId");
        person.setVersionCtrlNbr((short) 2);
        patientUpdater.update(person, data);

        verify(personRepository).save(personCaptor.capture());
        var savedPerson = personCaptor.getValue();

        var now = Instant.now();

        assertEquals(data.asOf().getEpochSecond(), savedPerson.getAsOfDateSex().getEpochSecond());
        assertEquals(data.birthGender(), savedPerson.getBirthGenderCd());
        assertEquals(data.currentGender(), savedPerson.getCurrSexCd());
        assertEquals(data.dateOfBirth(), LocalDate.ofInstant(savedPerson.getBirthTime(), ZoneId.systemDefault()));
        assertEquals(data.currentAge(), savedPerson.getAgeReported());
        assertEquals(data.ageReportedTime().getEpochSecond(), savedPerson.getAgeReportedTime().getEpochSecond());
        assertEquals(data.birthCity(), savedPerson.getBirthCityCd());
        assertEquals(data.birthCntry(), savedPerson.getBirthCntryCd());
        assertEquals(data.birthState(), savedPerson.getBirthStateCd());
        assertEquals(data.birthOrderNbr(), savedPerson.getBirthOrderNbr());
        assertEquals(data.multipleBirth(), savedPerson.getMultipleBirthInd());
        assertEquals(data.sexUnknown(), savedPerson.getSexUnkReasonCd());
        assertEquals(data.additionalGender(), savedPerson.getAdditionalGenderCd());
        assertEquals(data.transGenderInfo(), savedPerson.getPreferredGenderCd());

        assertEquals(Long.valueOf(data.updatedBy()), savedPerson.getLastChgUserId());
        assertEquals(Short.valueOf((short) 3), savedPerson.getVersionCtrlNbr());
        assertEquals(Long.valueOf(data.updatedBy()), savedPerson.getLastChgUserId());
        assertTrue(savedPerson.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
    }

    private UpdateSexAndBirthData getSexAndBirthData() {
        return new UpdateSexAndBirthData(
                "RequestId",
                123L,
                321L,
                Instant.now(),
                LocalDate.now(),
                Gender.F,
                Gender.M,
                "additional gender info",
                "trans info",
                "birth city",
                "birth Cntry",
                "birth state",
                (short) 1,
                "multiple birth",
                "sex unknown",
                "current age",
                Instant.now());
    }

    @Test
    void should_update_administrative_info() {
        var data = getAdministrativeData();
        var person = new Person(123L, "localId");
        person.setVersionCtrlNbr((short) 2);
        patientUpdater.update(person, data);

        verify(personRepository).save(personCaptor.capture());
        var savedPerson = personCaptor.getValue();

        var now = Instant.now();

        assertEquals(data.description(), savedPerson.getDescription());

        assertEquals(Long.valueOf(data.updatedBy()), savedPerson.getLastChgUserId());
        assertEquals(Short.valueOf((short) 3), savedPerson.getVersionCtrlNbr());
        assertEquals(Long.valueOf(data.updatedBy()), savedPerson.getLastChgUserId());
        assertTrue(savedPerson.getLastChgTime().until(now, ChronoUnit.SECONDS) < 5);
    }

    private UpdateAdministrativeData getAdministrativeData() {
        return new UpdateAdministrativeData(
                123L,
                "RequestId",
                321L,
                Instant.now(),
                "Administrative Data 1");
    }


}
