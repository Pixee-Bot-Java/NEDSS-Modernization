import { useContext, useEffect, useState } from 'react';
import { Button, Icon } from '@trussworks/react-uswds';
import format from 'date-fns/format';
import {
    FindMorbidityReportsForPatientQuery,
    useFindMorbidityReportsForPatientLazyQuery
} from 'generated/graphql/schema';

import { TOTAL_TABLE_DATA } from 'utils/util';
import { SortableTable } from 'components/Table/SortableTable';
import { RedirectControllerService } from 'generated';
import { UserContext } from 'providers/UserContext';
import { Config } from 'config';

export type PatientMorbidities = FindMorbidityReportsForPatientQuery['findMorbidityReportsForPatient'];

type PatientMoribidityTableProps = {
    patient?: string;
    pageSize?: number;
};

export const MorbidityTable = ({ patient, pageSize = TOTAL_TABLE_DATA }: PatientMoribidityTableProps) => {
    const { state } = useContext(UserContext);
    const NBS_URL = Config.nbsUrl;

    const [currentPage, setCurrentPage] = useState<number>(1);
    const [total, setTotal] = useState<number>(0);
    const [morbidityData, setMorbidityData] = useState<any>([]);
    const [tableHead, setTableHead] = useState<{ name: string; sortable: boolean; sort?: string }[]>([
        { name: 'Date received', sortable: true, sort: 'all' },
        { name: 'Provider', sortable: true, sort: 'all' },
        { name: 'Report date', sortable: true, sort: 'all' },
        { name: 'Condition', sortable: true, sort: 'all' },
        { name: 'Jurisdiction', sortable: true, sort: 'all' },
        { name: 'Associated with', sortable: true, sort: 'all' },
        { name: 'Event #', sortable: true, sort: 'all' }
    ]);

    const handleComplete = (data: FindMorbidityReportsForPatientQuery) => {
        setTotal(data?.findMorbidityReportsForPatient?.total || 0);
        setMorbidityData(data.findMorbidityReportsForPatient?.content);
    };

    const [getmorbidity] = useFindMorbidityReportsForPatientLazyQuery({ onCompleted: handleComplete });

    useEffect(() => {
        if (patient) {
            getmorbidity({
                variables: {
                    patient: patient,
                    page: {
                        pageNumber: currentPage - 1,
                        pageSize
                    }
                }
            });
        }
    }, [patient, currentPage]);

    const tableHeadChanges = (name: string, type: string) => {
        tableHead.map((item) => {
            if (item.name.toLowerCase() === name.toLowerCase()) {
                item.sort = type;
            } else {
                item.sort = 'all';
            }
        });
        setTableHead(tableHead);
    };

    const sortData = (name: string, type: string) => {
        setMorbidityData(
            name === 'associatedWith'
                ? morbidityData?.slice().sort((a: any, b: any) => {
                      if (a[name] && b[name]) {
                          if (a?.associatedWith?.condition.toLowerCase() < b?.associatedWith?.condition.toLowerCase()) {
                              return type === 'asc' ? -1 : 1;
                          }
                          if (a?.associatedWith?.condition.toLowerCase() > b?.associatedWith?.condition.toLowerCase()) {
                              return type === 'asc' ? 1 : -1;
                          }
                      }
                      return 0;
                  })
                : morbidityData?.slice().sort((a: any, b: any) => {
                      if (a[name] && b[name]) {
                          if (a[name].toLowerCase() < b[name].toLowerCase()) {
                              return type === 'asc' ? -1 : 1;
                          }
                          if (a[name].toLowerCase() > b[name].toLowerCase()) {
                              return type === 'asc' ? 1 : -1;
                          }
                      }
                      return 0;
                  })
        );
    };

    const handleSort = (name: string, type: string) => {
        tableHeadChanges(name, type);
        switch (name.toLowerCase()) {
            case 'date received':
                setMorbidityData(
                    morbidityData.slice().sort((a: any, b: any) => {
                        const dateA: any = new Date(a.receivedOn);
                        const dateB: any = new Date(b.receivedOn);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'report date':
                setMorbidityData(
                    morbidityData.slice().sort((a: any, b: any) => {
                        const dateA: any = new Date(a.reportedOn);
                        const dateB: any = new Date(b.reportedOn);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'provider':
                sortData('provider', type);
                break;
            case 'condition':
                sortData('condition', type);
                break;
            case 'jurisdiction':
                sortData('jurisdiction', type);
                break;
            case 'associated with':
                sortData('associatedWith', type);
                break;
            case 'event #':
                sortData('event', type);
                break;
        }
    };

    return (
        <SortableTable
            isPagination={true}
            buttons={
                <div className="grid-row">
                    <Button
                        type="button"
                        className="grid-row"
                        onClick={() => {
                            RedirectControllerService.preparePatientDetailsUsingGet({
                                authorization: 'Bearer ' + state.getToken()
                            }).then(() => {
                                window.location.href = `${NBS_URL}/LoadAddObservationMorb1.do?ContextAction=AddMorb`;
                            });
                        }}>
                        <Icon.Add className="margin-right-05" />
                        Add morbidity report
                    </Button>
                </div>
            }
            tableHeader={'Morbidity reports'}
            tableHead={tableHead}
            tableBody={
                morbidityData?.length > 0 &&
                morbidityData?.map((morbidity: any, index: number) => {
                    return (
                        <tr key={index}>
                            <td className={`font-sans-md table-data ${tableHead[0].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.receivedOn ? (
                                    <a href="#" className="table-span">
                                        {format(new Date(morbidity?.receivedOn), 'MM/dd/yyyy')} <br />{' '}
                                        {format(new Date(morbidity?.receivedOn), 'hh:mm a')}
                                    </a>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[1].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.provider ? (
                                    <>
                                        <strong>Reporting facility:</strong>
                                        <br />
                                        <span>{morbidity.provider}</span>
                                        <br />
                                    </>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[2].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.reportedOn ? (
                                    <span className="table-span">
                                        {format(new Date(morbidity?.reportedOn), 'MM/dd/yyyy')} <br />{' '}
                                        {format(new Date(morbidity?.reportedOn), 'hh:mm a')}
                                    </span>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[3].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.condition ? (
                                    <span>{morbidity?.condition}</span>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[4].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.jurisdiction ? (
                                    <span>{morbidity?.jurisdiction}</span>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[5].sort !== 'all' && 'sort-td'}`}>
                                {!morbidity || !morbidity?.associatedWith ? (
                                    <span className="no-data">No data</span>
                                ) : (
                                    <div>
                                        <p
                                            className="margin-0 text-primary text-bold link"
                                            style={{ wordBreak: 'break-word' }}>
                                            {morbidity?.associatedWith?.id}
                                        </p>
                                        <p className="margin-0">{morbidity?.associatedWith?.condition}</p>
                                    </div>
                                )}
                            </td>
                            <td className={`font-sans-md table-data ${tableHead[6].sort !== 'all' && 'sort-td'}`}>
                                {morbidity?.event ? (
                                    <span>{morbidity?.event}</span>
                                ) : (
                                    <span className="no-data">No data</span>
                                )}
                            </td>
                        </tr>
                    );
                })
            }
            totalResults={total}
            currentPage={currentPage}
            handleNext={setCurrentPage}
            sortData={handleSort}
        />
    );
};
