package gov.cdc.nbs.questionbank.valueset.read;

import gov.cdc.nbs.questionbank.valueset.request.ValueSetCreateRequest;
import gov.cdc.nbs.questionbank.valueset.response.CreateValueSetResponse;
import gov.cdc.nbs.questionbank.valueset.response.ValueSetSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import gov.cdc.nbs.questionbank.support.ExceptionHolder;
import gov.cdc.nbs.questionbank.valueset.ValueSetController;
import gov.cdc.nbs.questionbank.valueset.request.ValueSetSearchRequest;
import gov.cdc.nbs.questionbank.valueset.response.ValueSet;
import gov.cdc.nbs.questionbank.valueset.util.ValueSetHolder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;


import static org.junit.Assert.*;

public class ReadValueSetSteps {

  @Autowired
  private ValueSetController valueSetController;

  @Autowired
  private ExceptionHolder exceptionHolder;

  @Autowired
  private ValueSetHolder valueSetSearchHolder;

  private ValueSetSearchRequest search;

  private ValueSetCreateRequest request;

  @Given("A valueSet exists")
  public void a_valueSet_exists() {
    try {
      request = new ValueSetCreateRequest("type", "testValueSetCode", "testValueSetNm",
          "testCodeSetDescTxt");
      ResponseEntity<CreateValueSetResponse> codeSetResult = valueSetController.createValueSet(request);
    } catch (AccessDeniedException e) {
      exceptionHolder.setException(e);
    } catch (AuthenticationCredentialsNotFoundException e) {
      exceptionHolder.setException(e);
    }
  }


  @Given("I search for a value set that exists")
  public void i_search_for_a_value_set_that_exists() {
    try {
      search = new ValueSetSearchRequest("testValueSetNm",
          "testValueSetCode", "testCodeSetDescTxt");
      Pageable pageable = Pageable.ofSize(1);
      Page<ValueSetSearchResponse> results = valueSetController.searchValueSet(pageable, search);
      valueSetSearchHolder.setValueSetSearchResults(results);
    } catch (AccessDeniedException e) {
      exceptionHolder.setException(e);
    } catch (AuthenticationCredentialsNotFoundException e) {
      exceptionHolder.setException(e);
    }
  }

  @Given("I make a request for a value set that does not exist")
  public void i_make_a_request_for_a_value_set_that_does_not_exist() {
    try {
      search = new ValueSetSearchRequest("xxxxx", "xxxxx", "xxxxx");
      Pageable pageable = Pageable.ofSize(1);
      Page<ValueSetSearchResponse> results = valueSetController.searchValueSet(pageable, search);
      valueSetSearchHolder.setValueSetSearchResults(results);
    } catch (AccessDeniedException e) {
      exceptionHolder.setException(e);
    } catch (AuthenticationCredentialsNotFoundException e) {
      exceptionHolder.setException(e);
    }
  }

  @Given("I make a request to retrieve all value sets")
  public void i_make_a_request_to_retrieve_all_value_sets() {
    try {
      Page<ValueSet> results = valueSetController.findAllValueSets(PageRequest.ofSize(20));
      valueSetSearchHolder.setValueSetResults(results);
    } catch (AccessDeniedException e) {
      exceptionHolder.setException(e);
    } catch (AuthenticationCredentialsNotFoundException e) {
      exceptionHolder.setException(e);
    }
  }


  @Then("All Value sets should be returned")
  public void all_value_sets_should_be_returned() {
    Page<ValueSet> results = valueSetSearchHolder.getValueSetResults();
    assertNotNull(results);
    assertFalse(results.isEmpty());
  }

  @Then("Value sets should not be returned")
  public void Value_sets_should_not_be_returned() {
    Page<ValueSetSearchResponse> results = valueSetSearchHolder.getValueSetSearchResults();
    assertNotNull(results);
    assertTrue(results.isEmpty());
  }

  @Then("Value sets should be returned")
  public void value_sets_should_be_returned() {
    Page<ValueSetSearchResponse> result = valueSetSearchHolder.getValueSetSearchResults();
    assertNotNull(result);
    assertTrue(result.getSize() > 0);
  }

}
