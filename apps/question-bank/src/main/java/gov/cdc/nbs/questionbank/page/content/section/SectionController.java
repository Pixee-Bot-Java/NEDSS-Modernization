package gov.cdc.nbs.questionbank.page.content.section;


import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.page.content.section.request.CreateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.request.UpdateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.response.CreateSectionResponse;
import gov.cdc.nbs.questionbank.page.content.section.response.DeleteSectionResponse;
import gov.cdc.nbs.questionbank.page.content.section.response.UpdateSectionResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/pages/{page}/sections/")
@PreAuthorize("hasAuthority('LDFADMINISTRATION-SYSTEM')")
public class SectionController {

    private final SectionCreator creator;

    private final UserDetailsProvider userDetailsProvider;

    public SectionController(SectionCreator createSectionService, UserDetailsProvider userDetailsProvider) {
        this.userDetailsProvider = userDetailsProvider;
        this.creator = createSectionService;
    }

    @PostMapping
    @ResponseBody
    public CreateSectionResponse createSection(
            @PathVariable("page") Long page,
            @RequestBody CreateSectionRequest request) {
        Long userId = userDetailsProvider.getCurrentUserDetails().getId();
        return creator.createSection(page, userId, request);
    }

    @DeleteMapping("{sectionId}")
    @ResponseBody
    public DeleteSectionResponse deleteSection(@PathVariable("page") Long page, @PathVariable("sectionId") Long sectionId) {
        return creator.deleteSection(page, sectionId);
    }

    @PutMapping("{sectionId}")
    @ResponseBody
    public UpdateSectionResponse updateSection(@PathVariable("sectionId") Long sectionId, @RequestBody UpdateSectionRequest request) {
        return creator.updateSection(sectionId, request);
    }

}
