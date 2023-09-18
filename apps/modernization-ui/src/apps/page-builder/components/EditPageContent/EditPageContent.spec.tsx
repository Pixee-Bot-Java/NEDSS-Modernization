import { render } from "@testing-library/react";
import { EditPageContent } from "apps/page-builder/generated/models/EditPageContent";
import { EditPageContentComponent } from "./EditPageContent";

describe('when EditPageContent renders', () => {
    const content: EditPageContent = {
        id: 123456,
        name: 'Test Name',
        tabSections: [{
            id: 1119232,
            name: "Patient",
            sectionSubSections: [],
            visible: "T"
        },{
            id: 1119225,
            name: "Vaccination",
            sectionSubSections: [],
            visible: "T"
        }],
        visible: 'T'
    };
    const mockFunction = jest.fn();

    it('should display two sections', () => {
        const { container } = render(
            <EditPageContentComponent content={content} onAddSection={mockFunction} />
        );
        const sections = container.getElementsByClassName('section');

        expect(sections.length).toEqual(2);
    });
});