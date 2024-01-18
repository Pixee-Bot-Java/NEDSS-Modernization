import { PagesQuestion, PagesTab } from 'apps/page-builder/generated';
import { Sections } from '../section/Sections';
import { PageSideMenu } from './PageSideMenu';
import styles from './page-content.module.scss';
import { ModalComponent } from 'components/ModalComponent/ModalComponent';
import { EditStaticElement } from '../staticelement/EditStaticElement';
import { Icon, ModalRef } from '@trussworks/react-uswds';
import { useEffect, useRef, useState } from 'react';
import { AddQuestionModal } from '../../../../components/Subsection/AddQuestionModal/AddQuestionModal';
import { ValuesetLibrary } from '../../../../pages/ValuesetLibrary/ValuesetLibrary';
import { AddValueset } from '../../../../components/AddValueset/AddValueset';
import { CreateQuestion } from '../../../../components/CreateQuestion/CreateQuestion';
import { Heading } from '../../../../../../components/heading';

type Props = {
    tab: PagesTab;
    refresh?: () => void;
    handleManageSection?: () => void;
    handleAddSection?: () => void;
};

const hyperlinkId = 1003;
const commentsReadOnlyId = 1014;
const lineSeparatorId = 1012;
const originalElecDocId = 1036;
const readOnlyPartId = 1030;

const staticTypes = [hyperlinkId, commentsReadOnlyId, lineSeparatorId, originalElecDocId, readOnlyPartId];

const questionTypes = [1001, 1006, 1007, 1008, 1009, 1013, 1017, 1019, 1024, 1025, 1026, 1027, 1028, 1029, 1031, 1032];

export const PageContent = ({ tab, refresh, handleAddSection, handleManageSection }: Props) => {
    const editStaticElementRef = useRef<ModalRef>(null);
    const [currentEditQuestion, setCurrentEditQuestion] = useState<PagesQuestion>();
    const [subsectionId, setSubsectionId] = useState(0);
    const addQuestionModalRef = useRef<ModalRef>(null);
    const addValueModalRef = useRef<ModalRef>(null);
    const createValueModalRef = useRef<ModalRef>(null);
    const editQuestionModalRef = useRef<ModalRef>(null);

    const handleAddSubsection = (section: number) => {
        console.log('add subsection not yet implemented', section);
    };

    const onCloseModal = () => {
        if (staticTypes.includes(currentEditQuestion?.displayComponent!)) {
            editStaticElementRef.current?.toggleModal(undefined, false);
        } else {
            editQuestionModalRef.current?.toggleModal(undefined, false);
        }
        setCurrentEditQuestion(undefined);
    };

    const handleEditQuestion = (question: PagesQuestion) => {
        setCurrentEditQuestion(question);
    };

    useEffect(() => {
        if (staticTypes.includes(currentEditQuestion?.displayComponent!)) {
            editStaticElementRef.current?.toggleModal(undefined, true);
        } else if (questionTypes.includes(currentEditQuestion?.displayComponent!)) {
            editQuestionModalRef.current?.toggleModal(undefined, true);
        }
    }, [currentEditQuestion]);

    return (
        <div className={styles.pageContent}>
            <div className={styles.invisible} />
            <Sections
                sections={tab.sections ?? []}
                onAddSubsection={handleAddSubsection}
                onEditQuestion={handleEditQuestion}
                onAddQuestion={setSubsectionId}
                handleManageSection={() => handleManageSection!()}
                addQuestionModalRef={addQuestionModalRef}
            />
            <PageSideMenu onAddSection={() => handleAddSection!()} />
            <ModalComponent
                modalRef={editStaticElementRef}
                modalHeading={'Edit static elements'}
                modalBody={
                    currentEditQuestion !== undefined && (
                        <EditStaticElement
                            question={currentEditQuestion!}
                            onCloseModal={onCloseModal}
                            refresh={refresh}
                        />
                    )
                }
            />
            <AddQuestionModal
                subsectionId={subsectionId}
                modalRef={addQuestionModalRef}
                addValueModalRef={addValueModalRef}
            />
            <ModalComponent
                modalRef={editQuestionModalRef}
                closer
                size="wide"
                modalHeading={
                    <div className="edit-question-header">
                        <Heading level={2}>Edit question</Heading>
                    </div>
                }
                modalBody={
                    <CreateQuestion
                        onCloseModal={onCloseModal}
                        question={currentEditQuestion}
                        addValueModalRef={addValueModalRef}
                    />
                }
            />
            <ModalComponent
                size="wide"
                modalRef={addValueModalRef}
                modalHeading={<Heading level={2}>Search value set</Heading>}
                modalBody={
                    <ValuesetLibrary hideTabs modalRef={addValueModalRef} createValueModalRef={createValueModalRef} />
                }
                closer
            />
            <ModalComponent
                isLarge
                size="wide"
                modalRef={createValueModalRef}
                modalHeading={
                    <span className="header-icon-title">
                        <Icon.ArrowBack />
                        <Heading level={2}>Add value set</Heading>
                    </span>
                }
                modalBody={
                    <AddValueset
                        modalRef={createValueModalRef}
                        updateCallback={() => {
                            createValueModalRef.current?.toggleModal(undefined, false);
                            addValueModalRef.current?.toggleModal(undefined, false);
                        }}
                    />
                }
                closer
            />
        </div>
    );
};
