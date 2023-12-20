/* eslint-disable camelcase */
import { Button, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { useAlert } from 'alert';
import {
    CodedQuestion,
    DateQuestion,
    NumericQuestion,
    PageQuestionControllerService,
    PageSummary,
    TextQuestion
} from 'apps/page-builder/generated';
import { TableBody, TableComponent } from 'components/Table/Table';
import { useContext, useEffect, useRef, useState } from 'react';
import { Direction } from 'sorting';
import { ModalComponent } from '../../../../components/ModalComponent/ModalComponent';
import { UserContext } from '../../../../providers/UserContext';
import { QuestionsContext } from '../../context/QuestionsContext';
import { CreateQuestion } from '../../components/CreateQuestion/CreateQuestion';
import './QuestionLibraryTable.scss';
import { SearchBar } from './SearchBar';
import { useNavigate, useParams } from 'react-router-dom';

export enum Column {
    Type = 'Type',
    UniqueId = 'Unique ID',
    UniqueName = 'Unique name',
    SubGroup = 'Subgroup',
    Status = 'Status'
}

const tableColumns = [
    { name: Column.Type, sortable: true },
    { name: Column.UniqueId, sortable: true },
    { name: Column.UniqueName, sortable: true },
    { name: Column.SubGroup, sortable: true },
    { name: Column.Status, sortable: true }
];

type Question = TextQuestion | DateQuestion | NumericQuestion | CodedQuestion;

type Props = {
    summaries: Question[];
    pages?: any;
    qtnModalRef?: any;
};

export const QuestionLibraryTable = ({ summaries, pages, qtnModalRef }: Props) => {
    const { showAlert } = useAlert();
    const navigateTo = useNavigate();
    const [tableRows, setTableRows] = useState<TableBody[]>([]);
    const [selectedQuestion, setSelectedQuestion] = useState<Question>({});
    const { searchQuery, setSearchQuery, setCurrentPage, setSortBy, isLoading } = useContext(QuestionsContext);
    const { pageId, subsectionId } = useParams();
    const { state } = useContext(UserContext);
    const authorization = `Bearer ${state.getToken()}`;
    const asTableRow = (page: Question): TableBody => ({
        id: page.id,
        key: page.id,
        checkbox: true,
        selectable: true,
        tableDetails: [
            {
                id: 1,
                title: <div className="page-name">{page?.type || page.codeSet}</div> || null
            },
            { id: 2, title: <div className="event-text">{page?.uniqueId}</div> || null },
            {
                id: 3,
                title: <div>{page?.uniqueName}</div> || null
            },
            {
                id: 4,
                title: <div>{page?.subgroup}</div> || null
            },
            {
                id: 5,
                title: <div>{page?.status}</div> || null
            }
        ]
    });
    const handleSelected = ({ target }: any, item: any) => {
        if (target.checked) {
            const value: any = summaries.filter((val: any) => item.id === val.id);
            setSelectedQuestion(value);
        } else {
            setSelectedQuestion({});
        }
    };
    const asTableRows = (pages: PageSummary[] | undefined): TableBody[] => pages?.map(asTableRow) || [];

    /*
     * Converts header and Direction to API compatible sort string such as "name,asc"
     */
    const toSortString = (name: string, direction: Direction): string => {
        if (name && direction && direction !== Direction.None) {
            switch (name) {
                case Column.Type:
                    return `questionType,${direction}`;
                case Column.UniqueId:
                    return `uniqueId,${direction}`;
                case Column.UniqueName:
                    return `uniqueName,${direction}`;
                case Column.SubGroup:
                    return `subgroup,${direction}`;
                case Column.Status:
                    return `status,${direction}`;
                default:
                    return '';
            }
        }
        return '';
    };

    useEffect(() => {
        setTableRows(asTableRows(summaries));
    }, [summaries]);

    useEffect(() => {
        return () => localStorage.setItem('selectedQuestion', '0');
    }, []);

    const handleSort = (name: string, direction: Direction): void => {
        if (name && Direction) {
            setSortBy(toSortString(name, direction));
        }
    };

    const handleAddQsntoPage = async () => {
        const request = {
            orderNumber: subsectionId,
            questionId: selectedQuestion.id
        };
        history.go(-1);

        PageQuestionControllerService.addQuestionToPageUsingPost({
            authorization,
            page: Number(pageId),
            request
        }).then((response: any) => {
            setSelectedQuestion({});
            showAlert({ type: 'success', header: 'Add', message: 'Add Question successfully on page' });
            history.go(-1);
            return response;
        });
    };

    const footerActionBtn = (
        <div className="question-action-btn">
            {qtnModalRef ? (
                <ModalToggleButton className="cancel-btn" modalRef={qtnModalRef}>
                    Cancel
                </ModalToggleButton>
            ) : (
                <Button className="cancel-btn" type="button" onClick={() => setSelectedQuestion({})}>
                    Cancel
                </Button>
            )}
            <Button
                className="submit-btn"
                type="button"
                onClick={handleAddQsntoPage}
                disabled={!Object.keys(selectedQuestion).length}>
                Add to page
            </Button>
        </div>
    );

    const modalRef = useRef<ModalRef>(null);

    const dataNotAvailableElement = (
        <div className="no-data-available">
            <label className="margin-bottom-1em no-text">
                {searchQuery ? `No results found for ‘${searchQuery}’` : 'No results found '}
            </label>
            <Button
                className="submit-btn"
                type="button"
                onClick={() => {
                    navigateTo('/page-builder/add/question');
                }}
                outline>
                Create New
            </Button>
            <ModalToggleButton className="submit-btn display-none" type="button" modalRef={modalRef} outline>
                Create New
            </ModalToggleButton>
            <ModalComponent
                isLarge
                modalRef={modalRef}
                modalHeading={'Add question'}
                modalBody={<CreateQuestion modalRef={modalRef} />}
            />
        </div>
    );

    const searchAvailableElement = (
        <div className="no-data-available">
            <label className="no-text">Still can't find what are you're looking for?</label>
            <label className="margin-bottom-1em search-desc">
                Please try searching in the local library before creating new
            </label>
            <div>
                <ModalToggleButton className="submit-btn" type="button" modalRef={modalRef} outline>
                    Create New
                </ModalToggleButton>
                <Button className="submit-btn" type="button">
                    Search Local
                </Button>
            </div>
            <ModalComponent
                isLarge
                modalRef={modalRef}
                modalHeading={'Add question'}
                modalBody={<CreateQuestion modalRef={modalRef} />}
            />
        </div>
    );

    return (
        <div>
            <h3 className="h3-label"> you can search for an existing question or create new one</h3>
            <div>{<SearchBar onChange={setSearchQuery} />}</div>
            {summaries?.length ? (
                <TableComponent
                    contextName="questions"
                    tableHeader=""
                    tableHead={tableColumns}
                    tableBody={tableRows}
                    isPagination={true}
                    pageSize={pages?.pageSize}
                    totalResults={pages?.totalElements}
                    currentPage={pages?.currentPage}
                    handleNext={setCurrentPage}
                    sortData={handleSort}
                    selectable
                    handleSelected={handleSelected}
                    rangeSelector={true}
                    isLoading={isLoading}
                />
            ) : (
                dataNotAvailableElement
            )}
            {summaries?.length > 0 && searchQuery && searchAvailableElement}
            <div className="footer-action">{footerActionBtn}</div>
        </div>
    );
};
