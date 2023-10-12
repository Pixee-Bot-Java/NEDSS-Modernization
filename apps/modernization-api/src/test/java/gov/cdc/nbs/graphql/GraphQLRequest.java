package gov.cdc.nbs.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.cdc.nbs.Authenticated;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class GraphQLRequest {

  private final ObjectMapper mapper;
  private final Authenticated authenticated;
  private final MockMvc mvc;

  public GraphQLRequest(
      final ObjectMapper mapper,
      final Authenticated authenticated,
      final MockMvc mvc
  ) {
    this.mapper = mapper;
    this.authenticated = authenticated;
    this.mvc = mvc;
  }

  public ResultActions query(
      final String query,
      final JsonNode variables
  ) {

    try {
      ObjectNode payload = mapper.createObjectNode()
          .put("query", query)
          .set("variables", variables);

      byte[] content = mapper.writeValueAsBytes(payload);

      MvcResult graphQLRequest = mvc.perform(
              this.authenticated.withUser(post("/graphql"))
                  .content(content)
                  .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isOk())
          .andExpect(request().asyncStarted())
          .andReturn();
      return mvc.perform(asyncDispatch(graphQLRequest))
          .andExpect(status().isOk());
    } catch (Exception exception) {
      throw new IllegalStateException((exception));
    }
  }
}


