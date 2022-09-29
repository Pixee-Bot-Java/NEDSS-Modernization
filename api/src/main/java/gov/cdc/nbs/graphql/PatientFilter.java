package gov.cdc.nbs.graphql;

import java.time.Instant;

import gov.cdc.nbs.entity.enums.Deceased;
import gov.cdc.nbs.entity.enums.RecordStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientFilter {
    private GraphQLPage page = new GraphQLPage(50, 0);
    private Long id;
    private String lastName;
    private String firstName;
    private String ssn;
    private String phoneNumber;
    private Instant dateOfBirth;
    private String dateOfBirthOperator;
    private Character gender;
    private Deceased deceased;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private String mortalityStatus;
    private String ethnicity;
    private RecordStatus recordStatus;
}
