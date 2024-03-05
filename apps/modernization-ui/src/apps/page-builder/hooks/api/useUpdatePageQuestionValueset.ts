import { useEffect, useReducer } from 'react';

import { PageQuestionControllerService, PagesQuestion } from 'apps/page-builder/generated';
import { authorization } from 'authorization';

type State =
    | { status: 'idle' }
    | {
          status: 'updating';
          page: number;
          question: number;
          valueset: number;
      }
    | { status: 'complete'; response: PagesQuestion }
    | { status: 'error'; error: string };

type Action =
    | {
          type: 'update';
          page: number;
          question: number;
          valueset: number;
      }
    | { type: 'complete'; response: PagesQuestion }
    | { type: 'error'; error: string };

const initial: State = { status: 'idle' };

const reducer = (_state: State, action: Action): State => {
    switch (action.type) {
        case 'update':
            return { status: 'updating', page: action.page, question: action.question, valueset: action.valueset };
        case 'complete':
            return { status: 'complete', response: action.response };
        case 'error':
            return { status: 'error', error: action.error };
        default:
            return { ...initial };
    }
};

export const useUpdatePageQuestionValueset = () => {
    const [state, dispatch] = useReducer(reducer, initial);

    useEffect(() => {
        if (state.status === 'updating') {
            PageQuestionControllerService.updatePageCodedQuestionValuesetUsingPut({
                authorization: authorization(),
                questionId: state.question,
                page: state.page,
                request: { valueset: state.valueset }
            })
                .catch((error) => dispatch({ type: 'error', error: error.message }))
                .then((response) => dispatch({ type: 'complete', response: response }));
        }
    }, [state.status]);

    const value = {
        error: state.status === 'error' ? state.error : undefined,
        isLoading: state.status === 'updating',
        response: state.status === 'complete' ? state.response : undefined,
        update: (page: number, question: number, valueset: number) =>
            dispatch({ type: 'update', page, question, valueset })
    };

    return value;
};
