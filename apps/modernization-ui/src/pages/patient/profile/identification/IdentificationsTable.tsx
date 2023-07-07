import { useEffect, useRef, useState } from 'react';
import { Button, Icon, ModalRef } from '@trussworks/react-uswds';
import format from 'date-fns/format';
import { SortableTable } from 'components/Table/SortableTable';
import { Actions as ActionState } from 'components/Table/Actions';
import { TOTAL_TABLE_DATA } from 'utils/util';
import { Identification, IdentificationEntry } from './identification';
import {
    FindPatientProfileQuery,
    useAddPatientIdentificationMutation,
    useDeletePatientIdentificationMutation,
    useUpdatePatientIdentificationMutation
} from 'generated/graphql/schema';
import { Direction, sortByAlpha, sortByNestedProperty, withDirection } from 'sorting/Sort';
import { useFindPatientProfileIdentifications } from './useFindPatientProfileIdentifications';
import { maybeDescription, maybeId } from '../coded';
import { internalizeDate } from 'date';
import { tableActionStateAdapter, useTableActionState } from 'table-action';
import { EntryModal } from '../entry/EntryModal';
import { IdentificationEntryForm } from './IdentificationEntryForm';
import { ConfirmationModal } from 'confirmation';
import { Detail, DetailsModal } from '../DetailsModal';
import { useAlert } from 'alert/useAlert';

const asEntry = (identification: Identification): IdentificationEntry => ({
    patient: identification.patient,
    asOf: internalizeDate(identification?.asOf),
    type: maybeId(identification.type),
    value: identification.value,
    state: maybeId(identification?.authority),
    sequence: identification?.sequence
});

const asDetail = (data: Identification): Detail[] => [
    { name: 'As of', value: internalizeDate(data.asOf) },
    { name: 'Type', value: maybeDescription(data.type) },
    { name: 'Authority', value: maybeDescription(data.authority) },
    { name: 'Value', value: data.value }
];

const resolveInitialEntry = (patient: string): IdentificationEntry => ({
    patient: +patient,
    asOf: null,
    type: null,
    value: null,
    state: null
});

type Props = {
    patient: string;
};

export const IdentificationsTable = ({ patient }: Props) => {
    const { showAlert } = useAlert();
    const [tableHead, setTableHead] = useState<{ name: string; sortable: boolean; sort?: string }[]>([
        { name: 'As of', sortable: true, sort: 'all' },
        { name: 'Type', sortable: true, sort: 'all' },
        { name: 'Authority', sortable: true, sort: 'all' },
        { name: 'Value', sortable: true, sort: 'all' },
        { name: 'Actions', sortable: false }
    ]);

    const [total, setTotal] = useState<number>(0);
    const [currentPage, setCurrentPage] = useState<number>(1);

    const initial = resolveInitialEntry(patient);

    const [isActions, setIsActions] = useState<any>(null);
    const [identifications, setIdentifications] = useState<Identification[]>([]);

    const handleComplete = (data: FindPatientProfileQuery) => {
        setTotal(data?.findPatientProfile?.identification?.total ?? 0);
        setIdentifications(data?.findPatientProfile?.identification?.content || []);
    };

    const [getProfile, { refetch }] = useFindPatientProfileIdentifications({ onCompleted: handleComplete });

    const [add] = useAddPatientIdentificationMutation();
    const [update] = useUpdatePatientIdentificationMutation();
    const [remove] = useDeletePatientIdentificationMutation();

    const { selected, actions } = useTableActionState<Identification>();
    const modal = useRef<ModalRef>(null);

    useEffect(() => {
        modal.current?.toggleModal(undefined, selected !== undefined);
    }, [selected]);

    useEffect(() => {
        getProfile({
            variables: {
                patient: patient.toString(),
                page4: {
                    pageNumber: currentPage - 1,
                    pageSize: TOTAL_TABLE_DATA
                }
            }
        });
    }, [currentPage]);

    const onAdded = (entry: IdentificationEntry) => {
        add({
            variables: {
                input: {
                    patient: entry.patient,
                    asOf: entry.asOf,
                    value: entry.value || '',
                    type: entry.type || '',
                    authority: entry.state
                }
            }
        })
            .then(() => {
                showAlert({
                    type: 'success',
                    header: 'success',
                    message: `Added Identification`
                });
                refetch();
            })
            .then(actions.reset);
    };

    const onChanged = (entry: IdentificationEntry) => {
        if (entry.sequence) {
            update({
                variables: {
                    input: {
                        patient: entry.patient,
                        sequence: entry.sequence,
                        asOf: entry.asOf,
                        value: entry.value || '',
                        type: entry.type || '',
                        authority: entry.state
                    }
                }
            })
                .then(() => {
                    showAlert({
                        type: 'success',
                        header: 'success',
                        message: `Updated Identification`
                    });
                    refetch();
                })
                .then(actions.reset);
        }
    };

    const onDeleted = () => {
        if (selected?.type == 'delete') {
            remove({
                variables: {
                    input: {
                        patient: selected.item.patient,
                        sequence: selected.item.sequence
                    }
                }
            })
                .then(() => {
                    showAlert({
                        type: 'success',
                        header: 'success',
                        message: `Deleted Identification`
                    });
                    refetch();
                })
                .then(actions.reset);
        }
    };

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
                setIdentifications(
                    identifications?.slice().sort((a: Identification, b: Identification) => {
                        const dateA: any = new Date(a?.asOf);
                        const dateB: any = new Date(b?.asOf);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'type':
                setIdentifications(identifications.slice().sort(withDirection(sortByNestedProperty('type'), type)));
                break;
            case 'authority':
                setIdentifications(
                    identifications.slice().sort(withDirection(sortByNestedProperty('authority'), type))
                );
                break;
            case 'value':
                setIdentifications(identifications.slice().sort(withDirection(sortByAlpha('value') as any, type)));
                break;
        }
    };

    return (
        <>
            <SortableTable
                isPagination={true}
                buttons={
                    <div className="grid-row">
                        <Button type="button" onClick={actions.prepareForAdd} className="display-inline-flex">
                            <Icon.Add className="margin-right-05" />
                            Add identification
                        </Button>
                    </div>
                }
                tableHeader={'Identifications'}
                tableHead={tableHead}
                tableBody={identifications?.map((identification, index: number) => (
                    <tr key={index}>
                        <td className={`font-sans-md table-data ${tableHead[0].sort !== 'all' && 'sort-td'}`}>
                            {identification?.asOf ? (
                                <span>
                                    {format(new Date(identification?.asOf), 'MM/dd/yyyy')} <br />{' '}
                                </span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[1].sort !== 'all' && 'sort-td'}`}>
                            {identification?.type ? (
                                <span>{identification?.type.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[2].sort !== 'all' && 'sort-td'}`}>
                            {identification?.authority ? (
                                <span>{identification?.authority.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[3].sort !== 'all' && 'sort-td'}`}>
                            {identification?.value ? (
                                <span>{identification?.value}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
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
                                    <ActionState
                                        handleOutsideClick={() => setIsActions(null)}
                                        handleAction={(type: string) => {
                                            tableActionStateAdapter(actions, identification)(type);
                                            setIsActions(null);
                                        }}
                                    />
                                )}
                            </div>
                        </td>
                    </tr>
                ))}
                totalResults={total}
                currentPage={currentPage}
                handleNext={setCurrentPage}
                sortDirectionData={handleSort}
            />

            {selected?.type === 'add' && (
                <EntryModal modal={modal} id="add-patient-identification-modal" title="Add - Identification">
                    <IdentificationEntryForm
                        action={'Add'}
                        entry={initial}
                        onCancel={actions.reset}
                        onChange={onAdded}
                    />
                </EntryModal>
            )}

            {selected?.type === 'update' && (
                <EntryModal modal={modal} id="edit-patient-identification-modal" title="Edit - Identification">
                    <IdentificationEntryForm
                        action={'Edit'}
                        entry={asEntry(selected.item)}
                        onCancel={actions.reset}
                        onChange={onChanged}
                    />
                </EntryModal>
            )}

            {selected?.type === 'delete' && (
                <ConfirmationModal
                    modal={modal}
                    title="Delete Identification"
                    message="Are you sure you want to delete this identification record?"
                    confirmText="Yes, delete"
                    onConfirm={onDeleted}
                    onCancel={actions.reset}
                />
            )}

            {selected?.type === 'detail' && (
                <DetailsModal
                    title={'View details - Identification'}
                    modal={modal}
                    details={asDetail(selected.item)}
                    onClose={actions.reset}
                />
            )}
        </>
    );
};
