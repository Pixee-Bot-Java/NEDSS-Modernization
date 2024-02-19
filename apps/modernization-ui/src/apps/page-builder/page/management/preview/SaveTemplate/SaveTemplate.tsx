import { Button, Form, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { Input } from 'components/FormInputs/Input';
import { Controller, useForm } from 'react-hook-form';
import styles from './save-tempate.module.scss';
import { RefObject } from 'react';
import { CreateTemplateRequest, PagesService } from 'apps/page-builder/generated';
import { authorization as getAuthorization } from 'authorization';
import { usePageManagement } from '../../usePageManagement';
import { useAlert } from 'alert';
import { maxLengthRule } from 'validation/entry';

const initSave = {
    name: undefined,
    description: undefined
};

type Props = {
    modalRef: RefObject<ModalRef>;
};

export const SaveTemplate = ({ modalRef }: Props) => {
    const { page } = usePageManagement();
    const saveForm = useForm({
        mode: 'onBlur',
        defaultValues: { ...initSave }
    });
    const { handleSubmit, control } = saveForm;
    const authorization = getAuthorization();
    const { showAlert } = useAlert();

    const onSubmit = handleSubmit((data) => {
        if (data.name && data.description) {
            const request: CreateTemplateRequest = {
                name: data.name,
                description: data.description
            };
            try {
                PagesService.createTemplate({
                    authorization,
                    page: page.id,
                    request: request
                }).then(() => {
                    modalRef.current?.toggleModal();
                    showAlert({
                        type: 'success',
                        header: 'Success',
                        message: `${data.name} was saved successfully`
                    });
                });
            } catch (error: unknown) {
                modalRef.current?.toggleModal();
                if (error instanceof Error) {
                    console.error(error);
                    showAlert({
                        type: 'error',
                        header: 'error',
                        message: error.message
                    });
                } else {
                    console.error(error);
                    showAlert({
                        type: 'error',
                        header: 'error',
                        message: 'An unknown error occurred'
                    });
                }
            }
        }
    });

    return (
        <Form onSubmit={onSubmit} className={styles.saveTemplate}>
            <div className={styles.body}>
                <p>
                    All fields with <span>*</span> are required.
                </p>
                <Controller
                    control={control}
                    name="name"
                    rules={{ required: { value: true, message: 'Name is required' }, ...maxLengthRule(50) }}
                    render={({ field: { onChange, value, name }, fieldState: { error } }) => (
                        <Input
                            name={name}
                            type="text"
                            label="Template name"
                            defaultValue={value}
                            onChange={onChange}
                            error={error?.message}
                            required
                        />
                    )}
                />
                <Controller
                    control={control}
                    name="description"
                    rules={{ required: { value: true, message: 'Description is required' }, ...maxLengthRule(50) }}
                    render={({ field: { onChange, value, name }, fieldState: { error } }) => (
                        <Input
                            name={name}
                            type="text"
                            label="Template description"
                            defaultValue={value}
                            onChange={onChange}
                            error={error?.message}
                            required
                        />
                    )}
                />
            </div>
            <div className={styles.footer}>
                <ModalToggleButton type="button" closer outline modalRef={modalRef}>
                    Cancel
                </ModalToggleButton>
                <Button type="submit" disabled={!saveForm.formState.isValid}>
                    Save
                </Button>
            </div>
        </Form>
    );
};
