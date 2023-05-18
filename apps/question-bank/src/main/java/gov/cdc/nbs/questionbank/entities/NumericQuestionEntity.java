package gov.cdc.nbs.questionbank.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(NumericQuestionEntity.TYPE)
public class NumericQuestionEntity extends DisplayElementEntity {
    static final String TYPE = "numeric_question";

    @Column(name = "label", length = 300)
    private String label;

    @Column(name = "tooltip", length = 200)
    private String tooltip;

    @Column(name = "min_value")
    private Integer minValue;

    @Column(name = "max_value")
    private Integer maxValue;

    @ManyToOne
    @JoinColumn(name = "units_set")
    private ValueSet unitsSet;

    @Override
    public String getDisplayType() {
        return TYPE;
    }

}
