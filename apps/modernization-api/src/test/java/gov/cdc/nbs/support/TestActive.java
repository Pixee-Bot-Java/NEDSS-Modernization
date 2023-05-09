package gov.cdc.nbs.support;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestActive<I> {

    private I active;

    public void active(final I session) {
        this.active = session;
    }

    public void reset() {
        this.active = null;
    }

    public Optional<I> maybeActive() {
        return Optional.ofNullable(this.active);
    }

    public I active() {
        return maybeActive()
            .orElseThrow(() -> new IllegalStateException("there is nothing active"));
    }

}
