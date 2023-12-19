package gov.cdc.nbs.questionbank.pagerules;

import gov.cdc.nbs.questionbank.model.CreateRuleRequest;
import gov.cdc.nbs.questionbank.pagerules.exceptions.RuleException;
import gov.cdc.nbs.questionbank.pagerules.response.CreateRuleResponse;

public interface PageRuleService {

    CreateRuleResponse createPageRule(Long userId, CreateRuleRequest request,Long page) throws RuleException;


    CreateRuleResponse updatePageRule(Long ruleId, CreateRuleRequest request, Long userId,Long page) throws RuleException;

}
