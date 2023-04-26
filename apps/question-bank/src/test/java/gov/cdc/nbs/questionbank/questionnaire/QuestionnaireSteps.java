package gov.cdc.nbs.questionbank.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import gov.cdc.nbs.questionbank.support.QuestionnaireMother;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@Transactional
public class QuestionnaireSteps {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireResolver resolver;

    private Questionnaire search;
    private Questionnaire results;

    @Given("a {string} questionnaire exists with type {string}")
    public void a_questionnaire_exists(String condition, String type) {
        questionnaireRepository
                .save(QuestionnaireMother.questionnaire(
                        type,
                        null,
                        Collections.singletonList("123")));
    }

    @When("I search for a questionnaire")
    public void i_search_for_a_questionnaire() {
        search = questionnaireRepository.findAll().get(0);
        String conditionCd = search.getConditionCodes().get(0);
        String type = search.getQuestionnaireType();
        results = resolver.findQuestionnaire(new QuestionnaireContext(conditionCd, type));
    }

    @Then("the questionnaire is returned")
    public void the_questionnaire_is_returned() {
        assertNotNull(results);
        assertEquals(search.getId(), results.getId());
    }

}
