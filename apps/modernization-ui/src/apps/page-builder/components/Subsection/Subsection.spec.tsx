import { render } from '@testing-library/react';
import { Subsection } from "apps/page-builder/generated/models/Subsection"
import { SubsectionComponent } from "./Subsection";

describe('when Subsection renders', () => {
    const subsection: Subsection = {
        id: 123456,
        name: 'Test Subsection',
        pageQuestions: [],
        visible: 'T'
    };
    const mockFunction = jest.fn();

    const { container } = render(
        <SubsectionComponent subsection={subsection} />
    );

    it('should display Subsection name', () => {
        const name = container.getElementsByTagName('h2');
        expect(name[0].innerHTML).toEqual('Test Subsection');
    });
});