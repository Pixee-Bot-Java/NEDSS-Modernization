import { exists } from 'utils';

type DisplayableAddress = {
    address?: string | null;
    address2?: string | null;
    city?: string | null;
    state?: string | null;
    zipcode?: string | null;
};

const displayAddress = ({ address, address2, city, state, zipcode }: DisplayableAddress) => {
    const street = [address, address2].filter(exists).join(', ');
    const location = [city, state, zipcode].filter(exists).join(', ');
    return [street, location].filter(exists).join('\n');
};

export { displayAddress };