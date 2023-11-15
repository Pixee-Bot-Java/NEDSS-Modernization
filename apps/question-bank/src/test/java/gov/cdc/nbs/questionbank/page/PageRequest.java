package gov.cdc.nbs.questionbank.page;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import gov.cdc.nbs.testing.interaction.http.Authenticated;

@Component
public class PageRequest {
    private final Authenticated authenticated;
    private final MockMvc mvc;

    PageRequest(
            final Authenticated authenticated,
            final MockMvc mvc) {
        this.authenticated = authenticated;
        this.mvc = mvc;
    }

    ResultActions deletePageRequest(final long page) throws Exception {
        return mvc.perform(
                this.authenticated.withUser(delete("/api/v1/pages/{page}/delete-draft", page)));
    }
}
