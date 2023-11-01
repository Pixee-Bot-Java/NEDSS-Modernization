package gov.cdc.nbs.testing.interaction.http;

import gov.cdc.nbs.authentication.NBSUserDetailsResolver;
import gov.cdc.nbs.authentication.NbsUserDetails;
import gov.cdc.nbs.authentication.entity.AuthUser;
import gov.cdc.nbs.testing.authorization.ActiveUser;
import gov.cdc.nbs.testing.support.Active;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class Authenticated {

  private final Active<ActiveUser> active;
  private final NBSUserDetailsResolver resolver;
  private final EntityManager entityManager;

  public Authenticated(
      final Active<ActiveUser> active,
      final NBSUserDetailsResolver resolver,
      final EntityManager entityManager
  ) {
    this.active = active;
    this.resolver = resolver;
    this.entityManager = entityManager;
  }

  public void reset() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private NbsUserDetails userDetails() {
    ActiveUser activeUser = this.active.active();

    AuthUser authUser = this.entityManager.find(AuthUser.class, activeUser.id());

    return resolver.resolve(authUser);
  }

  private Authentication authentication(final NbsUserDetails details) {
    return new PreAuthenticatedAuthenticationToken(
        details,
        null,
        details.getAuthorities()
    );
  }

  private Authentication authentication() {
    NbsUserDetails details = userDetails();

    return new PreAuthenticatedAuthenticationToken(
        details,
        null,
        details.getAuthorities()
    );
  }

  /**
   * Executes the given {@code action} ensuring that the {@link SecurityContextHolder} is configured to be authenticated
   * with the Active User.
   *
   * @param action The action to perform while authenticated.
   * @param <T>    The type of the return value of the {@code action}
   * @return The result of the action
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public <T> T perform(final Supplier<T> action) {
    SecurityContextHolder.getContext().setAuthentication(authentication());

    try {
      return action.get();
    } finally {
      reset();
    }

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public <T> T using(final Function<NbsUserDetails, T> action) {
    NbsUserDetails details = userDetails();
    Authentication authentication = authentication(details);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    try {
      return action.apply(details);
    } finally {
      reset();
    }

  }

  public MockHttpServletRequestBuilder withUser(final MockHttpServletRequestBuilder builder) {
    return builder.header(
        HttpHeaders.AUTHORIZATION,
        "Bearer " + active.active().token().value()
    );


  }
}