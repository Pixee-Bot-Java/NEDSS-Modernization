package gov.cdc.nbs.questionbank.question;

import gov.cdc.nbs.questionbank.question.model.DisplayControlOptions;
import gov.cdc.nbs.questionbank.support.ExceptionHolder;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;


import static org.junit.jupiter.api.Assertions.*;


public class DisplayControlOptionsSteps {

  @Autowired
  private ExceptionHolder exceptionHolder;

  @Autowired
  private QuestionControllerHelper controllerHelper;

  DisplayControlOptions displayControlOptions;


  @When("I get list of question display control options")
  public void I_get_list_of_question_display_control_options() {
    try {
      displayControlOptions = controllerHelper.getDisplayControlOptions();
    } catch (AccessDeniedException e) {
      exceptionHolder.setException(e);
    } catch (AuthenticationCredentialsNotFoundException e) {
      exceptionHolder.setException(e);
    }
  }


  @Then("return list of question display control options")
  public void return_list_of_question_display_control_options() {
    assertNotNull(displayControlOptions);
    assertFalse(displayControlOptions.codedDisplayControl().isEmpty());
    assertFalse(displayControlOptions.textDisplayControl().isEmpty());
    assertFalse(displayControlOptions.numericDisplayControl().isEmpty());
    assertFalse(displayControlOptions.dateDisplayControl().isEmpty());
  }


}
