import { internalizeDate } from 'date';
import { Gender, Suffix, Deceased } from 'generated/graphql/schema';

type Maybe<T> = T | null;

type NameEntry = {
    lastName: Maybe<string>;
    firstName: Maybe<string>;
    middleName: Maybe<string>;
    suffix: Maybe<Suffix>;
};

type AddressEntry = {
    streetAddress1: Maybe<string>;
    streetAddress2: Maybe<string>;
    city: Maybe<string>;
    state: Maybe<string>;
    zip: Maybe<string>;
    county: Maybe<string>;
    censusTract: Maybe<string>;
    country: Maybe<string>;
};

type EmailEntry = {
    email: Maybe<string>;
};

type PhoneNumberEntry = {
    number: Maybe<string>;
    type: string;
    use: string;
};

type ContactEntry = {
    homePhone: Maybe<string>;
    workPhone: Maybe<string>;
    extension: Maybe<string>;
};

type IdentificationEntry = {
    type: Maybe<string>;
    authority: Maybe<string>;
    value: Maybe<string>;
};

type RequiredFields = { asOf: string };

//  these fields are required to actually shows an initial first entry
type FormRequired = {
    identification: IdentificationEntry[];
    phoneNumbers: PhoneNumberEntry[];
    emailAddresses: EmailEntry[];
};

type OptionalFields = {
    comments: Maybe<string>;
    dateOfBirth: Maybe<string>;
    currentGender: Maybe<Gender>;
    birthGender: Maybe<Gender>;
    deceased: Maybe<Deceased>;
    deceasedTime: Maybe<string>;
    maritalStatus: Maybe<string>;
    stateHIVCase: Maybe<string>;
    ethnicity: Maybe<string>;
    race: string[];
};

type NewPatientEntry = RequiredFields &
    Partial<OptionalFields> &
    Partial<AddressEntry> &
    Partial<ContactEntry> &
    Partial<NameEntry> &
    FormRequired;

type DefaultNewPatentEntry = RequiredFields & FormRequired;

const initialEntry = (asOf: Date = new Date()): DefaultNewPatentEntry => ({
    asOf: internalizeDate(asOf) || '',
    identification: [{ type: null, authority: null, value: null }],
    phoneNumbers: [{ number: null, type: 'CP', use: 'MC' }],
    emailAddresses: [{ email: null }]
});

export { initialEntry };
export type { NewPatientEntry, DefaultNewPatentEntry };
