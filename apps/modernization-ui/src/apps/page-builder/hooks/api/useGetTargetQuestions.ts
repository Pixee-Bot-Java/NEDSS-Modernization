import { PageRuleControllerService, PagesResponse, TargetQuestionRequest } from 'apps/page-builder/generated';
import { authorization } from 'authorization';
import { useEffect, useReducer } from 'react';

type State =
    | { status: 'idle' }
    | { status: 'fetching'; id: number; request: TargetQuestionRequest }
    | { status: 'complete'; page: PagesResponse }
    | { status: 'error'; error: string };

type Action =
    | { type: 'fetch'; id: number; request: TargetQuestionRequest }
    | { type: 'complete'; page: PagesResponse }
    | { type: 'error'; error: string };

const reducer = (_state: State, action: Action): State => {
    switch (action.type) {
        case 'fetch':
            return { status: 'fetching', id: action.id, request: action.request };
        case 'complete':
            return { status: 'complete', page: action.page };
        case 'error':
            return { status: 'error', error: action.error };
        default:
            return { status: 'idle' };
    }
};

const initial: State = { status: 'idle' };

export const useGetTargetQuestions = () => {
    const [state, dispatch] = useReducer(reducer, initial);

    useEffect(() => {
        if (state.status === 'fetching') {
            PageRuleControllerService.getTargetQuestionsUsingPost({
                authorization: authorization(),
                id: state.id,
                request: state.request
            })
                .catch((error) => dispatch({ type: 'error', error: error.message }))
                .then((response) =>
                    response
                        ? dispatch({ type: 'complete', page: response })
                        : dispatch({ type: 'error', error: 'Failed to retrieve questions' })
                );
        }
    }, [state.status]);

    const value = {
        error: state.status === 'error' ? state.error : undefined,
        response: state.status === 'complete' ? state.page : undefined,
        fetch: (id: number, request: TargetQuestionRequest) => dispatch({ type: 'fetch', id, request })
    };

    return value;
};
