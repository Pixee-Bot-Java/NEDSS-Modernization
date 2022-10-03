import { Button, Form, Grid, Search, Table } from '@trussworks/react-uswds';
import { Input } from '../../components/FormInputs/Input';
import { SelectInput } from '../../components/FormInputs/SelectInput';
import { stateList } from '../../constant/states';
import './home.scss';
import * as yup from 'yup';
import { Controller, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { Gender, PersonFilter, useFindPatientsByFilterLazyQuery } from '../../generated/graphql/schema';
import { DatePickerInput } from '../../components/FormInputs/DatePickerInput';
import { TableContent } from '../../components/TableContent/TableContent';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useEffect } from 'react';

type FormTypes = {
    firstName: string;
    lastName: string;
    gender?: Gender | '- Select -';
    state?: string;
    city?: string;
    zip?: string;
    patientId?: string;
    dob?: Date;
};

const tableHead = [
    { name: 'Person', sortable: true },
    { name: 'Date of birth', sortable: false },
    { name: 'Type', sortable: true },
    { name: 'Last test', sortable: true },
    { name: 'Last result', sortable: true },
    { name: 'Action', sortable: false }
];

export const Home = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    const [getFilteredData, { data }] = useFindPatientsByFilterLazyQuery();

    const schema = yup.object().shape({
        firstName: yup.string().required('First name is required.'),
        lastName: yup.string().required('Last name is required.')
    });

    const methods = useForm({
        resolver: yupResolver(schema)
    });

    const {
        handleSubmit,
        control,
        formState: { errors },
        reset,
        setValue
    } = methods;

    useEffect(() => {
        const rowData: PersonFilter = {
            firstName: searchParams?.get('firstName') as string,
            lastName: searchParams?.get('lastName') as string
        };
        searchParams?.get('city') && (rowData.city = searchParams?.get('city') as string);
        searchParams?.get('zip') && (rowData.zip = searchParams?.get('zip') as string);
        searchParams?.get('id') && (rowData.city = searchParams?.get('id') as string);
        searchParams?.get('DateOfBirth') && (rowData.DateOfBirth = searchParams?.get('DateOfBirth') as unknown as Date);
        setValue('firstName', rowData.firstName);
        setValue('lastName', rowData.lastName);
        setValue('city', rowData.city);
        setValue('zip', rowData.zip);
        setValue('patientId', rowData.id);
        setValue('dob', rowData.DateOfBirth);
        getFilteredData({
            variables: {
                filter: rowData
            }
        });
    }, []);

    const onSubmit: any = (body: FormTypes) => {
        const rowData: PersonFilter = {
            firstName: body.firstName,
            lastName: body.lastName
        };
        body.city && (rowData.city = body.city);
        body.zip && (rowData.zip = body.zip);
        body.patientId && (rowData.id = body.patientId);
        body.dob && (rowData.DateOfBirth = body.dob);
        body.gender !== '- Select -' && (rowData.gender = body.gender);
        body.state !== '- Select -' && (rowData.state = body.state);

        let search = `?firstName=${rowData.firstName}&lastName=${rowData.lastName}`;
        rowData.city && (search = `${search}&city=${rowData.city}`);
        rowData.zip && (search = `${search}&zip=${rowData.zip}`);
        rowData.id && (search = `${search}&id=${rowData.id}`);
        rowData.DateOfBirth && (search = `${search}&DateOfBirth=${rowData.DateOfBirth}`);

        getFilteredData({
            variables: {
                filter: rowData
            }
        }).then(() => {
            navigate({
                pathname: '/',
                search
            });
        });
    };

    return (
        <div className="home-page bg-base-lightest padding-y-5">
            <Grid row className="flex-justify-center">
                <Grid col={10}>
                    <Grid row className="flex-justify-end">
                        <Grid col={6}>
                            <Search
                                size="big"
                                className="flex-justify-end"
                                placeholder="Search for a patient"
                                onSubmit={() => console.log('submitted')}
                            />
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
            <Grid row className="flex-justify-center margin-y-2">
                <Grid
                    desktop={{ col: 10 }}
                    tablet={{ col: true }}
                    className="bg-white border-blue border padding-2 radius-md">
                    <Grid row className="flex-justify-center">
                        <Grid desktop={{ col: 10 }} tablet={{ col: true }} className="padding-2">
                            <div className="">
                                <h2 className="font-lang-lg margin-top-0 margin-bottom-3">Simple Search</h2>
                            </div>
                            <Form onSubmit={handleSubmit(onSubmit)} className="width-full maxw-full">
                                <Grid row gap={6} className="padding-bottom-3">
                                    <Grid col={6}>
                                        <Controller
                                            control={control}
                                            name="lastName"
                                            render={({ field: { onChange, value } }) => (
                                                <Input
                                                    onChange={onChange}
                                                    type="text"
                                                    label="Last Name"
                                                    name="lastName"
                                                    required
                                                    defaultValue={value}
                                                    htmlFor="lastName"
                                                    id="lastName"
                                                    error={errors?.lastName && 'Last name is required.'}
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={6}>
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
                                                    required
                                                    error={errors?.firstName && 'First name is required.'}
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={6}>
                                        <Grid row gap={3}>
                                            <Grid col={4}>
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
                                            <Grid col={8}>
                                                <Controller
                                                    control={control}
                                                    name="dob"
                                                    render={({ field: { onChange } }) => (
                                                        <DatePickerInput
                                                            onChange={onChange}
                                                            name="dob"
                                                            htmlFor={'dob'}
                                                            label="Date Of Birth"
                                                        />
                                                    )}
                                                />
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                    <Grid col={6}>
                                        <Controller
                                            control={control}
                                            name="city"
                                            render={({ field: { onChange, value } }) => (
                                                <Input
                                                    onChange={onChange}
                                                    defaultValue={value}
                                                    type="text"
                                                    label="City"
                                                    name="city"
                                                    htmlFor="city"
                                                    id="city"
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={6}>
                                        <Controller
                                            control={control}
                                            name="state"
                                            render={({ field: { onChange } }) => (
                                                <SelectInput
                                                    onChange={onChange}
                                                    name="state"
                                                    htmlFor={'state'}
                                                    label="State"
                                                    options={stateList}
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={3}>
                                        <Controller
                                            control={control}
                                            name="zip"
                                            render={({ field: { onChange, value } }) => (
                                                <Input
                                                    onChange={onChange}
                                                    defaultValue={value}
                                                    type="text"
                                                    label="Zip"
                                                    name="zip"
                                                    htmlFor="zip"
                                                    id="zip"
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={6}>
                                        <Controller
                                            control={control}
                                            name="patientId"
                                            render={({ field: { onChange, value } }) => (
                                                <Input
                                                    onChange={onChange}
                                                    defaultValue={value}
                                                    type="text"
                                                    label="Patient ID"
                                                    name="patientId"
                                                    htmlFor="patientId"
                                                    id="patientId"
                                                />
                                            )}
                                        />
                                    </Grid>
                                    <Grid col={6} className="flex-align-self-end">
                                        <div className="grid-row flex-justify-end flex-align-end flex-wrap">
                                            <p className="margin-right-105 text-primary text-bold margin-bottom-05">
                                                Advance Search
                                            </p>
                                            <Button
                                                type={'button'}
                                                onClick={() =>
                                                    reset({
                                                        lastName: '',
                                                        firstName: '',
                                                        city: '',
                                                        state: '',
                                                        zip: '',
                                                        patientId: ''
                                                    })
                                                }
                                                outline>
                                                Clear
                                            </Button>
                                            <Button type={'submit'}>Search</Button>
                                        </div>
                                    </Grid>
                                </Grid>
                            </Form>
                        </Grid>
                    </Grid>
                </Grid>

                {data?.findPatientsByFilter && (
                    <Grid desktop={{ col: 10 }} tablet={{ col: true }} className="bg-white margin-top-3 radius-md">
                        <Grid row className="flex-justify-center">
                            <Grid col={12} className="padding-4 border-bottom border-base-lightest">
                                <div className="grid-row flex-justify flex-align-center flex-wrap">
                                    <h2 className="font-ui-xl margin-top-0 margin-bottom-0">Search Results</h2>
                                    <div>
                                        <Button type={'button'} outline>
                                            Sort By
                                        </Button>
                                        <Button type={'button'} outline>
                                            Export Results
                                        </Button>
                                        <Button type={'button'}>Create New</Button>
                                    </div>
                                </div>
                            </Grid>
                            <Grid col={12} className="padding-4 table-checkbox">
                                <Table bordered={false} fullWidth>
                                    <TableContent tableHead={tableHead} tableBody={data?.findPatientsByFilter} />
                                </Table>
                                {data?.findPatientsByFilter && data?.findPatientsByFilter.length < 1 && (
                                    <p className="text-center font-ui-md">No records found.</p>
                                )}
                            </Grid>
                        </Grid>
                    </Grid>
                )}
            </Grid>
        </div>
    );
};
