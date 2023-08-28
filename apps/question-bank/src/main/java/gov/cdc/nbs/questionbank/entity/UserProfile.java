package gov.cdc.nbs.questionbank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(catalog = "NBS_ODSE", name = "USER_PROFILE")
public class UserProfile {
    @Id
    @Column(name = "NEDSS_ENTRY_ID", nullable = false)
    private Long id;

    @Column(name = "FIRST_NM", length = 50)
    private String firstNm;

    @Column(name = "LAST_UPD_TIME")
    private Instant lastUpdTime;

    @Column(name = "LAST_NM", length = 50)
    private String lastNm;

}