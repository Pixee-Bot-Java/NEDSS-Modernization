package gov.cdc.nbs.event.report.lab.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class LabResultParameterResolver {

  private static final int IDENTIFIER_COLUMN = 1;
  private static final String QUERY = """
      select
        lab_result_cd
      from NBS_SRTE.[dbo].Lab_Result
      where lab_result_desc_txt = ?
      """;

  private static final int DESCRIPTION_INDEX = 1;

  private final JdbcTemplate template;

  LabResultParameterResolver(final JdbcTemplate template) {
    this.template = template;
  }

  Optional<String> resolve(final String value) {
    return this.template.query(
        QUERY,
        statement -> {
          statement.setString(DESCRIPTION_INDEX, value);
          ;
        },
        (rs, row) -> rs.getString(IDENTIFIER_COLUMN)
    ).stream().findFirst();
  }
}
