import { useEffect } from 'react';
import {
    ButtonGroup,
    Checkbox,
    ModalFooter,
    ModalRef,
    ModalToggleButton,
    Radio,
    Tag,
    Icon as UswIcon
} from '@trussworks/react-uswds';
import { PagesResponse } from 'apps/page-builder/generated';
import { RefObject, useState } from 'react';
import { ModalComponent } from 'components/ModalComponent/ModalComponent';
import './TargetQuestion.scss';
import { Icon } from '../../../../components/Icon/Icon';
import { fetchPageDetails } from '../../services/pagesAPI';
import { authorization } from 'authorization';

type CommonProps = {
    modalRef: RefObject<ModalRef>;
    pageId: string;
    getList: (data: QuestionProps[]) => void;
    multiSelected?: boolean;
    header?: string;
};

type QuestionProps = {
    id: number;
    question: string;
    name: string;
    selected: boolean;
    valueSet: string;
};

const TargetQuestion = ({ modalRef, pageId, getList, header, multiSelected = true }: CommonProps) => {
    const [activeTab, setActiveTab] = useState(0);
    const [sourceList, setSourceList] = useState<QuestionProps[]>([]);
    const [subsectionOpen, setSubsectionOpen] = useState(false);
    const [sourceId, setSource] = useState(-1);
    const [page, setPage] = useState<PagesResponse>();

    useEffect(() => {
        if (pageId) {
            fetchPageDetails(authorization(), Number(pageId)).then((data) => {
                setPage(data);
            });
        }
    }, [pageId, modalRef.current?.modalIsOpen]);

    const visible = true;
    const selectedRecord = sourceList.filter((list) => list.selected);
    const isSelectedAll = selectedRecord?.length === sourceList.length;

    const handleSelectAll = (e: any) => {
        setSourceList((prevState: any) => prevState.map((list: any) => ({ ...list, selected: e.target.checked })));
    };

    const handleRemove = (val: any) => {
        const updateList = [...sourceList];
        const index = sourceList.findIndex((list) => list?.name === val);
        updateList[index] = { ...updateList[index], selected: false };
        setSourceList(updateList);
    };

    const handleSelect = (e: any, key: number) => {
        let updateList = [...sourceList];

        if (!multiSelected) {
            updateList = updateList.map((qtn: QuestionProps) => ({
                name: qtn.name,
                id: qtn.id,
                question: qtn.question,
                valueSet: qtn.valueSet,
                selected: false
            }));
        }
        updateList[key] = { ...updateList[key], selected: e.target.checked };

        setSourceList(updateList);
    };

    const handleSourceList = (question: QuestionProps[]) => {
        const newList = question.map((qtn: QuestionProps) => ({
            name: qtn.name,
            id: qtn.id,
            question: qtn.question,
            valueSet: qtn.valueSet,
            selected: false
        }));

        setSourceList(newList);
    };

    return (
        <ModalComponent
            size="wide"
            modalRef={modalRef}
            isLarge
            modalHeading={header || 'Target questions'}
            modalBody={
                <div className="target-question-modal__container">
                    <h5>{`Please select ${header ? header.toLowerCase() : 'targeted questions'}.`}</h5>
                    <div className="target-question-tabs">
                        <ul className="tabs">
                            {page?.tabs?.map(({ name }, key) => (
                                <li
                                    key={key}
                                    className={activeTab === key ? 'active' : ''}
                                    onClick={() => setActiveTab(key)}>
                                    {name}
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div className="selected-question list">
                        <div className="title">Selected questions</div>
                        <div className="search-tags">
                            {sourceList
                                .filter((question) => question.selected)
                                .map((question: QuestionProps, index: number) => {
                                    return (
                                        <div className="tag-cover" key={index}>
                                            <Tag className="question-tag">
                                                {question.name} ({question.question})
                                            </Tag>
                                            <UswIcon.Close onClick={() => handleRemove(question.name)} />
                                        </div>
                                    );
                                })}
                        </div>
                    </div>
                    <div className="question-list-container">
                        <div className="tree-section">
                            {page?.tabs?.[activeTab] &&
                                page?.tabs?.[activeTab]?.sections?.map((section: any, key) => (
                                    <div key={key} className={`reorder-section ${visible ? '' : 'hidden'}`}>
                                        <div className="reorder-section__tile">
                                            <div
                                                className={`reorder-section__toggle ${
                                                    subsectionOpen == section.id ? 'open' : ''
                                                } `}
                                                onClick={() => setSubsectionOpen(section.id)}>
                                                <Icon name={'group'} size={'m'} />
                                                <span>{section.name}</span>
                                            </div>
                                        </div>
                                        <div
                                            className={`reorder-section__subsections ${
                                                subsectionOpen === section.id ? '' : 'closed'
                                            }`}>
                                            {section.subSections.map((sub: any, id: number) => (
                                                <div
                                                    key={id}
                                                    className="reorder-section__tiles"
                                                    onClick={() => {
                                                        setSource(id);
                                                        handleSourceList(sub.questions);
                                                    }}>
                                                    <div
                                                        className={`reorder-question__tile ${
                                                            id === sourceId && 'selected'
                                                        }`}>
                                                        <Icon name="group" size={'m'} />
                                                        <span>{sub.name}</span>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                ))}
                        </div>
                        <div className="list-section">
                            {multiSelected && (
                                <Checkbox
                                    onChange={handleSelectAll}
                                    id="hots1"
                                    checked={isSelectedAll}
                                    name={'race1'}
                                    label="Select All"
                                />
                            )}
                            <br />
                            {sourceList.map((list: any, index) => {
                                if (multiSelected) {
                                    return (
                                        <Checkbox
                                            onChange={(e) => handleSelect(e, index)}
                                            key={index}
                                            id={`sourceId${index}`}
                                            checked={list.selected}
                                            name={`sourceName ${index}`}
                                            label={list?.name!}
                                        />
                                    );
                                } else {
                                    return (
                                        <Radio
                                            key={index}
                                            id={`sourceId${index}`}
                                            checked={list.selected}
                                            name={`sourceName ${index}`}
                                            label={list?.name!}
                                            onChange={(e) => handleSelect(e, index)}
                                        />
                                    );
                                }
                            })}
                        </div>
                    </div>
                </div>
            }
            modalFooter={
                <ModalFooter className="padding-2 margin-left-auto footer">
                    <ButtonGroup className="flex-justify-end">
                        <ModalToggleButton modalRef={modalRef} closer outline data-testid="condition-cancel-btn">
                            Cancel
                        </ModalToggleButton>
                        <ModalToggleButton
                            modalRef={modalRef}
                            closer
                            data-testid="section-add-btn"
                            onClick={() => {
                                getList !== undefined && getList(selectedRecord);
                            }}>
                            Continue
                        </ModalToggleButton>
                    </ButtonGroup>
                </ModalFooter>
            }></ModalComponent>
    );
};

export default TargetQuestion;
