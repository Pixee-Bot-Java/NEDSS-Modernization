/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { County } from '../models/County';
import type { CreateValuesetRequest } from '../models/CreateValuesetRequest';
import type { Page_ValueSetOption_ } from '../models/Page_ValueSetOption_';
import type { UpdateValueSetRequest } from '../models/UpdateValueSetRequest';
import type { Valueset } from '../models/Valueset';
import type { ValueSetOption } from '../models/ValueSetOption';
import type { ValueSetSearchRequest } from '../models/ValueSetSearchRequest';
import type { ValueSetStateChangeResponse } from '../models/ValueSetStateChangeResponse';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class ValueSetControllerService {

    /**
     * create
     * @returns Valueset OK
     * @returns any Created
     * @throws ApiError
     */
    public static createUsingPost({
        authorization,
        request,
    }: {
        authorization: string,
        /**
         * request
         */
        request: CreateValuesetRequest,
    }): CancelablePromise<Valueset | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/valueset',
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
     * findValueSetOptions
     * @returns ValueSetOption OK
     * @throws ApiError
     */
    public static findValueSetOptionsUsingGet({
        authorization,
    }: {
        authorization: string,
    }): CancelablePromise<Array<ValueSetOption>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/valueset/options',
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
     * searchValueSet
     * @returns Page_ValueSetOption_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static searchValueSetUsingPost({
        authorization,
        request,
        page,
        size,
        sort,
    }: {
        authorization: string,
        /**
         * request
         */
        request: ValueSetSearchRequest,
        page?: number,
        size?: number,
        sort?: string,
    }): CancelablePromise<Page_ValueSetOption_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/nbs/page-builder/api/v1/valueset/options/search',
            headers: {
                'Authorization': authorization,
            },
            query: {
                'page': page,
                'size': size,
                'sort': sort,
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
     * getValueset
     * @returns Valueset OK
     * @throws ApiError
     */
    public static getValuesetUsingGet({
        authorization,
        codeSetNm,
    }: {
        authorization: string,
        /**
         * codeSetNm
         */
        codeSetNm: string,
    }): CancelablePromise<Valueset> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/valueset/{codeSetNm}',
            path: {
                'codeSetNm': codeSetNm,
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
     * updateValueSet
     * @returns Valueset OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateValueSetUsingPut({
        authorization,
        codeSetNm,
        request,
    }: {
        authorization: string,
        /**
         * codeSetNm
         */
        codeSetNm: string,
        /**
         * request
         */
        request: UpdateValueSetRequest,
    }): CancelablePromise<Valueset | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/valueset/{codeSetNm}',
            path: {
                'codeSetNm': codeSetNm,
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
     * activateValueSet
     * @returns ValueSetStateChangeResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static activateValueSetUsingPut({
        authorization,
        codeSetNm,
    }: {
        authorization: string,
        /**
         * codeSetNm
         */
        codeSetNm: string,
    }): CancelablePromise<ValueSetStateChangeResponse | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/valueset/{codeSetNm}/activate',
            path: {
                'codeSetNm': codeSetNm,
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
     * deleteValueSet
     * @returns ValueSetStateChangeResponse OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteValueSetUsingPut({
        authorization,
        codeSetNm,
    }: {
        authorization: string,
        /**
         * codeSetNm
         */
        codeSetNm: string,
    }): CancelablePromise<ValueSetStateChangeResponse | any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/nbs/page-builder/api/v1/valueset/{codeSetNm}/deactivate',
            path: {
                'codeSetNm': codeSetNm,
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
     * findCountyByStateCode
     * @returns County OK
     * @throws ApiError
     */
    public static findCountyByStateCodeUsingGet({
        authorization,
        stateCode,
    }: {
        authorization: string,
        /**
         * stateCode
         */
        stateCode: string,
    }): CancelablePromise<Array<County>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/page-builder/api/v1/valueset/{stateCode}/counties',
            path: {
                'stateCode': stateCode,
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

}
