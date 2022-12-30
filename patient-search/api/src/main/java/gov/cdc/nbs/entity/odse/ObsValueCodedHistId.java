package gov.cdc.nbs.entity.odse;

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
public class ObsValueCodedHistId implements Serializable {
    private static final long serialVersionUID = 2984464738713322857L;
    @Column(name = "observation_uid", nullable = false)
    private Long observationUid;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        ObsValueCodedHistId entity = (ObsValueCodedHistId) o;
        return Objects.equals(this.code, entity.code) &&
                Objects.equals(this.versionCtrlNbr, entity.versionCtrlNbr) &&
                Objects.equals(this.observationUid, entity.observationUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, versionCtrlNbr, observationUid);
    }

}