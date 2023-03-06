import { Controller, useForm } from 'react-hook-form';
import { DatePickerInput } from '../FormInputs/DatePickerInput';
import { Button, ButtonGroup, Grid } from '@trussworks/react-uswds';
import { SelectInput } from '../FormInputs/SelectInput';
import { formatInterfaceString } from '../../utils/util';
import { Input } from '../FormInputs/Input';
import { SearchCriteriaContext } from '../../providers/SearchCriteriaContext';

export const RaceForm = ({ setRaceForm }: any) => {
    const methods = useForm();
    const { handleSubmit, control } = methods;

    const onSubmit = () => {
        setRaceForm();
    };

    return (
        <>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1 label-text">
                    As of:
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="nameAsOf"
                        render={({ field: { onChange, value } }) => (
                            <DatePickerInput
                                defaultValue={value}
                                onChange={onChange}
                                name="nameAsOf"
                                htmlFor={'nameAsOf'}
                            />
                        )}
                    />
                </Grid>
            </Grid>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1">
                    Race:
                </Grid>
                <Grid col={6}>
                    <SearchCriteriaContext.Consumer>
                        {({ searchCriteria }) => (
                            <Controller
                                control={control}
                                name="race"
                                render={({ field: { onChange, value } }) => (
                                    <SelectInput
                                        dataTestid="race"
                                        defaultValue={value}
                                        onChange={onChange}
                                        htmlFor={'race'}
                                        options={Object.values(searchCriteria.races).map((race) => {
                                            return {
                                                name: formatInterfaceString(race.codeDescTxt),
                                                value: race.id.code
                                            };
                                        })}
                                    />
                                )}
                            />
                        )}
                    </SearchCriteriaContext.Consumer>
                </Grid>
            </Grid>
            <Grid row className="flex-justify flex-align-center padding-2">
                <Grid col={6} className="margin-top-1">
                    Detailed race:
                </Grid>
                <Grid col={6}>
                    <Controller
                        control={control}
                        name="detailedRace"
                        render={({ field: { onChange, value } }) => (
                            <Input
                                onChange={onChange}
                                type="text"
                                defaultValue={value}
                                htmlFor="detailedRace"
                                id="detailedRace"
                            />
                        )}
                    />
                </Grid>
            </Grid>
            <div className="border-top border-base-lighter padding-2 margin-left-auto">
                <ButtonGroup className="flex-justify-end">
                    <Button
                        type="button"
                        data-testid="cancel-btn"
                        className="margin-top-0"
                        outline
                        onClick={setRaceForm}>
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