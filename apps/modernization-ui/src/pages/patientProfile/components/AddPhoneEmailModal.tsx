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
                            name="phoneEmailAsOf"
                            render={({ field: { onChange, value } }) => (
                                <DatePickerInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="phoneEmailAsOf"
                                    htmlFor={'phoneEmailAsOf'}
                                    label="Phone & email as of"
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
                            name="use"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="use"
                                    htmlFor={'use'}
                                    label="Use"
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="countryCode"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Country code"
                                    name="countryCode"
                                    htmlFor="countryCode"
                                    id="countryCode"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="phoneNumber"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Phone number"
                                    name="phoneNumber"
                                    htmlFor="phoneNumber"
                                    id="phoneNumber"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="ext"
                            render={({ field: { onChange, value } }) => (
                                <SelectInput
                                    defaultValue={value}
                                    onChange={onChange}
                                    name="ext"
                                    htmlFor={'ext'}
                                    label="Ext."
                                    options={[]}
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="email"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="Email"
                                    name="email"
                                    htmlFor="email"
                                    id="email"
                                />
                            )}
                        />
                    </Grid>
                    <Grid col={10}>
                        <Controller
                            control={control}
                            name="url"
                            render={({ field: { onChange, value } }) => (
                                <Input
                                    onChange={onChange}
                                    defaultValue={value}
                                    type="text"
                                    label="URL"
                                    name="url"
                                    htmlFor="url"
                                    id="url"
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

export const AddPhoneEmailModal = ({ modalRef }: AddCommentModalProps) => {
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