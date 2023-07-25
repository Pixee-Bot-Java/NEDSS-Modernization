/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { CreateQuestionRequest } from './CreateQuestionRequest';
import type { MessagingInfo } from './MessagingInfo';
import type { ReportingInfo } from './ReportingInfo';

export type Numeric = (CreateQuestionRequest & {
    adminComments?: string;
    codeSet?: string;
    dataMartInfo?: ReportingInfo;
    defaultValue?: string;
    description?: string;
    displayControl?: number;
    fieldLength?: string;
    label?: string;
    mask?: string;
    maxValue?: number;
    messagingInfo?: MessagingInfo;
    minValue?: number;
    subgroup?: string;
    tooltip?: string;
    type?: Numeric.type;
    uniqueId?: string;
    uniqueName?: string;
    unitTypeCd?: Numeric.unitTypeCd;
    unitValue?: string;
});

export namespace Numeric {

    export enum type {
        CODED = 'CODED',
        DATE = 'DATE',
        NUMERIC = 'NUMERIC',
        TEXT = 'TEXT',
    }

    export enum unitTypeCd {
        CODED = 'CODED',
        LITERAL = 'LITERAL',
    }


}
