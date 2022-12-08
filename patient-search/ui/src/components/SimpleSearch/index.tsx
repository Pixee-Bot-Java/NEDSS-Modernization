import { Accordion, Button, Form, Grid } from '@trussworks/react-uswds';
import { AccordionItemProps } from '@trussworks/react-uswds/lib/components/Accordion/Accordion';
import { useEffect } from 'react';
import { Controller, useForm } from 'react-hook-form';
import { Gender, PersonFilter } from '../../generated/graphql/schema';
import { DatePickerInput } from '../FormInputs/DatePickerInput';
import { Input } from '../FormInputs/Input';
import { SelectInput } from '../FormInputs/SelectInput';
import { AddressForm } from './AddressForm';
import { ContactForm } from './ContactForm';
import { EthnicityForm } from './EthnicityForm';
import { IDForm } from './IdForm';

type SimpleSearchProps = {
    handleSubmission: (data: PersonFilter) => void;
    data: PersonFilter | undefined;
};

export const SimpleSearch = ({ handleSubmission, data }: SimpleSearchProps) => {
    const methods = useForm();
    const {
        handleSubmit,
        control,
        formState: { errors },
        reset
    } = methods;

    useEffect(() => {
        if (data) {
            methods.reset({
                firstName: data.firstName,
                lastName: data.lastName,
                address: data.address,
                city: data.city,
                state: data.state,
                zip: data.zip,
                patientId: data.id,
                dob: data.dateOfBirth,
                gender: data.gender,
                phoneNumber: data.phoneNumber,
                email: data.email,
                identificationNumber: data.identification?.identificationNumber,
                identificationType: data.identification?.identificationType,
                ethnicity: data.ethnicity,
                race: data.race
            });
            console.log('values', methods.getValues());
        }
    }, [data]);

    const simpleSearchItems: AccordionItemProps[] = [
        {
            title: 'Basic Info',
            content: (
                <>
                    <Grid col={12}>
                        <Controller
                            control={control}
                            name="lastName"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    type="text"
                                    label="Last Name"
                                    name="lastName"
                                    defaultValue={value}
                                    htmlFor="lastName"
                                    id="lastName"
                                    error={errors?.lastName && 'Last name is required.'}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={12}>
                        <Controller
                            control={control}
                            name="firstName"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="First Name"
                                    name="firstName"
                                    htmlFor="firstName"
                                    id="firstName"
                                    error={errors?.firstName && 'First name is required.'}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={12}>
                        <Controller
                            control={control}
                            name="dob"
                            render={({ field: { onChange } }) => (
                                <DatePickerInput onChange={onChange} name="dob" htmlFor={'dob'} label="Date Of Birth" />
                            )}
                        />
                    </Grid>
                    <Grid col={12}>
                        <Controller
                            control={control}
                            name="gender"
                            render={({ field: { onChange } }) => (
                                <SelectInput
                                    onChange={onChange}
                                    name="gender"
                                    htmlFor={'gender'}
                                    label="Gender"
                                    options={[
                                        { name: 'Male', value: Gender.M },
                                        { name: 'Female', value: Gender.F },
                                        { name: 'Other', value: Gender.U }
                                    ]}
                                />
                            )}
                        />
                    </Grid>
                </>
            ),
            expanded: true,
            id: '1',
            headingLevel: 'h4',
            className: 'accordian-item'
        },
        {
            title: 'Address',
            content: <AddressForm control={control} />,
            expanded: false,
            id: '2',
            headingLevel: 'h4',
            className: 'accordian-item'
        },
        {
            title: 'Contact',
            content: <ContactForm control={control} />,
            expanded: false,
            id: '3',
            headingLevel: 'h4',
            className: 'accordian-item'
        },
        {
            title: 'ID',
            content: <IDForm control={control} />,
            expanded: false,
            id: '4',
            headingLevel: 'h4',
            className: 'accordian-item'
        },
        {
            title: 'Race / Ethnicity',
            content: <EthnicityForm control={control} />,
            expanded: false,
            id: '5',
            headingLevel: 'h4',
            className: 'accordian-item'
        }
    ];

    const onSubmit: any = (body: any) => {
        const rowData: PersonFilter = {
            firstName: body.firstName,
            lastName: body.lastName
        };
        body.dob && (rowData.dateOfBirth = body.dob);
        body.gender !== '- Select -' && (rowData.gender = body.gender);

        body.address && (rowData.address = body.address);
        body.city && (rowData.city = body.city);
        body.state !== '- Select -' && (rowData.state = body.state);
        body.zip && (rowData.zip = body.zip);

        body.phoneNumber && (rowData.phoneNumber = body.phoneNumber);
        body.email && (rowData.email = body.email);

        body.race !== '- Select -' && (rowData.race = body.race);
        body.ethnicity !== '- Select -' && (rowData.ethnicity = body.ethnicity);

        if (body.identificationNumber && body.identificationType !== '- Select -') {
            rowData.identification = {
                identificationNumber: body.identificationNumber,
                identificationType: body.identificationType
            };
        }
        handleSubmission(rowData);
    };

    return (
        <Form onSubmit={handleSubmit(onSubmit)} className="width-full maxw-full">
            <div style={{ height: `calc(100vh - 375px)`, overflowY: 'auto' }}>
                <Accordion items={simpleSearchItems} multiselectable={true} />
            </div>
            <Grid row className="bottom-search">
                <Grid col={12} className="padding-x-2">
                    <Button className="width-full clear-btn" type={'submit'}>
                        Search
                    </Button>
                </Grid>
                <Grid col={12} className="padding-x-2">
                    <Button className="width-full clear-btn" type={'button'} onClick={() => reset({})} outline>
                        Clear all
                    </Button>
                </Grid>
            </Grid>
        </Form>
    );
};
