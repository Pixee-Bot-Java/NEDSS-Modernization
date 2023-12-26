package gov.cdc.nbs.questionbank.page.service;

import gov.cdc.nbs.questionbank.page.model.PageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PageHistoryFinder {

  private final JdbcTemplate jdbcTemplate;

  private static final String QUERY = """
      with target as (
                       SELECT
                           template_nm as template_name
                       FROM WA_template
                       WHERE wa_template_uid = ?
                   )
      SELECT
        hist.publish_version_nbr AS publishVersionNbr,
        CONVERT(varchar, hist.last_chg_time, 101) AS lastUpdatedDate,
        CONCAT(userProfile.first_nm, ' ', userProfile.last_nm) AS lastUpdatedBy,
        hist.version_note AS notes
      FROM
        WA_template_hist hist
        LEFT OUTER JOIN user_profile userProfile ON hist.last_chg_user_id = userProfile.nedss_entry_id
      WHERE
        hist.template_nm = (select template_name from target)
      UNION
      SELECT
        tem.publish_version_nbr AS publishVersionNbr,
        CONVERT(varchar, tem.last_chg_time, 101) AS lastUpdatedDate,
        CONCAT(userProfile.first_nm, ' ', userProfile.last_nm) AS lastUpdatedBy,
        tem.version_note AS notes
      FROM
        WA_template tem
        LEFT OUTER JOIN user_profile userProfile ON tem.last_chg_user_id = userProfile.nedss_entry_id
      WHERE
        tem.template_nm = (select template_name from target)
        AND tem.template_type IN('Published With Draft', 'Published');
        """;

  public List<PageHistory> getPageHistory(Long pageId) {
    return jdbcTemplate.query(QUERY, setter -> setter.setLong(1, pageId)
        , (rs, rowNum) -> new PageHistory(
            rs.getString("publishVersionNbr"),
            rs.getString("lastUpdatedDate"),
            rs.getString("lastUpdatedBy"),
            rs.getString("notes")
        ));
  }

}

