import { Button, Icon } from '@trussworks/react-uswds';
import { PagesSection, PagesTab, SectionControllerService } from 'apps/page-builder/generated';
import { Icon as NbsIcon } from 'components/Icon/Icon';
import styles from './managesection.module.scss';
import { useState } from 'react';
import { AddSection } from './AddSection';
import { Heading } from 'components/heading';
import { authorization } from 'authorization';
import { AlertInLineProps } from './ManageSectionModal';
import { ManageSectionTile } from './ManageSectionTile/ManageSectionTile';
import { useDragDrop } from 'apps/page-builder/context/DragDropProvider';
import { DragDropContext, Droppable } from 'react-beautiful-dnd';

type ManageSectionProps = {
    pageId: number;
    tab?: PagesTab;
    onCancel?: () => void;
    onContentChange?: () => void;
    alert?: AlertInLineProps;
    onDeleteSection?: () => void;
    onResetAlert?: () => void;
};

export const ManageSection = ({
    onCancel,
    tab,
    onContentChange,
    pageId,
    alert,
    onDeleteSection,
    onResetAlert
}: ManageSectionProps) => {
    const [sectionState, setSectionState] = useState<'manage' | 'add'>('manage');

    const [confirmDelete, setConfirmDelete] = useState<PagesSection | undefined>(undefined);

    const [onAction, setOnAction] = useState<boolean>(false);

    const handleUpdateState = (state: 'manage' | 'add') => {
        setSectionState(state);
    };

    const onDelete = (section: PagesSection) => {
        SectionControllerService.deleteSectionUsingDelete({
            authorization: authorization(),
            page: pageId,
            sectionId: section.id
        }).then(() => {
            onContentChange?.();
            onDeleteSection?.();
        });
    };

    const { handleDragEnd, handleDragStart, handleDragUpdate } = useDragDrop();
    return (
        <>
            {sectionState === 'add' && (
                <AddSection
                    pageId={pageId}
                    onAddSectionCreated={() => {
                        onContentChange?.();
                        setSectionState('manage');
                    }}
                    onCancel={() => setSectionState('manage')}
                    tabId={tab?.id}
                />
            )}
            {sectionState === 'manage' && (
                <div className={styles.managesection}>
                    <div className={styles.header}>
                        <div className={styles.manageSectionHeader} data-testid="header">
                            <Heading level={4}>Manage sections</Heading>
                        </div>
                        <div className={styles.addSectionHeader}>
                            <Button
                                type="button"
                                onClick={() => {
                                    handleUpdateState('add');
                                }}
                                className={styles.addSectionBtn}
                                disabled={onAction}>
                                <Icon.Add size={3} className={styles.addIcon} />
                                Add new section
                            </Button>
                        </div>
                    </div>
                    <div className={styles.content}>
                        {alert !== undefined && (
                            <div className={styles.alert}>
                                <div className={styles.checkCircle}>
                                    <Icon.CheckCircle size={3} />
                                </div>
                                <div className={styles.alertContent}>
                                    <div className={styles.alertMessage}>{alert.message}</div>
                                    <div className={styles.closeBtn}>
                                        <Icon.Close size={3} onClick={() => onResetAlert?.()} />
                                    </div>
                                </div>
                            </div>
                        )}
                        <div className={styles.tab}>
                            <div className={styles.folderIcon}>
                                <NbsIcon name={'folder'} />
                            </div>
                            <p className={styles.tabName}>{tab?.name}</p>
                        </div>
                        <DragDropContext
                            onDragEnd={handleDragEnd}
                            onDragStart={handleDragStart}
                            onDragUpdate={handleDragUpdate}>
                            <Droppable droppableId="all-sections" type="sections">
                                {(provided) => (
                                    <div
                                        className="manage-sections"
                                        {...provided.droppableProps}
                                        ref={provided.innerRef}>
                                        {tab?.sections?.map((section, k) => {
                                            return (
                                                <ManageSectionTile
                                                    section={section}
                                                    index={k}
                                                    key={k}
                                                    setSelectedForDelete={setConfirmDelete}
                                                    selectedForDelete={confirmDelete}
                                                    handleDelete={onDelete}
                                                    setOnAction={setOnAction}
                                                    onAction={onAction}
                                                />
                                            );
                                        })}
                                        {provided.placeholder}
                                    </div>
                                )}
                            </Droppable>
                        </DragDropContext>
                    </div>
                    <div className={styles.footer}>
                        <Button
                            onClick={() => {
                                onCancel?.();
                                setConfirmDelete(undefined);
                            }}
                            type={'button'}
                            outline>
                            Close
                        </Button>
                    </div>
                </div>
            )}
        </>
    );
};
