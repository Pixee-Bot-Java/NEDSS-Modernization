import { Icon } from '@trussworks/react-uswds';
import DeleteQuestion from 'apps/page-builder/components/DeleteQuestion/DeleteQuestion';
import { ToggleButton } from 'apps/page-builder/components/ToggleButton';
import { PagesQuestion } from 'apps/page-builder/generated';
import { Heading } from 'components/heading';
import styles from './question-header.module.scss';

type Props = {
    question: PagesQuestion;
    onRequiredChange?: () => void;
    onEditQuestion?: () => void;
    onDeleteQuestion?: () => void;
};

const hyperlinkId = 1003;
const commentsReadOnlyId = 1014;
const lineSeparatorId = 1012;
const originalElecDocId = 1036;
const readOnlyPartId = 1030;

export const QuestionHeader = ({ question, onRequiredChange, onEditQuestion, onDeleteQuestion }: Props) => {
    const getHeadingText = (displayComponent: number | undefined) => {
        switch (displayComponent) {
            case hyperlinkId:
                return 'Static element: Hyperlink';
            case lineSeparatorId:
                return 'Static element: Line separator';
            case commentsReadOnlyId:
                return 'Static element: Comment';
            case originalElecDocId:
                return 'Static element: Electronic document list';
            case readOnlyPartId:
                return 'Static element: Participant list';
            default:
                return 'Question';
        }
    };
    return (
        <div className={styles.header}>
            <div className={styles.typeDisplay}>
                {question.isStandard && <div className={styles.standardIndicator}>S</div>}
                <Heading level={3}>{getHeadingText(question.displayComponent)}</Heading>
            </div>
            <div className={styles.questionButtons}>
                <Icon.Edit style={{ cursor: 'pointer' }} size={3} className="primary-color" onClick={onEditQuestion} />
                {!question.isStandard && <DeleteQuestion onDelete={onDeleteQuestion} />}
                <div className={styles.divider}>|</div>
                <div className={styles.requiredToggle}>Not required</div>
                <ToggleButton defaultChecked={question.required} onChange={onRequiredChange} />
                <div className={styles.requiredToggle}>Required</div>
            </div>
        </div>
    );
};
