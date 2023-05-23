package gov.cdc.nbs.patientlistener.request.update;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import gov.cdc.nbs.authentication.UserService;
import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.message.enums.Deceased;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.message.enums.Suffix;
import gov.cdc.nbs.message.patient.event.AddNameData;
import gov.cdc.nbs.message.patient.event.UpdateAddressData;
import gov.cdc.nbs.message.patient.event.UpdateEmailData;
import gov.cdc.nbs.message.patient.event.UpdateIdentificationData;
import gov.cdc.nbs.message.patient.event.UpdateNameData;
import gov.cdc.nbs.message.patient.event.UpdatePhoneData;
import gov.cdc.nbs.message.patient.event.DeleteAddressData;
import gov.cdc.nbs.message.patient.event.DeleteEmailData;
import gov.cdc.nbs.message.patient.event.DeleteIdentificationData;
import gov.cdc.nbs.message.patient.event.DeleteNameData;
import gov.cdc.nbs.message.patient.event.DeletePhoneData;
import gov.cdc.nbs.message.patient.event.UpdateAdministrativeData;
import gov.cdc.nbs.message.patient.event.UpdateGeneralInfoData;
import gov.cdc.nbs.message.patient.event.UpdateMortalityData;
import gov.cdc.nbs.message.patient.event.UpdateSexAndBirthData;
import gov.cdc.nbs.message.patient.event.UpdateEthnicityData;
import gov.cdc.nbs.message.patient.input.PatientInput.NameUseCd;
import gov.cdc.nbs.message.patient.input.PatientInput.PhoneType;
import gov.cdc.nbs.patientlistener.request.PatientNotFoundException;
import gov.cdc.nbs.patientlistener.request.UserNotAuthorizedException;
import gov.cdc.nbs.patientlistener.request.PatientRequestStatusProducer;
import gov.cdc.nbs.repository.PersonRepository;
import gov.cdc.nbs.repository.elasticsearch.ElasticsearchPersonRepository;

class PatientUpdateRequestHandlerTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UserService userService;
    @Mock
    private PatientUpdater patientUpdater;
    @Mock
    private PatientRequestStatusProducer statusProducer;
    @Mock
    private ElasticsearchPersonRepository elasticsearchPersonRepository;

    @InjectMocks
    private PatientUpdateRequestHandler updateHandler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenInfoUpdateSuccess() {
        var data = getGeneralInfoData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientGeneralInfoUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testGenInfoUpdateUnauthorized() {
        var data = getGeneralInfoData();
        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        // call handle update gen info
        try {
            updateHandler.handlePatientGeneralInfoUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());

    }

    @Test
    void testGenInfoUpdateNoPatient() {
        var data = getGeneralInfoData();
        // set authorized = true, patient = null
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        PatientNotFoundException ex = null;

        // call handle update gen info
        try {
            updateHandler.handlePatientGeneralInfoUpdate(data);
        } catch (PatientNotFoundException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    private UpdateGeneralInfoData getGeneralInfoData() {
        return new UpdateGeneralInfoData(123L,
                "RequestId",
                321L,
                Instant.now(),
                "marital status",
                "mothers maiden name",
                (short) 1,
                (short) 2,
                "occupation code",
                "education level",
                "prim language",
                "speaks english",
                "eharsId");
    }

    @Test
    void testMortalityInfoUpdateSuccess() {
        var data = getMortalityData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        updateHandler.handlePatientMortalityUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testMortalityInfoUpdateUnauthorized() {
        var data = getMortalityData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientMortalityUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testMortalityInfoUpdateNoPatient() {
        var data = getMortalityData();

        // set authorized = true, patient = null
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        PatientNotFoundException ex = null;

        try {
            updateHandler.handlePatientMortalityUpdate(data);
        } catch (PatientNotFoundException e) {
            ex = e;
        }

        // verify save requests are not called, failure status sent
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
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
    void testSexInfoUpdateSuccess() {
        var data = getSexAndBirthData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        updateHandler.handlePatientSexAndBirthUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testSexAndBirthUpdateUnauthorized() {
        var data = getSexAndBirthData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientSexAndBirthUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testSexAndBirthUpdateNoPatient() {
        var data = getSexAndBirthData();

        // set authorized = true, patient = null
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        PatientNotFoundException ex = null;

        try {
            updateHandler.handlePatientSexAndBirthUpdate(data);
        } catch (PatientNotFoundException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
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
    void testAdministrativeUpdateSuccess() {
        var data = getAdministrativeData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientAdministrativeUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateAdministrativeData getAdministrativeData() {
        return new UpdateAdministrativeData(
                123L,
                "RequestId",
                321L,
                Instant.now(),
                "Administrative Data 1");
    }

    @Test
    void testAdministrativeInfoUpdateUnauthorized() {
        var data = getAdministrativeData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientAdministrativeUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testAddNameSuccess() {
        var data = getAddNameData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientNameAdd(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private AddNameData getAddNameData() {
        return new AddNameData(123L,
                "RequestId",
                321L,
                Instant.now(),
                "First Name",
                "Middle Name",
                "Last Name",
                Suffix.III,
                NameUseCd.L);
    }

    @Test
    void testNameAddUnauthorized() {
        var data = getAddNameData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientNameAdd(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testUpdateNameSuccess() {
        var data = getUpdateNameData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientNameUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateNameData getUpdateNameData() {
        return new UpdateNameData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now(),
                "First Name",
                "Middle Name",
                "Last Name",
                Suffix.III,
                NameUseCd.L.toString());
    }

    @Test
    void testNameUpdateUnauthorized() {
        var data = getUpdateNameData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientNameUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testDeleteNameSuccess() {
        var data = getDeleteNameData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientNameDelete(
                data.requestId(),
                data.patientId(),
                data.personNameSeq(),
                data.updatedBy());

        // verify save requests called, success status sent
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(321L));
    }

    private DeleteNameData getDeleteNameData() {
        return new DeleteNameData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void testNameDeleteUnauthorized() {
        var data = getDeleteNameData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientNameDelete(
                    data.requestId(),
                    data.patientId(),
                    data.personNameSeq(),
                    data.updatedBy());
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testEthnicityUpdateSuccess() {
        var data = getEthnicityData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientEthnicityUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateEthnicityData getEthnicityData() {
        return new UpdateEthnicityData(
                123L,
                "RequestId",
                321L,
                Instant.now(),
                "Ethnicity Data 1");
    }

    @Test
    void testEthnicityInfoUpdateUnauthorized() {
        var data = getEthnicityData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientEthnicityUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testAddAddressSuccess() {
        var data = getAddressData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientAddressAdd(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testAddressAddUnauthorized() {
        var data = getAddressData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientAddressAdd(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testUpdateAddressSuccess() {
        var data = getAddressData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientAddressUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateAddressData getAddressData() {
        return new UpdateAddressData(123L,
                321L,
                "RequestId",
                321L,
                Instant.now(),
                "SA1",
                "SA2",
                "City",
                "State",
                "County",
                "Country",
                "Zip",
                "Census Tract");
    }

    @Test
    void testAddressUpdateUnauthorized() {
        var data = getAddressData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientAddressUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testDeleteAddressSuccess() {
        var data = getDeleteAddressData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientAddressDelete(data.requestId(), data.patientId(), data.id(),
                data.updatedBy());

        // verify save requests called, success status sent
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(321L));
    }

    private DeleteAddressData getDeleteAddressData() {
        return new DeleteAddressData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void testAddressDeleteUnauthorized() {
        var data = getDeleteAddressData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientAddressDelete(data.requestId(), data.patientId(), data.id(),
                    data.updatedBy());
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testAddPhoneSuccess() {
        var data = getPhoneData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientPhoneAdd(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testPhoneAddUnauthorized() {
        var data = getPhoneData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientPhoneAdd(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testUpdatePhoneSuccess() {
        var data = getPhoneData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientPhoneUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdatePhoneData getPhoneData() {
        return new UpdatePhoneData(123L,
                321L,
                "RequestId",
                321L,
                Instant.now(),
                "3145551212",
                "123",
                PhoneType.CELL);
    }

    @Test
    void testPhoneUpdateUnauthorized() {
        var data = getPhoneData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientPhoneUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testDeletePhoneSuccess() {
        var data = getDeletePhoneData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientPhoneDelete(data.requestId(), data.patientId(), data.id(),
                data.updatedBy());

        // verify save requests called, success status sent
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(321L));
    }

    private DeletePhoneData getDeletePhoneData() {
        return new DeletePhoneData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void testPhoneDeleteUnauthorized() {
        var data = getDeletePhoneData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientPhoneDelete(data.requestId(), data.patientId(), data.id(),
                    data.updatedBy());
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testAddEmailSuccess() {
        var data = getEmailData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientEmailAdd(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testEmailAddUnauthorized() {
        var data = getEmailData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientEmailAdd(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testUpdateEmailSuccess() {
        var data = getEmailData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientEmailUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateEmailData getEmailData() {
        return new UpdateEmailData(123L,
                321L,
                "RequestId",
                321L,
                Instant.now(),
                "email@test.com");
    }

    @Test
    void testEmailUpdateUnauthorized() {
        var data = getEmailData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientEmailUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testDeleteEmailSuccess() {
        var data = getDeleteEmailData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientEmailDelete(data.requestId(), data.patientId(), data.id(),
                data.updatedBy());

        // verify save requests called, success status sent
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(321L));
    }

    private DeleteEmailData getDeleteEmailData() {
        return new DeleteEmailData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void testEmailDeleteUnauthorized() {
        var data = getDeleteEmailData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientEmailDelete(data.requestId(), data.patientId(), data.id(),
                    data.updatedBy());
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testAddIdentificationSuccess() {
        var data = getIdentificationData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientIdentificationAdd(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    @Test
    void testIdentificationAddUnauthorized() {
        var data = getIdentificationData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientIdentificationAdd(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testUpdateIdentificationSuccess() {
        var data = getIdentificationData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientIdentificationUpdate(data);

        // verify save requests called, success status sent
        verify(patientUpdater, times(1)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(1)).save(Mockito.any());
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(123L));
    }

    private UpdateIdentificationData getIdentificationData() {
        return new UpdateIdentificationData(123L,
                (short) 321,
                "RequestId",
                321L,
                Instant.now(),
                "123",
                "assigningauth",
                "ssn");
    }

    @Test
    void testIdentificationUpdateUnauthorized() {
        var data = getIdentificationData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientIdentificationUpdate(data);
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }

    @Test
    void testDeleteIdentificationSuccess() {
        var data = getDeleteIdentificationData();

        // set valid mock returns
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person(123L, "localId")));
        when(patientUpdater.update(Mockito.any(), eq(data))).thenAnswer(i -> i.getArguments()[0]);

        // call handle update gen info
        updateHandler.handlePatientIdentificationDelete(data.requestId(), data.patientId(), data.id(),
                data.updatedBy());

        // verify save requests called, success status sent
        verify(statusProducer, times(1)).successful(eq("RequestId"), Mockito.anyString(), eq(321L));
    }

    private DeleteIdentificationData getDeleteIdentificationData() {
        return new DeleteIdentificationData(123L,
                (short) 1,
                "RequestId",
                321L,
                Instant.now());
    }

    @Test
    void testIdentificationDeleteUnauthorized() {
        var data = getDeleteIdentificationData();

        // set unauthorized mock
        when(userService.isAuthorized(eq(321L), Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        UserNotAuthorizedException ex = null;

        try {
            updateHandler.handlePatientIdentificationDelete(data.requestId(), data.patientId(), data.id(),
                    data.updatedBy());
        } catch (UserNotAuthorizedException e) {
            ex = e;
        }

        // verify exception thrown, save requests are not called
        assertNotNull(ex);
        verify(patientUpdater, times(0)).update(Mockito.any(), eq(data));
        verify(elasticsearchPersonRepository, times(0)).save(Mockito.any());
    }
}
