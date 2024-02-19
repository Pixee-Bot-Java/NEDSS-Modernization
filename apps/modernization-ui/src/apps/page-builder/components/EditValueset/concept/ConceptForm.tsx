import { Label, Radio, Textarea } from '@trussworks/react-uswds';
import { CreateConceptRequest } from 'apps/page-builder/generated';
import { useOptions } from 'apps/page-builder/hooks/api/useOptions';
import { DatePickerInput } from 'components/FormInputs/DatePickerInput';
import { Input } from 'components/FormInputs/Input';
import { SelectInput } from 'components/FormInputs/SelectInput';
import { ChangeEvent, useState } from 'react';
import { Controller, useFormContext } from 'react-hook-form';
import { maxLengthRule } from 'validation/entry';
import styles from './concept-form.module.scss';

type Props = {
    isEditing?: boolean;
};
export const ConceptForm = ({ isEditing = false }: Props) => {
    const form = useFormContext<CreateConceptRequest>();
    const [alwaysEffective, setAlwaysEffective] = useState<boolean>(true);
    const { options: codeSystems } = useOptions('CODE_SYSTEM');

    return (
        <div className={styles.conceptForm}>
            <div className={styles.formContent}>
                <Controller
                    control={form.control}
                    name="localCode"
                    rules={{
                        required: { value: !isEditing, message: 'Local code is required' },
                        pattern: { value: /^\w*$/, message: 'Valid characters are A-Z, a-z, 0-9, or _' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => {
                                onChange(e);
                                form.setValue('conceptCode', e.target.value);
                            }}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="Local code"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required={!isEditing}
                            disabled={isEditing}
                        />
                    )}
                />
                <Controller
                    control={form.control}
                    name="longName"
                    rules={{
                        required: { value: true, message: 'Long display name is required' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => {
                                onChange(e);
                                form.setValue('conceptName', e.target.value);
                            }}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="Long display name"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
                <Controller
                    control={form.control}
                    name="display"
                    rules={{
                        required: { value: true, message: 'UI display name is required' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => {
                                onChange(e);
                                form.setValue('preferredConceptName', e.target.value);
                            }}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="UI display name"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
                <div className={styles.effectiveTimeSection}>
                    <Radio
                        id="alwaysEffective"
                        name="alwaysEffective"
                        value={'true'}
                        label="Always effective"
                        checked={alwaysEffective}
                        onChange={() => {
                            setAlwaysEffective(true);
                            form.setValue('effectiveToTime', undefined);
                            form.trigger('adminComments'); // Trigger validation
                        }}
                    />
                    <Radio
                        id="effectiveUntil"
                        name="effectiveUntil"
                        value={'false'}
                        label="Effective until"
                        checked={!alwaysEffective}
                        onChange={() => {
                            setAlwaysEffective(false);
                            form.trigger('effectiveToTime');
                        }}
                    />
                    <Controller
                        control={form.control}
                        name="effectiveToTime"
                        rules={{ required: { value: !alwaysEffective, message: 'Effective until date is required' } }}
                        render={({ field: { onChange, onBlur, value } }) => (
                            <DatePickerInput
                                defaultValue={value}
                                onChange={onChange}
                                onBlur={onBlur}
                                disabled={alwaysEffective}
                                required={!alwaysEffective}
                            />
                        )}
                    />
                </div>
                <Controller
                    control={form.control}
                    name="status"
                    render={({ field: { onChange, value } }) => (
                        <div className={styles.radioButtons}>
                            <Radio
                                id="status_ACTIVE"
                                name="status"
                                value={'ACTIVE'}
                                label="ACTIVE"
                                onChange={onChange}
                                checked={value === 'ACTIVE'}
                            />
                            <Radio
                                id="status_INACTIVE"
                                name="status"
                                value={'INACTIVE'}
                                label="INACTIVE"
                                onChange={onChange}
                                checked={value === 'INACTIVE'}
                            />
                        </div>
                    )}
                />
                <Controller
                    control={form.control}
                    name="adminComments"
                    rules={{
                        ...maxLengthRule(2000)
                    }}
                    render={({ field: { onChange, value, onBlur, name } }) => (
                        <>
                            <Label htmlFor={name}>Administrative comments</Label>
                            <Textarea
                                className={styles.wideInput}
                                onChange={onChange}
                                onBlur={onBlur}
                                defaultValue={value}
                                name={name}
                                id={name}
                            />
                        </>
                    )}
                />

                <div className={styles.sectionHeading}>Messaging concept code</div>
                <Controller
                    control={form.control}
                    name="conceptCode"
                    rules={{
                        required: { value: true, message: 'Concept code is required' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={onChange}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="Concept code"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
                <Controller
                    control={form.control}
                    name="conceptName"
                    rules={{
                        required: { value: true, message: 'Concept name is required' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={onChange}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="Concept name"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
                <Controller
                    control={form.control}
                    name="preferredConceptName"
                    rules={{
                        required: { value: true, message: 'Preferred concept name is required' },
                        ...maxLengthRule(50)
                    }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <Input
                            className={styles.wideInput}
                            onChange={onChange}
                            onBlur={onBlur}
                            defaultValue={value}
                            label="Preferred concept name"
                            type="text"
                            error={error?.message}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
                <Controller
                    control={form.control}
                    name="codeSystem"
                    rules={{ required: { value: true, message: 'Code system name is required' } }}
                    render={({ field: { onChange, value, onBlur, name }, fieldState: { error } }) => (
                        <SelectInput
                            className={styles.wideInput}
                            label="Code system name"
                            defaultValue={value}
                            onChange={(e) => {
                                onChange(e);
                                onBlur();
                            }}
                            onBlur={onBlur}
                            error={error?.message}
                            options={codeSystems}
                            name={name}
                            htmlFor={name}
                            id={name}
                            required
                        />
                    )}
                />
            </div>
        </div>
    );
};
