package gov.cdc.nbs;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.cdc.nbs.controller.PatientController;
import gov.cdc.nbs.entity.enums.PregnancyStatus;
import gov.cdc.nbs.entity.odse.Act;
import gov.cdc.nbs.entity.odse.Organization;
import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.srte.JurisdictionCode;
import gov.cdc.nbs.graphql.GraphQLPage;
import gov.cdc.nbs.graphql.searchFilter.EventFilter;
import gov.cdc.nbs.graphql.searchFilter.EventFilter.EventType;
import gov.cdc.nbs.graphql.searchFilter.InvestigationFilter;
import gov.cdc.nbs.graphql.searchFilter.InvestigationFilter.IdType;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.EntryMethod;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.EventStatus;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.LabReportDateType;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.LabReportProviderSearch;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.LaboratoryEventDateSearch;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.LaboratoryEventIdType;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.ProcessingStatus;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.ProviderType;
import gov.cdc.nbs.graphql.searchFilter.LaboratoryReportFilter.UserType;
import gov.cdc.nbs.graphql.searchFilter.PatientFilter;
import gov.cdc.nbs.graphql.searchFilter.PatientFilter.Identification;
import gov.cdc.nbs.repository.ActRepository;
import gov.cdc.nbs.repository.JurisdictionCodeRepository;
import gov.cdc.nbs.repository.OrganizationRepository;
import gov.cdc.nbs.repository.PersonRepository;
import gov.cdc.nbs.repository.PostalLocatorRepository;
import gov.cdc.nbs.repository.TeleLocatorRepository;
import gov.cdc.nbs.support.EventMother;
import gov.cdc.nbs.support.EventMother.Event;
import gov.cdc.nbs.support.PersonMother;
import gov.cdc.nbs.support.util.PersonUtil;
import gov.cdc.nbs.support.util.RandomUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback(false)
public class PatientSearchSteps {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PostalLocatorRepository postalLocatorRepository;
    @Autowired
    private TeleLocatorRepository teleLocatorRepository;
    @Autowired
    private ActRepository actRepository;
    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private JurisdictionCodeRepository jurisdictionCodeRepository;
    @Autowired
    PatientController patientController;

    private Person searchPatient;
    private List<Person> searchResults;
    private List<Person> generatedPersons;

    @Given("there are {int} patients")
    public void there_are_patients(int patientCount) {
        // person data is randomly generated but the Ids are always the same.
        generatedPersons = PersonMother.getRandomPersons(patientCount);

        var generatedIds = generatedPersons.stream()
                .map(p -> p.getId()).collect(Collectors.toList());
        // find existing persons
        var existingPersons = personRepository.findAllById(generatedIds);
        // delete existing locator entries
        var existingPostal = PersonUtil.getPostalLocators(existingPersons);
        postalLocatorRepository.deleteAll(existingPostal);
        var existingTele = PersonUtil.getTeleLocators(existingPersons);
        teleLocatorRepository.deleteAll(existingTele);
        // delete existing persons
        personRepository.deleteAll(existingPersons);

        // create new persons
        teleLocatorRepository.saveAll(PersonUtil.getTeleLocators(generatedPersons));
        postalLocatorRepository.saveAll(PersonUtil.getPostalLocators(generatedPersons));
        personRepository.saveAll(generatedPersons);
    }

    @Given("Investigations exist")
    public void investigations_exist() {
        if (searchPatient == null) {
            searchPatient = RandomUtil.getRandomFromArray(generatedPersons);
        }
        var investigation1 = EventMother.investigation_bacterialVaginosis(searchPatient.getId());
        createEvent(investigation1);
        var investigation2 = EventMother.investigation_trichomoniasis(searchPatient.getId());
        createEvent(investigation2);
    }

    @Given("A lab report exist")
    public void lab_report_exist() {
        if (searchPatient == null) {
            searchPatient = RandomUtil.getRandomFromArray(generatedPersons);
        }
        var labReport1 = EventMother.labReport_acidFastStain(searchPatient.getId());
        createEvent(labReport1);
    }

    @Given("I am looking for one of them")
    public void I_am_looking_for_one_of_them() {
        // pick one of the existing patients at random
        var index = RandomUtil.getRandomInt(generatedPersons.size());
        searchPatient = generatedPersons.get(index);
    }

    @When("I search patients by {string} {string}")
    public void i_search_patients_by_field(String field, String qualifier) {
        PatientFilter filter = getPatientDataFilter(field, qualifier);
        searchResults = patientController.findPatientsByFilter(filter, new GraphQLPage(1000, 0));
    }

    @When("I search investigation events by {string} {string}")
    public void i_search_patients_by_investigation_events(String field, String qualifier) {
        EventFilter filter = getInvestigationFilter(field, qualifier);
        searchResults = patientController.findPatientsByEvent(filter, null);
    }

    @When("I search laboratory events by {string} {string}")
    public void i_search_patients_by_laboratory_events(String field, String qualifier) {
        EventFilter filter = getLabReportFilter(field, qualifier);
        searchResults = patientController.findPatientsByEvent(filter, null);
    }

    @Then("I find the patient")
    public void i_find_the_patient() {
        assertTrue("Search patient is not null", searchPatient != null);
        assertTrue("Search results are not empty", searchResults.size() > 0);
        assertTrue("Search results contains patient: " + searchPatient.getId(), searchResults.contains(searchPatient));
    }

    private EventFilter getLabReportFilter(String field, String qualifier) {
        var filter = new EventFilter();
        filter.setEventType(EventType.LABORATORY_REPORT);
        filter.setLaboratoryReportFilter(new LaboratoryReportFilter());
        var criteria = filter.getLaboratoryReportFilter();
        switch (field) {
            case "program area":
                criteria.setProgramAreas(Arrays.asList("STD"));
                break;
            case "jurisdiction":
                criteria.setJurisdictions(Arrays.asList(EventMother.CLAYTON_CODE));
                break;
            case "pregnancy status":
                criteria.setPregnancyStatus(PregnancyStatus.YES);
                break;
            case "event id":
                switch (qualifier) {
                    case "accession number":
                        criteria.setEventIdType(LaboratoryEventIdType.ACCESSION_NUMBER);
                        criteria.setEventId("accession number");
                        break;
                    case "lab id":
                        criteria.setEventIdType(LaboratoryEventIdType.LAB_ID);
                        criteria.setEventId("OBS10003024GA01");
                        break;
                }
                break;
            case "event date":
                var eds = new LaboratoryEventDateSearch();
                eds.setEventDateType(LabReportDateType.valueOf(qualifier));
                eds.setFrom(Instant.now().minus(5, ChronoUnit.DAYS));
                eds.setTo(Instant.now());
                criteria.setEventDateSearch(eds);
                break;
            case "entry method":
                criteria.setEntryMethods(Arrays.asList(EntryMethod.ELECTRONIC));
                criteria.setEnteredBy(Arrays.asList(UserType.EXTERNAL));
                break;
            case "entered by":
                criteria.setEnteredBy(Arrays.asList(UserType.EXTERNAL));
                break;
            case "event status":
                criteria.setEventStatus(Arrays.asList(EventStatus.NEW));
                break;
            case "processing status":
                criteria.setProcessingStatus(Arrays.asList(ProcessingStatus.UNPROCESSED));
                break;
            case "created by":
                criteria.setCreatedBy(EventMother.CREATED_BY);
                break;
            case "last updated by":
                criteria.setLastUpdatedBy(EventMother.UPDATED_BY);
                break;
            case "provider search":
                var ps = new LabReportProviderSearch();
                ps.setProviderType(ProviderType.valueOf(qualifier));
                ps.setProviderId(searchPatient.getId());
                break;
            case "resulted test":
                criteria.setResultedTest("Acid-Fast Stain");
                break;
            case "coded result":
                criteria.setCodedResult("abnormal");
                break;
            default:
                throw new IllegalArgumentException("Unsupported field: " + field);
        }

        return filter;
    }

    private EventFilter getInvestigationFilter(String field, String qualifier) {
        var filter = new EventFilter();
        filter.setEventType(EventType.INVESTIGATION);
        filter.setInvestigationFilter(new InvestigationFilter());
        var criteria = filter.getInvestigationFilter();
        switch (field) {
            case "condition":
                criteria.setConditions(Arrays.asList(qualifier));
                break;
            case "program area":
                criteria.setProgramAreas(Arrays.asList(qualifier));
                break;
            case "jurisdiction":
                if (qualifier.equals("jd1")) {
                    criteria.setJurisdictions((Arrays.asList(EventMother.DEKALB_CODE)));
                } else {
                    criteria.setJurisdictions(Arrays.asList(EventMother.CLAYTON_CODE));
                }
                break;
            case "pregnancy status":
                criteria.setPregnancyStatus(PregnancyStatus.YES);
                break;
            case "event id":
                criteria.setEventIdType(IdType.valueOf(qualifier));
                switch (criteria.getEventIdType()) {
                    case ABCS_CASE_ID:
                        criteria.setEventId("CityTypeRootExtensionText");
                        break;
                    case CITY_COUNTY_CASE_ID:
                        criteria.setEventId("CityTypeRootExtensionText");
                        break;
                    case INVESTIGATION_ID:
                        criteria.setEventId("CAS10001000GA01");
                        break;
                    case NOTIFICATION_ID:
                        criteria.setEventId("notificationLocalId");
                        break;
                    case STATE_CASE_ID:
                        criteria.setEventId("StateRootExtensionText");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid event id qualifer specified: " +
                                qualifier);
                }
                break;
            case "created by":
                criteria.setCreatedBy(EventMother.CREATED_BY);
                break;
            case "updated by":
                criteria.setLastUpdatedBy(EventMother.UPDATED_BY);
                break;
            default:
                throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return filter;
    }

    private PatientFilter getPatientDataFilter(String field, String qualifier) {
        var filter = new PatientFilter();
        switch (field) {
            case "last name":
                filter.setLastName(searchPatient.getLastNm());
                break;
            case "first name":
                filter.setFirstName(searchPatient.getFirstNm());
                break;
            case "race":
                filter.setRace(searchPatient.getRaces().get(0).getId().getRaceCd());
                break;
            case "identification":
                var patientId = searchPatient.getEntityIds().get(0);
                filter.setIdentification(new Identification(patientId.getRootExtensionTxt(), patientId.getTypeCd()));
                break;
            case "patient id":
                filter.setId(searchPatient.getId());
                break;
            case "ssn":
                filter.setSsn(searchPatient.getSsn());
                break;
            case "phone number":
                var teleLocator = PersonUtil.getTeleLocators(searchPatient).get(0);
                filter.setPhoneNumber(teleLocator.getPhoneNbrTxt());
                break;
            case "date of birth":
                filter.setDateOfBirth(getDobByQualifier(searchPatient, qualifier));
                filter.setDateOfBirthOperator(qualifier);
                break;
            case "gender":
                filter.setGender(searchPatient.getBirthGenderCd());
                break;
            case "deceased":
                filter.setDeceased(searchPatient.getDeceasedIndCd());
                break;
            case "address":
                var addressLocator = PersonUtil.getPostalLocators(searchPatient).get(0);
                filter.setAddress(addressLocator.getStreetAddr1());
                break;
            case "city":
                var cityLocator = PersonUtil.getPostalLocators(searchPatient).get(0);
                filter.setCity(cityLocator.getCityCd());
                break;
            case "state":
                var stateLocator = PersonUtil.getPostalLocators(searchPatient).get(0);
                filter.setState(stateLocator.getStateCd());
                break;
            case "country":
                var cntryLocator = PersonUtil.getPostalLocators(searchPatient).get(0);
                filter.setCountry(cntryLocator.getCntryCd());
                break;
            case "zip code":
                var zipLocator = PersonUtil.getPostalLocators(searchPatient).get(0);
                filter.setZip(zipLocator.getZipCd());
                break;
            case "ethnicity":
                filter.setEthnicity(searchPatient.getEthnicGroupInd());
                break;
            case "record status":
                filter.setRecordStatus(searchPatient.getRecordStatusCd());
                break;
            default:
                throw new IllegalArgumentException("Invalid field specified: " + field);
        }
        return filter;
    }

    private Instant getDobByQualifier(Person search, String qualifier) {
        switch (qualifier) {
            case "before":
                return search.getBirthTime().plus(15, ChronoUnit.DAYS);
            case "after":
                return search.getBirthTime().minus(15, ChronoUnit.DAYS);
            case "equal":
                return search.getBirthTime();
            default:
                throw new IllegalArgumentException("Invalid date of birth qualifier: " + qualifier);
        }
    }

    private void createEvent(Event event) {
        // delete if exsits
        var idList = event.getActs().stream().map(Act::getId).collect(Collectors.toList());
        var searchResults = actRepository.findAllById(idList);
        actRepository.deleteAll(searchResults);

        var orgs = event.getOrgs().stream().map(Organization::getId).collect(Collectors.toList());
        var orgResults = orgRepository.findAllById(orgs);
        orgRepository.deleteAll(orgResults);

        var personIds = event.getPeople().stream().map(Person::getId).collect(Collectors.toList());
        var personResults = personRepository.findAllById(personIds);
        personRepository.deleteAll(personResults);

        var jurisdictionIds = event.getJurisdictionCodes().stream().map(JurisdictionCode::getId)
                .collect(Collectors.toList());
        var jurisdictionCodes = jurisdictionCodeRepository.findAllById(jurisdictionIds);
        jurisdictionCodeRepository.deleteAll(jurisdictionCodes);

        // create new
        jurisdictionCodeRepository.saveAll(event.getJurisdictionCodes());
        orgRepository.saveAll(event.getOrgs());
        personRepository.saveAll(event.getPeople());
        actRepository.saveAll(event.getActs());
    }
}