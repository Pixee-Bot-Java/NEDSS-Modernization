package gov.cdc.nbs.patientlistener.request.update;

import gov.cdc.nbs.authentication.UserService;
import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.message.patient.event.UpdateAdministrativeData;
import gov.cdc.nbs.message.patient.event.UpdateSexAndBirthData;
import gov.cdc.nbs.patientlistener.request.PatientNotFoundException;
import gov.cdc.nbs.patientlistener.request.PatientRequestStatusProducer;
import gov.cdc.nbs.patientlistener.request.UserNotAuthorizedException;
import gov.cdc.nbs.repository.PersonRepository;
import gov.cdc.nbs.repository.elasticsearch.ElasticsearchPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
