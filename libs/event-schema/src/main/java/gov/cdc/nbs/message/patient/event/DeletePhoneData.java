package gov.cdc.nbs.message.patient.event;

import java.io.Serializable;
import java.time.Instant;

public record DeletePhoneData(
                long patientId,
                long id,
                String requestId,
                long updatedBy,
                Instant asOf) implements Serializable {
}
