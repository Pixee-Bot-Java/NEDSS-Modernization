package gov.cdc.nbs.questionbank.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gov.cdc.nbs.authentication.entity.QAuthUser;
import gov.cdc.nbs.questionbank.entity.QPageCondMapping;
import gov.cdc.nbs.questionbank.entity.QWaTemplate;
import gov.cdc.nbs.questionbank.entity.condition.QConditionCode;
import gov.cdc.nbs.questionbank.exception.QueryException;
import gov.cdc.nbs.questionbank.page.model.PageSummary;
import gov.cdc.nbs.questionbank.page.request.PageSummaryRequest;
import gov.cdc.nbs.questionbank.question.model.Condition;

@Component
@Transactional
public class PageSummaryFinder {
    private static final QWaTemplate waTemplate = QWaTemplate.waTemplate;
    private static final QAuthUser authUser = QAuthUser.authUser;
    private static final QConditionCode conditionCode = QConditionCode.conditionCode;
    private static final QPageCondMapping conditionMapping = QPageCondMapping.pageCondMapping;

    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityManager entityManager;

    private final JPAQueryFactory factory;

    public PageSummaryFinder(
            final JPAQueryFactory factory,
            final EntityManager entityManager,
            final CriteriaBuilderFactory criteriaBuilderFactory) {
        this.factory = factory;
        this.entityManager = entityManager;
        this.criteriaBuilderFactory = criteriaBuilderFactory;
    }

    public Page<PageSummary> find(Pageable pageable) {
        // get the Id's for the page to be returned
        BlazeJPAQuery<Long> query = new BlazeJPAQuery<Tuple>(entityManager, criteriaBuilderFactory)
                .select(waTemplate.id)
                .from(waTemplate)
                .where(waTemplate.templateType.in("Draft", "Published"));
        setOrderBy(query, pageable);

        PagedList<Long> pageIds = query.fetchPage((int) pageable.getOffset(), pageable.getPageSize());

        return fetchPageSummary(pageIds, pageable, (int) pageIds.getTotalSize());
    }

    public Page<PageSummary> find(PageSummaryRequest request, Pageable pageable) {
        // searches on page name and condition
        BlazeJPAQuery<Tuple> query = findSummaryQuery(request);
        setOrderBy(query, pageable);

        var pageIds = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // cannot paginate distinct, so we have to submit a count query
        var count = findSummaryQuery(request)
                .fetchCount();

        var ids = pageIds.stream().map(t -> t.get(waTemplate.id)).toList();
        return fetchPageSummary(ids, pageable, (int) count);
    }

    /**
     * Retrieves PageSummary objects for a list of Ids
     * 
     * @param ids
     * @param pageable
     * @param totalSize
     * @return
     */
    private Page<PageSummary> fetchPageSummary(List<Long> ids, Pageable pageable, int totalSize) {
        // get the summaries based on supplied Ids
        JPAQuery<Tuple> query = factory.select(
                waTemplate.id,
                waTemplate.templateType,
                waTemplate.templateNm,
                waTemplate.descTxt,
                waTemplate.busObjType,
                waTemplate.lastChgTime,
                waTemplate.lastChgUserId,
                authUser.userFirstNm,
                authUser.userLastNm,
                conditionCode.id,
                conditionCode.conditionShortNm)
                .from(waTemplate)
                .leftJoin(authUser).on(waTemplate.lastChgUserId.eq(authUser.nedssEntryId))
                .leftJoin(conditionMapping).on(waTemplate.id.eq(conditionMapping.waTemplateUid.id))
                .leftJoin(conditionCode).on(conditionMapping.conditionCd.eq(conditionCode.id))
                .where(waTemplate.id.in(ids));
        setOrderBy(query, pageable);

        Map<Long, PageSummary> results = query.transform(
                GroupBy.groupBy(waTemplate.id)
                        .as(Projections.constructor(
                                PageSummary.class,
                                waTemplate.id,
                                waTemplate.busObjType,
                                waTemplate.templateNm,
                                waTemplate.templateType,
                                waTemplate.nndEntityIdentifier,
                                GroupBy.list(Projections.constructor(
                                        Condition.class,
                                        conditionCode.id,
                                        conditionCode.conditionShortNm)),
                                waTemplate.lastChgTime,
                                authUser.userFirstNm.concat(" ").concat(authUser.userLastNm))));
        return new PageImpl<>(new ArrayList<>(results.values()), pageable, totalSize);
    }

    /**
     * Adds order by clausees to query. First order by is the user supplied clause, second is the identifier to prevent
     * non unique sort exception. Supported fields are: id, name, eventType, status, lastUpdate
     * 
     * @param pageable
     * @return
     */
    private void setOrderBy(QueryBase<?> query, Pageable pageable) {
        Order order = pageable.getSort().get().findFirst().orElse(null);
        if (order == null) {
            query.orderBy(waTemplate.id.desc());
            return;
        }
        boolean isAscending = order.getDirection().isAscending();
        var sort = switch (order.getProperty()) {
            case "id" -> isAscending ? waTemplate.id.asc() : waTemplate.id.desc();
            case "name" -> isAscending ? waTemplate.templateNm.asc() : waTemplate.templateNm.desc();
            case "eventType" -> isAscending ? waTemplate.busObjType.asc() : waTemplate.busObjType.desc();
            case "status" -> isAscending ? waTemplate.templateType.asc() : waTemplate.templateType.desc();
            case "lastUpdate" -> isAscending ? waTemplate.lastChgTime.asc() : waTemplate.lastChgTime.desc();
            default -> throw new QueryException(
                    "Invalid sort specified. Allowed values are: [name, id, eventType , status, lastUpdate]");
        };
        query.orderBy(sort);
        if (!order.getProperty().equals("id")) {
            query.orderBy(waTemplate.id.desc());
        }
    }


    private BlazeJPAQuery<Tuple> findSummaryQuery(PageSummaryRequest request) {
        String searchString = "%" + request.search() + "%";
        return new BlazeJPAQuery<Tuple>(entityManager, criteriaBuilderFactory)
                .select(
                        waTemplate.id,
                        waTemplate.templateType,
                        waTemplate.templateNm,
                        waTemplate.busObjType,
                        waTemplate.lastChgTime)
                .distinct()
                .from(waTemplate)
                .leftJoin(conditionMapping).on(waTemplate.id.eq(conditionMapping.waTemplateUid.id))
                .leftJoin(conditionCode).on(conditionMapping.conditionCd.eq(conditionCode.id))
                // do not load legacy, template, or 'Published with Draft' pages
                // see legacy code PageManagementDAOImpl.java#2485
                .where(waTemplate.templateType.in("Draft", "Published")
                        // query template name and condition name
                        .and(waTemplate.templateNm.like(searchString)
                                .or(conditionCode.conditionShortNm.like(searchString))));
    }

}
