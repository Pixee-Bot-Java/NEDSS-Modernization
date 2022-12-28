import { Control, Controller, FieldValues } from 'react-hook-form';
import { SelectInput } from './SelectInput';

type EventTypesProps = {
    control: Control<FieldValues, any>;
    name: string;
    onChangeMethod?: (event: any) => void;
    options: any;
    label?: string;
    isMulti?: boolean;
};

export const SelectControl = ({ control, name, onChangeMethod, options, label, isMulti }: EventTypesProps) => {
    return (
        <Controller
            control={control}
            name={name}
            render={({ field: { onChange, value } }) => (
                <SelectInput
                    defaultValue={value}
                    isMulti={isMulti}
                    onChange={(e: any) => {
                        onChange(e);
                        onChangeMethod?.(e);
                    }}
                    label={label}
                    options={options}
                />
            )}
        />
    );
};
