import { Button } from '@trussworks/react-uswds';
import { AddableQuestionSort, useFindAddableQuestions } from 'apps/page-builder/hooks/api/useFindAvailableQuestions';
import { SelectionMode } from 'components/Table';
import { PageProvider, Status, usePage } from 'page';
import { useEffect, useState } from 'react';

import { QuestionSearchTable } from './table/QuestionSearchTable';
import { CloseableHeader } from 'apps/page-builder/components/CloseableHeader/CloseableHeader';
import { ButtonBar } from 'apps/page-builder/components/ButtonBar/ButtonBar';
import styles from './question-search.module.scss';

export const QuestionSearch = (props: Props) => (
    <PageProvider>
        <QuestionSearchContent {...props} />
    </PageProvider>
);

type Props = {
    pageId: number;
    onCreateNew: () => void;
    onCancel: () => void;
    onAccept: (questions: number[]) => void;
};
const QuestionSearchContent = ({ pageId, onCreateNew, onCancel, onAccept }: Props) => {
    const { page, ready, firstPage, reload } = usePage();
    const [query, setQuery] = useState<string>('');
    const [sort, setSort] = useState<AddableQuestionSort | undefined>(undefined);
    const { isLoading, search, response } = useFindAddableQuestions();
    const [selectedQuestions, setSelectedQuestions] = useState<number[]>([]);
    const [resetTable, setResetTable] = useState<boolean>(false);

    useEffect(() => {
        search({ pageId });
    }, [pageId]);

    useEffect(() => {
        if (page.status === Status.Requested) {
            setSelectedQuestions([]);
            search({
                searchText: query,
                pageId: pageId,
                page: page.current - 1,
                pageSize: page.pageSize,
                sort
            });
        }
    }, [page.status]);

    useEffect(() => {
        if (page.current === 1) {
            reload();
        } else {
            firstPage();
        }
    }, [query, sort]);

    useEffect(() => {
        const currentPage = response?.number ? response?.number + 1 : 1;
        ready(response?.totalElements ?? 0, currentPage);
    }, [response]);

    const handleSelectedQuestionChange = (mode: SelectionMode, id: number) => {
        if (mode === 'deselect') {
            setSelectedQuestions((previous) => previous.filter((e) => e !== id));
        } else {
            setSelectedQuestions((previous) => [...previous, id]);
        }
    };

    const handleAccept = () => {
        setSelectedQuestions([]);
        setResetTable(true);
        onAccept(selectedQuestions);
    };

    const handleClose = () => {
        setQuery('');
        setSelectedQuestions([]);
        setResetTable(true);
        onCancel();
    };

    useEffect(() => {
        if (resetTable) {
            firstPage();
            setResetTable(false);
        }
    }, [resetTable]);

    return (
        <>
            <CloseableHeader onClose={handleClose} title="Add question" />
            <div className={styles.content}>
                <div className={styles.subHeading}>
                    <div className={styles.helpText}>You can search for an existing question or create a new one</div>
                </div>
                {!resetTable && (
                    <QuestionSearchTable
                        questions={response?.content ?? []}
                        isLoading={isLoading}
                        query={query}
                        onQuerySubmit={(query) => setQuery(query)}
                        onSortChange={setSort}
                        onSelectionChange={handleSelectedQuestionChange}
                        onCreateNew={onCreateNew}
                    />
                )}

                {response?.content?.length === 0 && (
                    <div className={styles.createNewNotification}>
                        <div className={styles.message}>Can't find what you're looking for?</div>
                        <Button type="button" outline onClick={onCreateNew}>
                            Create new
                        </Button>
                    </div>
                )}
            </div>
            <ButtonBar>
                <Button onClick={handleClose} type="button" outline>
                    Cancel
                </Button>
                <Button
                    onClick={handleAccept}
                    type="button"
                    disabled={selectedQuestions == undefined || selectedQuestions.length === 0}>
                    Apply to page
                </Button>
            </ButtonBar>
        </>
    );
};