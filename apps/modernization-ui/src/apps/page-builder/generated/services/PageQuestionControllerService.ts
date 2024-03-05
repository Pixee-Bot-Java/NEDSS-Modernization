/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AddQuestionRequest } from '../models/AddQuestionRequest';
import type { AddQuestionResponse } from '../models/AddQuestionResponse';
import type { EditableQuestion } from '../models/EditableQuestion';
import type { UpdatePageCodedQuestionRequest } from '../models/UpdatePageCodedQuestionRequest';
import type { UpdatePageCodedQuestionValuesetRequest } from '../models/UpdatePageCodedQuestionValuesetRequest';
import type { UpdatePageDateQuestionRequest } from '../models/UpdatePageDateQuestionRequest';
import type { UpdatePageNumericQuestionRequest } from '../models/UpdatePageNumericQuestionRequest';
import type { UpdatePageQuestionRequiredRequest } from '../models/UpdatePageQuestionRequiredRequest';
import type { UpdatePageTextQuestionRequest } from '../models/UpdatePageTextQuestionRequest';
import type { ValidationResponse } from '../models/ValidationResponse';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class PageQuestionControllerService {

    /**
     * updatePageCodedQuestion
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageCodedQuestionUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageCodedQuestionRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/coded/{questionId}',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updatePageCodedQuestionValueset
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageCodedQuestionValuesetUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageCodedQuestionValuesetRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/coded/{questionId}/valueset',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updatePageDateQuestion
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageDateQuestionUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageDateQuestionRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/date/{questionId}',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updatePageNumericQuestion
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageNumericQuestionUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageNumericQuestionRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/numeric/{questionId}',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updatePageTextQuestion
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageTextQuestionUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageTextQuestionRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/text/{questionId}',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteQuestion
     * @returns any OK
     * @throws ApiError
     */
    public static deleteQuestionUsingDelete({
        authorization,
        page,
        questionId,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
    }): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/{questionId}',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
            },
        });
    }

    /**
     * validateDatamart
     * @returns ValidationResponse OK
     * @throws ApiError
     */
    public static validateDatamartUsingGet({
        authorization,
        datamart,
        page,
        questionId,
    }: {
        authorization: string,
        /**
         * datamart
         */
        datamart: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
    }): CancelablePromise<ValidationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/{questionId}/datamart/validate',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            query: {
                'datamart': datamart,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getEditableQuestion
     * @returns EditableQuestion OK
     * @throws ApiError
     */
    public static getEditableQuestionUsingGet({
        authorization,
        page,
        questionId,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
    }): CancelablePromise<EditableQuestion> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/{questionId}/edit',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updatePageQuestionRequired
     * @returns EditableQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageQuestionRequiredUsingPut({
        authorization,
        page,
        questionId,
        request,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * questionId
         */
        questionId: number,
        /**
         * request
         */
        request: UpdatePageQuestionRequiredRequest,
    }): CancelablePromise<EditableQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/{questionId}/required',
            path: {
                'page': page,
                'questionId': questionId,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * addQuestionToPage
     * @returns AddQuestionResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static addQuestionToPageUsingPost({
        authorization,
        page,
        request,
        subsection,
    }: {
        authorization: string,
        /**
         * page
         */
        page: number,
        /**
         * request
         */
        request: AddQuestionRequest,
        /**
         * subsection
         */
        subsection: number,
    }): CancelablePromise<AddQuestionResponse | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/pages/{page}/subsection/{subsection}/questions',
            path: {
                'page': page,
                'subsection': subsection,
            },
            headers: {
                'Authorization': authorization,
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}
