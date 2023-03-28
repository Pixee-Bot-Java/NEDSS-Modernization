package gov.cdc.nbs.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import gov.cdc.nbs.entity.odse.Observation;
import gov.cdc.nbs.repository.ObservationRepository;
import gov.cdc.nbs.repository.ParticipationRepository;
import gov.cdc.nbs.repository.PersonRepository;
import gov.cdc.nbs.repository.elasticsearch.ElasticsearchPersonRepository;
import gov.cdc.nbs.service.EventService;
import gov.cdc.nbs.util.Constants;


class FindMorbidityReportsForPatientTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private ObservationRepository oboservationRepository;

    @Mock
    private ElasticsearchPersonRepository elasticPersonRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void FindMorbidityReportsTest() {
        Long patientID = 100543L;
        Long personsIDs = patientID + 1;
        Long actIDs = personsIDs + 1;
        Long resultID = patientID + 2;

        when(personRepository.getPersonIdsByPersonParentId(Mockito.anyLong())).thenReturn(List.of(patientID + 1));
        when(participationRepository.findIdActUidByIdTypeCdAndIdSubjectEntityUidIn(Constants.REPORT_TYPE,
                List.of(personsIDs))).thenReturn(List.of(personsIDs + 1));
        Observation obs = new Observation();
        obs.setId(actIDs);
        when(oboservationRepository.findByIdIn(List.of(actIDs))).thenReturn(List.of(obs));

        List<Observation> observations = eventService.findMorbidityReportsForPatient(patientID);
        assertNotNull(observations);
        assertTrue(observations.size() > 0);
        assertEquals(observations.get(0).getId(), resultID);
    }

}
