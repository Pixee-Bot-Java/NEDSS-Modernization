package gov.cdc.nbs;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cdc.nbs.entity.enums.SecurityEventType;
import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.odse.SecurityLog;
import gov.cdc.nbs.graphql.GraphQLPage;
import gov.cdc.nbs.graphql.filter.PatientFilter;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.repository.AuthUserRepository;
import gov.cdc.nbs.repository.SecurityLogRepository;
import gov.cdc.nbs.service.EncryptionService;
import gov.cdc.nbs.service.PatientService;
import gov.cdc.nbs.support.UserMother;
import gov.cdc.nbs.support.util.RandomUtil;
import gov.cdc.nbs.support.util.UserUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.Cookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RedirectSteps {
  @Autowired
  private MockMvc mvc;
  @Autowired
  private SecurityLogRepository securityLogRepository;
  @Autowired
  private AuthUserRepository authUserRepository;
  @Autowired
  private EncryptionService encryptionService;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private PatientService patientService;

  private MockHttpServletResponse response;
  private String sessionId;
  private Page<Person> searchResponse;

  private MultiValueMap<String, String> criteria;

  private PatientFilter patientFilter;

  @Before
  public void clearCriteria() {
    criteria = new LinkedMultiValueMap<>();
  }

  @Before
  public void resetFilter() {
    this.patientFilter = null;
  }

  @Given("I am logged into NBS and a security log entry exists")
  public void i_am_logged_into_nbs_and_a_security_log_entry_exists() {
    // make sure user exists
    var user = UserMother.clerical();
    UserUtil.insertIfNotExists(user, authUserRepository);
    // insert security_log event with sessionId
    sessionId = RandomUtil.getRandomString(40);
    var log = new SecurityLog();
    log.setId(securityLogRepository.getMaxId() + 1);
    log.setEventTypeCd(SecurityEventType.LOGIN_SUCCESS);
    log.setEventTime(Instant.now());
    log.setSessionId(sessionId);
    log.setNedssEntryId(user.getNedssEntryId());
    log.setFirstNm(user.getUserFirstNm());
    log.setLastNm(user.getUserLastNm());
    securityLogRepository.save(log);
  }

  @Given("A sessionId is not set")
  public void a_session_id_is_not_set() {
    sessionId = null;
  }

  @When("I send a request to the NBS simple search")
  public void i_send_a_request_to_the_nbs_simple_search() throws Exception {
    response = mvc
        .perform(
            MockMvcRequestBuilders.post("/nbs/redirect/simpleSearch")
                .cookie(new Cookie("JSESSIONID", sessionId)))
        .andReturn().getResponse();
  }

  @Then("I am redirected to the simple search react page")
  public void i_am_redirected_to_the_simple_search_react_page() {
    assertEquals(HttpStatus.FOUND.value(), response.getStatus());
    var redirectUrl = response.getRedirectedUrl();
    assertNotNull(redirectUrl);
    assertEquals("/advanced-search", redirectUrl);
  }

  @When("I perform a simple search with Date Of Birth as {string}")
  public void i_perform_a_simple_search_with_date_of_birth_as(final String date) {
    this.criteria.add("patientSearchVO.birthTime", date);
  }

  @When("I send a search request to the NBS simple search")
  public void I_send_a_search_request_to_the_nbs_simple_search() throws Exception {
    response = mvc.perform(
            MockMvcRequestBuilders
                .post("/nbs/redirect/simpleSearch")
                .param("patientSearchVO.lastName", "Doe")
                .param("patientSearchVO.firstName", "John")
                .param("patientSearchVO.currentSex", "M")
                .param("patientSearchVO.actType", "1000")
                .param("patientSearchVO.actId", "9876")
                .param("patientSearchVO.localID", "1234")
                .params(this.criteria)
                .cookie(new Cookie("JSESSIONID", sessionId))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)
        )
        .andReturn()
        .getResponse();

    this.patientFilter = resolveFilter(response.getRedirectedUrl());
  }

  private PatientFilter resolveFilter(final String url) {
    if (url == null) {
      return null;
    }

    int queryIndex = url.indexOf("?q=");

    if (queryIndex >= 0) {
      var q = url.substring(queryIndex + "?q=".length());
      q = URLDecoder.decode(q, StandardCharsets.UTF_8);
      return mapper.convertValue(encryptionService.handleDecryption(q), PatientFilter.class);
    }

    return null;

  }

  @When("I submit the search filter to the patient search API")
  public void i_submit_the_search_filter_to_the_patient_search_API() {
    searchResponse = patientService.findPatientsByFilter(this.patientFilter, new GraphQLPage(25));
  }

  @Then("The search is executed successfully")
  public void the_search_is_executed_successfully() {
    assertNotNull(searchResponse);
  }

  @Then("My search params are passed to the simple search react page")
  public void my_search_params_are_passed_to_the_simple_search_react_page() {
    var redirectUrl = response.getRedirectedUrl();

    assertThat(redirectUrl)
        .isNotNull()
        .contains("/advanced-search?");

    assertThat(patientFilter)
        .returns("Doe", PatientFilter::getLastName)
        .returns("John", PatientFilter::getFirstName)
        .returns(Gender.M, PatientFilter::getGender)
        .returns("1234", PatientFilter::getId);

  }

  @Then("the search parameters include Date of Birth as {string}")
  public void the_search_parameters_include_date_of_birth_as(final String date) {
    assertThat(patientFilter.getDateOfBirth()).isEqualTo(date);
  }

  @When("I navigate to the NBS advanced search page")
  public void i_navigate_to_the_NBS_advanced_search_page() throws Exception {
    response = mvc.perform(
            MockMvcRequestBuilders.get("/nbs/redirect/advancedSearch")
                .cookie(new Cookie("JSESSIONID", sessionId)))
        .andReturn().getResponse();
  }

  @Then("I am redirected to the advanced search react page")
  public void i_am_redirected_to_the_advanced_search_react_page() {
    assertEquals(HttpStatus.FOUND.value(), response.getStatus());
    var redirectUrl = response.getRedirectedUrl();
    assertNotNull(redirectUrl);
    assertTrue(redirectUrl.contains("/advanced-search"));
  }

  @Then("I am redirected to the timeout page")
  public void i_am_redirected_to_the_timeout_page() {
    assertEquals(HttpStatus.FOUND.value(), response.getStatus());
    var redirectUrl = response.getRedirectedUrl();
    assertNotNull(redirectUrl);
    assertTrue(redirectUrl.contains("/nbs/timeout"));
  }

  @Then("the user Id is present in the redirect")
  public void the_user_id_is_present_in_the_redirect() {
    var userIdCookie = response.getCookie("nbs_user");
    assertNotNull(userIdCookie);
    assertEquals(UserMother.clerical().getUserId(), userIdCookie.getValue());
  }


}
