import { screen, render } from '@testing-library/react';
import { BusinessRulesLibraryTable } from './BusinessRulesLibraryTable';
import { BrowserRouter } from 'react-router-dom';
import { Rule } from 'apps/page-builder/generated';

describe('BusinessRulesLibraryTable', () => {
    const modalRef = { current: null };

    describe('when rendered', () => {
        it('should display sentence cased headers', async () => {
            const rulesSummary: any = {
                id: 6376,
                template: 1000272,
                ruleFunction: 'Enable',
                ruleDescription: null,
                sourceIdentifier: 'ARB001',
                sourceValue: ['Dengue virus'],
                sourceQuestion: { label: 'Type of Arbovirus' },
                comparator: '=',
                targetType: 'QUESTION',
                errorMsgText: 'Type of Arbovirus = must be ( Dengue virus ) Dengue (DENV) Serotype',
                targetValueIdentifier: [{ label: '404400' }]
            };
            const summaries = [rulesSummary];
            const pages = {
                totalElements: 1
            };

            render(
                <BrowserRouter>
                    <BusinessRulesLibraryTable summaries={summaries} qtnModalRef={modalRef} pages={pages} />
                </BrowserRouter>
            );

            const tableHeads = await screen.findAllByRole('columnheader');
            expect(tableHeads[0]).toHaveTextContent('Source Field');
            expect(tableHeads[1]).toHaveTextContent('Logic');
            expect(tableHeads[2]).toHaveTextContent('Values');
            expect(tableHeads[3]).toHaveTextContent('Function');
            expect(tableHeads[4]).toHaveTextContent('Target Fields');
            expect(tableHeads[5]).toHaveTextContent('ID');
        });
    });

    describe('when at least one summary is available', () => {
        it('should display the Business rules summaries', async () => {
            const rulesSummary = {
                id: 6376,
                template: 1000272,
                ruleFunction: 'Enable' as Rule.ruleFunction,
                ruleDescription: 'test name',
                sourceIdentifier: 'ARB001',
                sourceValues: ['Dengue virus'],
                sourceQuestion: { label: 'test name' },
                comparator: '=' as Rule.comparator,
                targetType: 'QUESTION' as Rule.targetType,
                errorMsgText: 'Type of Arbovirus = must be ( Dengue virus ) Dengue (DENV) Serotype',
                targets: [{ label: '404400' }],
                anySourceValue: false
            };

            const summaries = [rulesSummary];
            const pages = {
                totalElements: 1
            };

            const { findAllByRole } = render(
                <BrowserRouter>
                    <BusinessRulesLibraryTable summaries={summaries} qtnModalRef={modalRef} pages={pages} />
                </BrowserRouter>
            );

            const tableData = await findAllByRole('cell');
            expect(tableData[0]).toHaveTextContent('test name');
            expect(tableData[1]).toHaveTextContent('Equal to');
            expect(tableData[2]).toHaveTextContent('Dengue virus');
            expect(tableData[3]).toHaveTextContent('Enable');
            expect(tableData[4]).toHaveTextContent('404400');
            expect(tableData[5]).toHaveTextContent('6376');
        });
    });
});
