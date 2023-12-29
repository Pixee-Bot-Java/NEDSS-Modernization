/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CreateRuleRequest } from '../models/CreateRuleRequest';
import type { CreateRuleResponse } from '../models/CreateRuleResponse';
import type { Page_ViewRuleResponse_ } from '../models/Page_ViewRuleResponse_';
import type { SearchPageRuleRequest } from '../models/SearchPageRuleRequest';
import type { ViewRuleResponse } from '../models/ViewRuleResponse';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class PageRuleControllerService {
    /**
     * getAllPageRule
     * @returns Page_ViewRuleResponse_ OK
     * @throws ApiError
     */
    public static getAllPageRuleUsingGet({
        authorization,
        id,
        page,
        size,
        sort
    }: {
        authorization: string;
        /**
         * id
         */
        id: number;
        page?: number;
        size?: number;
        sort?: string;
    }): CancelablePromise<Page_ViewRuleResponse_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/{id}/rules',
            path: {
                id: id
            },
            headers: {
                Authorization: authorization
            },
            query: {
                page: page,
                size: size,
                sort: sort
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`
            }
        });
    }

    /**
     * createBusinessRule
     * @returns CreateRuleResponse Created
     * @throws ApiError
     */
    public static createBusinessRuleUsingPost({
        authorization,
        page,
        request
    }: {
        authorization: string;
        /**
         * page
         */
        page: number;
        /**
         * request
         */
        request: CreateRuleRequest;
    }): CancelablePromise<CreateRuleResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/pages/{id}/rules',
            path: {
                page: page
            },
            headers: {
                Authorization: authorization
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`
            }
        });
    }

    /**
     * findPageRule
     * @returns Page_ViewRuleResponse_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static findPageRuleUsingPost({
        authorization,
        request,
        page,
        size,
        sort
    }: {
        authorization: string;
        /**
         * request
         */
        request: SearchPageRuleRequest;
        page?: number;
        size?: number;
        sort?: string;
    }): CancelablePromise<Page_ViewRuleResponse_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/pages/{id}/rules/search',
            headers: {
                Authorization: authorization
            },
            query: {
                page: page,
                size: size,
                sort: sort
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`
            }
        });
    }

    /**
     * viewRuleResponse
     * @returns ViewRuleResponse OK
     * @throws ApiError
     */
    public static viewRuleResponseUsingGet({
        authorization,
        ruleId
    }: {
        authorization: string;
        /**
         * ruleId
         */
        ruleId: number;
    }): CancelablePromise<ViewRuleResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/{id}/rules/{ruleId}',
            path: {
                ruleId: ruleId
            },
            headers: {
                Authorization: authorization
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`
            }
        });
    }

    /**
     * updatePageRule
     * @returns CreateRuleResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageRuleUsingPut({
        authorization,
        page,
        request,
        ruleId
    }: {
        authorization: string;
        /**
         * page
         */
        page: number;
        /**
         * request
         */
        request: CreateRuleRequest;
        /**
         * ruleId
         */
        ruleId: number;
    }): CancelablePromise<CreateRuleResponse | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{id}/rules/{ruleId}',
            path: {
                page: page,
                ruleId: ruleId
            },
            headers: {
                Authorization: authorization
            },
            body: request,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`
            }
        });
    }

    /**
     * deletePageRule
     * @returns any OK
     * @throws ApiError
     */
    public static deletePageRuleUsingDelete({
        authorization,
        pageId,
        ruleId
    }: {
        authorization: string;
        /**
         * page
         */

        pageId: string;
        /**
         * ruleId
         */
        ruleId: number;
    }): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: `/nbs/page-builder/api/v1/pages/${pageId}/rules/${ruleId}`,
            path: {
                page: pageId,
                ruleId: ruleId
            },
            headers: {
                Authorization: authorization
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`
            }
        });
    }
}
