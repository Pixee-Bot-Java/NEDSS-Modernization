import styles from './subsection.module.scss';
import { Button, Icon, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { MoreOptions } from 'apps/page-builder/components/MoreOptions/MoreOptions';
import { Icon as IconComponent } from 'components/Icon/Icon';
import { RefObject, useRef } from 'react';
import { ModalComponent } from 'components/ModalComponent/ModalComponent';
import { AddStaticElement } from 'apps/page-builder/page/management/edit/staticelement/AddStaticElement';

type Props = {
    name: string;
    id: number;
    questionCount: number;
    onAddQuestion: () => void;
    isExpanded: boolean;
    addQuestionModalRef: RefObject<ModalRef>;
    onExpandedChange: (isExpanded: boolean) => void;
};

export const SubsectionHeader = ({
    name,
    id,
    questionCount,
    isExpanded,
    onAddQuestion,
    onExpandedChange,
    addQuestionModalRef
}: Props) => {
    const addStaticElementModalRef = useRef<ModalRef>(null);
    const renderQuestionListModal = () => (
        <>
            <ModalToggleButton className="add-btn" outline onClick={onAddQuestion} modalRef={addQuestionModalRef}>
                Add Question
            </ModalToggleButton>
        </>
    );
    return (
        <div className={styles.header}>
            <div className={styles.info}>
                <div className={styles.name}>{name}</div>
                <div className={styles.count}>{`${questionCount} question${questionCount > 1 ? 's' : ''}`}</div>
            </div>
            <div className={styles.buttons}>
                <>{renderQuestionListModal()}</>
                <MoreOptions header={<Icon.MoreVert size={4} />}>
                    <Button type="button" onClick={() => console.log('BLAH')}>
                        <Icon.Edit size={3} /> Edit Subsection
                    </Button>
                    <Button type="button" onClick={() => console.log('BLAH')}>
                        <IconComponent name={'group'} size={'s'} /> Group Subsection
                    </Button>
                    <ModalToggleButton type="button" modalRef={addStaticElementModalRef}>
                        <Icon.Add size={3} /> Add static element
                    </ModalToggleButton>
                    <Button type="button" onClick={() => console.log('BLAH')}>
                        <Icon.Delete size={3} /> Delete
                    </Button>
                </MoreOptions>
                {isExpanded ? (
                    <Icon.ExpandLess size={4} onClick={() => onExpandedChange(false)} />
                ) : (
                    <Icon.ExpandMore size={4} onClick={() => onExpandedChange(true)} />
                )}
            </div>
            <ModalComponent
                modalRef={addStaticElementModalRef}
                modalHeading={'Add static element'}
                modalBody={<AddStaticElement modalRef={addStaticElementModalRef} subsectionId={id} />}
            />
        </div>
    );
};
