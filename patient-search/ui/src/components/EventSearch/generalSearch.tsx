import { Control, FieldValues } from 'react-hook-form';
import { EventType, PregnancyStatus } from '../../generated/graphql/schema';
import { SearchCriteriaContext } from '../../providers/SearchCriteriaContext';
import { SelectControl } from '../FormInputs/SelectControl';

type GeneralSearchProps = {
    control: Control<FieldValues, any>;
    searchType?: string;
};

export const GeneralSearch = ({ control, searchType = '' }: GeneralSearchProps) => {
    return (
        <>
            <SearchCriteriaContext.Consumer>
                {({ searchCriteria }) => (
                    <>
                        {searchType === EventType.Investigation && (
                            <SelectControl
                                control={control}
                                name="condition"
                                label="Condition:"
                                onChangeMethod={(e) => console.log(e)}
                                options={searchCriteria.conditions.map((c) => {
                                    return {
                                        name: c.conditionDescTxt,
                                        value: c.id
                                    };
                                })}
                            />
                        )}

                        <SelectControl
                            control={control}
                            name="programArea"
                            label="Program Area:"
                            onChangeMethod={(e) => console.log(e)}
                            options={searchCriteria.programAreas.map((p) => {
                                return {
                                    name: p.id,
                                    value: p.id
                                };
                            })}
                        />
                        <SelectControl
                            control={control}
                            name="jurisdiction"
                            label="Jurisdiction:"
                            onChangeMethod={(e) => console.log(e)}
                            options={searchCriteria.jurisdictions.map((j) => {
                                return {
                                    name: j.codeDescTxt,
                                    value: j.id
                                };
                            })}
                        />
                    </>
                )}
            </SearchCriteriaContext.Consumer>

            <SelectControl
                control={control}
                name="pregnancyTest"
                label="Pregnancy Test:"
                onChangeMethod={(e) => console.log(e)}
                options={[
                    { name: PregnancyStatus.Yes, value: PregnancyStatus.Yes },
                    { name: PregnancyStatus.No, value: PregnancyStatus.No },
                    { name: PregnancyStatus.Unknown, value: PregnancyStatus.Unknown }
                ]}
            />
        </>
    );
};
