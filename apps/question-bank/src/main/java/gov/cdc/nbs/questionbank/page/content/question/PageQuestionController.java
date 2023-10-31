package gov.cdc.nbs.questionbank.page.content.question;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.questionbank.page.content.question.request.AddQuestionRequest;
import gov.cdc.nbs.questionbank.page.content.question.response.AddQuestionResponse;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/pages/{page}/questions/")
@PreAuthorize("hasAuthority('LDFADMINISTRATION-SYSTEM')")
public class PageQuestionController {

    private final PageQuestionCreator creator;


    public PageQuestionController(final PageQuestionCreator contentManager) {
        this.creator = contentManager;
    }

    @PostMapping
    public AddQuestionResponse addQuestionToPage(
            @PathVariable("page") Long pageId,
            @RequestBody AddQuestionRequest request,
            @ApiIgnore @AuthenticationPrincipal final NbsUserDetails details) {
        return creator.addQuestion(pageId, request, details.getId());
    }
}
