package gov.cdc.nbs.support.util;

import org.springframework.security.core.context.SecurityContextHolder;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.authentication.entity.AuthUser;
import gov.cdc.nbs.authentication.entity.AuthUserRepository;

public class UserUtil {
    public static AuthUser insertIfNotExists(AuthUser user, AuthUserRepository repository) {
        var existingUser = repository.findByUserId(user.getUserId());
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            return repository.save(user);
        }
    }

    public static Long getCurrentUserId() {
        return ((NbsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getId();
    }
}
