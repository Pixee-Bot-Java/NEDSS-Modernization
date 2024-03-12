use NBS_ODSE
GO

IF (object_id('pii_Act_id') is not null)
    DROP PROCEDURE [dbo].[pii_Act_id]
GO

CREATE PROCEDURE [dbo].[pii_Act_id]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT act_uid, act_id_seq, root_extension_txt
    FROM dbo.Act_id
    WHERE @fromTime IS NULL OR (status_time IS NOT NULL AND status_time > @fromTime)
END
GO

IF (object_id('pii_Act_id_hist') is not null)
DROP PROCEDURE [dbo].[pii_Act_id_hist]
GO

CREATE PROCEDURE [dbo].[pii_Act_id_hist]
    @fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT act_uid, act_id_seq, version_ctrl_nbr, root_extension_txt
    FROM dbo.Act_id_hist
    WHERE @fromTime IS NULL OR (status_time IS NOT NULL AND status_time > @fromTime)
END
GO

IF (object_id('pii_Activity_log') is not null)
    DROP PROCEDURE [dbo].[pii_Activity_log]
GO

CREATE PROCEDURE [dbo].[pii_Activity_log]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT activity_log_uid, doc_nm, message_txt
    FROM dbo.Activity_log
    WHERE @fromTime IS NULL OR add_time > @fromTime
END
GO

IF (object_id('pii_Auth_user') is not null)
    DROP PROCEDURE [dbo].[pii_Auth_user]
GO

CREATE PROCEDURE [dbo].[pii_Auth_user]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT auth_user_uid, user_first_nm, user_last_nm
    FROM dbo.Auth_user
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_case_management') is not null)
    DROP PROCEDURE [dbo].[pii_case_management]
GO

CREATE PROCEDURE [dbo].[pii_case_management]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT case_management_uid, fld_foll_up_dispo_date, fld_foll_up_exam_date, fld_foll_up_expected_date, init_foll_up_closed_date, ooj_due_date,
           subj_complexion, subj_hair, subj_height, subj_oth_idntfyng_info, subj_size_build, surv_closed_date,
           ooj_initg_agncy_outc_due_date, ooj_initg_agncy_outc_snt_date, ooj_initg_agncy_recd_date, surv_assigned_date, foll_up_assigned_date,
           init_foll_up_assigned_date, interview_assigned_date, init_interview_assigned_date, case_closed_date, case_review_status_date
    FROM dbo.case_management
END
GO

IF (object_id('pii_case_management_hist') is not null)
    DROP PROCEDURE [dbo].[pii_case_management_hist]
GO

CREATE PROCEDURE [dbo].[pii_case_management_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT case_management_hist_uid, fld_foll_up_dispo_date, fld_foll_up_exam_date, fld_foll_up_expected_date, init_foll_up_closed_date, ooj_due_date,
           subj_complexion, subj_hair, subj_height, subj_oth_idntfyng_info, subj_size_build, surv_closed_date,
           ooj_initg_agncy_outc_due_date, ooj_initg_agncy_outc_snt_date, ooj_initg_agncy_recd_date, surv_assigned_date, foll_up_assigned_date,
           init_foll_up_assigned_date, interview_assigned_date, init_interview_assigned_date, case_closed_date, case_review_status_date
    FROM dbo.case_management_hist
END
GO

IF (object_id('pii_CDF_subform_import_log') is not null)
    DROP PROCEDURE [dbo].[pii_CDF_subform_import_log]
GO

CREATE PROCEDURE [dbo].[pii_CDF_subform_import_log]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT import_log_uid, admin_comment
    FROM dbo.CDF_subform_import_log
    WHERE @fromTime IS NULL OR (import_time IS NOT NULL AND import_time > @fromTime)
END
GO

IF (object_id('pii_Chart_report_metadata') is not null)
    DROP PROCEDURE [dbo].[pii_Chart_report_metadata]
GO

CREATE PROCEDURE [dbo].[pii_Chart_report_metadata]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT chart_report_metadata_uid, chart_report_desc_txt, chart_report_short_desc_txt
    FROM dbo.Chart_report_metadata
    WHERE @fromTime IS NULL OR (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Confirmation_method') is not null)
    DROP PROCEDURE [dbo].[pii_Confirmation_method]
GO

CREATE PROCEDURE [dbo].[pii_Confirmation_method]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT public_health_case_uid, confirmation_method_cd, confirmation_method_time
    FROM dbo.Confirmation_method
END
GO

IF (object_id('pii_Confirmation_method_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Confirmation_method_hist]
GO

CREATE PROCEDURE [dbo].[pii_Confirmation_method_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT public_health_case_uid, confirmation_method_cd, confirmation_method_time
    FROM dbo.Confirmation_method_hist
END
GO

IF (object_id('pii_CT_contact') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_uid, investigator_assigned_date, disposition_date, named_on_date,
           txt, symptom_onset_date, symptom_txt, evaluation_date, evaluation_txt,
           treatment_start_date, treatment_end_date, treatment_txt
    FROM dbo.CT_contact
    WHERE @fromTime IS NULL OR add_time > @fromTime
END
GO

IF (object_id('pii_CT_contact_hist') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact_hist]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_hist_uid, investigator_assigned_date, disposition_date, named_on_date,
           txt, symptom_onset_date, symptom_txt, evaluation_date, evaluation_txt,
           treatment_start_date, treatment_end_date, treatment_txt
    FROM dbo.CT_contact_hist
    WHERE @fromTime IS NULL OR add_time > @fromTime
END
GO

IF (object_id('pii_CT_contact_answer') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact_answer]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact_answer]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_answer_uid, answer_txt, answer_large_txt
    FROM dbo.CT_contact_answer
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_CT_contact_answer_hist') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact_answer_hist]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact_answer_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_answer_hist_uid, answer_txt, answer_large_txt
    FROM dbo.CT_contact_answer_hist
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_CT_contact_attachment') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact_attachment]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact_attachment]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_attachment_uid, desc_txt, file_nm_txt
    FROM dbo.CT_contact_attachment
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_CT_contact_note') is not null)
    DROP PROCEDURE [dbo].[pii_CT_contact_note]
GO

CREATE PROCEDURE [dbo].[pii_CT_contact_note]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT ct_contact_note_uid, note
    FROM dbo.CT_contact_note
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_Custom_queues') is not null)
    DROP PROCEDURE [dbo].[pii_Custom_queues]
GO

CREATE PROCEDURE [dbo].[pii_Custom_queues]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT queue_name, description
    FROM dbo.Custom_queues
    WHERE @fromTime IS NULL OR (last_chg_time IS NOT NULL AND last_chg_time > @fromTime)
END
GO

IF (object_id('pii_Data_migration_record') is not null)
    DROP PROCEDURE [dbo].[pii_Data_migration_record]
GO

CREATE PROCEDURE [dbo].[pii_Data_migration_record]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT data_migration_record_uid, data_migration_batch_uid, sub_nm
    FROM dbo.Data_migration_record
END
GO

IF (object_id('pii_dsm_algorithm') is not null)
    DROP PROCEDURE [dbo].[pii_dsm_algorithm]
GO

CREATE PROCEDURE [dbo].[pii_dsm_algorithm]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT dsm_algorithm_uid, algorithm_nm, admin_comment
    FROM dbo.dsm_algorithm
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_dsm_algorithm_hist') is not null)
    DROP PROCEDURE [dbo].[pii_dsm_algorithm_hist]
GO

CREATE PROCEDURE [dbo].[pii_dsm_algorithm_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT dsm_algorithm_hist_uid, algorithm_nm, admin_comment
    FROM dbo.dsm_algorithm_hist
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_EDX_activity_detail_log') is not null)
    DROP PROCEDURE [dbo].[pii_EDX_activity_detail_log]
GO

CREATE PROCEDURE [dbo].[pii_EDX_activity_detail_log]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT edx_activity_detail_log_uid, log_comment
    FROM dbo.EDX_activity_detail_log Eadl
    JOIN dbo.EDX_activity_log Eal ON Eal.edx_activity_log_uid = Eadl.edx_activity_log_uid
    WHERE @fromTime IS NULL OR (record_status_time IS NOT NULL AND record_status_time > @fromTime)
END
GO

IF (object_id('pii_EDX_activity_log') is not null)
    DROP PROCEDURE [dbo].[pii_EDX_activity_log]
GO

CREATE PROCEDURE [dbo].[pii_EDX_activity_log]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT edx_activity_log_uid, Entity_nm
    FROM dbo.EDX_activity_log
    WHERE @fromTime IS NULL OR (record_status_time IS NOT NULL AND record_status_time > @fromTime)
END
GO

IF (object_id('pii_EDX_Document') is not null)
    DROP PROCEDURE [dbo].[pii_EDX_Document]
GO

CREATE PROCEDURE [dbo].[pii_EDX_Document]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT EDX_Document_uid, original_payload
    FROM dbo.EDX_Document
    WHERE @fromTime IS NULL OR add_time > @fromTime
END
GO

IF (object_id('pii_ELR_activity_log') is not null)
    DROP PROCEDURE [dbo].[pii_ELR_activity_log]
GO

CREATE PROCEDURE [dbo].[pii_ELR_activity_log]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT msg_observation_uid, elr_activity_log_seq,
           process_time, subject_nm, report_fac_nm, detail_txt
    FROM dbo.ELR_activity_log
    WHERE @fromTime IS NULL OR process_time > @fromTime
END
GO

IF (object_id('pii_ELRWorkerQueue') is not null)
    DROP PROCEDURE [dbo].[pii_ELRWorkerQueue]
GO

CREATE PROCEDURE [dbo].[pii_ELRWorkerQueue]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT recordId, payloadName, errorMessage
    FROM dbo.ELRWorkerQueue
    WHERE @fromTime IS NULL OR (receivedTime IS NOT NULL AND receivedTime > @fromTime)
END
GO

IF (object_id('pii_Entity_id') is not null)
    DROP PROCEDURE [dbo].[pii_Entity_id]
GO

CREATE PROCEDURE [dbo].[pii_Entity_id]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT entity_uid, entity_id_seq, root_extension_txt
    FROM dbo.Entity_id
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Entity_id_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Entity_id_hist]
GO

CREATE PROCEDURE [dbo].[pii_Entity_id_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT entity_uid, entity_id_seq, version_ctrl_nbr,
           root_extension_txt
    FROM dbo.Entity_id_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Export_receiving_facility') is not null)
    DROP PROCEDURE [dbo].[pii_Export_receiving_facility]
GO

CREATE PROCEDURE [dbo].[pii_Export_receiving_facility]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT export_receiving_facility_uid, admin_comment
    FROM dbo.Export_receiving_facility
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_Intervention') is not null)
    DROP PROCEDURE [dbo].[pii_Intervention]
GO

CREATE PROCEDURE [dbo].[pii_Intervention]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT intervention_uid,
           txt, age_at_vacc, material_lot_nm, material_expiration_time
    FROM dbo.Intervention
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Intervention_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Intervention_hist]
GO

CREATE PROCEDURE [dbo].[pii_Intervention_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT intervention_uid, version_ctrl_nbr,
           txt, age_at_vacc, material_lot_nm, material_expiration_time
    FROM dbo.Intervention_hist
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Interview') is not null)
    DROP PROCEDURE [dbo].[pii_Interview]
GO

CREATE PROCEDURE [dbo].[pii_Interview]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT interview_uid, interview_date
    FROM dbo.Interview
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_Interview_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Interview_hist]
GO

CREATE PROCEDURE [dbo].[pii_Interview_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT interview_hist_uid, interview_date
    FROM dbo.Interview_hist
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_lab_event') is not null)
    DROP PROCEDURE [dbo].[pii_lab_event]
GO

CREATE PROCEDURE [dbo].[pii_lab_event]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT lab_event_uid,
           result_rpt_dt, specimen_analyzed_dt, specimen_collection_dt,
           suscep_specimen_collection_dt, suscep_result_rpt_dt,
           lab_result_comments, suscep_lab_result_comments
    FROM dbo.lab_event
END
GO

IF (object_id('pii_lab_event_hist') is not null)
    DROP PROCEDURE [dbo].[pii_lab_event_hist]
GO

CREATE PROCEDURE [dbo].[pii_lab_event_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT lab_event_hist_uid,
           result_rpt_dt, specimen_analyzed_dt, specimen_collection_dt,
           suscep_specimen_collection_dt, suscep_result_rpt_dt,
           lab_result_comments, suscep_lab_result_comments
    FROM dbo.lab_event_hist
END
GO

IF (object_id('pii_Manufactured_material') is not null)
    DROP PROCEDURE [dbo].[pii_Manufactured_material]
GO

CREATE PROCEDURE [dbo].[pii_Manufactured_material]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT material_uid, manufactured_material_seq,
           expiration_time, lot_nm
    FROM dbo.Manufactured_material
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_Manufactured_material_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Manufactured_material_hist]
GO

CREATE PROCEDURE [dbo].[pii_Manufactured_material_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT material_uid, manufactured_material_seq, version_ctrl_nbr,
           expiration_time, lot_nm
    FROM dbo.Manufactured_material_hist
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_nbs_answer') is not null)
    DROP PROCEDURE [dbo].[pii_nbs_answer]
GO

CREATE PROCEDURE [dbo].[pii_nbs_answer]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_answer_uid, answer_txt
    FROM dbo.nbs_answer
    WHERE nbs_question_uid IN (
        SELECT nbs_question_uid FROM dbo.NBS_ui_metadata
        WHERE nbs_ui_component_uid IN (1009, 1014, 1019, 1026, 1029))
            AND @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_nbs_answer_hist') is not null)
    DROP PROCEDURE [dbo].[pii_nbs_answer_hist]
GO

CREATE PROCEDURE [dbo].[pii_nbs_answer_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_answer_hist_uid, answer_txt
    FROM dbo.nbs_answer_hist ans
    WHERE nbs_question_uid IN (
        SELECT nbs_question_uid FROM dbo.NBS_ui_metadata
        WHERE nbs_ui_component_uid IN (1009, 1014, 1019, 1026, 1029))
            AND @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_NBS_case_answer') is not null)
    DROP PROCEDURE [dbo].[pii_NBS_case_answer]
GO

CREATE PROCEDURE [dbo].[pii_NBS_case_answer]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_case_answer_uid, answer_txt
    FROM dbo.NBS_case_answer ans
    WHERE nbs_question_uid IN (
        SELECT nbs_question_uid FROM dbo.NBS_ui_metadata
        WHERE nbs_ui_component_uid IN (1009, 1014, 1019, 1026, 1029))
            AND @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_NBS_case_answer_hist') is not null)
    DROP PROCEDURE [dbo].[pii_NBS_case_answer_hist]
GO

CREATE PROCEDURE [dbo].[pii_NBS_case_answer_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_case_answer_hist_uid, answer_txt
    FROM dbo.NBS_case_answer_hist ans
    WHERE nbs_question_uid IN (
        SELECT nbs_question_uid FROM dbo.NBS_ui_metadata
        WHERE nbs_ui_component_uid IN (1009, 1014, 1019, 1026, 1029))
            AND @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_NBS_document') is not null)
    DROP PROCEDURE [dbo].[pii_NBS_document]
GO

CREATE PROCEDURE [dbo].[pii_NBS_document]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_document_uid, txt, sending_facility_nm
    FROM dbo.NBS_document
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time > @fromTime)
END
GO

IF (object_id('pii_NBS_document_hist') is not null)
    DROP PROCEDURE [dbo].[pii_NBS_document_hist]
GO

CREATE PROCEDURE [dbo].[pii_NBS_document_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_document_hist_uid, txt, sending_facility_nm
    FROM dbo.NBS_document_hist
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time > @fromTime)
END
GO

IF (object_id('pii_NBS_note') is not null)
    DROP PROCEDURE [dbo].[pii_NBS_note]
GO

CREATE PROCEDURE [dbo].[pii_NBS_note]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nbs_note_uid, note
    FROM dbo.NBS_note
    WHERE @fromTime IS NULL OR last_chg_time > @fromTime
END
GO

IF (object_id('pii_Notification') is not null)
    DROP PROCEDURE [dbo].[pii_Notification]
GO

CREATE PROCEDURE [dbo].[pii_Notification]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT notification_uid, txt
    FROM dbo.Notification
    WHERE @fromTime IS NULL OR (last_chg_time IS NOT NULL AND last_chg_time > @fromTime)
END
GO

IF (object_id('pii_Notification_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Notification_hist]
GO

CREATE PROCEDURE [dbo].[pii_Notification_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT notification_uid, version_ctrl_nbr, txt
    FROM dbo.Notification_hist
    WHERE @fromTime IS NULL OR (last_chg_time IS NOT NULL AND last_chg_time > @fromTime)
END
GO

IF (object_id('pii_Obs_value_coded') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_coded]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_coded]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, code,
           display_name, original_txt
    FROM dbo.Obs_value_coded
END
GO

IF (object_id('pii_Obs_value_coded_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_coded_hist]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_coded_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, code,
           display_name, original_txt
    FROM dbo.Obs_value_coded_hist
END
GO

IF (object_id('pii_Obs_value_numeric') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_numeric]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_numeric]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, obs_value_numeric_seq,
           numeric_value_1, numeric_value_2
    FROM dbo.Obs_value_numeric
END
GO

IF (object_id('pii_Obs_value_numeric_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_numeric_hist]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_numeric_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, obs_value_numeric_seq, version_ctrl_nbr,
           numeric_value_1, numeric_value_2
    FROM dbo.Obs_value_numeric_hist
END
GO

IF (object_id('pii_Obs_value_txt') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_txt]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_txt]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, obs_value_txt_seq,
           value_txt
    FROM dbo.Obs_value_txt
END
GO

IF (object_id('pii_Obs_value_txt_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Obs_value_txt_hist]
GO

CREATE PROCEDURE [dbo].[pii_Obs_value_txt_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, obs_value_txt_seq, version_ctrl_nbr,
           value_txt
    FROM dbo.Obs_value_txt_hist
END
GO

IF (object_id('pii_Observation') is not null)
    DROP PROCEDURE [dbo].[pii_Observation]
GO

CREATE PROCEDURE [dbo].[pii_Observation]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid,
           txt, rpt_to_state_time
    FROM dbo.Observation
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Observation_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Observation_hist]
GO

CREATE PROCEDURE [dbo].[pii_Observation_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT observation_uid, version_ctrl_nbr,
           txt, rpt_to_state_time
    FROM dbo.Observation_hist
    WHERE @fromTime IS NULL OR
           (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
           (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Organization') is not null)
    DROP PROCEDURE [dbo].[pii_Organization]
GO

CREATE PROCEDURE [dbo].[pii_Organization]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT organization_uid,
           description, display_nm, city_desc_txt, zip_cd
    FROM dbo.Organization
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Organization_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Organization_hist]
GO

CREATE PROCEDURE [dbo].[pii_Organization_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT organization_uid, version_ctrl_nbr,
           description, display_nm, city_desc_txt, zip_cd
    FROM dbo.Organization_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Organization_name') is not null)
    DROP PROCEDURE [dbo].[pii_Organization_name]
GO

CREATE PROCEDURE [dbo].[pii_Organization_name]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nm.organization_uid, organization_name_seq, nm_txt
    FROM dbo.Organization_name nm
    INNER JOIN dbo.Organization org 
        ON org.organization_uid = nm.organization_uid
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Organization_name_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Organization_name_hist]
GO

CREATE PROCEDURE [dbo].[pii_Organization_name_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT nm.organization_uid, organization_name_seq, nm_txt
    FROM dbo.Organization_name_hist nm
    INNER JOIN dbo.Organization_hist org
        ON org.organization_uid = nm.organization_uid
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Person') is not null)
    DROP PROCEDURE [dbo].[pii_Person]
GO

CREATE PROCEDURE [dbo].[pii_Person]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT person_uid,
           age_calc, age_calc_time, age_reported, age_reported_time,
           birth_time, birth_time_calc, deceased_time, description,
           mothers_maiden_nm, first_nm, last_nm, middle_nm, nm_prefix, nm_suffix, preferred_nm,
           hm_street_addr1, hm_street_addr2, hm_city_desc_txt,
           hm_zip_cd, hm_phone_nbr, hm_email_addr, cell_phone_nbr,
           wk_street_addr1, wk_street_addr2, wk_city_desc_txt,
           wk_zip_cd, wk_phone_nbr, wk_email_addr,
           SSN, medicaid_num, dl_num, birth_city_desc_txt,
           as_of_date_admin, as_of_date_ethnicity, as_of_date_general, as_of_date_morbidity, as_of_date_sex

    FROM dbo.Person
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Person_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Person_hist]
GO

CREATE PROCEDURE [dbo].[pii_Person_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT person_uid, version_ctrl_nbr,
           age_calc, age_calc_time, age_reported, age_reported_time,
           birth_time, birth_time_calc, deceased_time, description,
           mothers_maiden_nm, first_nm, last_nm, middle_nm, nm_prefix, nm_suffix, preferred_nm,
           hm_street_addr1, hm_street_addr2, hm_city_desc_txt,
           hm_zip_cd, hm_phone_nbr, hm_email_addr, cell_phone_nbr,
           wk_street_addr1, wk_street_addr2, wk_city_desc_txt,
           wk_zip_cd, wk_phone_nbr, wk_email_addr,
           SSN, medicaid_num, dl_num, birth_city_desc_txt,
           as_of_date_admin, as_of_date_ethnicity, as_of_date_general, as_of_date_morbidity, as_of_date_sex

    FROM dbo.Person_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Person_name') is not null)
    DROP PROCEDURE [dbo].[pii_Person_name]
GO

CREATE PROCEDURE [dbo].[pii_Person_name]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT person_uid, person_name_seq,
           first_nm, first_nm_sndx, last_nm, last_nm_sndx, last_nm2, last_nm2_sndx,
           middle_nm, middle_nm2, nm_degree, nm_prefix, nm_suffix, nm_use_cd
    FROM dbo.Person_name
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Person_name_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Person_name_hist]
GO

CREATE PROCEDURE [dbo].[pii_Person_name_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT person_uid, person_name_seq, version_ctrl_nbr,
           first_nm, first_nm_sndx, last_nm, last_nm_sndx, last_nm2, last_nm2_sndx,
           middle_nm, middle_nm2, nm_degree, nm_prefix, nm_suffix, nm_use_cd
    FROM dbo.Person_name_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Postal_locator') is not null)
    DROP PROCEDURE [dbo].[pii_Postal_locator]
GO

CREATE PROCEDURE [dbo].[pii_Postal_locator]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT postal_locator_uid,
           city_desc_txt, cnty_desc_txt, street_addr1, street_addr2, zip_cd
    FROM dbo.Postal_locator
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Postal_locator_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Postal_locator_hist]
GO

CREATE PROCEDURE [dbo].[pii_Postal_locator_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT postal_locator_uid, version_ctrl_nbr,
           city_desc_txt, cnty_desc_txt, street_addr1, street_addr2, zip_cd
    FROM dbo.Postal_locator_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Public_health_case') is not null)
    DROP PROCEDURE [dbo].[pii_Public_health_case]
GO

CREATE PROCEDURE [dbo].[pii_Public_health_case]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT public_health_case_uid,
           diagnosis_time, outbreak_name, pat_age_at_onset,
           rpt_form_cmplt_time, rpt_to_county_time, rpt_to_state_time, txt,
           investigator_assigned_time, imported_city_desc_txt, deceased_time,
           contact_inv_txt
    FROM dbo.Public_health_case
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Public_health_case_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Public_health_case_hist]
GO

CREATE PROCEDURE [dbo].[pii_Public_health_case_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT public_health_case_uid, version_ctrl_nbr,
           diagnosis_time, outbreak_name, pat_age_at_onset,
           rpt_form_cmplt_time, rpt_to_county_time, rpt_to_state_time, txt,
           investigator_assigned_time, imported_city_desc_txt, deceased_time,
           contact_inv_txt
    FROM dbo.Public_health_case_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_PublicHealthCaseFact') is not null)
    DROP PROCEDURE [dbo].[pii_PublicHealthCaseFact]
GO

CREATE PROCEDURE [dbo].[pii_PublicHealthCaseFact]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT public_health_case_uid,
           ageInMonths, ageInYears, age_reported, birth_time, birth_time_calc,
           cnty_code_desc_txt, city_desc_txt, confirmation_method_time, county,
           deceased_time, diagnosis_date, event_date,
           firstNotificationSenddate, firstNotificationdate, geoLatitude, geoLongitude,
           investigatorAssigneddate, investigatorName, lastNotificationdate, lastNotificationSenddate,
           mart_record_creation_date, mart_record_creation_time, notificationdate, onSetDate,
           organizationName, outbreak_name, pat_age_at_onset, providerName, reporterName,
           rpt_form_cmplt_time, rpt_to_county_time, rpt_to_state_time, zip_cd,
           patientName, jurisdiction, investigationstartdate, report_date, state_case_id,
           rpt_cnty_desc_txt, outbreak_name_desc, HSPTL_ADMISSION_DT, HSPTL_DISCHARGE_DT
    FROM dbo.PublicHealthCaseFact
    WHERE @fromTime IS NULL OR
        (LASTUPDATE IS NOT NULL AND LASTUPDATE > @fromTime) OR
        (PHC_add_time IS NOT NULL AND PHC_add_time > @fromTime)
END
GO

IF (object_id('pii_Referral') is not null)
    DROP PROCEDURE [dbo].[pii_Referral]
GO

CREATE PROCEDURE [dbo].[pii_Referral]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT referral_uid,
           reason_txt, referral_desc_txt, txt
    FROM dbo.Referral
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Referral_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Referral_hist]
GO

CREATE PROCEDURE [dbo].[pii_Referral_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT referral_uid, version_ctrl_nbr,
           reason_txt, referral_desc_txt, txt
    FROM dbo.Referral_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_SUSPCT_MEAT_OBTND_DATA') is not null)
    DROP PROCEDURE [dbo].[pii_SUSPCT_MEAT_OBTND_DATA]
GO

CREATE PROCEDURE [dbo].[pii_SUSPCT_MEAT_OBTND_DATA]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT business_object_uid, ldf_uid, version_ctrl_nbr, ldf_value
    FROM dbo.SUSPCT_MEAT_OBTND_DATA
END
GO

IF (object_id('pii_Tele_locator') is not null)
    DROP PROCEDURE [dbo].[pii_Tele_locator]
GO

CREATE PROCEDURE [dbo].[pii_Tele_locator]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT tele_locator_uid, email_address, url_address
    FROM dbo.Tele_locator
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Tele_locator_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Tele_locator_hist]
GO

CREATE PROCEDURE [dbo].[pii_Tele_locator_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT tele_locator_uid, version_ctrl_nbr,
           email_address, url_address
    FROM dbo.Tele_locator_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Treatment') is not null)
    DROP PROCEDURE [dbo].[pii_Treatment]
GO

CREATE PROCEDURE [dbo].[pii_Treatment]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT treatment_uid, txt
    FROM dbo.Treatment
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_Treatment_hist') is not null)
    DROP PROCEDURE [dbo].[pii_Treatment_hist]
GO

CREATE PROCEDURE [dbo].[pii_Treatment_hist]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT treatment_uid, version_ctrl_nbr, txt
    FROM dbo.Treatment_hist
    WHERE @fromTime IS NULL OR
        (last_chg_time IS NOT NULL AND last_chg_time > @fromTime) OR
        (add_time IS NOT NULL AND add_time > @fromTime)
END
GO

IF (object_id('pii_USER_PROFILE') is not null)
    DROP PROCEDURE [dbo].[pii_USER_PROFILE]
GO

CREATE PROCEDURE [dbo].[pii_USER_PROFILE]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT NEDSS_ENTRY_ID, FIRST_NM, LAST_NM
    FROM dbo.USER_PROFILE
    WHERE @fromTime IS NULL OR (LAST_UPD_TIME IS NOT NULL AND LAST_UPD_TIME > @fromTime)
END
GO

IF (object_id('pii_Jurisdiction_code') is not null)
    DROP PROCEDURE [dbo].[pii_Jurisdiction_code]
GO

CREATE PROCEDURE [dbo].[pii_Jurisdiction_code]
@fromTime Datetime = null

AS
BEGIN
    SET NOCOUNT ON
    SELECT code, code_desc_txt, code_short_desc_txt
    FROM NBS_SRTE.dbo.Jurisdiction_code
    WHERE @fromTime IS NULL OR
        (status_time IS NOT NULL AND status_time > @fromTime)
END
GO



