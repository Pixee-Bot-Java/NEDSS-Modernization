package gov.cdc.nbs.entity.odse;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.cdc.nbs.entity.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Auth_user")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_user_uid", nullable = false)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_user_uid")
    private List<AuthUserRole> authUserRoles;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_user_uid")
    private List<AuthProgAreaAdmin> adminProgramAreas;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status_cd", nullable = false, length = 20)
    private RecordStatus recordStatusCd;

    @Column(name = "record_status_time", nullable = false)
    private Instant recordStatusTime;

    @Column(name = "jurisdiction_derivation_ind")
    private Character jurisdictionDerivationInd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_uid")
    private NBSEntity providerUid;

}