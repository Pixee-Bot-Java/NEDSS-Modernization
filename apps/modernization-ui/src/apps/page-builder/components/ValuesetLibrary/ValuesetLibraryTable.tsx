/* eslint-disable camelcase */
import { GetQuestionResponse, PageSummary, QuestionControllerService } from 'apps/page-builder/generated';
import { ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { ValueSet } from 'apps/page-builder/generated';
import { TableBody, TableComponent } from 'components/Table/Table';
import { usePage } from 'page';
import React, { useContext, useEffect, useRef, useState } from 'react';
import { Direction } from 'sorting';
import './ValuesetLibraryTable.scss';
import { SearchBar } from './SearchBar';
import { Icon } from '@trussworks/react-uswds';
import { UserContext } from '../../../../providers/UserContext';
import { useAlert } from 'alert';
import { ModalComponent } from '../../../../components/ModalComponent/ModalComponent';
import { AddValueset } from '../AddValueset/AddValueset';

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
    sortChange: (sort?: string) => void;
    summaries: ValueSet[];
    labModalRef?: any;
};
export const ValuesetLibraryTable = ({ summaries, sortChange, labModalRef }: Props) => {
    const { page, request } = usePage();
    const { showAlert } = useAlert();
    const [tableRows, setTableRows] = useState<TableBody[]>([]);
    const [selectedValueSet, setSelectedValueSet] = useState<ValueSet>({});

    const { state } = useContext(UserContext);
    const authorization = `Bearer ${state.getToken()}`;

    // @ts-ignore
    const asTableRow = (page: ValueSet): TableBody => ({
        id: page.nbsUid,
        checkbox: true,
        tableDetails: [
            {
                id: 1,
                title: <div className="page-name">{page?.valueSetTypeCd}</div> || null
            },
            { id: 2, title: <div className="event-text">{page?.valueSetNm}</div> || null },
            {
                id: 3,
                title: <div>{page?.codeSetDescTxt}</div> || null
            },
            {
                id: 4,
                title:
                    (
                        <div className="ds-u-text-align--right">
                            <Icon.ExpandMore style={{ cursor: 'pointer' }} size={4} color="black" />
                        </div>
                    ) || null
            }
        ]
    });
    const handleSelected = ({ target }: any, item: any) => {
        if (target.checked) {
            const value: any = summaries.filter((val: any) => item.id === val.nbsUid);
            setSelectedValueSet(value);
        } else {
            setSelectedValueSet({});
        }
    };
    const asTableRows = (pages: PageSummary[] | undefined): TableBody[] => pages?.map(asTableRow) || [];

    /*
     * Converts header and Direction to API compatible sort string such as "name,asc"
     */
    const toSortString = (name: string, direction: Direction): string | undefined => {
        if (name && direction && direction !== Direction.None) {
            switch (name) {
                case Column.Type:
                    return `valueSetTypeCd,${direction}`;
                case Column.ValuesetName:
                    return `valueSetNm,${direction}`;
                case Column.ValuesetDesc:
                    return `codeSetDescTxt,${direction}`;
                default:
                    return undefined;
            }
        }
        return undefined;
    };

    useEffect(() => {
        setTableRows(asTableRows(summaries));
    }, [summaries]);

    useEffect(() => {
        return () => localStorage.setItem('selectedQuestion', '0');
    }, []);

    const handleSort = (name: string, direction: Direction): void => {
        sortChange(toSortString(name, direction));
    };
    const handleAddQsn = async () => {
        // @ts-ignore
        // TODO :  we have to add logic for get quetion ID here
        const id: number = parseInt(localStorage.getItem('selectedQuestion'));
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

    const handlerSearch = (search: string) => {
        console.log(search);
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
        <div className="display-block">
            <div className="margin-bottom-1em">No data</div>
            <ModalToggleButton className="submit-btn" type="button" modalRef={modalRef}>
                Add value set
            </ModalToggleButton>
            <ModalComponent
                isLarge
                modalRef={modalRef}
                modalHeading={'Add value set'}
                modalBody={<AddValueset hideHeader modalRef={modalRef} />}
            />
        </div>
    );

    return (
        <TableComponent
            tableHeader=""
            tableHead={tableColumns}
            tableBody={tableRows}
            isPagination={true}
            pageSize={page.pageSize}
            totalResults={page.total}
            currentPage={page.current}
            handleNext={request}
            sortData={handleSort}
            buttons={<SearchBar onChange={handlerSearch} />}
            handleSelected={handleSelected}
            footerAction={footerActionBtn}
            dataNotAvailableElement={dataNotAvailableElement}
        />
    );
};
