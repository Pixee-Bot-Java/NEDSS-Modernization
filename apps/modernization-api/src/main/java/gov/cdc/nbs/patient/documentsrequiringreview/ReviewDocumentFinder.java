package gov.cdc.nbs.patient.documentsrequiringreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.entity.enums.RecordStatus;
import gov.cdc.nbs.entity.odse.QActRelationship;
import gov.cdc.nbs.entity.odse.QEdxEventProcess;
import gov.cdc.nbs.entity.odse.QNbsDocument;
import gov.cdc.nbs.entity.odse.QObsValueCoded;
import gov.cdc.nbs.entity.odse.QObservation;
import gov.cdc.nbs.entity.odse.QOrganization;
import gov.cdc.nbs.entity.odse.QParticipation;
import gov.cdc.nbs.entity.odse.QPerson;
import gov.cdc.nbs.entity.odse.QPersonName;
import gov.cdc.nbs.entity.srte.QConditionCode;
import gov.cdc.nbs.service.SecurityService;

@Component
public class ReviewDocumentFinder {
    private static final String UNPROCESSED = "UNPROCESSED";
    private static final String PATSBJ = "PATSBJ";
    // Document tables
    private static final QPerson PERSON = QPerson.person;
    private static final QParticipation PARTICIPATION = QParticipation.participation;
    private static final QNbsDocument DOCUMENT = QNbsDocument.nbsDocument;
    private static final QEdxEventProcess EDX_EVENT_PROCESS = QEdxEventProcess.edxEventProcess;

    // Lab + Morb report tables
    private static final QObservation OBSERVATION = QObservation.observation;
    private static final QObservation OBSERVATION_2 = new QObservation("obs2");
    private static final QParticipation PARTICIPATION_2 = new QParticipation("part2");
    private static final QOrganization ORGANIZATION = QOrganization.organization;
    private static final QPerson PERSON_2 = new QPerson("person2");
    private static final QActRelationship RELATIONSHIP = QActRelationship.actRelationship;
    private static final QObsValueCoded OBS_VALUE_CODED = QObsValueCoded.obsValueCoded;

    // Shared tables
    private static final QPersonName PERSON_NAME = QPersonName.personName;
    private static final QConditionCode CONDITION = QConditionCode.conditionCode;

    // Query alias
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String DATE_RECEIVED = "dateReceived";
    private static final String EVENT_DATE = "eventDate";
    private static final String LOCAL_ID = "localId";

    private final UserDetailsProvider detailsProvider;
    private final JPAQueryFactory queryFactory;
    private final SecurityService securityService;
    private final DocumentRequiringReviewMapper mapper = new DocumentRequiringReviewMapper();

    public ReviewDocumentFinder(
            final UserDetailsProvider detailsProvider,
            final JPAQueryFactory queryFactory,
            final SecurityService securityService) {
        this.detailsProvider = detailsProvider;
        this.queryFactory = queryFactory;
        this.securityService = securityService;
    }

    public Page<DocumentRequiringReview> find(long patient, Pageable pageable) {
        // Get the base data for documents with paging and sorting applied
        Page<DocumentRequiringReview> docs = findSortedAndPaged(patient, pageable);

        // Fill in missing data
        setLabAndMorbReportData(docs);

        return docs;
    }

    /**
     * Queries for Documents, Lab, and Morbidity reports with sorting / paging.
     */
    private Page<DocumentRequiringReview> findSortedAndPaged(long patient, Pageable pageable) {
        // Build count query
        JPAQuery<Long> countQuery = queryFactory.selectDistinct(DOCUMENT.id.coalesce(OBSERVATION.id).countDistinct());
        countQuery = addFromWhere(countQuery, patient);
        Long total = countQuery.fetchOne();

        // Build select query
        JPAQuery<Tuple> query = queryFactory.selectDistinct(
                DOCUMENT.id.coalesce(OBSERVATION.id).as(ID),
                OBSERVATION.ctrlCdDisplayForm.coalesce("Document").as(TYPE),
                DOCUMENT.addTime.coalesce(OBSERVATION.addTime).as(DATE_RECEIVED),
                DOCUMENT.addTime.coalesce(OBSERVATION.effectiveFromTime, OBSERVATION.activityToTime).as(EVENT_DATE),
                DOCUMENT.localId.coalesce(OBSERVATION.localId).as(LOCAL_ID),
                DOCUMENT.sendingFacilityNm,
                DOCUMENT.externalVersionCtrlNbr,
                OBSERVATION.electronicInd,
                CONDITION.conditionDescTxt);
        query = addFromWhere(query, patient);
        List<Tuple> results = query
                // Paging
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                // Sorting
                .orderBy(getSort(pageable))
                .fetch();
        var mappedResults = results.stream().map(mapper::toDocumentRequiringReview).filter(Objects::nonNull).toList();
        return new PageImpl<>(mappedResults, pageable, total);
    }

    private <T> JPAQuery<T> addFromWhere(JPAQuery<T> query, long patient) {
        return query
                .from(PERSON)
                // Document Tables
                .leftJoin(PARTICIPATION).on(PARTICIPATION.id.subjectEntityUid.eq(PERSON.id))
                .leftJoin(DOCUMENT).on(DOCUMENT.id.eq(PARTICIPATION.actUid.id))
                .leftJoin(EDX_EVENT_PROCESS).on(EDX_EVENT_PROCESS.nbsDocumentUid.id.eq(DOCUMENT.id))
                // Lab + Morbidity report tables
                .leftJoin(OBSERVATION).on(OBSERVATION.id.eq(PARTICIPATION.actUid.id))
                .leftJoin(PARTICIPATION_2).on(PARTICIPATION_2.actUid.id.eq(OBSERVATION.id))
                .leftJoin(ORGANIZATION).on(ORGANIZATION.id.eq(PARTICIPATION_2.id.subjectEntityUid))
                .leftJoin(PERSON_2).on(PERSON_2.id.eq(PARTICIPATION_2.id.subjectEntityUid))
                .leftJoin(RELATIONSHIP).on(RELATIONSHIP.targetActUid.id.eq(PARTICIPATION_2.actUid.id))
                .leftJoin(OBS_VALUE_CODED).on(OBS_VALUE_CODED.id.observationUid.eq(RELATIONSHIP.sourceActUid.id))
                .leftJoin(CONDITION).on(CONDITION.id.eq(DOCUMENT.cd).or(CONDITION.id.eq(OBSERVATION.cd)))
                // Return only document types user has access to
                .where(getWhereForPermissionSet(patient));
    }

    @SuppressWarnings("squid:S1452") // generic wildcard
    OrderSpecifier<?> getSort(Pageable pageable) {
        if (pageable == null) {
            return Expressions.stringPath(TYPE).desc();
        }

        return pageable.getSort()
                .stream()
                .findFirst()
                .map(m -> {
                    var direction = m.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    return switch (m.getProperty()) {
                        case TYPE -> new OrderSpecifier<>(direction, Expressions.stringPath(TYPE));
                        case DATE_RECEIVED -> new OrderSpecifier<>(direction,
                                Expressions.numberPath(Long.class, DATE_RECEIVED));
                        case EVENT_DATE -> new OrderSpecifier<>(direction,
                                Expressions.numberPath(Long.class, EVENT_DATE));
                        case LOCAL_ID -> new OrderSpecifier<>(direction, Expressions.stringPath(LOCAL_ID));
                        default -> Expressions.stringPath(TYPE).desc();
                    };
                })
                .orElse(Expressions.stringPath(TYPE).desc());
    }

    private BooleanExpression getWhereForPermissionSet(long patient) {
        NbsUserDetails userDetails = detailsProvider.getCurrentUserDetails();
        // compile a list of the 'where' clauses the user has access to
        List<BooleanExpression> whereClauses = getWhereClauses(userDetails);

        // combine the where claueses into a single ((clause1) OR (clause2)...)
        BooleanExpression combined = whereClauses.get(0);
        for (int i = 1; i < whereClauses.size(); i++) {
            combined = combined.or(whereClauses.get(i));
        }

        // get the programJurisdictionOids the user has access to
        Set<Long> programJurisdictionOids = securityService.getProgramAreaJurisdictionOids(userDetails);

        // create our final where
        return PERSON.personParentUid.id.eq(patient)
                .and(combined)
                .and(DOCUMENT.programJurisdictionOid.in(programJurisdictionOids)
                        .or(OBSERVATION.programJurisdictionOid.in(programJurisdictionOids)));
    }

    /**
     * Returns the appropriate 'WHERE' clasues given the users permission set
     * 
     * @param userDetails
     * @return
     */
    List<BooleanExpression> getWhereClauses(NbsUserDetails userDetails) {
        List<BooleanExpression> clauses = new ArrayList<>();
        if (userDetails.hasPermission("DOCUMENT", "VIEW")) {
            clauses.add(documentWhereClause());
        }
        if (userDetails.hasPermission("OBSERVATIONLABREPORT", "VIEW")) {
            clauses.add(labReportWhereClause());
        }
        if (userDetails.hasPermission("OBSERVATIONMORBIDITYREPORT", "VIEW")) {
            clauses.add(morbidityReportWhereClause());
        }
        return clauses;
    }

    private BooleanExpression documentWhereClause() {
        return DOCUMENT.recordStatusCd.eq(UNPROCESSED)
                .and(PARTICIPATION.id.typeCd.eq("SubjOfDoc"))
                .and(PARTICIPATION.actClassCd.eq("DOC"))
                .and(PARTICIPATION.subjectClassCd.eq("PSN"))
                .and(PARTICIPATION.recordStatusCd.eq(RecordStatus.ACTIVE))
                .and(EDX_EVENT_PROCESS.docEventTypeCd.in("CASE", "LabReport", "MorbReport", "CT"))
                .and(EDX_EVENT_PROCESS.parsedInd.eq('N'));
    }

    private BooleanExpression labReportWhereClause() {
        return PARTICIPATION.id.typeCd.eq(PATSBJ)
                .and(PARTICIPATION.actClassCd.eq("OBS"))
                .and(PARTICIPATION.subjectClassCd.eq("PSN"))
                .and(PARTICIPATION.recordStatusCd.eq(RecordStatus.ACTIVE))
                .and(PARTICIPATION_2.id.typeCd.in("AUT", "SPC", PATSBJ, "ORD"))
                .and(OBSERVATION.recordStatusCd.eq(UNPROCESSED));
    }

    private BooleanExpression morbidityReportWhereClause() {
        return PARTICIPATION.id.typeCd.eq("SubjOfMorbReport")
                .and(PARTICIPATION.actClassCd.eq("OBS"))
                .and(PARTICIPATION.subjectClassCd.eq("PSN"))
                .and(PARTICIPATION.recordStatusCd.eq(RecordStatus.ACTIVE))
                .and(OBSERVATION.recordStatusCd.eq(UNPROCESSED));
    }



    void setLabAndMorbReportData(Page<DocumentRequiringReview> docs) {
        List<Long> ids = docs.stream()
                .filter(d -> d.type().equals("LabReport") || d.type().equals("MorbReport"))
                .map(DocumentRequiringReview::id)
                .toList();
        List<Tuple> extendedData = fetchLabAndMorbReportData(ids);

        extendedData.forEach(row -> {
            // Which doc does the data belong to?
            DocumentRequiringReview doc = docs.stream()
                    .filter(d -> d.id().equals(row.get(OBSERVATION.id)))
                    .findFirst().orElseThrow(() -> new RuntimeException("Failed to find matching document id"));

            // What type of data are we adding?
            String type = row.get(PARTICIPATION.id.typeCd);
            if (type.equals("PhysicianOfMorb") || type.equals("ORD")) {
                doc.facilityProviders().setOrderingProvider((mapper.toOrderingProvider(row)));
            } else if (type.equals("ReporterOfMorbReport") || type.equals("AUT")) {
                doc.facilityProviders().setReportingFacility((mapper.toReportingFacility(row)));
            } else if (type.equals("SPC")) {
                doc.descriptions().add(mapper.toDescription(row));
            }
        });
    }


    private List<Tuple> fetchLabAndMorbReportData(List<Long> ids) {
        return queryFactory.select(
                OBSERVATION.id,
                PARTICIPATION.subjectClassCd,
                PARTICIPATION.id.typeCd,
                PARTICIPATION.id.subjectEntityUid,
                ORGANIZATION.displayNm,
                PERSON_NAME.nmPrefix,
                PERSON_NAME.firstNm,
                PERSON_NAME.lastNm,
                PERSON_NAME.nmSuffix,
                OBS_VALUE_CODED.displayName,
                OBSERVATION_2.cdDescTxt)
                .from(OBSERVATION)
                .leftJoin(PARTICIPATION).on(PARTICIPATION.actUid.id.eq(OBSERVATION.id))
                .leftJoin(ORGANIZATION).on(ORGANIZATION.id.eq(PARTICIPATION.id.subjectEntityUid))
                .leftJoin(PERSON_NAME).on(PERSON_NAME.personUid.id.eq(PARTICIPATION.id.subjectEntityUid))
                .join(RELATIONSHIP).on(RELATIONSHIP.targetActUid.id.eq(PARTICIPATION.actUid.id))
                .join(OBS_VALUE_CODED).on(OBS_VALUE_CODED.observationUid.id.eq(RELATIONSHIP.sourceActUid.id))
                .leftJoin(OBSERVATION_2).on(OBSERVATION_2.id.eq(OBS_VALUE_CODED.observationUid.id))
                .where(OBSERVATION.id.in(ids)
                        .and(PARTICIPATION.id.typeCd.notIn(PATSBJ, "SubjOfMorbReport"))
                        .and((PARTICIPATION.subjectClassCd.in("ORG")
                                .and(PARTICIPATION.id.typeCd.in("AUT", "ReporterOfMorbReport"))
                                .or(PARTICIPATION.subjectClassCd.in("PSN")
                                        .and(PARTICIPATION.id.typeCd.in("ORD", "PhysicianOfMorb")))
                                .or(PARTICIPATION.subjectClassCd.in("MAT")
                                        .and(PARTICIPATION.id.typeCd.in("SPC"))))))
                .fetch();
    }
}