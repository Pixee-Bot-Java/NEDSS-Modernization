package gov.cdc.nbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Person_hist")
public class PersonHist {
    @EmbeddedId
    private PersonHistId id;

    @MapsId("personUid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_uid", nullable = false)
    private Person personUid;

    @Column(name = "add_reason_cd", length = 20)
    private String addReasonCd;

    @Column(name = "add_time")
    private Instant addTime;

    @Column(name = "add_user_id")
    private Long addUserId;

    @Column(name = "administrative_gender_cd", length = 20)
    private String administrativeGenderCd;

    @Column(name = "age_calc")
    private Short ageCalc;

    @Column(name = "age_calc_time")
    private Instant ageCalcTime;

    @Column(name = "age_calc_unit_cd")
    private Character ageCalcUnitCd;

    @Column(name = "age_category_cd", length = 20)
    private String ageCategoryCd;

    @Column(name = "age_reported", length = 10)
    private String ageReported;

    @Column(name = "age_reported_time")
    private Instant ageReportedTime;

    @Column(name = "age_reported_unit_cd", length = 20)
    private String ageReportedUnitCd;

    @Column(name = "birth_gender_cd")
    private Character birthGenderCd;

    @Column(name = "birth_order_nbr")
    private Short birthOrderNbr;

    @Column(name = "birth_time")
    private Instant birthTime;

    @Column(name = "birth_time_calc")
    private Instant birthTimeCalc;

    @Column(name = "cd", length = 50)
    private String cd;

    @Column(name = "cd_desc_txt", length = 100)
    private String cdDescTxt;

    @Column(name = "curr_sex_cd")
    private Character currSexCd;

    @Column(name = "deceased_ind_cd", length = 20)
    private String deceasedIndCd;

    @Column(name = "deceased_time")
    private Instant deceasedTime;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "education_level_cd", length = 20)
    private String educationLevelCd;

    @Column(name = "education_level_desc_txt", length = 100)
    private String educationLevelDescTxt;

    @Column(name = "ethnic_group_ind", length = 20)
    private String ethnicGroupInd;

    @Column(name = "last_chg_reason_cd", length = 20)
    private String lastChgReasonCd;

    @Column(name = "last_chg_time")
    private Instant lastChgTime;

    @Column(name = "last_chg_user_id")
    private Long lastChgUserId;

    @Column(name = "local_id", length = 50)
    private String localId;

    @Column(name = "marital_status_cd", length = 20)
    private String maritalStatusCd;

    @Column(name = "marital_status_desc_txt", length = 20)
    private String maritalStatusDescTxt;

    @Column(name = "mothers_maiden_nm", length = 50)
    private String mothersMaidenNm;

    @Column(name = "multiple_birth_ind", length = 20)
    private String multipleBirthInd;

    @Column(name = "occupation_cd", length = 20)
    private String occupationCd;

    @Column(name = "preferred_gender_cd", length = 20)
    private String preferredGenderCd;

    @Column(name = "prim_lang_cd", length = 20)
    private String primLangCd;

    @Column(name = "prim_lang_desc_txt", length = 100)
    private String primLangDescTxt;

    @Column(name = "record_status_cd", length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time")
    private Instant recordStatusTime;

    @Column(name = "status_cd")
    private Character statusCd;

    @Column(name = "status_time")
    private Instant statusTime;

    @Column(name = "survived_ind_cd")
    private Character survivedIndCd;

    @Column(name = "user_affiliation_txt", length = 20)
    private String userAffiliationTxt;

    @Column(name = "first_nm", length = 50)
    private String firstNm;

    @Column(name = "last_nm", length = 50)
    private String lastNm;

    @Column(name = "middle_nm", length = 50)
    private String middleNm;

    @Column(name = "nm_prefix", length = 20)
    private String nmPrefix;

    @Column(name = "nm_suffix", length = 20)
    private String nmSuffix;

    @Column(name = "preferred_nm", length = 50)
    private String preferredNm;

    @Column(name = "hm_street_addr1", length = 100)
    private String hmStreetAddr1;

    @Column(name = "hm_street_addr2", length = 100)
    private String hmStreetAddr2;

    @Column(name = "hm_city_cd", length = 20)
    private String hmCityCd;

    @Column(name = "hm_city_desc_txt", length = 100)
    private String hmCityDescTxt;

    @Column(name = "hm_state_cd", length = 20)
    private String hmStateCd;

    @Column(name = "hm_zip_cd", length = 20)
    private String hmZipCd;

    @Column(name = "hm_cnty_cd", length = 20)
    private String hmCntyCd;

    @Column(name = "hm_cntry_cd", length = 20)
    private String hmCntryCd;

    @Column(name = "hm_phone_nbr", length = 20)
    private String hmPhoneNbr;

    @Column(name = "hm_phone_cntry_cd", length = 20)
    private String hmPhoneCntryCd;

    @Column(name = "hm_email_addr", length = 100)
    private String hmEmailAddr;

    @Column(name = "cell_phone_nbr", length = 20)
    private String cellPhoneNbr;

    @Column(name = "wk_street_addr1", length = 100)
    private String wkStreetAddr1;

    @Column(name = "wk_street_addr2", length = 100)
    private String wkStreetAddr2;

    @Column(name = "wk_city_cd", length = 20)
    private String wkCityCd;

    @Column(name = "wk_city_desc_txt", length = 100)
    private String wkCityDescTxt;

    @Column(name = "wk_state_cd", length = 20)
    private String wkStateCd;

    @Column(name = "wk_zip_cd", length = 20)
    private String wkZipCd;

    @Column(name = "wk_cnty_cd", length = 20)
    private String wkCntyCd;

    @Column(name = "wk_cntry_cd", length = 20)
    private String wkCntryCd;

    @Column(name = "wk_phone_nbr", length = 20)
    private String wkPhoneNbr;

    @Column(name = "wk_phone_cntry_cd", length = 20)
    private String wkPhoneCntryCd;

    @Column(name = "wk_email_addr", length = 100)
    private String wkEmailAddr;

    @Column(name = "SSN", length = 100)
    private String ssn;

    @Column(name = "medicaid_num", length = 100)
    private String medicaidNum;

    @Column(name = "dl_num", length = 100)
    private String dlNum;

    @Column(name = "dl_state_cd", length = 20)
    private String dlStateCd;

    @Column(name = "race_cd", length = 20)
    private String raceCd;

    @Column(name = "race_seq_nbr")
    private Short raceSeqNbr;

    @Column(name = "race_category_cd", length = 20)
    private String raceCategoryCd;

    @Column(name = "ethnicity_group_cd", length = 20)
    private String ethnicityGroupCd;

    @Column(name = "ethnicity_group_seq_nbr")
    private Short ethnicityGroupSeqNbr;

    @Column(name = "adults_in_house_nbr")
    private Short adultsInHouseNbr;

    @Column(name = "children_in_house_nbr")
    private Short childrenInHouseNbr;

    @Column(name = "birth_city_cd", length = 20)
    private String birthCityCd;

    @Column(name = "birth_city_desc_txt", length = 100)
    private String birthCityDescTxt;

    @Column(name = "birth_cntry_cd", length = 20)
    private String birthCntryCd;

    @Column(name = "birth_state_cd", length = 20)
    private String birthStateCd;

    @Column(name = "race_desc_txt", length = 100)
    private String raceDescTxt;

    @Column(name = "ethnic_group_desc_txt", length = 100)
    private String ethnicGroupDescTxt;

    @Column(name = "as_of_date_admin")
    private Instant asOfDateAdmin;

    @Column(name = "as_of_date_ethnicity")
    private Instant asOfDateEthnicity;

    @Column(name = "as_of_date_general")
    private Instant asOfDateGeneral;

    @Column(name = "as_of_date_morbidity")
    private Instant asOfDateMorbidity;

    @Column(name = "as_of_date_sex")
    private Instant asOfDateSex;

    @Column(name = "electronic_ind")
    private Character electronicInd;

    @Column(name = "person_parent_uid")
    private Long personParentUid;

    @Column(name = "dedup_match_ind")
    private Character dedupMatchInd;

    @Column(name = "group_nbr")
    private Integer groupNbr;

    @Column(name = "group_time")
    private Instant groupTime;

    @Column(name = "edx_ind", length = 1)
    private String edxInd;

    @Column(name = "speaks_english_cd", length = 20)
    private String speaksEnglishCd;

    @Column(name = "additional_gender_cd", length = 50)
    private String additionalGenderCd;

    @Column(name = "ehars_id", length = 20)
    private String eharsId;

    @Column(name = "ethnic_unk_reason_cd", length = 20)
    private String ethnicUnkReasonCd;

    @Column(name = "sex_unk_reason_cd", length = 20)
    private String sexUnkReasonCd;

}