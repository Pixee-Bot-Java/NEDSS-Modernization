package gov.cdc.nbs.graphql.filter;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import gov.cdc.nbs.message.enums.Deceased;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.entity.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class PatientFilter {
    private String id;
    private String lastName;
    private String firstName;
    private String race;
    private Identification identification;
    private String ssn;
    private String phoneNumber;
    private String email;
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
    private String ethnicity;
    private List<RecordStatus> recordStatus;
    private String treatmentId;
    private String vaccinationId;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Identification {
        private String identificationNumber;
        private String assigningAuthority;
        private String identificationType;
    }
}
