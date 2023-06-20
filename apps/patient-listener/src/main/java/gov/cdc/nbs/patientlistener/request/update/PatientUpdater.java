package gov.cdc.nbs.patientlistener.request.update;

import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.odse.PostalEntityLocatorParticipation;
import gov.cdc.nbs.id.IdGeneratorService;
import gov.cdc.nbs.message.patient.event.AddIdentificationData;
import gov.cdc.nbs.message.patient.event.DeleteIdentificationData;
import gov.cdc.nbs.message.patient.event.DeleteMortalityData;
import gov.cdc.nbs.message.patient.event.UpdateAdministrativeData;
import gov.cdc.nbs.message.patient.event.UpdateGeneralInfoData;
import gov.cdc.nbs.message.patient.event.UpdateIdentificationData;
import gov.cdc.nbs.message.patient.event.UpdateMortalityData;
import gov.cdc.nbs.message.patient.event.UpdateSexAndBirthData;
import gov.cdc.nbs.patient.PatientCommand;
import gov.cdc.nbs.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class PatientUpdater {
    private final IdGeneratorService idGenerator;
    private final PersonRepository personRepository;

    public PatientUpdater(
        IdGeneratorService idGenerator,
        PersonRepository personRepository) {
        this.idGenerator = idGenerator;
        this.personRepository = personRepository;
    }

    public Person update(final Person person, final UpdateGeneralInfoData data) {
        person.update(asUpdateGeneralInfo(data));
        return personRepository.save(person);
    }

    public Person update(final Person person, final UpdateAdministrativeData data) {
        person.update(asUpdateAdministrativeInfo(data));
        return personRepository.save(person);
    }

    public Person update(final Person person, final AddIdentificationData data) {
        person.add(asAddIdentificationInfo(data));
        return personRepository.save(person);
    }

    public Person update(final Person person, final UpdateIdentificationData data) {
        person.update(asUpdateIdentificationInfo(data));
        return personRepository.save(person);
    }

    public Person update(final Person person, final DeleteIdentificationData data) {
        person.delete(asDeleteIdentificationInfo(data));
        return personRepository.save(person);
    }

    public Person update(final Person person, final UpdateMortalityData data) {
        Optional.ofNullable(person.getNbsEntity().getEntityLocatorParticipations())
            .stream()
            .flatMap(List::stream)
            .filter(elp -> elp.getUseCd().equals("DTH"))
            .findFirst()
            .ifPresentOrElse(
                elp ->
                    // If postalEntityLocator exists with useCd of "DTH", update it
                    ((PostalEntityLocatorParticipation) elp).updateMortalityLocator(asUpdateMortalityLocator(data)),
                () -> {
                    // If postalEntityLocator with useCd of "DTH" does not exist, create it
                    var id = idGenerator.getNextValidId(IdGeneratorService.EntityType.NBS).getId();
                    person.add(asAddMortalityLocator(data, id));
                });

        return personRepository.save(person);
    }

    public Person update(final Person person, final DeleteMortalityData data) {
        person.delete(asDeleteMortalityLocator(data));
        return personRepository.save(person);
    }

    public Person update(Person person, UpdateSexAndBirthData data) {
        person.update(asUpdateSexAndBirthInfo(data));
        return personRepository.save(person);
    }

    private PatientCommand.UpdateMortalityLocator asUpdateMortalityLocator(UpdateMortalityData data) {
        return new PatientCommand.UpdateMortalityLocator(
            data.patientId(),
            data.asOf(),
            data.deceased(),
            data.deceasedTime(),
            data.cityOfDeath(),
            data.stateOfDeath(),
            data.countyOfDeath(),
            data.countryOfDeath(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.AddMortalityLocator asAddMortalityLocator(UpdateMortalityData data, long id) {
        return new PatientCommand.AddMortalityLocator(
            data.patientId(),
            id,
            data.asOf(),
            data.deceased(),
            data.deceasedTime(),
            data.cityOfDeath(),
            data.stateOfDeath(),
            data.countyOfDeath(),
            data.countryOfDeath(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.UpdateGeneralInfo asUpdateGeneralInfo(UpdateGeneralInfoData data) {
        return new PatientCommand.UpdateGeneralInfo(
            data.patientId(),
            data.asOf(),
            data.maritalStatus(),
            data.mothersMaidenName(),
            data.adultsInHouseNumber(),
            data.childrenInHouseNumber(),
            data.occupationCode(),
            data.educationLevelCode(),
            data.primaryLanguageCode(),
            data.speaksEnglishCode(),
            data.eharsId(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.UpdateAdministrativeInfo asUpdateAdministrativeInfo(UpdateAdministrativeData data) {
        return new PatientCommand.UpdateAdministrativeInfo(
            data.patientId(),
            data.asOf(),
            data.description(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.UpdateSexAndBirthInfo asUpdateSexAndBirthInfo(UpdateSexAndBirthData data) {
        return new PatientCommand.UpdateSexAndBirthInfo(
            data.patientId(),
            data.asOf(),
            data.dateOfBirth(),
            data.birthGender(),
            data.currentGender(),
            data.additionalGender(),
            data.transGenderInfo(),
            data.birthCity(),
            data.birthCntry(),
            data.birthState(),
            data.birthOrderNbr(),
            data.multipleBirth(),
            data.sexUnknown(),
            data.currentAge(),
            data.ageReportedTime(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.AddIdentification asAddIdentificationInfo(AddIdentificationData data) {
        return new PatientCommand.AddIdentification(
            data.patientId(),
            data.identificationNumber(),
            data.assigningAuthority(),
            data.identificationType(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.UpdateIdentification asUpdateIdentificationInfo(UpdateIdentificationData data) {
        return new PatientCommand.UpdateIdentification(
            data.patientId(),
            data.id(),
            data.identificationNumber(),
            data.assigningAuthority(),
            data.identificationType(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.DeleteIdentification asDeleteIdentificationInfo(DeleteIdentificationData data) {
        return new PatientCommand.DeleteIdentification(
            data.patientId(),
            data.id(),
            data.updatedBy(),
            Instant.now());
    }

    private PatientCommand.DeleteMortalityLocator asDeleteMortalityLocator(DeleteMortalityData data) {
        return new PatientCommand.DeleteMortalityLocator(
            data.patientId(),
            data.id(),
            data.updatedBy(),
            Instant.now());
    }

}
