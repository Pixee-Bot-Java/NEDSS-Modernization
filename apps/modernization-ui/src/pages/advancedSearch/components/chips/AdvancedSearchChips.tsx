import { InvestigationFilter, LabReportFilter, PersonFilter } from 'generated/graphql/schema';

import { SEARCH_TYPE } from 'pages/advancedSearch/AdvancedSearch';
import { InvestigationChips } from './InvestigationChips';
import { PatientChips } from '../../../../apps/search/patient/chips/PatientChips';
import { LabReportChips } from './LabReportChips';

type ChipProps = {
    lastSearchType: SEARCH_TYPE | undefined;
    personFilter?: PersonFilter;
    onPersonFilterChange: (personFilter: PersonFilter) => void;
    investigationFilter?: InvestigationFilter;
    onInvestigationFilterChange: (investigationFilter: InvestigationFilter) => void;
    labReportFilter?: LabReportFilter;
    onLabReportFilterChange: (labReportFilter: LabReportFilter) => void;
};
export const AdvancedSearchChips = ({
    lastSearchType,
    personFilter,
    onPersonFilterChange,
    investigationFilter,
    onInvestigationFilterChange,
    labReportFilter,
    onLabReportFilterChange
}: ChipProps) => {
    return (
        <>
            {lastSearchType === SEARCH_TYPE.PERSON && personFilter ? (
                <PatientChips filter={personFilter} onChange={onPersonFilterChange} />
            ) : null}
            {lastSearchType === SEARCH_TYPE.INVESTIGATION && investigationFilter ? (
                <InvestigationChips filter={investigationFilter} onChange={onInvestigationFilterChange} />
            ) : null}
            {lastSearchType === SEARCH_TYPE.LAB_REPORT && labReportFilter ? (
                <LabReportChips filter={labReportFilter} onChange={onLabReportFilterChange} />
            ) : null}
        </>
    );
};
