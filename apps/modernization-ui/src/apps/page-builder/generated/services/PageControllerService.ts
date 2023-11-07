/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Page_PageSummary_ } from '../models/Page_PageSummary_';
import type { PageCreateRequest } from '../models/PageCreateRequest';
import type { PageCreateResponse } from '../models/PageCreateResponse';
import type { PageStateResponse } from '../models/PageStateResponse';
import type { PageSummary } from '../models/PageSummary';
import type { PageSummaryRequest } from '../models/PageSummaryRequest';
import type { Resource } from '../models/Resource';
import type { UpdatePageDetailsRequest } from '../models/UpdatePageDetailsRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class PageControllerService {
    /**
     * getAllPageSummary
     * @returns Page_PageSummary_ OK
     * @throws ApiError
     */
    public static getAllPageSummaryUsingGet({
        authorization,
        page,
        size,
        sort
    }: {
        authorization: any;
        page?: number;
        size?: number;
        sort?: string;
    }): CancelablePromise<Page_PageSummary_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages',
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
     * createPage
     * @returns PageCreateResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static createPageUsingPost({
        authorization,
        request
    }: {
        authorization: any;
        /**
         * request
         */
        request: PageCreateRequest;
    }): CancelablePromise<PageCreateResponse | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/pages',
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
     * downloadPageLibrary
     * @returns Resource OK
     * @throws ApiError
     */
    public static downloadPageLibraryUsingGet({ authorization }: { authorization: any }): CancelablePromise<Resource> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/download',
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
     * downloadPageLibraryPDF
     * @returns string OK
     * @throws ApiError
     */
    public static downloadPageLibraryPdfUsingGet({ authorization }: { authorization: any }): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/pages/downloadPDF',
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
     * search
     * @returns Page_PageSummary_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static searchUsingPost({
        authorization,
        request,
        page,
        size,
        sort
    }: {
        authorization: any;
        /**
         * request
         */
        request: PageSummaryRequest;
        page?: number;
        size?: number;
        sort?: string;
    }): CancelablePromise<Page_PageSummary_> {
        try {
            return __request(OpenAPI, {
                method: 'POST',
                url: '/nbs/page-builder/api/v1/pages/search',
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
        } catch (error: any) {
            throw new Error(error);
        }
    }

    /**
     * deletePageDraft
     * @returns PageStateResponse OK
     * @throws ApiError
     */
    public static deletePageDraftUsingDelete({
        authorization,
        id
    }: {
        authorization: any;
        /**
         * id
         */
        id: number;
    }): CancelablePromise<PageStateResponse> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/nbs/page-builder/api/v1/pages/{id}/delete-draft',
            path: {
                id: id
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

    /**
     * updatePageDetails
     * @returns PageSummary OK
     * @returns any Created
     * @throws ApiError
     */
    public static updatePageDetailsUsingPut({
        authorization,
        id,
        request
    }: {
        authorization: any;
        /**
         * id
         */
        id: number;
        /**
         * request
         */
        request: UpdatePageDetailsRequest;
    }): CancelablePromise<PageSummary | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{id}/details',
            path: {
                id: id
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
     * savePageDraft
     * @returns PageStateResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static savePageDraftUsingPut({
        authorization,
        id
    }: {
        authorization: any;
        /**
         * id
         */
        id: number;
    }): CancelablePromise<PageStateResponse | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/pages/{id}/draft',
            path: {
                id: id
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
}
