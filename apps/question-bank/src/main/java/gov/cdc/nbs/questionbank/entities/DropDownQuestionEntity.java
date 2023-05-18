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
@DiscriminatorValue(DropDownQuestionEntity.TYPE)
public class DropDownQuestionEntity extends DisplayElementEntity {
    static final String TYPE = "dropdown_question";

    @Column(name = "label", length = 300)
    private String label;

    @Column(name = "tooltip", length = 200)
    private String tooltip;

    @ManyToOne
    @JoinColumn(name = "value_set_id")
    private ValueSet valueSet;

    @ManyToOne
    @JoinColumn(name = "default_answer_id")
    private ValueEntity defaultAnswer;

    @Column(name = "multiselect", nullable = false)
    private boolean multiSelect;

    @Override
    public String getDisplayType() {
        return TYPE;
    }
}
