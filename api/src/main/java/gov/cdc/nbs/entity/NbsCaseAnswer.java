package gov.cdc.nbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "NBS_case_answer")
public class NbsCaseAnswer {
    @Id
    @Column(name = "nbs_case_answer_uid", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "act_uid", nullable = false)
    private Act actUid;

    @Column(name = "add_time", nullable = false)
    private Instant addTime;

    @Column(name = "add_user_id", nullable = false)
    private Long addUserId;

    @Column(name = "answer_txt", length = 2000)
    private String answerTxt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nbs_question_uid", nullable = false)
    private NbsQuestion nbsQuestionUid;

    @Column(name = "nbs_question_version_ctrl_nbr", nullable = false)
    private Integer nbsQuestionVersionCtrlNbr;

    @Column(name = "last_chg_time", nullable = false)
    private Instant lastChgTime;

    @Column(name = "last_chg_user_id", nullable = false)
    private Long lastChgUserId;

    @Column(name = "record_status_cd", nullable = false, length = 20)
    private String recordStatusCd;

    @Column(name = "record_status_time", nullable = false)
    private Instant recordStatusTime;

    @Column(name = "seq_nbr")
    private Integer seqNbr;

    @Lob
    @Column(name = "answer_large_txt")
    private String answerLargeTxt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nbs_table_metadata_uid")
    private NbsTableMetadatum nbsTableMetadataUid;

    @Column(name = "nbs_ui_metadata_ver_ctrl_nbr")
    private Integer nbsUiMetadataVerCtrlNbr;

    @Column(name = "answer_group_seq_nbr")
    private Integer answerGroupSeqNbr;

}