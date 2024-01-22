/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { DataMartInfo } from './DataMartInfo';
import type { MessagingInfo } from './MessagingInfo';
import type { Question } from './Question';

export type TextQuestion = (Question & {
    adminComments?: string;
    codeSet?: string;
    dataMartInfo?: DataMartInfo;
    defaultValue?: string;
    description?: string;
    displayControl?: number;
    fieldSize?: string;
    id?: number;
    label?: string;
    mask?: string;
    messagingInfo?: MessagingInfo;
    status?: string;
    subgroup?: string;
    tooltip?: string;
    type?: string;
    uniqueId?: string;
    uniqueName?: string;
});

