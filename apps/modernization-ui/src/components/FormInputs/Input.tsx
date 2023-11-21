import { Textarea, TextInput, TextInputMask } from '@trussworks/react-uswds';
import classNames from 'classnames';
import './Input.scss';
import { EntryWrapper } from 'components/Entry';
import { RefObject } from 'react';

type InputProps = {
    name?: string;
    className?: string;
    htmlFor?: string;
    label?: string;
    id?: string;
    required?: boolean;
    type: 'text' | 'email' | 'number' | 'password' | 'search' | 'tel' | 'url';
    error?: any;
    onChange?: any;
    defaultValue?: string | null;
    placeholder?: string;
    inputMode?: 'none' | 'text' | 'tel' | 'url' | 'email' | 'numeric' | 'decimal' | 'search';
    flexBox?: boolean;
    multiline?: boolean;
    textInputRef?: RefObject<HTMLInputElement>;
    textAreaRef?: RefObject<HTMLTextAreaElement>;
    mask?: string;
    pattern?: string;
} & Omit<JSX.IntrinsicElements['input'], 'defaultValue'>;

export const Input = ({
    name,
    className,
    htmlFor = '',
    label,
    id = '',
    required,
    type,
    error,
    onChange,
    defaultValue,
    placeholder,
    inputMode,
    flexBox,
    multiline,
    textInputRef,
    textAreaRef,
    mask,
    pattern,
    ...props
}: InputProps) => {
    const orientation = flexBox ? 'horizontal' : 'vertical';

    return (
        <div className={classNames('input', { 'input--error': error })}>
            <EntryWrapper
                orientation={orientation}
                label={label ?? ''}
                htmlFor={htmlFor ?? ''}
                required={required}
                error={error}>
                {!multiline ? (
                    mask ? (
                        <TextInputMask
                            autoComplete="off"
                            inputMode={inputMode}
                            placeholder={placeholder}
                            {...props}
                            id={id}
                            onChange={onChange}
                            value={defaultValue ?? ''}
                            name={name ?? ''}
                            inputRef={textInputRef}
                            validationStatus={error ? 'error' : undefined}
                            aria-describedby={`${error}-message`}
                            className={`${classNames(className)} masked-input`}
                            type={type}
                            mask={mask}
                            pattern={pattern}
                        />
                    ) : (
                        <TextInput
                            autoComplete="off"
                            inputMode={inputMode}
                            placeholder={placeholder}
                            {...props}
                            id={id}
                            onChange={onChange}
                            value={defaultValue ?? ''}
                            name={name ?? ''}
                            inputRef={textInputRef}
                            validationStatus={error ? 'error' : undefined}
                            aria-describedby={`${error}-message`}
                            className={classNames(className)}
                            type={type}
                        />
                    )
                ) : (
                    <Textarea
                        autoComplete="off"
                        placeholder={placeholder}
                        id={id}
                        onChange={onChange}
                        value={defaultValue ?? ''}
                        name={name ?? ''}
                        inputRef={textAreaRef}
                        aria-describedby={`${error}-message`}
                        className={classNames(className)}
                    />
                )}
            </EntryWrapper>
        </div>
    );
};
