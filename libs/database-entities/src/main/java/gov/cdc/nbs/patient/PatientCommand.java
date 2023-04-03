package gov.cdc.nbs.patient;

import gov.cdc.nbs.address.City;
import gov.cdc.nbs.address.Country;
import gov.cdc.nbs.address.County;
import gov.cdc.nbs.message.enums.Deceased;
import gov.cdc.nbs.message.enums.Gender;
import gov.cdc.nbs.message.enums.Suffix;
import gov.cdc.nbs.message.patient.input.PatientInput;
import java.time.Instant;
import java.time.LocalDate;

public sealed interface PatientCommand {

    long person();

    long requester();

    Instant requestedOn();

    record AddPatient(
            long person,
            String localId,
            String ssn,
            LocalDate dateOfBirth,
            Gender birthGender,
            Gender currentGender,
            Deceased deceased,
            Instant deceasedTime,
            String maritalStatus,
            String ethnicityCode,
            Instant asOf,
            String comments,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }


    record AddName(
            long person,
            String first,
            String middle,
            String last,
            Suffix suffix,
            PatientInput.NameUseCd type,
            long requester,
            Instant requestedOn) implements PatientCommand {

    }


    record AddRace(
            long person,
            String category,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }


    record AddAddress(
            long person,
            long id,
            String address1,
            String address2,
            City city,
            String state,
            String zip,
            County county,
            Country country,
            String censusTract,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }


    record AddPhoneNumber(
            long person,
            long id,
            String number,
            String extension,
            PatientInput.PhoneType type,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }


    record AddEmailAddress(
            long person,
            long id,
            String email,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }

    record UpdateMortalityLocator(
            long person,
            Instant asOf,
            Deceased deceased,
            Instant deceasedTime,
            String cityOfDeath,
            String stateOfDeath,
            String countyOfDeath,
            String countryOfDeath,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }
    record AddMortalityLocator(
            long person,
            long id,
            Instant asOf,
            Deceased deceased,
            Instant deceasedTime,
            String cityOfDeath,
            String stateOfDeath,
            String countyOfDeath,
            String countryOfDeath,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }

    record UpdateGeneralInfo(
            long person,
            Instant asOf,
            String maritalStatus,
            String mothersMaidenName,
            Short adultsInHouseNumber,
            Short childrenInHouseNumber,
            String occupationCode,
            String educationLevelCode,
            String primaryLanguageCode,
            String speaksEnglishCode,
            String eharsId,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }

    record UpdateSexAndBirthInfo(
            long person,
            Instant asOf,
            LocalDate dateOfBirth,
            Gender birthGender,
            Gender currentGender,
            String additionalGender,
            String transGenderInfo,
            String birthCity,
            String birthCntry,
            String birthState,
            Short birthOrderNbr,
            String multipleBirth,
            String sexUnknown,
            String currentAge,
            Instant ageReportedTime,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }

    record Delete(
            long person,
            long requester,
            Instant requestedOn) implements PatientCommand {
    }
}
