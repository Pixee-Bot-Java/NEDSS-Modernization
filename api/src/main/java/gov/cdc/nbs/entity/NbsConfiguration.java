package gov.cdc.nbs.entity;

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
@Table(name = "NBS_configuration")
public class NbsConfiguration {
    @Id
    @Column(name = "config_key", nullable = false, length = 200)
    private String id;

    @Column(name = "config_value", length = 2000)
    private String configValue;

    @Column(name = "short_name", length = 80)
    private String shortName;

    @Column(name = "desc_txt", length = 2000)
    private String descTxt;

    @Column(name = "default_value", length = 2000)
    private String defaultValue;

    @Column(name = "valid_values", length = 2000)
    private String validValues;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "add_release", length = 50)
    private String addRelease;

    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

    @Column(name = "add_user_id", nullable = false)
    private Long addUserId;

    @Column(name = "add_time", nullable = false)
    private Instant addTime;

    @Column(name = "last_chg_user_id", nullable = false)
    private Long lastChgUserId;

    @Column(name = "last_chg_time", nullable = false)
    private Instant lastChgTime;

    @Column(name = "status_cd", nullable = false)
    private Character statusCd;

    @Column(name = "status_time", nullable = false)
    private Instant statusTime;

    @Column(name = "admin_comment", length = 2000)
    private String adminComment;

    @Column(name = "system_usage", length = 2000)
    private String systemUsage;

}