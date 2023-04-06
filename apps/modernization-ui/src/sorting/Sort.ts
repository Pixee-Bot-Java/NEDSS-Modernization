type Comparator<T> = (left: T, right: T) => number;

export enum Direction {
    Ascending = 'asc',
    Decending = 'desc'
}

export const ascending = <T>(comparator: Comparator<T>): Comparator<T> => comparator;

export const descending =
    <T>(comparator: Comparator<T>): Comparator<T> =>
    (left: T, right: T) =>
        -1 * comparator(left, right);

export const withDirection = <T>(c: Comparator<T>, type?: Direction): Comparator<T> => {
    switch (type) {
        case Direction.Decending:
            return descending(c);
        default:
            return ascending(c);
    }
};

export const sortBy =
    <T>(property: keyof T): Comparator<T> =>
    (left: T, right: T): number => {
        const value = left[property];
        const comparing = right[property];

        if (value > comparing) {
            return -1;
        } else if (value < comparing) {
            return 1;
        } else {
            return 0;
        }
    };

export const sortByAlpha =
    <T>(property: keyof T): Comparator<T> =>
    (left: T, right: T): number => {
        const value = left[property];
        const comparing = right[property];

        if (typeof value === 'string' && typeof comparing === 'string') {
            return value.localeCompare(comparing);
        }
        return fallbackComparator(value, comparing);
    };

export const sortByAlphanumeric =
    <T>(property: keyof T): Comparator<T> =>
    (left: T, right: T): number => {
        const value = left[property];
        const comparing = right[property];

        if (typeof value === 'string' && typeof comparing === 'string') {
            return value.localeCompare(comparing, undefined, { numeric: true });
        }
        return fallbackComparator(value, comparing);
    };

export const sortByDate =
    <T>(property: keyof T) =>
    (left: T, right: T): number => {
        const value = left[property];
        const comparing = right[property];

        if (value instanceof Date && comparing instanceof Date) {
            return value.getTime() - comparing.getTime();
        }
        return fallbackComparator(value, comparing);
    };

const fallbackComparator = (left: any, right: any): number => {
    if (left && !right) {
        return 1;
    } else if (!left && right) {
        return -1;
    } else {
        return 0;
    }
};

export const simpleSort = fallbackComparator;