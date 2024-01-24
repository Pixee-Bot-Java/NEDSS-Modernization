import { useEffect } from 'react';

const useAltX = (callback: () => void) => {
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent) => {
            if (event.altKey && event.keyCode === 88) {
                event.preventDefault();
                callback();
            }
        };

        window.addEventListener('keydown', handleKeyDown);

        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, []);
};

export { useAltX };
