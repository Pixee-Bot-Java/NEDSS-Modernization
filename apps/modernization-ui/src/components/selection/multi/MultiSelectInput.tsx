import ReactSelect, { MultiValue, components } from 'react-select';
import { FocusEventHandler, useEffect, useMemo, useState } from 'react';
import './style.scss';
import { mapNonNull } from 'utils';

const CheckedOption = (props: any) => {
    return (
        <div>
            <components.Option {...props}>
                <input type="checkbox" checked={props.isSelected} readOnly /> <label>{props.label}</label>
            </components.Option>
        </div>
    );
};

const USWDSDropdownIndicator = (props: any) => (
    // Replaces the default arrow indicator from react-select with the select indicator from USDWS
    <components.DropdownIndicator {...props}>
        <div className="multi-select select-indicator" />
    </components.DropdownIndicator>
);

const asSelectable = (selectables: Selectable[]) => (item: string) =>
    selectables.find((option) => option.value === item) || null;

type Option = { name: string; value: string };

type MultiSelectInputProps = {
    id?: string;
    name?: string;
    options?: Option[];
    value?: string[];
    onChange?: (value: string[]) => void;
    onBlur?: FocusEventHandler<HTMLInputElement> | undefined;
};

type Selectable = { value: string; label: string };

export const MultiSelectInput = ({ id, name, options = [], onChange, onBlur, value = [] }: MultiSelectInputProps) => {
    const selectableOptions = useMemo(
        () => options.map((item) => ({ value: item.value, label: item.name })),
        [JSON.stringify(options)]
    );

    const [selectedOptions, setSelectedOptions] = useState<Selectable[]>([]);

    useEffect(() => {
        const selected = mapNonNull(asSelectable(selectableOptions), value);
        setSelectedOptions(selected);
    }, [JSON.stringify(value), selectableOptions]);

    const handleOnChange = (newValue: MultiValue<Selectable>) => {
        setSelectedOptions([...newValue]);

        if (onChange) {
            const values = newValue.map((item) => item.value);
            onChange(values);
        }
    };

    return (
        <ReactSelect
            isMulti={true}
            id={id}
            name={name}
            value={selectedOptions}
            placeholder="- Select -"
            classNamePrefix="multi-select"
            hideSelectedOptions={false}
            closeMenuOnSelect={false}
            closeMenuOnScroll={false}
            onChange={handleOnChange}
            onBlur={onBlur}
            options={selectableOptions}
            components={{ Option: CheckedOption, DropdownIndicator: USWDSDropdownIndicator }}
        />
    );
};