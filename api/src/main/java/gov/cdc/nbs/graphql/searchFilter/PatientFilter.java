package gov.cdc.nbs.graphql.searchFilter;

import java.time.Instant;

import gov.cdc.nbs.entity.enums.Deceased;
import gov.cdc.nbs.entity.enums.Ethnicity;
import gov.cdc.nbs.entity.enums.Gender;
import gov.cdc.nbs.entity.enums.IdentificationType;
import gov.cdc.nbs.entity.enums.Race;
import gov.cdc.nbs.entity.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientFilter {
    private Long id;
    private String lastName;
    private String firstName;
    private Race race;
    private Identification identification;
    private String ssn;
    private String phoneNumber;
    private Instant dateOfBirth;
    private String dateOfBirthOperator;
    private Gender gender;
    private Deceased deceased;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String mortalityStatus;
    private Ethnicity ethnicity;
    private RecordStatus recordStatus;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Identification {
        private String identificationNumber;
        private IdentificationType identificationType;
    }
}
