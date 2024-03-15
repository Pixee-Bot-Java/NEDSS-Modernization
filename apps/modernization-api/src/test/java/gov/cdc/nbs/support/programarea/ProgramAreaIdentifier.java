package gov.cdc.nbs.support.programarea;

import gov.cdc.nbs.support.jurisdiction.JurisdictionIdentifier;

public record ProgramAreaIdentifier(long identifier, String code) {

  public long oid(final JurisdictionIdentifier jurisdiction) {
    return (jurisdiction.identifier() * 100000L) + identifier();
  }
}
