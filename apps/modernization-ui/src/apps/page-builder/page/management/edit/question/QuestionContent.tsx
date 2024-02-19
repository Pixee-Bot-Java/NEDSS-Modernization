import styles from './question-content.module.scss';
import { Input } from 'components/FormInputs/Input';
import { Heading } from 'components/heading';
import { authorization } from 'authorization';
import { useEffect, useState } from 'react';
import { SelectInput } from 'components/FormInputs/SelectInput';
import { Selectable } from 'options/selectable';
import { Icon as NbsIcon } from 'components/Icon/Icon';
import { RadioButtons } from 'apps/page-builder/components/RadioButton/RadioButton';
import { Button, Icon } from '@trussworks/react-uswds';
import { ConceptOptionsResponse, ConceptOptionsService } from 'generated';

type Props = {
    defaultValue: string;
    name: string;
    type: string;
    displayComponent?: number;
    identifier: string;
    valueSet: string;
    id: number;
    isStandard: boolean;
};

const hyperlinkId = 1003;
const commentsReadOnlyId = 1014;
const lineSeparatorId = 1012;
const participantListId = 1030;
const originalElecDocId = 1036;

const staticTypes = [hyperlinkId, commentsReadOnlyId, lineSeparatorId, participantListId, originalElecDocId];

export const QuestionContent = ({
    type,
    valueSet,
    name,
    identifier,
    displayComponent,
    defaultValue,
    isStandard
}: Props) => {
    const [conceptState, setConceptState] = useState<Selectable[]>([]);

    useEffect(() => {
        if (valueSet) {
            ConceptOptionsService.allUsingGet({
                authorization: authorization(),
                name: valueSet
            }).then((resp: ConceptOptionsResponse) => {
                setConceptState(resp.options);
            });
        }
    }, [valueSet]);

    const renderLabelWithComponent = (
        <div className={styles.content}>
            <div className={styles.questionHeader}>
                <Heading level={2}>{name}</Heading>
                <div className={styles.identifier}>{`(${identifier})`}</div>
            </div>
            <div className={styles.questionContent}>
                <div className={styles.inputContent}>
                    {(displayComponent === 1007 ||
                        displayComponent === 1013 ||
                        displayComponent === 1024 ||
                        displayComponent === 1025 ||
                        displayComponent === 1027 ||
                        displayComponent === 1028 ||
                        displayComponent === 1031) && (
                        <SelectInput
                            onChange={() => {}}
                            defaultValue={''}
                            options={conceptState}
                            data-testid="dropdown-input"
                        />
                    )}
                    {/* create custom checkbox component */}
                    {/* need to create an api that grabs the race since it is in another table, once that is done a custom component can be created */}
                    {displayComponent === 1001 && <RadioButtons options={conceptState} />}

                    {displayComponent === 1008 && (
                        <Input onChange={() => {}} defaultValue="" type="text" className={styles.questionInput} />
                    )}

                    {(displayComponent === 1009 || displayComponent === 1019) && (
                        <Input
                            onChange={() => {}}
                            defaultValue=""
                            type="text"
                            className={styles.questionInput}
                            multiline
                        />
                    )}
                </div>

                {type === 'DATE' && <Icon.CalendarToday size={4} className={styles.icon} data-testid="calendar-icon" />}
            </div>
            {valueSet && !isStandard && (
                <div className="margin-top-1em">
                    <Button
                        className={`${styles.unStyledButton} margin-right-2`}
                        type="button"
                        onClick={() => {}}
                        unstyled>
                        <Icon.Edit className="margin-right-2px" />
                        <span> Edit value set</span>
                    </Button>
                    <Button className={styles.unStyledButton} type="button" onClick={() => {}} unstyled>
                        <Icon.Edit className="margin-right-2px" />
                        <span> Change value set</span>
                    </Button>
                </div>
            )}
        </div>
    );
    return (
        <div className={styles.question}>
            <div className={styles.reorderIcon}>
                <NbsIcon name={'drag'} />
            </div>
            {!staticTypes.includes(displayComponent ?? 0) ? (
                renderLabelWithComponent
            ) : (
                <>
                    {displayComponent === commentsReadOnlyId && <div>{name}</div>}
                    {displayComponent === hyperlinkId && <a href={defaultValue}>{name}</a>}
                    {displayComponent === lineSeparatorId && (
                        <div className={styles.inputContent}>Line separator will be placed here</div>
                    )}
                    {displayComponent === participantListId && (
                        <div className={styles.inputContent}>Participants list will be placed here</div>
                    )}
                    {displayComponent === originalElecDocId && (
                        <div className={styles.inputContent}>Original document list will be placed here</div>
                    )}
                </>
            )}
        </div>
    );
};
