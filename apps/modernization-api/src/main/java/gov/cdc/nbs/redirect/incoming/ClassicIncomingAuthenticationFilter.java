package gov.cdc.nbs.redirect.incoming;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import gov.cdc.nbs.authentication.config.SecurityProperties;
import java.io.IOException;

/**
 * A {@code Filter} that ensures a redirected request from Classic NBS has the required authorization cookies and that
 * the user is still authorized. An unauthorized user will be redirected to `/nbs/timeout`/
 */
class ClassicIncomingAuthenticationFilter implements Filter {

    private final ClassicIncomingContextResolver resolver;
    private final SecurityProperties securityProperties;

    ClassicIncomingAuthenticationFilter(
            final ClassicIncomingContextResolver resolver,
            final SecurityProperties securityProperties) {
        this.resolver = resolver;
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest incoming
                && response instanceof HttpServletResponse outgoing) {

            ClassicIncomingAuthorization context = resolver.resolve(incoming);

            if (context instanceof ClassicIncomingAuthorization.Authorized authorized) {
                authorized.apply(securityProperties).accept(outgoing);
                chain.doFilter(incoming, outgoing);
            } else if (context instanceof ClassicIncomingAuthorization.Unauthorized unauthorized) {
                unauthorized.apply(securityProperties).accept(outgoing);
            }

        }

    }

}