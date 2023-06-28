package gov.cdc.nbs.questionbank.question.service;

import gov.cdc.nbs.questionbank.question.VocabSearchController;
import gov.cdc.nbs.questionbank.question.client.PhinVadsClient;
import gov.cdc.nbs.questionbank.question.model.Compose;
import gov.cdc.nbs.questionbank.question.model.Concept;
import gov.cdc.nbs.questionbank.question.model.Include;
import gov.cdc.nbs.questionbank.question.response.PhinvadsValueSetByIDData;
import gov.cdc.nbs.questionbank.question.response.ValueSetByOIDResponse;
import gov.cdc.nbs.questionbank.question.response.ValueSetByOIDResults;
import gov.cdc.nbs.questionbank.question.response.ValueSetConcept;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VocabSearchServiceTest {

    @Mock
    PhinVadsClient phinVadsClient;

    @InjectMocks
    private VocabSearchService vocabSearchService;

    @Test
    void should_return_valueset_by_oid_exist() {
        PhinvadsValueSetByIDData phinvadsValueSetByIDData = new PhinvadsValueSetByIDData();
        phinvadsValueSetByIDData.setDescription("Test Value");
        Compose compose = new Compose();
        List<Include> includeList = new ArrayList<>();
        Include include = new Include();
        Concept c1 = Concept.builder().code("XXX").display("Display Name").build();
        Concept c2 = Concept.builder().code("XXX").display("Display Name").build();
        List<Concept> concepts = new ArrayList<>();
        concepts.add(c1);
        concepts.add(c2);
        include.setConcept(concepts);
        includeList.add(include);
        includeList.add(include);
        compose.setInclude(includeList);
        phinvadsValueSetByIDData.setCompose(compose);
        when(phinVadsClient.getValueSetByOID("2.16.840.1.114222.4.11.878")).thenReturn(phinvadsValueSetByIDData);
        ValueSetByOIDResults results = vocabSearchService.fetchValueSetInfoByOID("2.16.840.1.114222.4.11.878");
        assertEquals(phinvadsValueSetByIDData.getDescription(), results.getValueSetDesc());
    }

    @Test
    void should_return_exception_when_oid_not_found() {
        when(phinVadsClient.getValueSetByOID("2.16.840.1.114222.4.11.000")).thenThrow(ResponseStatusException.class);
        try {
            ValueSetByOIDResults results = vocabSearchService.fetchValueSetInfoByOID("2.16.840.1.114222.4.11.000");
        }catch (ResponseStatusException e){
            assertTrue(e instanceof  ResponseStatusException);
        }
    }

    @Test
    void test_expected_exception() {
        when(phinVadsClient.getValueSetByOID("2.16.840.1.114222.4.11.000")).thenReturn(null);
        Exception exception =assertThrows(RuntimeException.class, ()-> {
            vocabSearchService.fetchValueSetInfoByOID("2.16.840.1.114222.4.11.000").getValueSetDesc().replaceAll("</?.*?>", "");
        });
        assertTrue(exception instanceof  NullPointerException);

    }
}