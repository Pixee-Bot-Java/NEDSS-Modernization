package gov.cdc.nbs.patient.demographic;

import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.odse.PersonRace;
import gov.cdc.nbs.patient.PatientCommand;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Embeddable
public class PatientRaceDemographic {

    private static Predicate<PersonRace> inCategory(final String category) {
        return test -> Objects.equals(test.getRaceCategoryCd(), category);
    }

    private static Predicate<PersonRace> identifiedBy(final String race) {
        return test -> Objects.equals(test.getRaceCd(), race);
    }

    private static Predicate<PersonRace> isDetail() {
        return test -> !Objects.equals(test.getRaceCategoryCd(), test.getRaceCd());
    }

    @OneToMany(
        mappedBy = "personUid",
        fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE
        },
        orphanRemoval = true
    )
    private List<PersonRace> races;


    public void add(
        final Person patient,
        final PatientCommand.AddRace added
    ) {
        //  Add a PersonRace for the category
        add(
            patient,
            new PatientCommand.AddRaceCategory(
                added.person(),
                added.asOf(),
                added.category(),
                added.requester(),
                added.requestedOn()
            )
        );

        //  Add a PersonRace for each detail
        added.detailed().stream()
            .map(
                detail -> new PatientCommand.AddDetailedRace(
                    added.person(),
                    added.asOf(),
                    added.category(),
                    detail,
                    added.requester(),
                    added.requestedOn()
                )
            )
            .forEach(detail -> add(patient, detail));
    }

    private void add(final Person patient, final PatientCommand.AddRaceCategory added) {
        ensureRaces().add(new PersonRace(patient, added));
    }

    private void add(final Person patient, final PatientCommand.AddDetailedRace added) {
        ensureRaces().add(new PersonRace(patient, added));
    }

    public void update(
        final Person patient,
        final PatientCommand.UpdateRaceInfo changes
    ) {

        // change all the races for this category
        Collection<PersonRace> changed = ensureRaces().stream()
            .filter(inCategory(changes.category()))
            .map(race -> race.update(changes))
            .toList();

        List<String> detailed = changes.detailed();

        Collection<String> existingDetails = changed.stream()
            .filter(isDetail())
            .map(PersonRace::getRaceCd)
            .toList();

        ArrayList<String> added = new ArrayList<>(detailed);
        added.removeAll(existingDetails);

        added.stream()
            .map(
                detail -> new PatientCommand.AddDetailedRace(
                    changes.person(),
                    changes.asOf(),
                    changes.category(),
                    detail,
                    changes.requester(),
                    changes.requestedOn()
                )
            ).forEach(detail -> add(patient, detail));


        ArrayList<String> removed = new ArrayList<>(existingDetails);
        removed.removeAll(detailed);

        removed.stream()
            .map(PatientRaceDemographic::identifiedBy)
            .reduce(Predicate::or)
            .ifPresent(criteria -> ensureRaces().removeIf(criteria));

    }

    public void delete(final PatientCommand.DeleteRaceInfo info) {
        //  Race is deleted by category, the details should be removed also
        ensureRaces().removeIf(inCategory(info.category()));
    }

    private Collection<PersonRace> ensureRaces() {
        if (this.races == null) {
            this.races = new ArrayList<>();
        }
        return this.races;
    }

    public List<PersonRace> races() {
        return this.races == null ? List.of() : List.copyOf(this.races);
    }

}
