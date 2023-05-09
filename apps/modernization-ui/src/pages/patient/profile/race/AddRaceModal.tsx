import { useState } from 'react';
import { ModalComponent } from '../../../../components/ModalComponent/ModalComponent';
import {
    Button,
    ButtonGroup,
    Form,
    Grid,
    Label,
    ModalFooter,
    ModalToggleButton,
    Textarea
} from '@trussworks/react-uswds';
import { Controller, useForm } from 'react-hook-form';
import { DatePickerInput } from '../../../../components/FormInputs/DatePickerInput';
import { SelectInput } from '../../../../components/FormInputs/SelectInput';
import { Input } from '../../../../components/FormInputs/Input';
import { SearchCriteriaContext } from 'providers/SearchCriteriaContext';
import { formatInterfaceString } from 'utils/util';

type ModalProps = {
    modalRef: any;
    handleSubmission?: (type: 'error' | 'success' | 'warning' | 'info', message: string) => void;
    modalHead?: string;
};

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const ModalBody = ({ control, onSubmit, modalRef }: any) => {
    return (
        <Form onSubmit={onSubmit} className="width-full maxw-full modal-form">
            <div className="modal-body">
                <Grid row>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <Controller
                            control={control}
                            name="asOf"
                            render={({ field: { onChange, value } }) => (
                                <DatePickerInput
                                    flexBox
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="asOf"
                                    htmlFor={'asOf'}
                                    label="As of"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <SearchCriteriaContext.Consumer>
                            {({ searchCriteria }) => (
                                <Controller
                                    control={control}
                                    name="race"
                                    render={({ field: { onChange, value } }) => (
                                        <SelectInput
                                            flexBox
                                            dataTestid="race"
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
                            )}
                        </SearchCriteriaContext.Consumer>
                    </Grid>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <Controller
                            control={control}
                            name="detailedRace"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    flexBox
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Detailed race"
                                    name="detailedRace"
                                    htmlFor="detailedRace"
                                    id="detailedRace"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={12} className="border-bottom border-base-lighter padding-bottom-2 padding-2">
                        <Controller
                            control={control}
                            name="additionalComments"
                            render={({ field: { onChange } }) => (
                                <Grid row>
                                    <Grid col={6} className="flex-align-self-center">
                                        <Label htmlFor={'additionalComments'}>Additional comments:</Label>
                                    </Grid>
                                    <Grid col={6}>
                                        <Textarea
                                            onChange={onChange}
                                            name="additionalComments"
                                            id={'additionalComments'}
                                        />
                                    </Grid>
                                </Grid>
                            )}
                        />
                    </Grid>
                </Grid>
            </div>

            <ModalFooter className="border-top border-base-lighter padding-2 margin-left-auto margin-0">
                <ButtonGroup>
                    <ModalToggleButton type="button" className="margin-top-0" outline modalRef={modalRef} closer>
                        Go back
                    </ModalToggleButton>
                    <Button type="submit" className="padding-105 text-center margin-top-0">
                        Add
                    </Button>
                </ButtonGroup>
            </ModalFooter>
        </Form>
    );
};

export const AddRaceModal = ({ modalRef, handleSubmission, modalHead }: ModalProps) => {
    const methods = useForm();
    const { handleSubmit, control } = methods;

    const [submitted, setSubmitted] = useState<boolean>(false);

    const onSubmit = (data: any) => {
        modalRef.current?.toggleModal();
        handleSubmission?.('success', `${data?.last}, ${data?.first}`);
        setSubmitted(true);
    };

    return (
        <ModalComponent
            modalRef={modalRef}
            modalHeading={modalHead || 'Add - Race'}
            modalBody={
                <ModalBody
                    submitted={submitted}
                    control={control}
                    onSubmit={handleSubmit(onSubmit)}
                    modalRef={modalRef}
                />
            }
        />
    );
};
