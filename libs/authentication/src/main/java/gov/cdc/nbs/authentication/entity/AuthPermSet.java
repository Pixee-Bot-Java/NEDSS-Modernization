package gov.cdc.nbs.authentication.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "Auth_perm_set", catalog = "NBS_ODSE")
public class AuthPermSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_perm_set_uid", nullable = false)
    private Long id;

    @Column(name = "perm_set_nm", length = 100)
    private String permSetNm;

    @Column(name = "perm_set_desc", length = 1000)
    private String permSetDesc;

    @Column(name = "sys_defined_perm_set_ind")
    private Character sysDefinedPermSetInd;

    @Embedded
    private AuthAudit audit;

}