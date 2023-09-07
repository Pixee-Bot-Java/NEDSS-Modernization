package gov.cdc.nbs.questionbank.page.content.subsection;


import gov.cdc.nbs.questionbank.page.content.subsection.request.UpdateSubSectionRequest;
import gov.cdc.nbs.questionbank.page.content.subsection.response.DeleteSubSectionResponse;
import gov.cdc.nbs.questionbank.page.content.subsection.response.UpdateSubSectionResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.page.content.subsection.request.CreateSubSectionRequest;
import gov.cdc.nbs.questionbank.page.content.subsection.response.CreateSubSectionResponse;


@RestController
@RequestMapping("/api/v1/pages/{page}/subsections/")
@PreAuthorize("hasAuthority('LDFADMINISTRATION-SYSTEM')")
public class SubSectionController {

    private final SubSectionCreator creator;

    private final UserDetailsProvider userDetailsProvider;

    public SubSectionController(SubSectionCreator createSubSectionService,
            UserDetailsProvider userDetailsProvider) {
        this.userDetailsProvider = userDetailsProvider;
        this.creator = createSubSectionService;
    }

    @PostMapping
    @ResponseBody
    public CreateSubSectionResponse createSubSection(
            @PathVariable("page") Long page,
            @RequestBody CreateSubSectionRequest request) {
        Long userId = userDetailsProvider.getCurrentUserDetails().getId();
        return creator.createSubSection(page, userId, request);
    }


    @DeleteMapping("{subSectionId}")
    @ResponseBody
    public DeleteSubSectionResponse deleteSubSection(@PathVariable("page") Long page, @PathVariable Long subSectionId) {
        return creator.deleteSubSection(page, subSectionId);
    }

    @PutMapping("{subSectionId}")
    @ResponseBody
    public UpdateSubSectionResponse updateSubSection(@PathVariable("subSectionId") Long subSectionId, @RequestBody UpdateSubSectionRequest request) {
        return creator.updateSubSection(subSectionId, request);
    }

}
