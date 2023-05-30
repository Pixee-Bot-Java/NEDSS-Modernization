/* eslint-disable @typescript-eslint/no-unused-vars */
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
import { AddNameModal } from 'pages/patient/profile/names/AddNameModal';
import { DetailsNameModal } from 'pages/patient/profile/names/DetailsNameModal';
import { Actions } from 'components/Table/Actions';
import { TOTAL_TABLE_DATA } from 'utils/util';
import { Address } from './addresses';
import { FindPatientProfileQuery, PatientAddress } from 'generated/graphql/schema';
import { Direction, sortByAlpha, sortByDate, sortByNestedProperty, withDirection } from 'sorting/Sort';
import { useFindPatientProfileAddresses } from './useFindPatientProfileAddresses';
import { AddAddressModal } from 'pages/patient/profile/addresses/AddressModal';
import { DetailsAddressModal } from 'pages/patient/profile/addresses/DetailsAddressModal';

type PatientLabReportTableProps = {
    patient: string | undefined;
};

export const AddressesTable = ({ patient }: PatientLabReportTableProps) => {
    const [tableHead, setTableHead] = useState<{ name: string; sortable: boolean; sort?: string }[]>([
        { name: 'As of', sortable: true, sort: 'all' },
        { name: 'Type', sortable: true, sort: 'all' },
        { name: 'Address', sortable: true, sort: 'all' },
        { name: 'City', sortable: true, sort: 'all' },
        { name: 'State', sortable: true, sort: 'all' },
        { name: 'Zip', sortable: true, sort: 'all' },
        { name: 'Actions', sortable: false }
    ]);
    const [currentPage, setCurrentPage] = useState<number>(1);

    const addModalRef = useRef<ModalRef>(null);
    const detailsModalRef = useRef<ModalRef>(null);
    const deleteModalRef = useRef<ModalRef>(null);

    const [isEditModal, setIsEditModal] = useState<boolean>(false);
    const [details, setDetails] = useState<any>(undefined);
    const [isActions, setIsActions] = useState<any>(null);
    const [addresses, setAddresses] = useState<Address[]>([]);
    const [isDeleteModal, setIsDeleteModal] = useState<boolean>(false);

    const handleComplete = (data: FindPatientProfileQuery) => {
        if (data?.findPatientProfile?.addresses?.content && data?.findPatientProfile?.addresses?.content?.length > 0) {
            setAddresses(data?.findPatientProfile?.addresses?.content);
        }
    };

    const [getProfile, { data }] = useFindPatientProfileAddresses({ onCompleted: handleComplete });

    useEffect(() => {
        if (patient) {
            getProfile({
                variables: {
                    shortId: +patient,
                    page2: {
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
                setAddresses(
                    addresses?.slice().sort((a: Address, b: Address) => {
                        const dateA: any = new Date(a?.asOf);
                        const dateB: any = new Date(b?.asOf);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'type':
                setAddresses(addresses.slice().sort(withDirection(sortByNestedProperty('type'), type)));
                break;
            case 'address':
                setAddresses(addresses.slice().sort(withDirection(sortByAlpha('address1') as any, type)));
                break;
            case 'city':
                setAddresses(addresses.slice().sort(withDirection(sortByAlpha('city') as any, type)));
                break;
            case 'state':
                setAddresses(addresses.slice().sort(withDirection(sortByNestedProperty('state'), type)));
                break;
            case 'zip':
                setAddresses(addresses.slice().sort(withDirection(sortByAlpha('zipcode') as any, type)));
                break;
        }
    };

    const getAddress = (name: PatientAddress) => {
        return `${name?.address1 ?? ''} ${name?.address2 ?? ''} ${name?.address2 ?? ''}`;
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
                            Add address
                        </Button>
                        <AddAddressModal
                            modalHead={isEditModal ? 'Edit - Address' : 'Add - Address'}
                            modalRef={addModalRef}
                        />
                        <DetailsAddressModal data={details} modalRef={detailsModalRef} />
                    </div>
                }
                tableHeader={'Address'}
                tableHead={tableHead}
                tableBody={addresses?.map((name, index: number) => (
                    <tr key={index}>
                        <td className={`font-sans-md table-data ${tableHead[0].sort !== 'all' && 'sort-td'}`}>
                            {name?.asOf ? (
                                <span>
                                    {format(new Date(name?.asOf), 'MM/dd/yyyy')} <br />{' '}
                                </span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[1].sort !== 'all' && 'sort-td'}`}>
                            {name?.type ? (
                                <span>{name?.type.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[2].sort !== 'all' && 'sort-td'}`}>
                            {name?.address1 || name?.address2 ? (
                                <span>{getAddress(name)}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[3].sort !== 'all' && 'sort-td'}`}>
                            {name?.city ? <span>{name?.city}</span> : <span className="no-data">No data</span>}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[4].sort !== 'all' && 'sort-td'}`}>
                            {name?.state ? (
                                <span>{name?.state?.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[5].sort !== 'all' && 'sort-td'}`}>
                            {name?.zipcode ? <span>{name?.zipcode}</span> : <span className="no-data">No data</span>}
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
                                                deleteModalRef.current?.toggleModal();
                                            }
                                            if (type === 'details') {
                                                setDetails(name);
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
                totalResults={data?.findPatientProfile?.addresses?.total}
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
                        <p id="modal-1-description">Are you sure you want to delete this address record?</p>
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
