package gov.cdc.nbs.patient.profile.summary;

import java.time.LocalDate;
import java.util.Collection;

 record PatientSummary(
    long patient,
    Name legalName,
    LocalDate birthday,
    Integer age,
    String gender,
    String ethnicity,
    String race,
    Collection<Phone> phone,
    Collection<Email> email,
    Address address
) {

    record Name(
        String prefix,
        String first,
        String middle,
        String last,
        String suffix
    ) {
    }

    record Phone (
        String use,
        String number
    ){}

    record Email (
        String use,
        String address
    ){}

    record Address(
        String street,
        String city,
        String state,
        String zipcode,
        String country
    ){}
}