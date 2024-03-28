/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Option } from '../models/Option';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ProviderOptionsService {
    /**
     * NBS Provider Option Autocomplete
     * Provides options from Providers that have a name matching a criteria.
     * @returns Option OK
     * @throws ApiError
     */
    public static providerAutocomplete({
        criteria,
        limit = 15,
    }: {
        criteria: string,
        limit?: number,
    }): CancelablePromise<Array<Option>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/nbs/api/options/providers/search',
            query: {
                'criteria': criteria,
                'limit': limit,
            },
        });
    }
}
