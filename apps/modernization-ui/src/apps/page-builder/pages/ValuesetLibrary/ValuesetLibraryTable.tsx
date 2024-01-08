/* eslint-disable camelcase */
import { Button, Icon, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { useAlert } from 'alert';
import { GetQuestionResponse, PageSummary, QuestionControllerService, ValueSet } from 'apps/page-builder/generated';
import { TableBody, TableComponent } from 'components/Table/Table';
import { useContext, useEffect, useRef, useState } from 'react';
import { Direction } from 'sorting';
import { ModalComponent } from '../../../../components/ModalComponent/ModalComponent';
import { UserContext } from '../../../../providers/UserContext';
import { AddValueset } from '../../components/AddValueset/AddValueset';
import { SearchBar } from './SearchBar';
import './ValuesetLibraryTable.scss';
import ValuesetLibraryTableRowExpanded from './ValuesetLibraryTableRowExpanded';
import { ValueSetsContext } from '../../context/ValueSetContext';
import { QuestionsContext } from '../../context/QuestionsContext';

export enum Column {
    Type = 'Type',
    ValuesetName = 'Value set name',
    ValuesetDesc = 'Value set description'
}

const tableColumns = [
    { name: Column.Type, sortable: true },
    { name: Column.ValuesetName, sortable: true },
    { name: Column.ValuesetDesc, sortable: true },
    { name: '', sortable: false }
];

type Props = {
    summaries: ValueSet[];
    labModalRef?: any;
    pages?: any;
};
export const ValuesetLibraryTable = ({ summaries, labModalRef, pages }: Props) => {
    const { showAlert } = useAlert();
    const [tableRows, setTableRows] = useState<TableBody[]>([]);
    const [selectedValueSet, setSelectedValueSet] = useState<ValueSet>({});
    const [expandedRows, setExpandedRows] = useState<number[]>([]);
    const { searchQuery, setSearchQuery, setCurrentPage, setSortBy, isLoading } = useContext(ValueSetsContext);
    const { setSearchValueSet } = useContext(QuestionsContext);
    const { state } = useContext(UserContext);
    const authorization = `Bearer ${state.getToken()}`;
    // @ts-ignore
    const asTableRow = (valueSet: ValueSet): TableBody => ({
        id: valueSet.nbsUid,
        expanded: expandedRows.some((id) => id === valueSet.nbsUid),
        expandedViewComponent: <ValuesetLibraryTableRowExpanded data={valueSet} />,
        selectable: true,
        tableDetails: [
            {
                id: 1,
                title: <div className="page-name">{valueSet?.valueSetTypeCd}</div> || null
            },
            { id: 2, title: <div className="event-text">{valueSet?.valueSetNm}</div> || null },
            {
                id: 3,
                title: <div>{valueSet?.codeSetDescTxt}</div> || null
            },
            {
                id: 4,
                title:
                    (
                        <div className="ds-u-text-align--right">
                            {expandedRows.some((id) => id === valueSet.nbsUid) ? (
                                <Button
                                    type="button"
                                    unstyled
                                    aria-label="expand-less"
                                    key={valueSet.nbsUid}
                                    data-testid="expand-less">
                                    <Icon.ExpandLess
                                        style={{ cursor: 'pointer' }}
                                        size={4}
                                        color="black"
                                        onClick={() => handleExpandLessClick(valueSet.nbsUid)}
                                    />
                                </Button>
                            ) : (
                                <Button
                                    data-testid="expand-more"
                                    type="button"
                                    unstyled
                                    aria-label="expand-more"
                                    key={valueSet.nbsUid}>
                                    <Icon.ExpandMore
                                        style={{ cursor: 'pointer' }}
                                        size={4}
                                        color="black"
                                        onClick={() => handleExpandMoreClick(valueSet.nbsUid)}
                                    />
                                </Button>
                            )}
                        </div>
                    ) || null
            }
        ]
    });

    const handleExpandMoreClick = (id: number | undefined) => {
        if (id) {
            setExpandedRows([...expandedRows, id]);
        }
    };

    const handleExpandLessClick = (id: number | undefined) => {
        const indexToRemove = expandedRows.findIndex((rowId) => rowId === id);

        if (indexToRemove !== -1) {
            const rows = [...expandedRows];
            rows.splice(indexToRemove, 1);
            setExpandedRows(rows);
        }
    };

    const handleSelected = ({ target }: any, item: any) => {
        if (target.checked) {
            const value = summaries.find((val: ValueSet) => item.id === val.nbsUid) || {};
            setSelectedValueSet(value);
            setSearchValueSet?.(value?.nbsUid!);
        } else {
            setSelectedValueSet({});
        }
    };
    const asTableRows = (pages: PageSummary[] | undefined): TableBody[] => pages?.map(asTableRow) || [];

    /*
     * Converts header and Direction to API compatible sort string such as "name,asc"
     */
    const toSortString = (name: string, direction: Direction): string => {
        if (name && direction && direction !== Direction.None) {
            switch (name) {
                case Column.Type:
                    return `valueSetTypeCd,${direction}`;
                case Column.ValuesetName:
                    return `valueSetNm,${direction}`;
                case Column.ValuesetDesc:
                    return `codeSetDescTxt,${direction}`;
                default:
                    return '';
            }
        }
        return '';
    };

    useEffect(() => {
        setTableRows(asTableRows(summaries));
    }, [summaries, expandedRows]);

    useEffect(() => {
        return () => localStorage.setItem('selectedQuestion', '0');
    }, []);

    const handleSort = (name: string, direction: Direction): void => {
        if (name && Direction) {
            setSortBy(toSortString(name, direction));
        }
    };

    const handleAddQsn = async () => {
        // @ts-ignore
        const id: number = parseInt(localStorage.getItem('selectedQuestion'));
        if (!id) return;
        const { question }: GetQuestionResponse = await QuestionControllerService.getQuestionUsingGet({
            authorization,
            id
        }).then((response: GetQuestionResponse) => {
            return response;
        });

        const {
            valueSet,
            unitValue,
            description,
            messagingInfo,
            label,
            tooltip,
            displayControl,
            mask,
            dataMartInfo,
            uniqueName,
            fieldLength,
            size,
            fieldSize
        }: any = question;

        const request = {
            description,
            labelInMessage: messagingInfo.labelInMessage,
            messageVariableId: messagingInfo.messageVariableId,
            hl7DataType: messagingInfo.hl7DataType,
            label,
            tooltip,
            displayControl,
            mask,
            fieldLength,
            defaultLabelInReport: dataMartInfo.defaultLabelInReport,
            uniqueName,
            valueSet,
            unitValue,
            size: size,
            fieldSize
        };

        QuestionControllerService.updateQuestionUsingPut({
            authorization,
            id,
            request
        }).then((response: any) => {
            setSelectedValueSet({});
            showAlert({ type: 'success', header: 'Add', message: 'Question Added successfully' });
            return response;
        });
    };

    const footerActionBtn = (
        <div className="valueset-action-btn ds-u-text-align--right margin-bottom-1em">
            <ModalToggleButton
                closer
                modalRef={labModalRef}
                className="cancel-btn"
                type="button"
                onClick={() => setSelectedValueSet({})}>
                Cancel
            </ModalToggleButton>
            <ModalToggleButton
                closer
                modalRef={labModalRef}
                className="submit-btn"
                type="button"
                onClick={handleAddQsn}
                disabled={!Object.keys(selectedValueSet).length}>
                Add to question
            </ModalToggleButton>
        </div>
    );

    const modalRef = useRef<ModalRef>(null);
    const dataNotAvailableElement = (
        <div className="no-data-available">
            <label className="margin-bottom-1em no-text">
                {searchQuery ? `No results found for ‘${searchQuery}’` : 'No results found '}
            </label>
            <ModalToggleButton className="submit-btn" type="button" modalRef={modalRef} outline>
                Add value set
            </ModalToggleButton>
            <ModalComponent
                isLarge
                modalRef={modalRef}
                modalHeading={
                    <span className="header-icon-title">
                        <Icon.ArrowBack /> Add value set
                    </span>
                }
                modalBody={<AddValueset hideHeader modalRef={modalRef} />}
            />
        </div>
    );
    const searchAvailableElement = (
        <div className="no-data-available">
            <label className="no-text">Still can't find what are you're looking for?</label>
            <label className="margin-bottom-1em search-desc">
                Please try searching in the local library before creating new
            </label>
            <div>
                <ModalToggleButton className="submit-btn" type="button" modalRef={modalRef} outline>
                    Create New
                </ModalToggleButton>
                <Button className="submit-btn" type="button">
                    Search Local
                </Button>
            </div>
            <ModalComponent
                isLarge
                modalRef={modalRef}
                modalHeading={
                    <span className="header-icon-title">
                        <Icon.ArrowBack /> Add value set
                    </span>
                }
                modalBody={<AddValueset hideHeader modalRef={modalRef} />}
            />
        </div>
    );

    return (
        <div>
            <div>{<SearchBar onChange={setSearchQuery} />}</div>
            {summaries?.length ? (
                <TableComponent
                    contextName="valuesets"
                    tableHeader=""
                    tableHead={tableColumns}
                    tableBody={tableRows}
                    isPagination={true}
                    pageSize={pages?.pageSize || 0}
                    totalResults={pages?.totalElements || 0}
                    currentPage={pages?.currentPage || 0}
                    handleNext={setCurrentPage}
                    sortData={handleSort}
                    selectable
                    handleSelected={handleSelected}
                    rangeSelector={true}
                    isLoading={isLoading}
                />
            ) : (
                dataNotAvailableElement
            )}
            {searchQuery && summaries?.length > 0 && searchAvailableElement}
            <div className="footer-action">{footerActionBtn}</div>
        </div>
    );
};
