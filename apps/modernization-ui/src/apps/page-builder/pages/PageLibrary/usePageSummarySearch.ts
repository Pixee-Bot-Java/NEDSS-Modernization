import { useEffect, useReducer } from 'react';

import { PageSummary, PageSummaryService, Filter as APIFilter } from 'apps/page-builder/generated';
import { authorization } from 'authorization';
import { Status as PageStatus, usePage } from 'page';
import { Filter, externalize } from 'filters';
import { useSorting } from 'sorting';

type Sorting = {
    property: string;
    direction: 'asc' | 'desc';
};

type Status = 'initialize' | 'idle' | 'searching' | 'found';

type State = { status: Status; keyword?: string; filters: APIFilter[]; sorting?: Sorting; pages: PageSummary[] };

type Action =
    | { type: 'reset' }
    | { type: 'sort'; sorting: Sorting }
    | { type: 'search'; keyword?: string }
    | { type: 'filter'; filters: APIFilter[] }
    | { type: 'found'; result: PageSummary[] }
    | { type: 'refresh' };

const reducer = (current: State, action: Action): State => {
    switch (action.type) {
        case 'sort':
            return { ...current, status: 'searching', sorting: action.sorting };
        case 'search': {
            return action.keyword !== current.keyword
                ? { ...current, status: 'searching', keyword: action.keyword, pages: [] }
                : current;
        }
        case 'filter':
            return { ...current, status: 'searching', filters: action.filters, pages: [] };
        case 'found':
            return { ...current, status: 'found', pages: action.result };
        case 'refresh':
            return { ...current, status: 'searching' };
        case 'reset':
            return { ...current, status: 'idle' };
    }
};

const initial: State = {
    status: 'idle',
    filters: [],
    pages: []
};

const usePageSummarySearch = () => {
    const [state, dispatch] = useReducer(reducer, initial);

    const { page, ready } = usePage();
    const { sorting } = useSorting();

    useEffect(() => {
        if (page.status == PageStatus.Requested) {
            dispatch({ type: 'refresh' });
        }
    }, [page.status, dispatch]);

    useEffect(() => {
        dispatch({ type: 'refresh' });
    }, [sorting, dispatch]);

    useEffect(() => {
        if (state.status === 'searching') {
            PageSummaryService.search({
                authorization: authorization(),
                page: page.current - 1,
                size: page.pageSize,
                sort: sorting,
                request: {
                    search: state.keyword,
                    filters: state.filters
                }
            })
                .then((response) => ({
                    content: response.content ?? [],
                    total: response.totalElements ?? 0,
                    current: response.number ?? 0
                }))
                .then((result) => {
                    dispatch({ type: 'found', result: result.content });
                    ready(result.total, result.current + 1);
                });
        }
    }, [state.status, page.pageSize, page, state.keyword, state.filters, ready, sorting]);

    return {
        searching: state.status === 'searching',
        pages: state.pages,
        search: (keyword?: string) => dispatch({ type: 'search', keyword }),
        filter: (filters: Filter[]) => dispatch({ type: 'filter', filters: externalize(filters) })
    };
};

export { usePageSummarySearch };