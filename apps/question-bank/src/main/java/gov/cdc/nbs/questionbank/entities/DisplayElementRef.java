package gov.cdc.nbs.questionbank.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue(DisplayElementRef.TYPE)
public class DisplayElementRef extends ReferenceEntity {
    static final String TYPE = "display_element";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "display_element_id")
    @JoinColumn(name = "display_element_version")
    private DisplayElementEntity displayElementEntity;

    @Override
    public String getReferenceType() {
        return TYPE;
    }

}