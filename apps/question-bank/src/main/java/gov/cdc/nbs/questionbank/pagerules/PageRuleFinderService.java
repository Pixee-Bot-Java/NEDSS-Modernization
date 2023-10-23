package gov.cdc.nbs.questionbank.pagerules;

import gov.cdc.nbs.questionbank.model.ViewRuleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageRuleFinderService {

    ViewRuleResponse getRuleResponse(Long ruleId);

    Page<ViewRuleResponse> getAllPageRule(Pageable pageRequest,Long page);

    Page<ViewRuleResponse> findPageRule(SearchPageRuleRequest request, Pageable pageable);
}
