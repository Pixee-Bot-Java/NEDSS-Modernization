import { render, waitFor, screen, act, getByTestId, fireEvent } from '@testing-library/react';
import { QuickConditionLookup } from './QuickConditionLookup';
import { PagesContext } from 'apps/page-builder/context/PagesContext';
import { Direction } from 'sorting';
import { Column } from 'apps/page-builder/pages/ManagePages/ManagePagesTable';
import { BrowserRouter } from 'react-router-dom';
import { ConditionControllerService } from 'apps/page-builder/generated/services/ConditionControllerService';

const pageContext = {
    currentPage: 1,
    filter: '',
    setFilter: jest.fn(),
    sortBy: Column.PageName,
    setSortBy: jest.fn(),
    sortDirection: Direction.Ascending,
    setSortDirection: jest.fn(),
    searchQuery: '',
    setSearchQuery: jest.fn(),
    setCurrentPage: jest.fn(),
    pageSize: 10,
    setPageSize: jest.fn(),
    isLoading: false,
    setIsLoading: jest.fn()
};
const onClose = jest.fn();
const addConditions = jest.fn();
const mockSearchConditionUsingPost = jest.spyOn(ConditionControllerService, 'searchConditionUsingPost');

beforeEach(async () => {
    mockSearchConditionUsingPost.mockResolvedValue({
        content: [
            {
                conditionShortNm: 'test name',
                id: 10056,
                familyCd: 'test family',
                investigationFormCd: 'test code',
                progAreaCd: 'test area',
                statusCd: 'A'
            }
        ],
        totalElements: 1,
        totalPages: 1,
        size: 10,
        number: 1,
        numberOfElements: 1,
        first: true,
        last: true,
        empty: false
    });
});

afterEach(() => {
    jest.resetAllMocks();
});

describe('QuickConditionLookup', () => {
    it('should render successfully', () => {
        const modal = { current: null };

        const { baseElement } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );
        expect(baseElement).toBeTruthy();
    });

    it('should fetch  the conditions when mounted', () => {
        const modal = { current: null };
        const { container } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        expect(mockSearchConditionUsingPost).toHaveBeenCalled();
    });

    it('has the correct title', async () => {
        const modal = { current: null };
        const { getByText } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const title = getByText('Search and add condition(s)');
        expect(title).toBeInTheDocument();
    });

    it('has a search bar', async () => {
        const modal = { current: null };
        const { container } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const searchBar = await screen.findByTestId('condition-search');
        expect(searchBar).toBeInTheDocument();
    });

    it('has a search button', async () => {
        const modal = { current: null };
        const { container } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const searchBtn = await screen.findByTestId('condition-search-btn');
        expect(searchBtn).toBeInTheDocument();
    });

    it('should display the correct table headers', async () => {
        const modal = { current: null };
        const { container } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const condition = await screen.findByText('Condition');
        const conditionCode = await screen.findByText('Code');
        const programArea = await screen.findByText('Program area');
        const conditionFamily = await screen.findByText('Condition Family');
        const investigationPage = await screen.findByText('Investigateion page');
        const status = await screen.findByText('Status');

        expect(condition).toBeInTheDocument();
        expect(conditionCode).toBeInTheDocument();
        expect(programArea).toBeInTheDocument();
        expect(conditionFamily).toBeInTheDocument();
        expect(investigationPage).toBeInTheDocument();
        expect(status).toBeInTheDocument();
    });

    it('should display the correct table data', async () => {
        const modal = { current: null };
        const { container } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const condition = await screen.findByText('test name');
        const conditionCode = await screen.findByText('test code');
        const programArea = await screen.findByText('test area');
        const conditionFamily = await screen.findByText('test family');
        const status = await screen.findByText('Active');

        expect(condition).toBeInTheDocument();
        expect(conditionCode).toBeInTheDocument();
        expect(programArea).toBeInTheDocument();
        expect(conditionFamily).toBeInTheDocument();
        expect(status).toBeInTheDocument();
    });

    it('should have a cancel button', async () => {
        const modal = { current: null };
        const { container, getByTestId } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const cancelBtn = getByTestId('condition-cancel-btn');
        expect(cancelBtn).toBeInTheDocument();
    });

    it('should have an add condition button', async () => {
        const modal = { current: null };
        const { container, getByTestId } = render(
            <BrowserRouter>
                <PagesContext.Provider value={pageContext}>
                    <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                </PagesContext.Provider>
            </BrowserRouter>
        );

        const addBtn = getByTestId('condition-add-btn');
        expect(addBtn).toBeInTheDocument();
    });

    describe('when the cancel button is clicked', () => {
        it('should close the modal', async () => {
            const modal = { current: null };
            const { container, getByTestId } = render(
                <BrowserRouter>
                    <PagesContext.Provider value={pageContext}>
                        <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                    </PagesContext.Provider>
                </BrowserRouter>
            );

            const cancelBtn = getByTestId('condition-cancel-btn');
            act(() => {
                cancelBtn.dispatchEvent(new MouseEvent('click', { bubbles: true }));
            });
            expect(onClose).toHaveBeenCalled();
        });
    });

    describe('when the search button is clicked', () => {
        it('should search for the condition', async () => {
            const modal = { current: null };
            const { container, getByTestId } = render(
                <BrowserRouter>
                    <PagesContext.Provider value={pageContext}>
                        <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                    </PagesContext.Provider>
                </BrowserRouter>
            );

            const searchBtn = getByTestId('condition-search-btn');
            act(() => {
                searchBtn.dispatchEvent(new MouseEvent('click', { bubbles: true }));
            });
            expect(mockSearchConditionUsingPost).toHaveBeenCalled();
        });

        it('should call the searchConditionUsingPost with the correct parameters', async () => {
            const modal = { current: null };
            const { container, getByTestId } = render(
                <BrowserRouter>
                    <PagesContext.Provider value={pageContext}>
                        <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                    </PagesContext.Provider>
                </BrowserRouter>
            );

            const searchBtn = getByTestId('condition-search-btn');

            // type data into search bar
            const searchBar = await screen.findByTestId('condition-search');
            // type hello into searchBar
            act(() => {
                fireEvent.change(searchBar, { target: { value: 'hello' } });
            });
            // click search button
            act(() => {
                searchBtn.dispatchEvent(new MouseEvent('click', { bubbles: true }));
            });

            expect(mockSearchConditionUsingPost).toHaveBeenCalledWith({
                authorization: 'Bearer undefined',
                page: 1,
                request: {
                    searchText: 'hello'
                },
                size: 10
            });
        });
    });

    describe('when the add button is clicked', () => {
        it('should add the selected conditions', async () => {
            const modal = { current: null };
            const { container, getByTestId } = render(
                <BrowserRouter>
                    <PagesContext.Provider value={pageContext}>
                        <QuickConditionLookup modal={modal} onClose={onClose} addConditions={addConditions} />
                    </PagesContext.Provider>
                </BrowserRouter>
            );

            const addBtn = getByTestId('condition-add-btn');
            act(() => {
                addBtn.dispatchEvent(new MouseEvent('click', { bubbles: true }));
            });
            expect(addConditions).toHaveBeenCalled();
        });
    });
});