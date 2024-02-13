/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AddQuestionRequest } from '../models/AddQuestionRequest';
import type { AddQuestionResponse } from '../models/AddQuestionResponse';
import type { PagesQuestion } from '../models/PagesQuestion';
import type { UpdatePageQuestionRequest } from '../models/UpdatePageQuestionRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class PageQuestionControllerService {

    /**
     * updatePageQuestion
     * @returns PagesQuestion OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageQuestionUsingPut({
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
        request: UpdatePageQuestionRequest,
    }): CancelablePromise<PagesQuestion | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{page}/questions/{questionId}',
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
