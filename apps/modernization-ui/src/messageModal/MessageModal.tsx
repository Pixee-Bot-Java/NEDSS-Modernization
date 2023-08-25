import { RefObject } from 'react';
import { Icon, Modal, ModalHeading, ModalRef } from '@trussworks/react-uswds';
import { Note } from 'components/Note';
import './MessageModal.scss';

export type MessageModalContent = { message: string; detail?: string };

type Props = {
    modal: RefObject<ModalRef>;
    title: string;
    content: MessageModalContent;
    ariaDescribedBy?: string;
};

export const MessageModal = ({ modal, title = '', content, ariaDescribedBy = 'message-description' }: Props) => {
    return (
        <Modal
            ref={modal}
            id="example-modal-1"
            aria-labelledby="modal-1-heading"
            className="padding-0 modal-message modal"
            aria-describedby="modal-1-description">
            <ModalHeading id="modal-1-heading" className="border-bottom border-base-lighter font-sans-lg padding-2">
                {title}
            </ModalHeading>

            <div className="modal-content margin-2 padding-bottom-2">
                <div className="warning">
                    <Icon.Warning className="font-sans-2xl margin-x-2" />
                </div>
                <div className="modal-message">
                    <p className="margin-top-0" id={ariaDescribedBy}>
                        {content.message}
                    </p>

                    {content.detail && (
                        <div>
                            <Note>{content.detail}</Note>
                        </div>
                    )}
                </div>
            </div>
        </Modal>
    );
};
