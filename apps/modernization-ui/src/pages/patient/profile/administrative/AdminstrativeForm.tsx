import { Button, ButtonGroup, Grid, Label, ModalFooter, Textarea, ErrorMessage } from '@trussworks/react-uswds';
import { Controller, FieldValues, useForm } from 'react-hook-form';
import { DatePickerInput } from '../../../../components/FormInputs/DatePickerInput';
import { AdministrativeEntry } from './AdminstrativeEntry';

type EntryProps = {
    action: string;
    entry: AdministrativeEntry;
    onChange: (updated: AdministrativeEntry) => void;
    onCancel: () => void;
};

export const AdministrativeForm = ({ action, entry, onChange, onCancel }: EntryProps) => {
    const {
        handleSubmit,
        control,
        formState: { isValid }
    } = useForm({ mode: 'onBlur' });

    const onSubmit = (entered: FieldValues) => {
        onChange({
            asOf: entered.asOf,
            comment: entered.comment
        });
    };

    return (
        <div onSubmit={onSubmit} className="width-full maxw-full modal-form">
            <div className="modal-body">
                <Grid row>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <Controller
                            control={control}
                            name="asOf"
                            defaultValue={entry.asOf}
                            rules={{ required: { value: true, message: 'As of date is required.' } }}
                            render={({ field: { onBlur, onChange, value }, fieldState: { error } }) => (
                                <DatePickerInput
                                    flexBox
                                    defaultValue={value}
                                    onBlur={onBlur}
                                    onChange={onChange}
                                    name="asOf"
                                    htmlFor={'asOf'}
                                    label="Administrative as of"
                                    errorMessage={error?.message}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <Controller
                            control={control}
                            defaultValue={entry.comment}
                            name="comment"
                            rules={{ required: { value: true, message: 'Comments are required.' } }}
                            render={({ field: { onBlur, onChange }, fieldState: { error } }) => (
                                <Grid>
                                    <Grid className="flex-align-self-center">
                                        <Label htmlFor={'comment'}>Additional comments:</Label>
                                    </Grid>
                                    <Grid>
                                        <Textarea
                                            defaultValue={entry.comment ?? undefined}
                                            onBlur={onBlur}
                                            onChange={onChange}
                                            name="comment"
                                            id={'comment'}
                                        />
                                    </Grid>
                                    {error && <ErrorMessage id={`${error}-message`}>{error?.message}</ErrorMessage>}
                                </Grid>
                            )}
                        />
                    </Grid>
                </Grid>
            </div>

            <ModalFooter className="border-top border-base-lighter padding-2 margin-left-auto margin-0">
                <ButtonGroup className="flex-justify-end">
                    <Button type="button" className="margin-top-0" data-testid="cancel-btn" outline onClick={onCancel}>
                        Go Back
                    </Button>
                    <Button
                        disabled={!isValid}
                        onClick={handleSubmit(onSubmit)}
                        type="submit"
                        className="padding-105 text-center margin-top-0">
                        {action}
                    </Button>
                </ButtonGroup>
            </ModalFooter>
        </div>
    );
};