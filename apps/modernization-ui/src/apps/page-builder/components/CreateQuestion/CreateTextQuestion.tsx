import React from 'react';
import './CreateQuestion.scss';
import { Control, Controller } from 'react-hook-form';
import { Input } from '../../../../components/FormInputs/Input';
import { SelectInput } from '../../../../components/FormInputs/SelectInput';
import {
    CreateCodedQuestionRequest,
    CreateDateQuestionRequest,
    CreateNumericQuestionRequest,
    CreateTextQuestionRequest,
    MessagingInfo,
    ReportingInfo
} from '../../generated';
import { QuestionFormType, optionsType } from './CreateQuestion';
import { maxLengthRule } from '../../../../validation/entry';

type CreateQuestionFormType = CreateNumericQuestionRequest &
    CreateCodedQuestionRequest &
    CreateDateQuestionRequest &
    CreateTextQuestionRequest &
    ReportingInfo &
    MessagingInfo &
    QuestionFormType;
type TextQuestionProps = {
    control?: Control<CreateQuestionFormType, any>;
    isText: boolean;
    options: optionsType[];
};

export const CreateTextQuestion = ({ control, options, isText }: TextQuestionProps) => {
    const type = isText ? 'TXT' : 'NUM';
    const maskOption = options.filter((opt) => opt.value?.includes(type)) || [];
    return (
        <>
            <Controller
                control={control}
                name="mask"
                defaultValue={type}
                rules={{ required: { value: true, message: 'Mask required' } }}
                render={({ field: { onChange, value }, fieldState: { error } }) => (
                    <SelectInput
                        defaultValue={type}
                        value={value}
                        label="Mask"
                        onChange={onChange}
                        options={maskOption}
                        error={error?.message}
                        required
                    />
                )}
            />
            <Controller
                control={control}
                name="fieldLength"
                rules={{
                    required: { value: true, message: 'Field length required' },
                    ...maxLengthRule(4)
                }}
                render={({ field: { onChange, value }, fieldState: { error } }) => (
                    <Input
                        onChange={onChange}
                        className="field-space"
                        defaultValue={value?.toString()}
                        label="Field length"
                        type="number"
                        error={error?.message}
                        min={1}
                        max={2000}
                        required
                    />
                )}
            />
        </>
    );
};
