import { Controller, FieldValues, useForm, useWatch } from 'react-hook-form';
import { Button, ButtonGroup, Grid } from '@trussworks/react-uswds';
import { SelectInput } from 'components/FormInputs/SelectInput';
import { DatePickerInput } from 'components/FormInputs/DatePickerInput';
import { usePatientEthnicityCodedValues } from 'pages/patient/profile/ethnicity';

const UNKNOWN = 'UNK';
const HISPANIC = '2135-2';

type Props = {
    entry?: EthnicityEntry | null;
    onChanged?: (updated: EthnicityEntry) => void;
    onCancel?: () => void;
};

export type EthnicityEntry = {
    asOf: string | null;
    ethnicGroup: string | null;
    unknownReason: string | null;
    detailed: string[];
};

export const EthnicityForm = ({ entry, onChanged = () => {}, onCancel = () => {} }: Props) => {
    const { handleSubmit, control } = useForm();

    const coded = usePatientEthnicityCodedValues();

    const selectedEthinicity = useWatch({ control, name: 'ethnicGroup', defaultValue: entry?.ethnicGroup });

    const onSubmit = (entered: FieldValues) => {
        onChanged({
            asOf: entered.asOf,
            ethnicGroup: entered.ethnicGroup,
            unknownReason: entered.unknownReason,
            detailed: entered.detailed ? [entered.detailed] : []
        });
    };

    return (
        <>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1 label-text">
                    <label htmlFor="asOf">As of:</label>
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="asOf"
                        rules={{ required: true }}
                        defaultValue={entry?.asOf}
                        render={({ field: { onChange, value } }) => (
                            <DatePickerInput
                                id="asOf"
                                defaultValue={value}
                                onChange={onChange}
                                name="asOf"
                                htmlFor={'asOf'}
                            />
                        )}
                    />
                </Grid>
            </Grid>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1">
                    <label htmlFor="ethnicGroup">Ethnicity:</label>
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="ethnicGroup"
                        defaultValue={entry?.ethnicGroup}
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
                <Grid row className="flex-justify flex-align-center padding-2">
                    <Grid col={6} className="margin-top-1">
                        Spanish origin:
                    </Grid>
                    <Grid col={6}>
                        <Controller
                            control={control}
                            name="detailed"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    htmlFor={'detailed'}
                                    options={coded.detailedEthnicities}
                                />
                            )}
                        />
                    </Grid>
                </Grid>
            )}
            {selectedEthinicity === UNKNOWN && (
                <Grid row className="flex-justify flex-align-center padding-2">
                    <Grid col={6} className="margin-top-1">
                        Reason unknown:
                    </Grid>
                    <Grid col={6}>
                        <Controller
                            control={control}
                            name="unknownReason"
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
