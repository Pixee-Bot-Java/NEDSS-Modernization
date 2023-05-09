import { useEffect, useRef, useState } from 'react';
import {
    Button,
    ButtonGroup,
    Icon,
    Modal,
    ModalFooter,
    ModalHeading,
    ModalRef,
    ModalToggleButton
} from '@trussworks/react-uswds';
import format from 'date-fns/format';
import { SortableTable } from 'components/Table/SortableTable';
import { Actions } from 'components/Table/Actions';
import { TOTAL_TABLE_DATA } from 'utils/util';
import { FindPatientProfileQuery } from 'generated/graphql/schema';
import { Direction, sortByNestedProperty, withDirection } from 'sorting/Sort';
import { Race } from './race';
import { useFindPatientProfileRace } from './useFindPatientProfileRace';
import { AddRaceModal } from 'pages/patient/profile/race/AddRaceModal';
import { DetailsRaceModal } from 'pages/patient/profile/race/DetailsRaceModal';

type PatientLabReportTableProps = {
    patient: string | undefined;
};

export const RacesTable = ({ patient }: PatientLabReportTableProps) => {
    const [tableHead, setTableHead] = useState<{ name: string; sortable: boolean; sort?: string }[]>([
        { name: 'As of', sortable: true, sort: 'all' },
        { name: 'Race', sortable: true, sort: 'all' },
        { name: 'Detailed race', sortable: true, sort: 'all' },
        { name: 'Actions', sortable: false }
    ]);
    const [currentPage, setCurrentPage] = useState<number>(1);

    const addModalRef = useRef<ModalRef>(null);
    const detailsModalRef = useRef<ModalRef>(null);
    const deleteModalRef = useRef<ModalRef>(null);

    const [isEditModal, setIsEditModal] = useState<boolean>(false);
    const [details, setDetails] = useState<any>(undefined);
    const [isActions, setIsActions] = useState<any>(null);
    const [races, setRaces] = useState<Race[]>([]);
    const [isDeleteModal, setIsDeleteModal] = useState<boolean>(false);

    const handleComplete = (data: FindPatientProfileQuery) => {
        if (data?.findPatientProfile?.races?.content && data?.findPatientProfile?.races?.content?.length > 0) {
            setRaces(data?.findPatientProfile?.races?.content);
        }
    };

    const [getProfile, { data }] = useFindPatientProfileRace({ onCompleted: handleComplete });

    useEffect(() => {
        if (patient) {
            getProfile({
                variables: {
                    patient: patient,
                    page5: {
                        pageNumber: currentPage - 1,
                        pageSize: TOTAL_TABLE_DATA
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

    const handleSort = (name: string, type: Direction): any => {
        tableHeadChanges(name, type);
        switch (name.toLowerCase()) {
            case 'as of':
                setRaces(
                    races?.slice().sort((a: Race, b: Race) => {
                        const dateA: any = new Date(a?.asOf);
                        const dateB: any = new Date(b?.asOf);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'race':
                setRaces(races.slice().sort(withDirection(sortByNestedProperty('category'), type)));
                break;
            case 'detailed race':
                setRaces(
                    races?.slice().sort((a: Race, b: Race) => {
                        const detailedA: any = a?.detailed?.[0]?.description;
                        const detailedB: any = b?.detailed?.[0]?.description;
                        return type === 'asc' ? detailedB - detailedA : detailedA - detailedB;
                    })
                );
                break;
        }
    };

    useEffect(() => {
        if (isDeleteModal) {
            deleteModalRef.current?.toggleModal();
        }
    }, [isDeleteModal]);

    return (
        <>
            <SortableTable
                isPagination={true}
                buttons={
                    <div className="grid-row">
                        <Button
                            type="button"
                            onClick={() => {
                                addModalRef.current?.toggleModal();
                                setDetails(null);
                                setIsEditModal(false);
                            }}
                            className="display-inline-flex">
                            <Icon.Add className="margin-right-05" />
                            Add race
                        </Button>
                        <AddRaceModal modalHead={isEditModal ? 'Edit - Race' : 'Add - Race'} modalRef={addModalRef} />
                        <DetailsRaceModal data={details} modalRef={detailsModalRef} />
                    </div>
                }
                tableHeader={'Races'}
                tableHead={tableHead}
                tableBody={races?.map((race, index: number) => (
                    <tr key={index}>
                        <td className={`font-sans-md table-data ${tableHead[0].sort !== 'all' && 'sort-td'}`}>
                            {race?.asOf ? (
                                <a href="#">
                                    {format(new Date(race?.asOf), 'MM/dd/yyyy')} <br />{' '}
                                </a>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[1].sort !== 'all' && 'sort-td'}`}>
                            {race?.category?.description ? (
                                <span>{race?.category?.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[2].sort !== 'all' && 'sort-td'}`}>
                            {race?.detailed?.map((detail) => (
                                <span key={detail?.id}>
                                    {detail?.description} <br />
                                </span>
                            )) || <span className="no-data">No data</span>}
                        </td>
                        <td>
                            <div className="table-span">
                                <Button
                                    type="button"
                                    unstyled
                                    onClick={() => setIsActions(isActions === index ? null : index)}>
                                    <Icon.MoreHoriz className="font-sans-lg" />
                                </Button>

                                {isActions === index && (
                                    <Actions
                                        handleOutsideClick={() => setIsActions(null)}
                                        handleAction={(type: string) => {
                                            if (type === 'edit') {
                                                setIsEditModal(true);
                                                addModalRef.current?.toggleModal();
                                            }
                                            if (type === 'delete') {
                                                setIsDeleteModal(true);
                                            }
                                            if (type === 'details') {
                                                setDetails(race);
                                                detailsModalRef.current?.toggleModal();
                                            }
                                            setIsActions(null);
                                        }}
                                    />
                                )}
                            </div>
                        </td>
                    </tr>
                ))}
                totalResults={data?.findPatientProfile?.races?.total}
                currentPage={currentPage}
                handleNext={setCurrentPage}
                sortDirectionData={handleSort}
            />
            {isDeleteModal && (
                <Modal
                    forceAction
                    ref={deleteModalRef}
                    id="example-modal-1"
                    aria-labelledby="modal-1-heading"
                    className="padding-0"
                    aria-describedby="modal-1-description">
                    <ModalHeading
                        id="modal-1-heading"
                        className="border-bottom border-base-lighter font-sans-lg padding-2">
                        Delete name
                    </ModalHeading>
                    <div className="margin-2 grid-row flex-no-wrap border-left-1 border-accent-warm flex-align-center">
                        <Icon.Warning className="font-sans-2xl margin-x-2" />
                        <p id="modal-1-description">Are you sure you want to delete this race record?</p>
                    </div>
                    <ModalFooter className="border-top border-base-lighter padding-2 margin-left-auto">
                        <ButtonGroup>
                            <Button type="button" onClick={() => setIsDeleteModal(false)} outline>
                                Cancel
                            </Button>
                            <ModalToggleButton modalRef={deleteModalRef} closer className="padding-105 text-center">
                                Yes, delete
                            </ModalToggleButton>
                        </ButtonGroup>
                    </ModalFooter>
                </Modal>
            )}
        </>
    );
};
