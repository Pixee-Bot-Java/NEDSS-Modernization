import { Button, Icon, Table, Pagination, Checkbox } from '@trussworks/react-uswds';
import React, { useState } from 'react';
import './style.scss';
import { TOTAL_TABLE_DATA } from '../../utils/util';
import { Actions } from './Actions';

export type TableDetail = {
    id: string | number;
    title: React.ReactNode | React.ReactNode[] | string;
    class?: string;
    link?: string;
    textAlign?: string;
    type?: string;
};

export type TableBody = {
    id: number | string | undefined | null;
    checkbox?: boolean;
    tableDetails: TableDetail[];
};

export type TableContentProps = {
    tableHeader?: string;
    tableSubHeader?: React.ReactNode | React.ReactNode[] | string;
    tableHead: { name: string; sortable: boolean }[];
    tableBody: TableBody[];
    isPagination?: boolean;
    totalResults?: number;
    currentPage?: number;
    handleNext?: (page: number) => void;
    buttons?: React.ReactNode | React.ReactNode[];
    sortData?: (name: string, type: string) => void;
    handleAction?: (type: string, data: any) => void;
};

export const TableComponent = ({
    tableHeader,
    tableHead,
    tableBody,
    isPagination = false,
    totalResults = 20,
    currentPage = 1,
    handleNext,
    buttons,
    tableSubHeader,
    handleAction,
    sortData
}: TableContentProps) => {
    const [sort, setSort] = useState<boolean>(false);
    const handleSort = (headerName: string) => {
        setSort(!sort);
        sortData?.(headerName, !sort ? 'asc' : 'desc');
    };
    const [isActions, setIsActions] = useState<any>(null);

    return (
        <div>
            <div className="grid-row flex-align-center flex-justify padding-x-2 padding-y-3 border-bottom border-base-lighter">
                <p className="font-sans-lg text-bold margin-0 table-header">
                    {tableHeader}
                    {tableSubHeader}
                </p>
                {buttons}
            </div>
            <Table bordered={false} fullWidth>
                <thead>
                    <tr>
                        {tableHead.map((head: any, index) => (
                            <th key={index} scope="col">
                                <div className="table-head">
                                    <span className="head-name">{head.name}</span>
                                    {head.sortable && (
                                        <Button
                                            className="usa-button--unstyled"
                                            type={'button'}
                                            onClick={() => handleSort(head.name)}>
                                            <Icon.SortArrow color="black" />
                                        </Button>
                                    )}
                                </div>
                            </th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {tableBody?.length > 0 ? (
                        tableBody.map((item: any, index) => (
                            <tr key={index}>
                                {item.tableDetails.map((td: any, ind: number) =>
                                    td.title ? (
                                        td.title === 'Not available yet' ? (
                                            <td key={ind} className="font-sans-md no-data table-data">
                                                {td.title}
                                            </td>
                                        ) : (
                                            <td
                                                className={`${td?.textAlign ? `text-${td?.textAlign}` : ''} table-data`}
                                                key={ind}>
                                                {ind === 0 && item.checkbox && (
                                                    <Checkbox key={index} id={td.title} name={'tableCheck'} label="" />
                                                )}
                                                {td?.type !== 'actions' && (
                                                    <span
                                                        className={
                                                            index === 0 && ind === 0 && item.checkbox
                                                                ? 'check-title'
                                                                : td.class
                                                                ? td.class
                                                                : 'table-span'
                                                        }>
                                                        {td.title}
                                                    </span>
                                                )}
                                                {td?.type === 'actions' && (
                                                    <div className="table-span">
                                                        <Button
                                                            onClick={() =>
                                                                setIsActions(isActions === index ? null : index)
                                                            }
                                                            type="button"
                                                            unstyled>
                                                            {td.title}
                                                        </Button>
                                                        {isActions === index && (
                                                            <Actions
                                                                handleAction={(e: string) => {
                                                                    handleAction?.(e, item);
                                                                    setIsActions(null);
                                                                }}
                                                            />
                                                        )}
                                                    </div>
                                                )}
                                            </td>
                                        )
                                    ) : (
                                        <td key={ind} className="font-sans-md no-data table-data">
                                            No data
                                        </td>
                                    )
                                )}
                            </tr>
                        ))
                    ) : (
                        <tr className="text-center no-data not-available">
                            <td colSpan={tableHead.length}>Not Available</td>
                        </tr>
                    )}
                </tbody>
            </Table>
            <div className="padding-2 padding-top-0 grid-row flex-align-center flex-justify">
                <p className="margin-0 show-length-text">
                    Showing {tableBody?.length} of {tableBody?.length}
                </p>
                {isPagination && tableBody?.length >= TOTAL_TABLE_DATA && (
                    <Pagination
                        className="margin-0 pagination"
                        totalPages={Math.ceil(totalResults / TOTAL_TABLE_DATA)}
                        currentPage={currentPage}
                        pathname={'/patient-profile'}
                        onClickNext={() => handleNext?.(currentPage + 1)}
                        onClickPrevious={() => handleNext?.(currentPage - 1)}
                        onClickPageNumber={(_, page) => handleNext?.(page)}
                    />
                )}
            </div>
        </div>
    );
};
