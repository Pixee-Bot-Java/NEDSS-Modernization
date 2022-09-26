package gov.cdc.nbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class InterventionHistId implements Serializable {
    private static final long serialVersionUID = -8089224375955799830L;
    @Column(name = "intervention_uid", nullable = false)
    private Long interventionUid;

    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        InterventionHistId entity = (InterventionHistId) o;
        return Objects.equals(this.versionCtrlNbr, entity.versionCtrlNbr) &&
                Objects.equals(this.interventionUid, entity.interventionUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionCtrlNbr, interventionUid);
    }

}