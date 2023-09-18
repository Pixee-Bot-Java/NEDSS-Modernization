const valid = /^([0-9]{7,10}|(([0-9]{3}|\([0-9]{3}\))[-. ]?)([0-9]{3})?([-. ][0-9]{4})?)$/;

export const validate = (number: string): boolean => {
    return !number || valid.test(number);
};