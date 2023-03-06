import { Ref } from 'react';
import { ModalComponent } from '../../../components/ModalComponent/ModalComponent';
import {
    Button,
    ButtonGroup,
    Form,
    Grid,
    Label,
    ModalFooter,
    ModalRef,
    ModalToggleButton,
    Textarea
} from '@trussworks/react-uswds';
import { Controller, useForm } from 'react-hook-form';
import { DatePickerInput } from '../../../components/FormInputs/DatePickerInput';
import { SelectInput } from '../../../components/FormInputs/SelectInput';
import { Input } from '../../../components/FormInputs/Input';

type AddCommentModalProps = {
    modalRef: Ref<ModalRef> | undefined;
};

const ModalBody = ({ control, onSubmit, modalRef }: any) => {
    return (
        <Form onSubmit={onSubmit} className="width-full maxw-full modal-form">
            <div className="padding-2 modal-body">
                <Grid row>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="nameAsOf"
                            render={({ field: { onChange, value } }) => (
                                <DatePickerInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="nameAsOf"
                                    htmlFor={'nameAsOf'}
                                    label="Name as of"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="type"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="type"
                                    htmlFor={'type'}
                                    label="Type"
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="prefex"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="prefex"
                                    htmlFor={'prefex'}
                                    label="Prefex"
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="suffix"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="suffix"
                                    htmlFor={'suffix'}
                                    label="Suffix"
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="degree"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="degree"
                                    htmlFor={'degree'}
                                    label="Degree"
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="first"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="First"
                                    name="first"
                                    htmlFor="first"
                                    id="first"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="middle"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Middle"
                                    name="middle"
                                    htmlFor="middle"
                                    id="middle"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="last"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Last"
                                    name="last"
                                    htmlFor="last"
                                    id="last"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="secondLast"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Second last"
                                    name="secondLast"
                                    htmlFor="secondLast"
                                    id="secondLast"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="additionalComments"
                            render={({ field: { onChange } }) => (
                                <>
                                    <Label htmlFor={'additionalComments'}>Additional comments</Label>
                                    <Textarea onChange={onChange} name="additionalComments" id={'additionalComments'} />
                                </>
                            )}
                        />
                    </Grid>
                </Grid>
            </div>

            <ModalFooter className="border-top border-base-lighter padding-2 margin-left-auto">
                <ButtonGroup>
                    <ModalToggleButton className="margin-top-0" outline modalRef={modalRef} closer>
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

export const AddNameModal = ({ modalRef }: AddCommentModalProps) => {
    const methods = useForm();
    const { handleSubmit, control } = methods;

    const onSubmit = (data: any) => {
        console.log(data);
    };

    return (
        <ModalComponent
            modalRef={modalRef}
            modalHeading="Add - Comment"
            modalBody={<ModalBody control={control} onSubmit={handleSubmit(onSubmit)} modalRef={modalRef} />}
        />
    );
};