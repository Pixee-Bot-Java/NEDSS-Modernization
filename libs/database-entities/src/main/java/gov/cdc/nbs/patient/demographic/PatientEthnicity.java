package gov.cdc.nbs.patient.demographic;

import gov.cdc.nbs.entity.odse.Person;
import gov.cdc.nbs.entity.odse.PersonEthnicGroup;
import gov.cdc.nbs.patient.PatientCommand;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class PatientEthnicity {

    @Column(name = "as_of_date_ethnicity")
    private Instant asOfDateEthnicity;
    @Column(name = "ethnic_group_ind", length = 20)
    private String ethnicGroupInd;

    @Column(name = "ethnic_unk_reason_cd", length = 20)
    private String ethnicUnkReasonCd;

    @OneToMany(
        mappedBy = "personUid", fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE
        },
        orphanRemoval = true
    )
    private List<PersonEthnicGroup> ethnicities;

    public PatientEthnicity() {

    }

    public PatientEthnicity(final PatientCommand.AddPatient patient) {
        this.ethnicGroupInd = patient.ethnicityCode();

        if (this.ethnicGroupInd != null) {
            this.asOfDateEthnicity = patient.asOf();
        }
    }

    public Instant asOf() {
        return asOfDateEthnicity;
    }

    public String ethnicGroup() {
        return ethnicGroupInd;
    }

    public String unknownReason() {
        return ethnicUnkReasonCd;
    }

    public List<PersonEthnicGroup> ethnicities() {
        return ethnicities == null ? List.of() : List.copyOf(ethnicities);
    }

    public void update(final PatientCommand.UpdateEthnicityInfo info) {
        this.asOfDateEthnicity = info.asOf();
        this.ethnicGroupInd = info.ethnicGroup();
        this.ethnicUnkReasonCd = info.unknownReason();
    }

    public PersonEthnicGroup add(
        final Person patient,
        final PatientCommand.AddDetailedEthnicity add
    ) {

        PersonEthnicGroup added = new PersonEthnicGroup(
            patient,
            add
        );

        ensureEthnicities().add(added);

        return added;
    }

    private List<PersonEthnicGroup> ensureEthnicities() {
        if (this.ethnicities == null) {
            this.ethnicities = new ArrayList<>();
        }
        return this.ethnicities;
    }

    public void remove(final PatientCommand.RemoveDetailedEthnicity remove) {
        this.ethnicities.removeIf(detail -> Objects.equals(detail.getId().getEthnicGroupCd(), remove.ethnicity()));
    }
}
