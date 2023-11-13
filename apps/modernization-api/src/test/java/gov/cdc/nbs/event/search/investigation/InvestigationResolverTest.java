package gov.cdc.nbs.event.search.investigation;

import gov.cdc.nbs.event.search.InvestigationFilter;
import gov.cdc.nbs.graphql.GraphQLPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

class InvestigationResolverTest {

  @Mock
  private InvestigationFinder finder;

  @Captor
  private ArgumentCaptor<InvestigationFilter> filterCaptor;

  @Captor
  private ArgumentCaptor<Long> patientIdCaptor;

  @Captor
  private ArgumentCaptor<Pageable> pageableCaptor;

  private final int maxPageSize = 23;

  private InvestigationResolver resolver;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    resolver = new InvestigationResolver(maxPageSize, finder);
  }

  @Test
  void should_pass_filter() {
    var filter = InvestigationTestUtil.createFilter();
    resolver.findInvestigationsByFilter(filter, new GraphQLPage(maxPageSize));
    verify(finder).find(filterCaptor.capture(), Mockito.any());
    var actualFilter = filterCaptor.getValue();
    assertEquals(filter.getPatientId(), actualFilter.getPatientId());
    assertEquals(filter.getConditions(), actualFilter.getConditions());
    assertEquals(filter.getProgramAreas(), actualFilter.getProgramAreas());
    assertEquals(filter.getJurisdictions(), actualFilter.getJurisdictions());
    assertEquals(filter.getPregnancyStatus(), actualFilter.getPregnancyStatus());
    assertEquals(filter.getEventId().getInvestigationEventType(),
        actualFilter.getEventId().getInvestigationEventType());
    assertEquals(filter.getEventId(), actualFilter.getEventId());
    assertEquals(filter.getEventDate(), actualFilter.getEventDate());
    assertEquals(filter.getCreatedBy(), actualFilter.getCreatedBy());
    assertEquals(filter.getLastUpdatedBy(), actualFilter.getLastUpdatedBy());
    assertEquals(filter.getProviderFacilitySearch(), actualFilter.getProviderFacilitySearch());
    assertEquals(filter.getInvestigatorId(), actualFilter.getInvestigatorId());
    assertEquals(filter.getInvestigationStatus(), actualFilter.getInvestigationStatus());
    assertEquals(filter.getOutbreakNames(), actualFilter.getOutbreakNames());
    assertEquals(filter.getCaseStatuses(), actualFilter.getCaseStatuses());
    assertEquals(filter.getNotificationStatuses(), actualFilter.getNotificationStatuses());
    assertEquals(filter.getProcessingStatuses(), actualFilter.getProcessingStatuses());
  }

}
