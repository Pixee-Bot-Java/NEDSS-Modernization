package gov.cdc.nbs.questionbank.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import gov.cdc.nbs.questionbank.valueset.command.ValueSetCommand.Update;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(catalog = "NBS_SRTE", name = "Codeset_Group_Metadata")
public class CodeSetGroupMetadatum {
  @Id
  @Column(name = "code_set_group_id", nullable = false)
  private Long id;

  @Column(name = "code_set_nm", nullable = false, length = 256)
  private String codeSetNm;

  @Column(name = "vads_value_set_code", length = 256)
  private String vadsValueSetCode;

  @Column(name = "code_set_desc_txt", length = 2000)
  private String codeSetDescTxt;

  @Column(name = "code_set_short_desc_txt", length = 2000)
  private String codeSetShortDescTxt;

  @Column(name = "ldf_picklist_ind_cd")
  private Character ldfPicklistIndCd;

  @Column(name = "phin_std_val_ind")
  private Character phinStdValInd;

  @OneToMany(mappedBy = "codeSetGroup")
  private Set<Codeset> codesets = new LinkedHashSet<>();

  public CodeSetGroupMetadatum(Long id, String codeSetDescTxt, String codeSetShortDescTxt, String codeSetName) {
    this.id = id;
    this.codeSetDescTxt = codeSetDescTxt;
    this.codeSetShortDescTxt = codeSetShortDescTxt;
    this.codeSetNm = codeSetName;
    this.vadsValueSetCode = codeSetName;
    this.ldfPicklistIndCd = 'Y';
    this.phinStdValInd = 'N';
  }

  public void update(Update command) {
    this.codeSetShortDescTxt = command.name();
    this.codeSetDescTxt = command.description();
  }
}
