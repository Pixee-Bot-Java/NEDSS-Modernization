package gov.cdc.nbs.investigation;

import java.time.Instant;
import java.util.List;

import gov.cdc.nbs.message.enums.PregnancyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestigationFilter {
    private Long patientId;
    private List<String> conditions;
    private List<String> programAreas;
    private List<Long> jurisdictions;
    private PregnancyStatus pregnancyStatus;
    private IdType eventIdType;
    private String eventId;
    private InvestigationEventDateSearch eventDateSearch;
    private Long createdBy;
    private Long lastUpdatedBy;
    private ProviderFacilitySearch providerFacilitySearch;
    private Long investigatorId;
    private InvestigationStatus investigationStatus;
    private List<String> outbreakNames;
    private List<CaseStatus> caseStatuses;
    private List<NotificationStatus> notificationStatuses;
    private List<ProcessingStatus> processingStatuses;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class InvestigationEventDateSearch {
        private EventDateType eventDateType;
        private Instant from;
        private Instant to;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ProviderFacilitySearch {
        private ReportingEntityType entityType;
        private Long id;
    }

    public enum IdType {
        ABCS_CASE_ID,
        CITY_COUNTY_CASE_ID,
        INVESTIGATION_ID,
        NOTIFICATION_ID,
        STATE_CASE_ID
    }

    public enum ReportingEntityType {
        FACILITY,
        PROVIDER
    }

    public enum ProcessingStatus {
        UNASSIGNED,
        AWAITING_INTERVIEW,
        CLOSED_CASE,
        FIELD_FOLLOW_UP,
        NO_FOLLOW_UP,
        OPEN_CASE,
        SURVEILLANCE_FOLLOW_UP
    }

    public enum NotificationStatus {
        UNASSIGNED,
        APPROVED,
        COMPLETED,
        MESSAGE_FAILED,
        PENDING_APPROVAL,
        REJECTED
    }

    public enum CaseStatus {
        UNASSIGNED,
        CONFIRMED,
        NOT_A_CASE,
        PROBABLE,
        SUSPECT,
        UNKNOWN
    }

    public enum EventDateType {
        DATE_OF_REPORT,
        INVESTIGATION_CLOSED_DATE,
        INVESTIGATION_CREATE_DATE,
        INVESTIGATION_START_DATE,
        LAST_UPDATE_DATE,
        NOTIFICATION_CREATE_DATE
    }

    public enum InvestigationStatus {
        OPEN,
        CLOSED
    }
}