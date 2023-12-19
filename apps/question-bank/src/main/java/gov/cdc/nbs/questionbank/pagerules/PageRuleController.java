package gov.cdc.nbs.questionbank.pagerules;


import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.model.ViewRuleResponse;
import gov.cdc.nbs.questionbank.page.content.rule.PageRuleDeleter;
import gov.cdc.nbs.questionbank.pagerules.exceptions.RuleException;
import gov.cdc.nbs.questionbank.pagerules.response.CreateRuleResponse;
import gov.cdc.nbs.questionbank.model.CreateRuleRequest;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@PreAuthorize("hasAuthority('LDFADMINISTRATION-SYSTEM')")
@RequestMapping("/api/v1/pages/{page}/rules")
public class PageRuleController {

  private final PageRuleService pageRuleService;

  private final UserDetailsProvider userDetailsProvider;

  private final PageRuleFinderService pageRuleFinderService;


  private final PageRuleDeleter pageRuleDeleter;

  private final PageRuleCreator pageRuleCreator;

  public PageRuleController(PageRuleService pageRuleService, UserDetailsProvider userDetailsProvider,
      PageRuleFinderService pageRuleFinderService,
      PageRuleDeleter pageRuleDeleter, PageRuleCreator pageRuleCreator) {
    this.userDetailsProvider = userDetailsProvider;
    this.pageRuleService = pageRuleService;
    this.pageRuleFinderService = pageRuleFinderService;
    this.pageRuleDeleter = pageRuleDeleter;
    this.pageRuleCreator = pageRuleCreator;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public CreateRuleResponse createBusinessRule(
      @RequestBody CreateRuleRequest request,
      @PathVariable Long page,
      @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details) {
    try {
      return pageRuleCreator.createPageRule(details.getId(), request, page);
    } catch (RuleException e) {
      return new CreateRuleResponse(null, "Error in Creating a Rules");
    }
  }

  @DeleteMapping("/{ruleId}")
  @ResponseBody
  public void deletePageRule(
      @PathVariable("page") Long page,
      @PathVariable Long ruleId,
      @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details
  ) {
    pageRuleDeleter.delete(page, ruleId, details.getId());
  }

  @PutMapping("/{ruleId}")
  @ResponseBody
  public CreateRuleResponse updatePageRule(@PathVariable Long ruleId,
      @RequestBody CreateRuleRequest request, @PathVariable Long page) throws RuleException {
    Long userId = userDetailsProvider.getCurrentUserDetails().getId();
    return pageRuleService.updatePageRule(ruleId, request, userId, page);
  }

  @GetMapping("/{ruleId}")
  @ResponseBody
  public ViewRuleResponse viewRuleResponse(@PathVariable Long ruleId) {
    return pageRuleFinderService.getRuleResponse(ruleId);
  }

  @GetMapping
  @ResponseBody
  public Page<ViewRuleResponse> getAllPageRule(@PageableDefault(size = 25) Pageable pageable,
      @PathVariable Long page) {
    return pageRuleFinderService.getAllPageRule(pageable, page);
  }

  @PostMapping("/search")
  @ResponseBody
  public Page<ViewRuleResponse> findPageRule(@RequestBody SearchPageRuleRequest request,
      @PageableDefault(size = 25) Pageable pageable) {
    return pageRuleFinderService.findPageRule(request, pageable);
  }
}
