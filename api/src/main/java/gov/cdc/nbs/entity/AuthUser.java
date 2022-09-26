package gov.cdc.nbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Auth_user")
public class AuthUser {
    @Id
    @Column(name = "auth_user_uid", nullable = false)
    private Long id;

    @Column(name = "user_id", length = 256)
    private String userId;

    @Column(name = "user_type", length = 100)
    private String userType;

    @Column(name = "user_title", length = 100)
    private String userTitle;

    @Column(name = "user_department", length = 100)
    private String userDepartment;

    @Column(name = "user_first_nm", length = 100)
    private String userFirstNm;

    @Column(name = "user_last_nm", length = 100)
    private String userLastNm;

    @Column(name = "user_work_email", length = 100)
    private String userWorkEmail;

    @Column(name = "user_work_phone", length = 100)
    private String userWorkPhone;

    @Column(name = "user_mobile_phone", length = 100)
    private String userMobilePhone;

    @Column(name = "master_sec_admin_ind")
    private Character masterSecAdminInd;

    @Column(name = "prog_area_admin_ind")
    private Character progAreaAdminInd;

    @Column(name = "nedss_entry_id", nullable = false)
    private Long nedssEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_org_uid")
    private Organization externalOrgUid;

    @Column(name = "user_password", length = 100)
    private String userPassword;

    @Column(name = "user_comments", length = 100)
    private String userComments;

    @Column(name = "add_time", nullable = false)
    private Instant addTime;

    @Column(name = "add_user_id", nullable = false)
    private Long addUserId;

    @Column(name = "last_chg_time", nullable = false)
    private Instant lastChgTime;

    @Column(name = "last_chg_user_id", nullable = false)
    private Long lastChgUserId;

    @Column(name = "record_status_cd", nullable = false, length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time", nullable = false)
    private Instant recordStatusTime;

    @Column(name = "jurisdiction_derivation_ind")
    private Character jurisdictionDerivationInd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_uid")
    private NBSEntity providerUid;

}