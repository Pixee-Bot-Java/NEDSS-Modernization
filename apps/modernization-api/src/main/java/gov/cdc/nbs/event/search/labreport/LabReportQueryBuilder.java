package gov.cdc.nbs.event.search.labreport;

import gov.cdc.nbs.authorization.permission.scope.PermissionScope;
import gov.cdc.nbs.entity.elasticsearch.ElasticsearchActId;
import gov.cdc.nbs.entity.elasticsearch.ElasticsearchObservation;
import gov.cdc.nbs.entity.elasticsearch.ElasticsearchOrganizationParticipation;
import gov.cdc.nbs.entity.elasticsearch.ElasticsearchPersonParticipation;
import gov.cdc.nbs.entity.elasticsearch.Investigation;
import gov.cdc.nbs.entity.elasticsearch.LabReport;
import gov.cdc.nbs.event.search.LabReportFilter;
import gov.cdc.nbs.event.search.LabReportFilter.EntryMethod;
import gov.cdc.nbs.event.search.LabReportFilter.EventStatus;
import gov.cdc.nbs.event.search.LabReportFilter.UserType;
import gov.cdc.nbs.exception.QueryException;
import gov.cdc.nbs.time.FlexibleInstantConverter;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class LabReportQueryBuilder {
  private static final String PATIENT_SUBJECT = "PATSBJ";

  private QueryBuilder onlyLabReports() {
    return QueryBuilders.boolQuery()
        .must(QueryBuilders.termsQuery(LabReport.CLASS_CD, "OBS"))
        .must(QueryBuilders.termsQuery(LabReport.MOOD_CD, "EVN"));
  }

  private QueryBuilder withPermission(final PermissionScope scope) {
    return QueryBuilders.termsQuery(Investigation.PROGRAM_JURISDICTION_OID, scope.any());
  }


  @SuppressWarnings({"squid:S3776", "java:S6541"})
  // ignore high cognitive complexity as the method is simply going through the
  // passed in parameters, checking if null, and if not appending to the query
  public NativeSearchQuery buildLabReportQuery(
      final PermissionScope scope,
      final LabReportFilter filter,
      final Pageable pageable
  ) {

    BoolQueryBuilder builder = QueryBuilders.boolQuery();
    // Lab reports are secured by Program Area and Jurisdiction
    builder.must(onlyLabReports())
        .must(withPermission(scope));

    if (filter == null) {
      return new NativeSearchQueryBuilder()
          .withQuery(builder)
          .withSorts(buildLabReportSort(pageable))
          .withPageable(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()))
          .build();
    }

    // program area
    if (filter.getProgramAreas() != null && !filter.getProgramAreas().isEmpty()) {
      builder.must(QueryBuilders.termsQuery(LabReport.PROGRAM_AREA_CD, filter.getProgramAreas()));
    }
    // jurisdictions
    if (filter.getJurisdictions() != null && !filter.getJurisdictions().isEmpty()) {
      builder.must(QueryBuilders.termsQuery(LabReport.JURISDICTION_CD, filter.getJurisdictions()));
    }
    // pregnancy status
    if (filter.getPregnancyStatus() != null) {
      builder.must(
          QueryBuilders.termQuery(
              LabReport.PREGNANT_IND_CD,
              filter.getPregnancyStatus().value()
          )
      );
    }
    // event Id
    if (filter.getEventId() != null) {
      switch (filter.getEventId().getLabEventType()) {
        case ACCESSION_NUMBER:
          var accessionNumberQuery = QueryBuilders.boolQuery()
              .must(QueryBuilders.matchQuery(
                  LabReport.ACT_IDS + "."
                      + ElasticsearchActId.TYPE_DESC_TXT,
                  "Filler Number"))
              .must(QueryBuilders.matchQuery(
                  LabReport.ACT_IDS + "."
                      + ElasticsearchActId.ROOT_EXTENSION_TXT,
                  filter.getEventId().getLabEventId()));
          var nestedAccessionNumberQuery = QueryBuilders.nestedQuery(Investigation.ACT_IDS,
              accessionNumberQuery,
              ScoreMode.None);
          builder.must(nestedAccessionNumberQuery);
          break;
        case LAB_ID:
          builder.must(QueryBuilders.matchQuery(LabReport.LOCAL_ID, filter.getEventId().getLabEventId()));
          break;
        default:
          throw new QueryException("Invalid event type: " + filter.getEventId().getLabEventType());
      }
    }
    // event date search
    if (filter.getEventDate() != null) {
      var eds = filter.getEventDate();
      if (eds.getFrom() == null || eds.getTo() == null) {
        throw new QueryException("'From' and 'To' required when performing event date search");
      }
      String field = switch (eds.getType()) {
        case DATE_OF_REPORT -> LabReport.ACTIVITY_TO_TIME;
        case DATE_OF_SPECIMEN_COLLECTION -> LabReport.EFFECTIVE_FROM_TIME;
        case DATE_RECEIVED_BY_PUBLIC_HEALTH -> LabReport.RPT_TO_STATE_TIME;
        case LAB_REPORT_CREATE_DATE -> LabReport.ADD_TIME;
        case LAST_UPDATE_DATE -> LabReport.OBSERVATION_LAST_CHG_TIME;
        default -> throw new QueryException(
            "Invalid event date type specified: " + eds.getType());
      };
      var from = FlexibleInstantConverter.toString(eds.getFrom());
      var to = FlexibleInstantConverter.toString(eds.getTo());
      builder.must(QueryBuilders.rangeQuery(field).from(from).to(to));
    }
    // entry methods / entered by
    /*-
     * Entry Method Electronic = electronicInd: 'Y'
     * Entry Method Manual = electronicInd: 'N'
     * Entered by External = electronicInd: 'E
     */
    if ((filter.getEntryMethods() != null && !filter.getEntryMethods().isEmpty())
        || (filter.getEnteredBy() != null && !filter.getEnteredBy().isEmpty())) {
      var electronicIndCodes = new ArrayList<String>();
      if (filter.getEntryMethods() != null) {
        filter.getEntryMethods().forEach(em -> {
          if (em.equals(EntryMethod.ELECTRONIC)) {
            electronicIndCodes.add("Y");
          } else if (em.equals(EntryMethod.MANUAL)) {
            electronicIndCodes.add("N");
          }
        });
      }
      if (filter.getEnteredBy() != null && filter.getEnteredBy().contains(UserType.EXTERNAL)) {
        electronicIndCodes.add("E");
      }
      builder.must(QueryBuilders.termsQuery(LabReport.ELECTRONIC_IND, electronicIndCodes));
    }
    // event status
    if (filter.getEventStatus() != null && !filter.getEventStatus().isEmpty()) {
      if (filter.getEventStatus().contains(EventStatus.NEW)) {
        if (filter.getEventStatus().contains(EventStatus.UPDATE)) {
          builder.must(QueryBuilders.rangeQuery(LabReport.VERSION_CTRL_NBR).gte(1));
        } else {
          builder.must(QueryBuilders.matchQuery(LabReport.VERSION_CTRL_NBR, 1));
        }
      } else if (filter.getEventStatus().contains(EventStatus.UPDATE)) {
        builder.must(QueryBuilders.rangeQuery(LabReport.VERSION_CTRL_NBR).gt(1));
      }
    }

    // processing status
    if (filter.getProcessingStatus() != null && !filter.getProcessingStatus().isEmpty()) {
      var validStatuses = new ArrayList<String>();
      if (filter.getProcessingStatus().contains(LabReportFilter.ProcessingStatus.PROCESSED)) {
        validStatuses.add("PROCESSED");
      }
      if (filter.getProcessingStatus().contains(LabReportFilter.ProcessingStatus.UNPROCESSED)) {
        validStatuses.add("UNPROCESSED");
      }
      builder.must(QueryBuilders.termsQuery(LabReport.RECORD_STATUS_CD, validStatuses));
    }
    // created by
    if (filter.getCreatedBy() != null) {
      builder.must(QueryBuilders.matchQuery(LabReport.ADD_USER_ID, filter.getCreatedBy()));
    }

    // last update
    if (filter.getLastUpdatedBy() != null) {
      builder.must(QueryBuilders.matchQuery(LabReport.LAST_CHG_USER_ID, filter.getLastUpdatedBy()));
    }

    if (filter.getPatientId() != null) {
      var patientIdQuery = QueryBuilders.boolQuery();
      patientIdQuery
          .must(QueryBuilders.matchQuery(
              LabReport.PERSON_PARTICIPATIONS + "." + ElasticsearchPersonParticipation.TYPE_CD,
              PATIENT_SUBJECT));
      patientIdQuery
          .must(QueryBuilders.matchQuery(
              LabReport.PERSON_PARTICIPATIONS + "." + ElasticsearchPersonParticipation.PERSON_PARENT_UID,
              filter.getPatientId()));
      builder.must(
          QueryBuilders.nestedQuery(LabReport.PERSON_PARTICIPATIONS, patientIdQuery, ScoreMode.None));
    }

    // event provider/facility
    if (filter.getProviderSearch() != null) {
      var pSearch = filter.getProviderSearch();
      switch (pSearch.getProviderType()) {
        case ORDERING_FACILITY:
          var orderingFacilityQuery = QueryBuilders.boolQuery()
              .must(QueryBuilders.matchQuery(
                  LabReport.ORGANIZATION_PARTICIPATIONS + "."
                      + ElasticsearchOrganizationParticipation.TYPE_CD,
                  "ORD"))
              .must(QueryBuilders.matchQuery(
                  LabReport.ORGANIZATION_PARTICIPATIONS + "."
                      + ElasticsearchOrganizationParticipation.SUBJECT_CLASS_CD,
                  "ORG"))
              .must(QueryBuilders.matchQuery(
                  LabReport.ORGANIZATION_PARTICIPATIONS + "."
                      + ElasticsearchOrganizationParticipation.ENTITY_ID,
                  pSearch.getProviderId()));
          var nestedOrderingFacilityQuery = QueryBuilders.nestedQuery(LabReport.ORGANIZATION_PARTICIPATIONS,
              orderingFacilityQuery,
              ScoreMode.None);
          builder.must(nestedOrderingFacilityQuery);
          break;
        case ORDERING_PROVIDER:
          var orderingProviderQuery = QueryBuilders.boolQuery()
              .must(QueryBuilders.matchQuery(
                  LabReport.PERSON_PARTICIPATIONS + "."
                      + ElasticsearchPersonParticipation.TYPE_CD,
                  "ORD"))
              .must(QueryBuilders.matchQuery(
                  LabReport.PERSON_PARTICIPATIONS + "."
                      + ElasticsearchPersonParticipation.SUBJECT_CLASS_CD,
                  "PSN"))
              .must(QueryBuilders.matchQuery(
                  LabReport.PERSON_PARTICIPATIONS + "."
                      + ElasticsearchPersonParticipation.ENTITY_ID,
                  pSearch.getProviderId()));
          var nestedOrderingProviderQuery = QueryBuilders.nestedQuery(LabReport.PERSON_PARTICIPATIONS,
              orderingProviderQuery,
              ScoreMode.None);
          builder.must(nestedOrderingProviderQuery);
          break;
        case REPORTING_FACILITY:
          var reportingFacilityQuery = QueryBuilders.boolQuery()
              .must(QueryBuilders.matchQuery(
                  LabReport.ORGANIZATION_PARTICIPATIONS + "."
                      + ElasticsearchOrganizationParticipation.TYPE_CD,
                  "AUT"))
              .must(QueryBuilders.matchQuery(
                  LabReport.ORGANIZATION_PARTICIPATIONS + "."
                      + ElasticsearchOrganizationParticipation.ENTITY_ID,
                  pSearch.getProviderId()));
          var nestedReportingFacilityQuery = QueryBuilders.nestedQuery(LabReport.ORGANIZATION_PARTICIPATIONS,
              reportingFacilityQuery,
              ScoreMode.None);
          builder.must(nestedReportingFacilityQuery);
          break;
      }
    }
    // resulted test
    if (filter.getResultedTest() != null) {
      var resultedTestQuery = QueryBuilders.boolQuery()
          .must(QueryBuilders.matchQuery(
              LabReport.OBSERVATIONS_FIELD + "." + ElasticsearchObservation.CD_DESC_TXT,
              filter.getResultedTest()));
      var nestedResultedTestQuery = QueryBuilders.nestedQuery(LabReport.OBSERVATIONS_FIELD, resultedTestQuery,
          ScoreMode.None);
      builder.must(nestedResultedTestQuery);
    }

    // coded result
    if (filter.getCodedResult() != null) {
      var codedResultQuery = QueryBuilders.boolQuery()
          .must(QueryBuilders.matchQuery(
              LabReport.OBSERVATIONS_FIELD + "." + ElasticsearchObservation.DISPLAY_NAME,
              filter.getCodedResult()));
      var nestedCodedResultQuery = QueryBuilders.nestedQuery(LabReport.OBSERVATIONS_FIELD, codedResultQuery,
          ScoreMode.None);
      builder.must(nestedCodedResultQuery);
    }

    return new NativeSearchQueryBuilder()
        .withQuery(builder)
        .withSorts(buildLabReportSort(pageable))
        .withPageable(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()))
        .build();
  }

  private Collection<SortBuilder<?>> buildLabReportSort(Pageable pageable) {
    if (pageable.getSort().isEmpty()) {
      return new ArrayList<>();
    }
    Collection<SortBuilder<?>> sorts = new ArrayList<>();
    pageable.getSort().stream().forEach(sort -> {
      switch (sort.getProperty()) {
        case "lastNm":
          sorts.add(createNestedSortWithFilter(
              LabReport.PERSON_PARTICIPATIONS,
              ElasticsearchPersonParticipation.LAST_NAME + ".keyword",
              ElasticsearchPersonParticipation.TYPE_CD,
              PATIENT_SUBJECT,
              sort.getDirection()));
          break;
        case "birthTime":
          sorts.add(createNestedSortWithFilter(
              LabReport.PERSON_PARTICIPATIONS,
              ElasticsearchPersonParticipation.BIRTH_TIME,
              ElasticsearchPersonParticipation.TYPE_CD,
              PATIENT_SUBJECT,
              sort.getDirection()));
          break;
        default:
          sorts.add(SortBuilders.scoreSort());
      }
    });
    return sorts;
  }

  /**
   * Creates a sort that adhere to the following format
   *
   * <pre>
   * "sort" : [
   *  {
   *    "nestedField.childField": {
   *      "order" : "asc",
   *      "nested": {
   *        "path": "nestedField",
   *        "filter": {
   *          "term" : {"nestedField.filterField" : "filterValue"}
   *        }
   *      }
   *   }
   *  }
   * ]
   *
   * </pre>
   */
  private FieldSortBuilder createNestedSortWithFilter(String nestedField, String childField, String filterField,
      String filterValue, Direction direction) {
    return SortBuilders
        .fieldSort(nestedField + "." + childField)
        .order(direction == Direction.ASC ? SortOrder.ASC : SortOrder.DESC)
        .setNestedSort(new NestedSortBuilder(nestedField)
            .setFilter(
                QueryBuilders.termQuery(nestedField + "." + filterField, filterValue)));
  }

}
