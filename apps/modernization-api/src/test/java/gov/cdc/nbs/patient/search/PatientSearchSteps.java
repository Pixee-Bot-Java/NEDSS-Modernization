package gov.cdc.nbs.patient.search;

import gov.cdc.nbs.search.support.SortCriteria;
import gov.cdc.nbs.testing.support.Active;
import io.cucumber.java.en.When;
import org.springframework.test.web.servlet.ResultActions;

public class PatientSearchSteps {

  private final PatientSearchRequester request;

  private final Active<PatientFilter> criteria;
  private final Active<SortCriteria> sorting;
  private final Active<ResultActions> results;

  PatientSearchSteps(
      final PatientSearchRequester request,
      final Active<PatientFilter> criteria,
      final Active<SortCriteria> sorting,
      final Active<ResultActions> results
  ) {
    this.request = request;
    this.criteria = criteria;
    this.sorting = sorting;
    this.results = results;
  }

  @When("I search for patients")
  public void i_search_for_patients_() {
    results.active(
        this.request.search(
            this.criteria.active(),
            this.sorting.active()
        )
    );

  }

}
