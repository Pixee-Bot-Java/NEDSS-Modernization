package gov.cdc.nbs.entity.srte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(catalog = "NBS_SRTE", name = "Snomed_code")
public class SnomedCode {
    @Id
    @Column(name = "snomed_cd", nullable = false, length = 20)
    private String id;

    @Column(name = "snomed_desc_txt", length = 100)
    private String snomedDescTxt;

    @Column(name = "source_concept_id", nullable = false, length = 20)
    private String sourceConceptId;

    @Column(name = "source_version_id", nullable = false, length = 20)
    private String sourceVersionId;

    @Column(name = "status_cd")
    private Character statusCd;

    @Column(name = "status_time")
    private Instant statusTime;

    @Column(name = "nbs_uid")
    private Integer nbsUid;

    @Column(name = "effective_from_time")
    private Instant effectiveFromTime;

    @Column(name = "effective_to_time")
    private Instant effectiveToTime;

    @Column(name = "pa_derivation_exclude_cd")
    private Character paDerivationExcludeCd;

    @OneToMany(mappedBy = "snomedCd")
    private Set<LabResultSnomed> labResultSnomeds = new LinkedHashSet<>();

    @OneToMany(mappedBy = "snomedCd")
    private Set<LoincSnomedCondition> loincSnomedConditions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "snomedCd")
    private Set<CodeValueClinical> codeValueClinicals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "id.snomedCd")
    private Set<SnomedCondition> snomedConditions = new LinkedHashSet<>();

}