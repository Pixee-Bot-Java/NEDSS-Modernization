import { ManagePagesTable } from './ManagePagesTable';
import { render } from '@testing-library/react';
import { PageSummary } from 'apps/page-builder/generated';
import { BrowserRouter } from 'react-router-dom';

describe('when rendered', () => {
    it('should display sentence cased headers', async () => {
        const { container } = render(
            <BrowserRouter>
                <ManagePagesTable summaries={[]} currentPage={1} pageSize={10} totalElements={50}></ManagePagesTable>
            </BrowserRouter>
        );

        const tableHeader = container.getElementsByClassName('table-header');
        expect(tableHeader[0].innerHTML).toBe('Page Library');

        const tableHeads = container.getElementsByClassName('head-name');

        expect(tableHeads[0].innerHTML).toBe('Page name');
        expect(tableHeads[1].innerHTML).toBe('Event name');
        expect(tableHeads[2].innerHTML).toBe('Related conditions');
        expect(tableHeads[3].innerHTML).toBe('Status');
        expect(tableHeads[4].innerHTML).toBe('Last updated');
        expect(tableHeads[5].innerHTML).toBe('Last updated by');
    });
});

describe('when at least one summary is available', () => {
    const pageSummary: PageSummary = {
        conditions: [{ id: 'Some condition', name: 'condition display' }],
        eventType: { name: 'Investigation', value: 'INV' },
        id: 1,
        lastUpdate: '2019-09-25T13:27:16.380Z',
        lastUpdateBy: 'last updateBy',
        messageMappingGuide: { value: 'MMG Id', name: 'MMG display' },
        name: 'test page',
        status: 'Draft'
    };
    const summaries = [pageSummary];

    it('should display the page summaries', async () => {
        const { container } = render(
            <BrowserRouter>
                <ManagePagesTable summaries={summaries} currentPage={1} pageSize={10} totalElements={50}></ManagePagesTable>
            </BrowserRouter>
        );

        const tableData = container.getElementsByClassName('table-data');

        expect(tableData[0]).toHaveTextContent('test page');
        expect(tableData[1]).toHaveTextContent('Investigation');
        expect(tableData[2]).toHaveTextContent('condition display');
        expect(tableData[3]).toHaveTextContent('Draft');
        expect(tableData[4]).toHaveTextContent('9/25/2019, 1:27:16 PM');
        expect(tableData[5]).toHaveTextContent('last updateBy');
    });
});