package gov.cdc.nbs.questionbank.pagerules;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.questionbank.page.content.rule.PageRuleDeleter;
import gov.cdc.nbs.questionbank.page.detail.PagesResponse;
import gov.cdc.nbs.questionbank.pagerules.exceptions.RuleException;
import gov.cdc.nbs.questionbank.pagerules.request.RuleRequest;
import gov.cdc.nbs.questionbank.pagerules.request.SourceQuestionRequest;
import gov.cdc.nbs.questionbank.pagerules.request.TargetQuestionRequest;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@PreAuthorize("hasAuthority('LDFADMINISTRATION-SYSTEM')")
@RequestMapping("/api/v1/pages/{id}/rules")
@SuppressWarnings("squid:S107")
public class PageRuleController {

  private final PageRuleDeleter pageRuleDeleter;
  private final PageRuleCreator pageRuleCreator;
  private final PageRuleUpdater pageRuleUpdater;
  private final PageRuleFinder pageRuleFinder;
  private final SourceQuestionFinder sourceQuestionFinder;
  private final TargetQuestionFinder targetQuestionFinder;
  private final PdfCreator pdfCreator;
  private final CsvCreator csvCreator;

  public PageRuleController(
      final TargetQuestionFinder targetQuestionFinder,
      final SourceQuestionFinder sourceQuestionFinder,
      final PageRuleDeleter pageRuleDeleter,
      final PageRuleCreator pageRuleCreator,
      final PageRuleUpdater pageRuleUpdater,
      final PageRuleFinder pageRuleFinder,
      final PdfCreator pdfCreator,
      final CsvCreator csvCreator) {
    this.pageRuleDeleter = pageRuleDeleter;
    this.sourceQuestionFinder = sourceQuestionFinder;
    this.pageRuleCreator = pageRuleCreator;
    this.pageRuleUpdater = pageRuleUpdater;
    this.pageRuleFinder = pageRuleFinder;
    this.pdfCreator = pdfCreator;
    this.csvCreator = csvCreator;
    this.targetQuestionFinder = targetQuestionFinder;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Rule createBusinessRule(
      @RequestBody RuleRequest request,
      @PathVariable("id") Long page,
      @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details) {
    return pageRuleCreator.createPageRule(request, page, details.getId());
  }

  @DeleteMapping("{ruleId}")
  public void deletePageRule(
      @PathVariable("id") Long page,
      @PathVariable Long ruleId,
      @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details) {
    pageRuleDeleter.delete(page, ruleId, details.getId());
  }

  @PutMapping("/{ruleId}")
  public Rule updatePageRule(
      @PathVariable Long ruleId,
      @RequestBody RuleRequest request,
      @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details) throws RuleException {
    return pageRuleUpdater.updatePageRule(ruleId, request, details.getId());
  }

  @GetMapping("/{ruleId}")
  public Rule viewRuleResponse(@PathVariable Long ruleId) {
    return pageRuleFinder.findByRuleId(ruleId);
  }

  @PostMapping("/search")
  public Page<Rule> findPageRule(
      @PathVariable("id") Long pageId,
      @RequestBody SearchPageRuleRequest request,
      @PageableDefault(size = 25, sort = "add_time") Pageable pageable) {
    return pageRuleFinder.searchPageRule(pageId, request, pageable);
  }

  @PostMapping("/pdf")
  public ResponseEntity<byte[]> downloadRulePdf(
      @PathVariable("id") Long pageId,
      @RequestBody SearchPageRuleRequest request,
      @PageableDefault(size = 25, sort = "add_time") Pageable pageable) {
    Page<Rule> rules = pageRuleFinder.searchPageRule(pageId, request, pageable);
    byte[] pdf = pdfCreator.create(rules.toList());
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename("ManageRulesLibrary.pdf")
                .build()
                .toString())
        .body(pdf);
  }

  @PostMapping("/csv")
  public ResponseEntity<byte[]> downloadRuleCsv(
      @PathVariable("id") Long pageId,
      @RequestBody SearchPageRuleRequest request,
      @PageableDefault(size = 25, sort = "add_time") Pageable pageable) {
    Page<Rule> rules = pageRuleFinder.searchPageRule(pageId, request, pageable);

    byte[] csv = csvCreator.create(rules.toList());
    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename("ManageRulesLibrary.csv")
                .build()
                .toString())
        .body(csv);
  }

  @GetMapping("/getAll")
  public List<Rule> getAllRules(@PathVariable("id") Long pageId) {
    return pageRuleFinder.getAllRules(pageId);
  }

  @PostMapping("/source/questions")
  public PagesResponse getSourceQuestions(@PathVariable("id") Long pageId, @RequestBody SourceQuestionRequest request) {
    return sourceQuestionFinder.filterQuestions(pageId, request);
  }

  @PostMapping("/target/questions")
  public PagesResponse getTargetQuestions(@PathVariable("id") Long pageId, @RequestBody TargetQuestionRequest request) {
    return targetQuestionFinder.filterQuestions(pageId, request);
  }
}
