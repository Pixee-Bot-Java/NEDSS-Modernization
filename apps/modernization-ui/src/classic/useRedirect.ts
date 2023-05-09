import { UserState } from 'providers/UserContext';
import { useEffect, useReducer } from 'react';
import { useUser } from 'user';

type Redirect = {
    redirecting: boolean;
    url: string | null;
    location: string | null;
};

type Action = { type: 'redirect'; url: string } | { type: 'redirected'; location: string };
type Dispatch = (action: Action) => void;

const redirectReducer = (state: Redirect, action: Action) => {
    switch (action.type) {
        case 'redirect':
            return { redirecting: true, url: action.url, location: null };
        case 'redirected':
            return { ...state, redirecting: false, location: action.location };
        default:
            return state;
    }
};

const resolveRedirect = (user: UserState, url: string) => {
    return fetch(url, { headers: { Authorization: `Bearer ${user.getToken()}` } }).then((response) =>
        response.headers.get('Location')
    );
};

const useRedirect = () => {
    const { state: user } = useUser();

    const initial = { redirecting: false, url: '', location: null };

    const [redirect, dispatch] = useReducer(redirectReducer, initial);

    useEffect(() => {
        if (redirect.url) {
            resolveRedirect(user, redirect.url).then((location) => {
                if (location) {
                    dispatch({ type: 'redirected', location });
                }
            });
        }
    }, [redirect.url]);

    return { redirect, dispatch };
};

const redirectTo = (url: string, dispatch: Dispatch) => dispatch({ type: 'redirect', url });

const navigateTo = (location: string) => (window.location.href = location);

export { useRedirect, redirectTo, navigateTo };
