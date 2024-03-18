import React, { useEffect, useState } from 'react';
import styles from './TargetQuestion.module.scss';
import { useGetPageDetails } from 'apps/page-builder/page/management';
import {
    PagesQuestion,
    PagesResponse,
    PagesSection,
    PagesSubSection,
    PagesTab,
    Rule,
    Target
} from 'apps/page-builder/generated';
import { Icon } from 'components/Icon/Icon';
import { Button, Checkbox, Tag, Icon as UswIcon } from '@trussworks/react-uswds';
import { useGetAllPageRules } from 'apps/page-builder/hooks/api/useGetAllPageRules';

type Props = {
    ruleFunction?: string;
    sourceQuestion?: PagesQuestion;
    onSubmit: (questions: PagesQuestion[]) => void;
    onCancel: () => void;
    editTargetQuestion?: PagesQuestion[];
};

const staticType = [1014, 1003, 1012, 1030, 1036];

export const TargetQuestion = ({ ruleFunction, sourceQuestion, onCancel, onSubmit, editTargetQuestion }: Props) => {
    const [activeTab, setActiveTab] = useState(0);
    const [activeSection, setActiveSection] = useState<number>(0);
    const [activeSubsection, setActiveSubsection] = useState<number>(0);
    const [filteredPage, setFilteredPage] = useState<PagesResponse>();
    const [targetList, setTargetList] = useState<PagesQuestion[]>([]);
    const [targetIdent, setTargetIdent] = useState<string[]>([]);
    const [selectedList, setSelectedList] = useState<PagesQuestion[]>([]);

    const { fetch, rules } = useGetAllPageRules();

    const { page } = useGetPageDetails();

    const handleRemove = (question: PagesQuestion) => {
        setSelectedList(selectedList.filter((qtn) => qtn.id !== question.id));
    };

    const handleSelect = (question: PagesQuestion, e: React.ChangeEvent<HTMLInputElement>) => {
        const tempList = [...selectedList];

        if (e.target.checked) {
            tempList.push(question);
            setSelectedList(tempList);
        } else {
            const index = tempList.findIndex((qtn) => qtn.id === question.id);
            tempList.splice(index, 1);
            setSelectedList(tempList);
        }
    };

    const handleSelectAll = (questions: PagesQuestion[], e: React.ChangeEvent<HTMLInputElement>) => {
        const tempList = [...selectedList];
        if (e.target.checked) {
            questions?.map((question) => {
                if (!tempList.find((qtn) => qtn.id === question.id)) {
                    tempList.push(question);
                }
            });
            setSelectedList(tempList);
        } else {
            questions.map((question) => {
                const index = tempList.findIndex((qtn) => qtn.id === question.id);
                tempList.splice(index, 1);
            });
            setSelectedList(tempList);
        }
    };

    const isSameGroup = (question: PagesQuestion) => question.questionGroupSeq === sourceQuestion?.questionGroupSeq;

    const isDate = (question: PagesQuestion) => question.dataType === 'DATE' || question.dataType === 'DATETIME';

    const isNotStatic = (question: PagesQuestion) => !staticType.includes(question.displayComponent ?? 0);

    const isNotUsed = (question: PagesQuestion) => !targetIdent?.includes(question.question ?? '');

    const isNotSubsection = (question: PagesQuestion) => question.displayComponent !== 1016;

    const isNotSource = (question: PagesQuestion) => question.question !== sourceQuestion?.question;

    const handleTargetCases = (question: PagesQuestion[]) => {
        if (ruleFunction === Rule.ruleFunction.DATE_COMPARE) {
            const filteredList = question.filter(isDate);
            return filteredList;
        } else {
            let filteredList = question.filter(isNotUsed).filter(isNotStatic).filter(isNotSource);
            if (sourceQuestion?.blockName) {
                filteredList = filteredList.filter(isSameGroup).filter(isNotSubsection);
            }
            return filteredList;
        }
    };

    useEffect(() => {
        if (editTargetQuestion) {
            setSelectedList(editTargetQuestion);
        }
    }, [JSON.stringify(editTargetQuestion)]);

    useEffect(() => {
        const targetsIdentifiers: string[] = [];
        if (rules) {
            rules.forEach((rule: Rule) => {
                rule.targets.forEach((target: Target) => {
                    if (
                        !editTargetQuestion?.find(
                            (question: PagesQuestion) => question.question === target.targetIdentifier
                        )
                    ) {
                        targetsIdentifiers.push(target.targetIdentifier ?? '');
                    }
                });
            });
            setTargetIdent(targetsIdentifiers);
        }
    }, [JSON.stringify(rules)]);

    useEffect(() => {
        fetch();
    }, []);

    const handleTargetQuestion = (questions: PagesQuestion[]) => {
        setTargetList(handleTargetCases(questions));
    };

    const onReset = () => {
        setActiveTab(0);
        setActiveSection(0);
        setActiveSubsection(0);
        setTargetList([]);
    };

    useEffect(() => {
        if (page) {
            const result: PagesResponse = {
                id: page.id,
                description: page.description,
                name: page.name,
                root: page.root,
                rules: page.rules,
                status: page.status,
                tabs: []
            };
            page.tabs?.forEach((tab: PagesTab) => {
                const newTab: PagesTab = {
                    id: tab.id,
                    name: tab.name,
                    sections: [],
                    visible: tab.visible,
                    order: tab.order
                };

                tab.sections.forEach((section: PagesSection) => {
                    const newSection: PagesSection = {
                        id: section.id,
                        name: section.name,
                        order: section.order,
                        subSections: [],
                        visible: section.visible
                    };

                    section.subSections.forEach((subsection: PagesSubSection) => {
                        if (subsection.questions.length > 0) {
                            if (handleTargetCases(subsection.questions).length > 0) {
                                newSection?.subSections.push(subsection);
                            }
                        }
                    });

                    if (newSection) {
                        newSection.subSections.length > 0 && newTab?.sections.push(newSection);
                    }
                });
                if (newTab) {
                    newTab.sections.length > 0 && result?.tabs?.push(newTab);
                }
            });
            setFilteredPage(result);
        }
    }, [page, ruleFunction]);

    return (
        <div className={styles.targetQuestion}>
            <div className={styles.headerTitle}>
                <h2>Target question</h2>
            </div>
            <div className={styles.header}>
                <div className={styles.headerMessage}>Please select target question</div>
                <div className={styles.targetTabs}>
                    <ul className={styles.tabs}>
                        {filteredPage?.tabs?.map(({ name }, tabKey) => (
                            <li
                                key={tabKey}
                                className={activeTab === tabKey ? styles.active : ''}
                                onClick={() => {
                                    setActiveTab(tabKey);
                                    setActiveSection(0);
                                    setTargetList([]);
                                }}>
                                {name}
                            </li>
                        ))}
                    </ul>
                </div>
                <div className={styles.selectedQuestions}>
                    <div className={styles.title}>Selected questions: </div>
                    <div className={styles.content}>
                        {selectedList.map((question, key) => (
                            <div key={key} className={styles.selectedQuestion}>
                                <Tag className={styles.selectedQuestion}>
                                    {question.name} ({question.question})
                                </Tag>
                                <UswIcon.Close onClick={() => handleRemove(question)} />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className={styles.body}>
                <div className={styles.content}>
                    <div className={styles.sections}>
                        {filteredPage?.tabs?.[activeTab] &&
                            filteredPage?.tabs?.[activeTab]?.sections.map((section: PagesSection, key) => (
                                <div key={key}>
                                    <div key={key} className={styles.section}>
                                        <div
                                            className={styles.sectionToggle}
                                            onClick={() =>
                                                activeSection === section.id
                                                    ? setActiveSection(0)
                                                    : setActiveSection(section.id)
                                            }>
                                            <Icon name={'group'} size={'m'} />
                                            <span className={styles.name}>{section.name}</span>
                                        </div>
                                    </div>
                                    {activeSection === section.id && (
                                        <div className={styles.subsections}>
                                            {section.subSections.map((subsection: PagesSubSection, id) => (
                                                <div
                                                    key={id}
                                                    className={styles.subsection}
                                                    onClick={() => {
                                                        if (activeSubsection === subsection.id) {
                                                            setActiveSubsection(0);
                                                            setTargetList([]);
                                                        } else {
                                                            setActiveSubsection(subsection.id);
                                                            handleTargetQuestion(subsection.questions);
                                                        }
                                                    }}>
                                                    <Icon name={'group'} size={'m'} />
                                                    <span className={styles.name}>{subsection.name}</span>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                    </div>
                    <div className={styles.questionsList}>
                        <div className={styles.selectAll}>
                            <Checkbox
                                onChange={(e) => handleSelectAll(targetList, e)}
                                id="hots1"
                                name={'race1'}
                                label="Select All"
                            />
                        </div>
                        {targetList.map((question: PagesQuestion, index) => (
                            <div className={styles.question} key={index}>
                                <Checkbox
                                    onChange={(e) => handleSelect(question, e)}
                                    checked={selectedList.find((qtn) => qtn.id === question.id) !== undefined}
                                    id={`sourceId${index}`}
                                    name={`sourceName ${index}`}
                                    label={question?.name}
                                />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className={styles.footerBtn}>
                <Button
                    type="button"
                    outline
                    onClick={() => {
                        onReset?.();
                        onCancel?.();
                    }}>
                    Cancel
                </Button>
                <Button
                    type="button"
                    onClick={() => {
                        onSubmit(selectedList);
                        onReset?.();
                        onCancel?.();
                    }}>
                    Continue
                </Button>
            </div>
        </div>
    );
};
