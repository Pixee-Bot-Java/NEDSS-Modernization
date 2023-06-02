package gov.cdc.nbs.patient.delete;

import gov.cdc.nbs.patient.PatientException;

public class PatientHasAssociatedEventsException extends PatientException {

    public PatientHasAssociatedEventsException(final long patient) {
        super(patient, "Cannot delete patient with Active Revisions");
    }
}
