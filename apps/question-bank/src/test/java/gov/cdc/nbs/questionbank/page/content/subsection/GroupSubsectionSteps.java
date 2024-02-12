package gov.cdc.nbs.questionbank.page.content.subsection;

import gov.cdc.nbs.authentication.UserDetailsProvider;
import gov.cdc.nbs.questionbank.entity.WaTemplate;
import gov.cdc.nbs.questionbank.entity.WaUiMetadata;
import gov.cdc.nbs.questionbank.entity.question.WaQuestion;
import gov.cdc.nbs.questionbank.entity.repository.WaUiMetadataRepository;
import gov.cdc.nbs.questionbank.exception.BadRequestException;
import gov.cdc.nbs.questionbank.page.PageMother;
import gov.cdc.nbs.questionbank.page.content.question.PageQuestionController;
import gov.cdc.nbs.questionbank.page.content.question.request.AddQuestionRequest;
import gov.cdc.nbs.questionbank.page.content.question.response.AddQuestionResponse;
import gov.cdc.nbs.questionbank.page.content.staticelement.PageStaticController;
import gov.cdc.nbs.questionbank.page.content.subsection.exception.UpdateSubSectionException;
import gov.cdc.nbs.questionbank.page.content.subsection.request.GroupSubSectionRequest;
import gov.cdc.nbs.questionbank.support.ExceptionHolder;
import gov.cdc.nbs.questionbank.support.QuestionMother;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Transactional
public class GroupSubsectionSteps {

    @Autowired
    private SubSectionController subsectionController;

    @Autowired
    private PageMother pageMother;

    @Autowired
    private ExceptionHolder exceptionHolder;

    @Autowired
    private UserDetailsProvider user;


    @Autowired
    PageQuestionController pageQuestionController;

    @Autowired
    PageStaticController pageStaticController;

    @Autowired
    private QuestionMother questionMother;

    @Autowired
    private WaUiMetadataRepository repository;

    private List<Long> questionsIds = new ArrayList<>();
    private Long staticElementId;

    @Given("i add a list of questions to a subsection")
    public void i_add_a_list_of_questions_to_a_subsection() {
        WaTemplate page = pageMother.one();
        WaUiMetadata section = getSection(page);
        List<WaQuestion> questionsList = questionMother.list(2);
        try {
            for (WaQuestion question : questionsList) {
                AddQuestionResponse response = pageQuestionController.addQuestionToPage(
                    page.getId(),
                    section.getId(),
                    new AddQuestionRequest(Arrays.asList(question.getId())),
                    user.getCurrentUserDetails());
                questionsIds.add(response.ids().get(0));
            }
        } catch (AccessDeniedException e) {
            exceptionHolder.setException(e);
        } catch (AuthenticationCredentialsNotFoundException e) {
            exceptionHolder.setException(e);
        }
    }

    @When("I send a group subsection request")
    public void i_send_a_group_subsection_request() {
        WaTemplate page = pageMother.one();
        WaUiMetadata section = getSection(page);
        try {
            subsectionController.groupSubSection(
                page.getId(),
                new GroupSubSectionRequest(
                    section.getId(),
                    "BLOCK_NAME",
                    getBatchList()),
                user.getCurrentUserDetails());
        } catch (AccessDeniedException e) {
            exceptionHolder.setException(e);
        } catch (AuthenticationCredentialsNotFoundException e) {
            exceptionHolder.setException(e);
        } catch (BadRequestException e) {
            exceptionHolder.setException(e);
        }
    }

    @Then("the subsection is grouped")
    public void the_subsection_is_grouped() {
        assertNull(exceptionHolder.getException());
    }

    @Then("An Update SubSection Exception is thrown")
    public void the_subsection_is_not_grouped() {
        assertTrue(exceptionHolder.getException() instanceof UpdateSubSectionException);
    }

    List<GroupSubSectionRequest.Batch> getBatchList() {
        List<GroupSubSectionRequest.Batch> batchList = new ArrayList<>();
        if (staticElementId != null) {
            batchList.add(new GroupSubSectionRequest.Batch(staticElementId, 'Y', "header_" + staticElementId, 50));
            batchList.add(new GroupSubSectionRequest.Batch(questionsIds.get(0), 'Y', "header_" + questionsIds.get(0), 50));
        } else {
            if (!questionsIds.isEmpty()) {
                batchList.add(new GroupSubSectionRequest.Batch(questionsIds.get(0), 'Y', "header_" + questionsIds.get(0), 50));
                batchList.add(new GroupSubSectionRequest.Batch(questionsIds.get(1), 'Y', "header_" + questionsIds.get(1), 50));
            }
        }
        return batchList;
    }

    WaUiMetadata getSection(WaTemplate page) {
        WaUiMetadata section = page.getUiMetadata().stream()
            .filter(u -> u.getNbsUiComponentUid() == 1016l)
            .findFirst()
            .orElseThrow();
        return section;
    }
}
