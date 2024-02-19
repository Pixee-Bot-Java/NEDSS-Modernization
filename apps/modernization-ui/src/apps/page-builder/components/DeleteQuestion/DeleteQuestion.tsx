import React, { useRef } from 'react';
import { ModalRef, Icon, ModalToggleButton } from '@trussworks/react-uswds';
import styles from './delete-question.module.scss';
import { ConfirmationModal } from '../../../../confirmation';

type CommonProps = {
    onDelete?: () => void;
};

const DeleteQuestion = ({ onDelete }: CommonProps) => {
    const deleteModalRef = useRef<ModalRef>(null);
    const handleDeleteQuetions = () => {
        onDelete?.();
    };
    return (
        <div className={styles.delete}>
            <ModalToggleButton modalRef={deleteModalRef} className="delete-btn" unstyled>
                <Icon.Delete style={{ cursor: 'pointer' }} className="primary-color" />
            </ModalToggleButton>
            <ConfirmationModal
                modal={deleteModalRef}
                title="Warning"
                message="Are you sure you want to delete the question?"
                detail="Deleting this question cannot be undone. Are you sure you want to continue?"
                confirmText="Yes, delete"
                onConfirm={() => {
                    handleDeleteQuetions();
                    deleteModalRef.current?.toggleModal();
                }}
                onCancel={() => {
                    deleteModalRef.current?.toggleModal();
                }}
            />
        </div>
    );
};

export default DeleteQuestion;
