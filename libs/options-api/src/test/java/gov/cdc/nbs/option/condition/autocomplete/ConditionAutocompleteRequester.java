package gov.cdc.nbs.option.condition.autocomplete;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
class ConditionAutocompleteRequester {

  private final MockMvc mvc;

  ConditionAutocompleteRequester(final MockMvc mvc) {
    this.mvc = mvc;
  }

  ResultActions complete(final String criteria) throws Exception {
    return mvc.perform(
        get("/nbs/api/options/conditions/search")
            .param("criteria", criteria)
    ).andDo(print());
  }

  ResultActions complete(final String criteria, final int limit) throws Exception {
    return mvc.perform(
        get("/nbs/api/options/conditions/search")
            .param("criteria", criteria)
            .param("limit", String.valueOf(limit))
    ).andDo(print());
  }
}
