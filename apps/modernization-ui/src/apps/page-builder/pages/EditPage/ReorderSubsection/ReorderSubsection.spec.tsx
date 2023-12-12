import { render } from '@testing-library/react';
import { ReorderSubsection } from './ReorderSubsection';
import { PagesSubSection, PagesResponse } from 'apps/page-builder/generated';
import DragDropProvider from 'apps/page-builder/context/DragDropProvider';
import { DragDropContext, Droppable } from 'react-beautiful-dnd';

describe('when ReorderSubsection renders', () => {
    const content: PagesResponse = {
        id: 123,
        name: 'Test Page',
        tabs: [
            {
                id: 123456,
                name: 'Test Page',
                sections: [
                    {
                        id: 1234,
                        name: 'Section1',
                        subSections: [],
                        visible: true
                    },
                    {
                        id: 5678,
                        name: 'Section2',
                        subSections: [],
                        visible: true
                    }
                ],
                visible: true
            }
        ]
    };
    const subsection: PagesSubSection = {
        id: 123456,
        name: 'Test Section',
        questions: [
            {
                allowFutureDates: true,
                coInfection: true,
                dataType: 'asdf',
                description: 'asdf',
                display: true,
                enabled: true,
                id: 123,
                mask: 'asdf',
                name: 'asdf',
                question: 'asdf',
                tooltip: 'asdf',
                standard: 'asdf',
                required: true,
                subGroup: 'asdf'
            },
            {
                allowFutureDates: true,
                coInfection: false,
                dataType: 'asdf',
                description: 'asdf',
                display: true,
                enabled: true,
                id: 234,
                mask: 'asdf',
                name: 'asdf',
                question: 'asdf',
                tooltip: 'asdf',
                standard: 'asdf',
                required: false,
                subGroup: 'asdf'
            }
        ],
        visible: true
    };
    const { container } = render(
        <DragDropProvider pageData={content} currentTab={0}>
            <DragDropContext onDragEnd={() => {}}>
                <Droppable droppableId='testId'>
                    {(provided) => (
                        <div
                            {...provided.droppableProps}
                            ref={provided.innerRef}
                            className="test__subsections">
                        <ReorderSubsection subsection={subsection} index={1} visible />
                    </div>)}
                </Droppable>
            </DragDropContext>
        </DragDropProvider>);
    it('should display Questions', () => {
        const questions = container.getElementsByClassName('reorder-question');
        expect(questions.length).toEqual(2);
    });
});
