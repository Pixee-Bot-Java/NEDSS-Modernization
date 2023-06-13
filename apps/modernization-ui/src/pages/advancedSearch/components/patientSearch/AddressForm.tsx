import { Grid } from '@trussworks/react-uswds';
import { Input } from '../../../../components/FormInputs/Input';
import { SelectInput } from '../../../../components/FormInputs/SelectInput';
import { Controller } from 'react-hook-form';
import { SearchCriteriaContext } from '../../../../providers/SearchCriteriaContext';

export const AddressForm = ({ control, errors }: any) => {
    return (
        <>
            <Grid col={12}>
                <Controller
                    control={control}
                    name="address"
                    render={({ field: { onChange, value } }) => (
                        <Input
                            onChange={onChange}
                            type="text"
                            label="Street address"
                            defaultValue={value}
                            htmlFor="address"
                            id="address"
                        />
                    )}
                />
            </Grid>
            <Grid col={12}>
                <Controller
                    control={control}
                    name="city"
                    render={({ field: { onChange, value } }) => (
                        <Input
                            onChange={onChange}
                            defaultValue={value}
                            type="text"
                            label="City"
                            htmlFor="city"
                            id="city"
                        />
                    )}
                />
            </Grid>
            <Grid col={12}>
                <SearchCriteriaContext.Consumer>
                    {({ searchCriteria }) => (
                        <Controller
                            control={control}
                            name="state"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    htmlFor={'state'}
                                    label="State"
                                    options={searchCriteria.states.map((state) => {
                                        return {
                                            value: state?.id!,
                                            name: state?.codeDescTxt!
                                        };
                                    })}
                                />
                            )}
                        />
                    )}
                </SearchCriteriaContext.Consumer>
            </Grid>
            <Grid col={12}>
                <Controller
                    control={control}
                    name="zip"
                    rules={{
                        pattern: { value: /^\d{5}(?:[-\s]\d{4})?$/, message: 'Invalid zip code' }
                    }}
                    render={({ field: { onChange, value } }) => (
                        <Input
                            onChange={onChange}
                            defaultValue={value}
                            type="text"
                            label="Zip code"
                            htmlFor="zip"
                            id="zip"
                            error={errors && errors.zip && errors.zip.message}
                        />
                    )}
                />
            </Grid>
        </>
    );
};
