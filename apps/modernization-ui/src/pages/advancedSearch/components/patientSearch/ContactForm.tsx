import { Grid } from '@trussworks/react-uswds';
import { Input } from '../../../../components/FormInputs/Input';
import { Controller } from 'react-hook-form';
import { PhoneNumberInput } from '../../../../components/FormInputs/PhoneNumberInput/PhoneNumberInput';
import { validate as validatePhoneNumber } from 'validation/phone/search';

export const ContactForm = ({ control, errors }: any) => {
    return (
        <>
            <Grid col={12}>
                <Controller
                    control={control}
                    name="phoneNumber"
                    rules={{
                        validate: {
                            properNumber: (value) => validatePhoneNumber(value)
                        }
                    }}
                    render={({ field: { onChange, value } }) => (
                        <PhoneNumberInput
                            onChange={onChange}
                            label="Phone number"
                            defaultValue={value}
                            error={errors && errors.phoneNumber && 'Invalid phone number'}
                        />
                    )}
                />
            </Grid>
            <Grid col={12}>
                <Controller
                    control={control}
                    name="email"
                    rules={{
                        pattern: {
                            value: /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/,
                            message: 'Invalid email'
                        }
                    }}
                    render={({ field: { onChange, value } }) => (
                        <Input
                            onChange={onChange}
                            defaultValue={value}
                            type="text"
                            label="Email"
                            htmlFor="email"
                            id="email"
                            error={errors && errors.email && errors.email.message}
                        />
                    )}
                />
            </Grid>
        </>
    );
};
