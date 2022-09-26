package gov.cdc.nbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DF_sf_metadata_group")
public class DfSfMetadataGroup {
    @Id
    @Column(name = "df_sf_metadata_group_uid", nullable = false)
    private Long id;

    @Column(name = "group_name", length = 3000)
    private String groupName;

    @Column(name = "version_ctrl_nbr", nullable = false)
    private Short versionCtrlNbr;

}