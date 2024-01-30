package gov.cdc.nbs.event.lab.result;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class LabTestParameterResolver {

  private static final int IDENTIFIER_COLUMN = 1;
  private static final String QUERY = """
      select
          lab_test_cd
      from NBS_SRTE..Lab_test
      where lab_test_desc_txt = ?
      """;
  private static final int NAME_INDEX = 1;

  private final JdbcTemplate template;

  LabTestParameterResolver(final JdbcTemplate template) {
    this.template = template;
  }

  Optional<String> resolve(final String value) {
    return this.template.query(
        QUERY,
        statement -> {
          statement.setString(NAME_INDEX, value);
        },
        (rs, row) -> rs.getString(IDENTIFIER_COLUMN)
    ).stream().findFirst();
  }
}
