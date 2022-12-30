package gov.cdc.nbs.entity.odse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import gov.cdc.nbs.entity.enums.Race;
import gov.cdc.nbs.entity.enums.converter.RaceConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class PersonRaceId implements Serializable {
    private static final long serialVersionUID = -8655697160777324427L;
    @Column(name = "person_uid", nullable = false)
    private Long personUid;

    @Convert(converter = RaceConverter.class)
    @Column(name = "race_cd", nullable = false, length = 20)
    private Race raceCd;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        PersonRaceId entity = (PersonRaceId) o;
        return Objects.equals(this.personUid, entity.personUid) &&
                Objects.equals(this.raceCd, entity.raceCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personUid, raceCd);
    }

}