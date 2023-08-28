import {
    Button,
    ButtonGroup,
    Icon,
    Modal,
    ModalFooter,
    ModalHeading,
    ModalRef,
    ModalToggleButton,
    TextInput
} from '@trussworks/react-uswds';
import { ChangeEvent, RefObject, useContext, useEffect, useState } from 'react';
import './QuickConditionLookup.scss';
import { TableComponent, TableBody } from 'components/Table/Table';
import { ConditionControllerService, ReadConditionRequest } from 'apps/page-builder/generated';
import { UserContext } from 'user';
import { NavLink } from 'react-router-dom';
import { PagesContext } from 'apps/page-builder/context/PagesContext';
import { NoData } from 'components/NoData';

type Props = {
    modal: RefObject<ModalRef>;
    addConditions: (conditions: string[]) => void;
};

const tableHeaders = [
    { name: 'Condition', sortable: true, className: 'asdf' },
    { name: 'Code', sortable: true },
    { name: 'Program area', sortable: true },
    { name: 'Condition Family', sortable: true },
    { name: 'Investigateion page', sortable: true },
    { name: 'Status', sortable: true }
];

export const QuickConditionLookup = ({ modal, addConditions }: Props) => {
    const [conditions, setConditions] = useState<any[]>([]);
    const [selectedConditions, setSelectedConditions] = useState<string[]>([]);
    const [searchText, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(false);
    const [totalConditions, setTotalConditions] = useState(0);
    const [tableRows, setTableRows] = useState<TableBody[]>([]);
    const { state } = useContext(UserContext);
    const { currentPage, setCurrentPage } = useContext(PagesContext);

    const handleSelectConditions = (event: ChangeEvent<HTMLInputElement>, condition: any) => {
        if (selectedConditions.includes(condition.id)) {
            const newSelectedConditions = selectedConditions.filter((c) => c !== condition.id);
            setSelectedConditions(newSelectedConditions);
        } else {
            setSelectedConditions([...selectedConditions, condition.id]);
        }
    };

    const handleSearch = (value: string) => {
        setSearchTerm(value);
    };

    const handleAddConditions = () => {
        addConditions(selectedConditions);
    };

    const handleSubmitSearch = (page: number) => {
        setLoading(true);
        const authorization = `Bearer ${state.getToken()}`;
        const search: ReadConditionRequest = { searchText };

        ConditionControllerService.searchConditionsUsingPost({
            authorization,
            search,
            page,
            size: 10
        })
            .then((response: any) => {
                setLoading(false);
                setConditions(response.content);
                setTotalConditions(response.totalElements);
            })
            .catch((error: any) => {
                console.error('Error', error);
            });
    };

    useEffect(() => {
        handleSubmitSearch(currentPage);
    }, [currentPage]);

    useEffect(() => {
        addConditions(selectedConditions);
        setTableRows(asTableRows(conditions));
    }, [conditions]);

    const asTableRow = (condition: any): TableBody => ({
        id: condition.id,
        checkbox: true,
        expanded: true,
        tableDetails: [
            {
                id: 1,
                title: <div className="condition-name">{condition?.conditionShortNm || null}</div>
            },
            { id: 2, title: condition.id || null },
            {
                id: 3,
                title: condition?.progAreaCd || null
            },
            { id: 4, title: condition.familyCd },
            { id: 5, title: condition.investigationFormCd || null },
            { id: 6, title: condition.statusCd === 'A' ? 'Active' : 'Inactive' }
        ]
    });

    const asTableRows = (conditions: any | undefined): TableBody[] => conditions?.map(asTableRow) || [];

    return (
        <Modal
            style={{ minWidth: '85%' }}
            ref={modal}
            forceAction
            id="quick-condition-lookup"
            isInitiallyOpen={false}
            isLarge
            aria-labelledby="incomplete-form-confirmation-modal-heading"
            className="padding-0"
            aria-describedby="incomplete-form-confirmation-modal-description">
            <ModalHeading
                id="incomplete-form-confirmation-modal-heading"
                className="border-bottom border-base-lighter font-sans-lg padding-2">
                <div className="header">
                    <div>Search and add condition(s)</div>
                    <Button
                        type="button"
                        unstyled
                        onClick={() => modal.current?.toggleModal(undefined, false)}
                        className="close-btn">
                        <Icon.Close />
                    </Button>
                </div>
            </ModalHeading>
            <div className="condition-lookup-modal-body">
                <p className="description">You can search for existing condition(s) or create a new one</p>
                <div className="search-container">
                    <div style={{ display: 'flex' }}>
                        <TextInput
                            inputSize="medium"
                            value={searchText}
                            onChange={(e) => handleSearch(e.target.value)}
                            placeholder="Search by keyword"
                            data-testid="condition-search"
                            id="condition-search"
                            style={{ height: '41px', border: 'none' }}
                            name={'condition-search'}
                            type="search"
                        />
                        <Button
                            data-testid="condition-search-btn"
                            type="button"
                            style={{ height: '41px', borderRadius: 0 }}
                            onClick={() => {
                                handleSubmitSearch(1);
                                setCurrentPage ? setCurrentPage(1) : null;
                            }}>
                            <Icon.Search />
                        </Button>
                    </div>
                    <Button type="button" outline style={{ height: '41px' }}>
                        <Icon.FilterAlt />
                        Filter
                    </Button>
                    <NavLink to={'page-builder/add/condition'}>
                        <Button type="button" style={{ height: '41px' }}>
                            Add new condition
                        </Button>
                    </NavLink>
                </div>
                {conditions?.length ? (
                    <TableComponent
                        isLoading={loading}
                        tableHeader=""
                        tableHead={tableHeaders}
                        tableBody={tableRows}
                        isPagination={true}
                        pageSize={10}
                        totalResults={totalConditions}
                        currentPage={currentPage}
                        handleNext={setCurrentPage}
                        handleSelected={handleSelectConditions}
                    />
                ) : (
                    <NoData />
                )}
            </div>
            <ModalFooter className="padding-2 margin-left-auto footer">
                <ButtonGroup className="flex-justify-end">
                    <ModalToggleButton modalRef={modal} closer outline data-testid="condition-cancel-btn">
                        Cancel
                    </ModalToggleButton>
                    <ModalToggleButton
                        modalRef={modal}
                        closer
                        onClick={handleAddConditions}
                        data-testid="condition-add-btn">
                        Add Condition
                    </ModalToggleButton>
                </ButtonGroup>
            </ModalFooter>
        </Modal>
    );
};
