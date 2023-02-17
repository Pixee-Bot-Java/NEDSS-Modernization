import { Grid } from '@trussworks/react-uswds';
import { SelectInput } from '../../../../components/FormInputs/SelectInput';
import { Controller } from 'react-hook-form';
import { formatInterfaceString } from '../../../../utils/util';
import { SearchCriteriaContext } from '../../../../providers/SearchCriteriaContext';

export const EthnicityForm = ({ control }: any) => {
    return (
        <>
            <SearchCriteriaContext.Consumer>
                {({ searchCriteria }) => (
                    <>
                        <Grid col={12}>
                            <Controller
                                control={control}
                                name="ethnicity"
                                render={({ field: { onChange, value } }) => (
                                    <SelectInput
                                        defaultValue={value}
                                        onChange={onChange}
                                        htmlFor={'ethnicity'}
                                        label="Ethnicity"
                                        options={Object.values(searchCriteria.ethnicities).map((ethnicity) => {
                                            return {
                                                name: formatInterfaceString(ethnicity.codeDescTxt),
                                                value: ethnicity.id.code
                                            };
                                        })}
                                    />
                                )}
                            />
                        </Grid>
                        <Grid col={12}>
                            <Controller
                                control={control}
                                name="race"
                                render={({ field: { onChange, value } }) => (
                                    <SelectInput
                                        defaultValue={value}
                                        onChange={onChange}
                                        htmlFor={'race'}
                                        label="Race"
                                        options={Object.values(searchCriteria.races).map((race) => {
                                            return {
                                                name: formatInterfaceString(race.codeDescTxt),
                                                value: race.id.code
                                            };
                                        })}
                                    />
                                )}
                            />
                        </Grid>
                    </>
                )}
            </SearchCriteriaContext.Consumer>
        </>
    );
};