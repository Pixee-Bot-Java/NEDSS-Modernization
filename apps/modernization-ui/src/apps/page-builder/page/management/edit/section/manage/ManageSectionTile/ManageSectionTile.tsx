import { PagesSection } from 'apps/page-builder/generated';
import { Draggable, DraggableProvided } from 'react-beautiful-dnd';
import { Icon as IconComponent } from 'components/Icon/Icon';
import { Button, Icon } from '@trussworks/react-uswds';
import styles from './manageSectionTile.module.scss';
import { AlertInLineProps } from '../ManageSectionModal';

type Props = {
    section: PagesSection;
    index: number;
    setSelectedForDelete: (section: PagesSection | undefined) => void;
    selectedForDelete: PagesSection | undefined;
    handleDelete: (section: PagesSection) => void;
    setOnAction: (action: boolean) => void;
    setAlert?: (alerts: AlertInLineProps) => void;
    onAction: boolean;
};

export const ManageSectionTile = ({
    section,
    index,
    setSelectedForDelete,
    selectedForDelete,
    handleDelete,
    setOnAction,
    onAction
}: Props) => {
    const deleteHeader = (section: PagesSection) => {
        if (section.subSections.length !== 0) {
            return `Section cannot be deleted. This section contains elements (subsections and questions) inside it. Remove the contents first, and then the section can be deleted.`;
        } else {
            return `Are you sure you want to delete this section?`;
        }
    };

    const isValidDelete = (section: PagesSection) => {
        if (section.subSections.length !== 0) {
            return false;
        } else {
            return true;
        }
    };

    return (
        <Draggable draggableId={section.id?.toString()} index={index} key={section.id?.toString()}>
            {(provided: DraggableProvided) => (
                <div className={styles.tile} ref={provided.innerRef} {...provided.draggableProps}>
                    {selectedForDelete !== undefined && selectedForDelete.id === section.id ? (
                        <div className={styles.warningModal}>
                            <div className={styles.warningModalHeader}>
                                <div className={styles.warningIcon}>
                                    <Icon.Warning size={3} />
                                </div>
                                {deleteHeader?.(section)}
                            </div>
                            <div className={styles.warningModalContent}>
                                <div className={styles.content}>
                                    <div className={styles.warningDrag} {...provided.dragHandleProps}>
                                        <IconComponent name="drag" size="3" />
                                    </div>
                                    <div className={styles.warningGroup}>
                                        <IconComponent name="group" size="3" />
                                    </div>
                                    <div>
                                        {section.name}&nbsp;
                                        {`${section.name}(${section.subSections.length})`}
                                    </div>
                                </div>
                                <div className={styles.warningModalBtns}>
                                    {isValidDelete(section) ? (
                                        <>
                                            <div
                                                onClick={() => {
                                                    handleDelete?.(section);
                                                    setSelectedForDelete(undefined);
                                                    setOnAction(false);
                                                }}>
                                                Yes, delete
                                            </div>
                                            <div className={styles.separator}>|</div>
                                            <div
                                                onClick={() => {
                                                    setSelectedForDelete(undefined);
                                                    setOnAction(false);
                                                }}>
                                                Cancel
                                            </div>
                                        </>
                                    ) : (
                                        <div
                                            onClick={() => {
                                                setSelectedForDelete(undefined);
                                                setOnAction(false);
                                            }}>
                                            OK
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className={styles.manageSectionTile}>
                            <div className={styles.handle} {...provided.dragHandleProps}>
                                <IconComponent name="drag" size="3" />
                            </div>
                            <div className={styles.label}>
                                <IconComponent name="group" size="3" />
                                <span data-testid="manageSectionTileId">
                                    {`${section.name}(${section.subSections.length})`}
                                </span>
                            </div>
                            <div className={styles.buttons}>
                                <Button
                                    type="button"
                                    onClick={() => {
                                        console.log('edit here');
                                    }}
                                    outline
                                    className={styles.iconBtn}
                                    disabled={onAction}>
                                    <Icon.Edit style={{ cursor: 'pointer' }} size={3} />
                                </Button>
                                <Button
                                    type="button"
                                    className={styles.iconBtn}
                                    outline
                                    disabled={onAction}
                                    onClick={() => {
                                        setSelectedForDelete(section);
                                        setOnAction(true);
                                    }}>
                                    <Icon.Delete style={{ cursor: 'pointer' }} size={3} />
                                </Button>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </Draggable>
    );
};
