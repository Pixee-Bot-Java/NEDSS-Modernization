import { TextInput, Label, ErrorMessage, TextInputMask } from '@trussworks/react-uswds';
import './PhoneNumberInput.scss';

type PhoneNumberInputProps = {
    label?: string;
    defaultValue?: string;
    value?: string;
    onChange?: any;
    error?: any;
    placeholder?: string;
    id?: string;
    mask?: string;
    pattern?: string;
    onBlur?: any;
};
export const PhoneNumberInput = ({
    label,
    defaultValue,
    onChange,
    error,
    placeholder,
    id,
    mask,
    pattern,
    onBlur,
    value,
    ...props
}: PhoneNumberInputProps) => {
    return (
        <div className={`phone-number-input ${error ? 'input--error' : ''}`}>
            <Label htmlFor={id || 'phoneNumber'}>{label}</Label>
            {error && <ErrorMessage id={`${error}-message`}>{error}</ErrorMessage>}
            {mask ? (
                <TextInputMask
                    {...props}
                    id={id || 'phoneNumber'}
                    onChange={onChange}
                    onBlur={onBlur}
                    defaultValue={defaultValue ? defaultValue : ''}
                    name={id || 'phoneNumber'}
                    validationStatus={error ? 'error' : undefined}
                    type="tel"
                    placeholder={placeholder || ''}
                    mask={mask}
                    pattern={pattern}
                    className={'masked-input'}
                    value={value}
                />
            ) : (
                <TextInput
                    onBlur={onBlur}
                    {...props}
                    id={id || 'phoneNumber'}
                    onChange={onChange}
                    defaultValue={defaultValue ? defaultValue : ''}
                    name={id || 'phoneNumber'}
                    validationStatus={error ? 'error' : undefined}
                    type="tel"
                    value={value}
                    placeholder={placeholder || ''}
                />
            )}
        </div>
    );
};
