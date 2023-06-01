package gov.cdc.nbs.authentication.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import gov.cdc.nbs.authentication.NbsAuthority;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.authentication.entity.AuthAudit;
import gov.cdc.nbs.authentication.entity.AuthProgAreaAdmin;
import gov.cdc.nbs.authentication.entity.AuthUser;
import gov.cdc.nbs.authentication.enums.AuthRecordStatus;

public class AuthObjectUtil {
    public static final String BUSINESS_OBJECT = "OBJECT";
    public static final String BUSINESS_OPERATION = "OPERATION";
    public static final String AUTHORITY = "OPERATION-OBJECT";

    public static AuthUser authUser() {
        var user = new AuthUser();
        user.setId(1L);
        user.setUserFirstNm("test");
        user.setUserLastNm("user");
        user.setMasterSecAdminInd('T');
        user.setProgAreaAdminInd('T');
        user.setAdminProgramAreas(AuthObjectUtil.progAreaAdmins(user));
        user.setUserId("test");
        user.setAudit(audit());
        return user;
    }

    public static AuthAudit audit() {
        var now = Instant.now();
        var audit = new AuthAudit();
        audit.setAddTime(now);
        audit.setAddUserId(999L);
        audit.setLastChgTime(now);
        audit.setLastChgUserId(999L);
        audit.setRecordStatusCd(AuthRecordStatus.ACTIVE);
        audit.setRecordStatusTime(now);
        return audit;
    }

    public static List<AuthProgAreaAdmin> progAreaAdmins(AuthUser user) {
        var adminAreas = new ArrayList<AuthProgAreaAdmin>();
        adminAreas.add(
                new AuthProgAreaAdmin(
                        null,
                        "progArea",
                        user,
                        'T',
                        audit()));
        return adminAreas;
    }

    public static NbsUserDetails userDetails() {
        return new NbsUserDetails(
                1L,
                "test",
                "test",
                "test",
                false,
                false,
                null,
                null,
                authorities(),
                "token",
                true);
    }

    public static Set<NbsAuthority> authorities() {
        var authorities = new HashSet<NbsAuthority>();
        authorities.add(new NbsAuthority(
                BUSINESS_OPERATION,
                BUSINESS_OBJECT,
                "programArea",
                123,
                "jurisdiction",
                AUTHORITY));
        return authorities;
    }
}