import styles from './subsection.module.scss';
import { Button, Icon, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { MoreOptions } from 'apps/page-builder/components/MoreOptions/MoreOptions';
import { Icon as IconComponent } from 'components/Icon/Icon';
import { useRef } from 'react';
import { ModalComponent } from 'components/ModalComponent/ModalComponent';
import { AddStaticElement } from 'apps/page-builder/page/management/edit/staticelement/AddStaticElement';

type Props = {
    name: string;
    id: number;
    questionCount: number;
    onAddQuestion: () => void;
    isExpanded: boolean;
    onExpandedChange: (isExpanded: boolean) => void;
    onDeleteSubsection: () => void;
};

export const SubsectionHeader = ({
    name,
    id,
    questionCount,
    isExpanded,
    onAddQuestion,
    onExpandedChange,
    onDeleteSubsection
}: Props) => {
    const addStaticElementModalRef = useRef<ModalRef>(null);

    return (
        <div className={styles.header}>
            <div className={styles.info}>
                <div className={styles.name}>{name}</div>
                <div className={styles.count}>{`${questionCount} question${questionCount > 1 ? 's' : ''}`}</div>
            </div>
            <div className={styles.buttons}>
                <Button type="button" className="add-btn" outline onClick={onAddQuestion}>
                    Add Question
                </Button>
                <MoreOptions header={<Icon.MoreVert size={4} />}>
                    <Button type="button" onClick={() => console.log('BLAH')}>
                        <Icon.Edit size={3} /> Edit subsection
                    </Button>
                    <Button type="button" onClick={() => console.log('BLAH')}>
                        <IconComponent name={'group'} size={'s'} /> Group subsection
                    </Button>
                    <ModalToggleButton type="button" modalRef={addStaticElementModalRef}>
                        <Icon.Add size={3} /> Add static element
                    </ModalToggleButton>
                    <Button type="button" onClick={onDeleteSubsection}>
                        <Icon.Delete size={3} /> Delete subsection
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
