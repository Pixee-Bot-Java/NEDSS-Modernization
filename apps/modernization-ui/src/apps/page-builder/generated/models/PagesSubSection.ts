/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { PagesQuestion } from './PagesQuestion';

export type PagesSubSection = {
    id: number;
    isGroupable: boolean;
    isGrouped: boolean;
    name: string;
    order: number;
    questions: Array<PagesQuestion>;
    visible: boolean;
};

