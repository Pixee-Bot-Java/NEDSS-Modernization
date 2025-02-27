import { Controller, FieldValues, useForm, useWatch } from 'react-hook-form';
import { Button, ButtonGroup, Grid } from '@trussworks/react-uswds';
import { SelectInput } from 'components/FormInputs/SelectInput';
import { DatePickerInput } from 'components/FormInputs/DatePickerInput';
import { usePatientEthnicityCodedValues } from 'apps/patient/profile/ethnicity';
import { MultiSelectInput } from 'components/selection/multi';
import { orNull } from 'utils';
import { externalizeDateTime, internalizeDate } from 'date';

const UNKNOWN = 'UNK';
const HISPANIC = '2135-2';

type Props = {
    entry: EthnicityEntry;
    onChanged: (updated: EthnicityEntry) => void;
    onCancel: () => void;
};

export type EthnicityEntry = {
    asOf: string | null;
    ethnicGroup: string | null;
    unknownReason: string | null;
    detailed: string[];
};

export const EthnicityForm = ({ entry, onChanged = () => {}, onCancel = () => {} }: Props) => {
    const {
        handleSubmit,
        control,
        formState: { isValid }
    } = useForm({ mode: 'onBlur' });

    const coded = usePatientEthnicityCodedValues();

    const selectedEthinicity = useWatch({ control, name: 'ethnicGroup', defaultValue: entry.ethnicGroup });

    const onSubmit = (entered: FieldValues) => {
        onChanged({
            asOf: externalizeDateTime(entered.asOf),
            ethnicGroup: orNull(entered.ethnicGroup),
            unknownReason: orNull(entered.unknownReason),
            detailed: entered.detailed ? entered.detailed : []
        });
    };

    return (
        <>
            <Grid row className="flex-justify flex-align-center padding-2 border-bottom border-base-lighter">
                <Grid col={6} className="margin-top-1 label-text">
                    <label className="required text-bold" htmlFor="asOf">
                        As of:
                    </label>
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="asOf"
                        rules={{ required: { value: true, message: 'As of date is required.' } }}
                        defaultValue={entry?.asOf ?? internalizeDate(new Date())}
                        render={({ field: { onBlur, onChange, value, name }, fieldState: { error } }) => (
                            <DatePickerInput
                                data-testid="asOf"
                                defaultValue={value}
                                onBlur={onBlur}
                                onChange={onChange}
                                name={name}
                                disableFutureDates
                                errorMessage={error?.message}
                            />
                        )}
                    />
                </Grid>
            </Grid>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1">
                    <label className="text-bold" htmlFor="ethnicGroup">
                        Ethnicity:
                    </label>
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="ethnicGroup"
                        defaultValue={entry.ethnicGroup}
                        render={({ field: { onChange, value } }) => (
                            <SelectInput
                                id="ethnicGroup"
                                defaultValue={value}
                                onChange={onChange}
                                options={coded.ethnicGroups}
                            />
                        )}
                    />
                </Grid>
            </Grid>
            {selectedEthinicity === HISPANIC && (
                <Grid row className="flex-justify flex-align-center padding-2 border-bottom border-base-lighter">
                    <Grid col={6} className="margin-top-1 text-bold">
                        Spanish origin:
                    </Grid>
                    <Grid col={6}>
                        <Controller
                            control={control}
                            name="detailed"
                            shouldUnregister
                            defaultValue={entry.detailed}
                            render={({ field: { onChange, value } }) => (
                                <MultiSelectInput
                                    id="detailed"
                                    value={value}
                                    onChange={onChange}
                                    options={coded.detailedEthnicities}
                                />
                            )}
                        />
                    </Grid>
                </Grid>
            )}
            {selectedEthinicity === UNKNOWN && (
                <Grid row className="flex-justify flex-align-center padding-2 border-bottom border-base-lighter">
                    <Grid col={6} className="margin-top-1 text-bold">
                        Reason unknown:
                    </Grid>
                    <Grid col={6}>
                        <Controller
                            control={control}
                            name="unknownReason"
                            shouldUnregister
                            defaultValue={entry?.unknownReason}
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    htmlFor={'unknownReason'}
                                    options={coded.ethnicityUnknownReasons}
                                />
                            )}
                        />
                    </Grid>
                </Grid>
            )}
            <div className="border-top border-base-lighter padding-2 margin-left-auto">
                <ButtonGroup className="flex-justify-end">
                    <Button type="button" className="margin-top-0" data-testid="cancel-btn" outline onClick={onCancel}>
                        Cancel
                    </Button>
                    <Button
                        disabled={!isValid}
                        onClick={handleSubmit(onSubmit)}
                        type="submit"
                        className="padding-105 text-center margin-top-0">
                        Save
                    </Button>
                </ButtonGroup>
            </div>
        </>
    );
};
