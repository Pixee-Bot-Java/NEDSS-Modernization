import { useEffect, useRef, useState } from 'react';
import format from 'date-fns/format';
import { Button, Icon, ModalRef } from '@trussworks/react-uswds';
import {
    FindPatientProfileQuery,
    PatientName,
    useAddPatientNameMutation,
    useDeletePatientNameMutation,
    useUpdatePatientNameMutation
} from 'generated/graphql/schema';
import { Direction, sortByAlpha, sortByNestedProperty, withDirection } from 'sorting/Sort';
import { externalizeDateTime, internalizeDate } from 'date';
import { TOTAL_TABLE_DATA } from 'utils/util';
import { orNull } from 'utils/orNull';
import { SortableTable } from 'components/Table/SortableTable';
import { Actions as ActionState } from 'components/Table/Actions';
import { ConfirmationModal } from 'confirmation';
import { Detail, DetailsModal } from 'pages/patient/profile/DetailsModal';
import EntryModal from 'pages/patient/profile/entry';
import { maybeDescription, maybeId } from 'pages/patient/profile/coded';
import { useFindPatientProfileNames } from './useFindPatientProfileNames';
import { NameEntryForm } from './NameEntryForm';
import { NameEntry } from './NameEntry';
import { useTableActionState, tableActionStateAdapter } from 'table-action';
import { useAlert } from 'alert/useAlert';

const asDetail = (data: PatientName): Detail[] => [
    { name: 'As of', value: internalizeDate(data.asOf) },
    { name: 'Type', value: maybeDescription(data.use) },
    { name: 'Prefix', value: maybeDescription(data.prefix) },
    { name: 'First name', value: data.first },
    { name: 'Middle name', value: data.middle },
    { name: 'Second middle name', value: data.secondMiddle },
    { name: 'Last name', value: data.last },
    { name: 'Second last name', value: data.secondLast },
    { name: 'Suffix', value: maybeDescription(data.suffix) },
    { name: 'Degree', value: maybeDescription(data.degree) }
];

const asEntry = (name: PatientName): NameEntry => ({
    patient: name.patient,
    sequence: name.sequence,
    asOf: internalizeDate(name.asOf),
    type: maybeId(name.use),
    prefix: maybeId(name.prefix),
    first: orNull(name.first),
    middle: orNull(name.middle),
    secondMiddle: orNull(name.secondMiddle),
    last: orNull(name.last),
    secondLast: orNull(name.secondLast),
    suffix: maybeId(name.suffix),
    degree: maybeId(name.degree)
});

const resolveInitialEntry = (patient: string): NameEntry => ({
    patient: +patient,
    sequence: null,
    asOf: null,
    type: null,
    prefix: null,
    first: null,
    middle: null,
    secondMiddle: null,
    last: null,
    secondLast: null,
    suffix: null,
    degree: null
});

type Props = {
    patient: string;
};

export const NamesTable = ({ patient }: Props) => {
    const { showAlert } = useAlert();
    const [tableHead, setTableHead] = useState<{ name: string; sortable: boolean; sort?: string }[]>([
        { name: 'As of', sortable: true, sort: 'all' },
        { name: 'Type', sortable: true, sort: 'all' },
        { name: 'Prefix', sortable: true, sort: 'all' },
        { name: 'Name ( last, first middle )', sortable: true, sort: 'all' },
        { name: 'Suffix', sortable: true, sort: 'all' },
        { name: 'Degree', sortable: true, sort: 'all' },
        { name: 'Actions', sortable: false }
    ]);

    const [total, setTotal] = useState<number>(0);
    const [currentPage, setCurrentPage] = useState<number>(1);

    const initial = resolveInitialEntry(patient);

    const { selected, actions } = useTableActionState<PatientName>();

    const modal = useRef<ModalRef>(null);

    const [isActions, setIsActions] = useState<number | null>(null);
    const [names, setNames] = useState<PatientName[]>([]);

    const handleComplete = (data: FindPatientProfileQuery) => {
        setTotal(data?.findPatientProfile?.names?.total ?? 0);
        setNames(data?.findPatientProfile?.names?.content ?? []);
    };

    const [fetch, { refetch, loading }] = useFindPatientProfileNames({ onCompleted: handleComplete });

    const [add] = useAddPatientNameMutation();
    const [update] = useUpdatePatientNameMutation();
    const [remove] = useDeletePatientNameMutation();

    useEffect(() => {
        fetch({
            variables: {
                patient: patient,
                page: {
                    pageNumber: currentPage - 1,
                    pageSize: TOTAL_TABLE_DATA
                }
            }
        });
    }, [currentPage]);

    useEffect(() => {
        modal.current?.toggleModal(undefined, selected !== undefined);
    }, [selected]);

    const onAdded = (entry: NameEntry) => {
        if (entry.type) {
            add({
                variables: {
                    input: {
                        patient: entry.patient,
                        type: entry.type,
                        asOf: externalizeDateTime(entry.asOf),
                        prefix: entry.prefix,
                        first: entry.first,
                        middle: entry.middle,
                        secondMiddle: entry.secondMiddle,
                        last: entry.last,
                        secondLast: entry.secondLast,
                        suffix: entry.suffix,
                        degree: entry.degree
                    }
                }
            })
                .then(() => {
                    refetch();
                    showAlert({
                        type: 'success',
                        header: 'success',
                        message: `Added name`
                    });
                })
                .then(actions.reset);
        }
    };

    const onChanged = (entry: NameEntry) => {
        if (entry.type && entry.sequence !== null) {
            update({
                variables: {
                    input: {
                        patient: entry.patient,
                        sequence: entry.sequence,
                        type: entry.type,
                        asOf: externalizeDateTime(entry.asOf),
                        prefix: entry.prefix,
                        first: entry.first,
                        middle: entry.middle,
                        secondMiddle: entry.secondMiddle,
                        last: entry.last,
                        secondLast: entry.secondLast,
                        suffix: entry.suffix,
                        degree: entry.degree
                    }
                }
            })
                .then(() => {
                    refetch();
                    showAlert({
                        type: 'success',
                        header: 'success',
                        message: `Updated name`
                    });
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
                    refetch();
                    showAlert({
                        type: 'success',
                        header: 'success',
                        message: `Deleted name`
                    });
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
                setNames(
                    names?.slice().sort((a: PatientName, b: PatientName) => {
                        const dateA: any = new Date(a?.asOf);
                        const dateB: any = new Date(b?.asOf);
                        return type === 'asc' ? dateB - dateA : dateA - dateB;
                    })
                );
                break;
            case 'prefix':
                setNames(names.slice().sort(withDirection(sortByNestedProperty('prefix'), type)));
                break;
            case 'name ( last, first middle )':
                setNames(names.slice().sort(withDirection(sortByAlpha('prefix') as any, type)));
                break;
            case 'suffix':
                setNames(names.slice().sort(withDirection(sortByNestedProperty('suffix'), type)));
                break;
            case 'degree':
                setNames(names.slice().sort(withDirection(sortByNestedProperty('degree'), type)));
                break;
            case 'type':
                setNames(names.slice().sort(withDirection(sortByNestedProperty('use'), type)));
                break;
        }
    };

    return (
        <>
            <SortableTable
                isLoading={loading}
                isPagination={true}
                buttons={
                    <div className="grid-row">
                        <Button type="button" onClick={actions.prepareForAdd} className="display-inline-flex">
                            <Icon.Add className="margin-right-05" />
                            Add name
                        </Button>
                    </div>
                }
                tableHeader={'Names'}
                tableHead={tableHead}
                tableBody={names?.map((name, index: number) => (
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
                            {name?.use ? (
                                <span>{name?.use.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[2].sort !== 'all' && 'sort-td'}`}>
                            {name?.prefix ? (
                                <span>{name?.prefix.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[3].sort !== 'all' && 'sort-td'}`}>
                            {name?.last || name?.first ? (
                                <span>{`${name?.last ? name?.last + ',' : ''} ${name?.first || ''} ${
                                    name?.middle || ''
                                }`}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[4].sort !== 'all' && 'sort-td'}`}>
                            {name?.suffix ? (
                                <span>{name?.suffix.description}</span>
                            ) : (
                                <span className="no-data">No data</span>
                            )}
                        </td>
                        <td className={`font-sans-md table-data ${tableHead[5].sort !== 'all' && 'sort-td'}`}>
                            {name?.degree ? (
                                <span>{name?.degree.description}</span>
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
                                            tableActionStateAdapter(actions, name)(type);
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
                <EntryModal modal={modal} id="add-patient-name-modal" title="Add - Name">
                    <NameEntryForm action={'Add'} entry={initial} onCancel={actions.reset} onChange={onAdded} />
                </EntryModal>
            )}
            {selected?.type === 'update' && (
                <EntryModal modal={modal} id="edit-patient-name-modal" title="Edit - Name">
                    <NameEntryForm
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
                    title="Delete name"
                    message="Are you sure you want to delete this name record?"
                    confirmText="Yes, delete"
                    onConfirm={onDeleted}
                    onCancel={actions.reset}
                />
            )}

            {selected?.type === 'detail' && (
                <DetailsModal
                    title={'View details - Name'}
                    modal={modal}
                    details={asDetail(selected.item)}
                    onClose={actions.reset}
                />
            )}
        </>
    );
};
