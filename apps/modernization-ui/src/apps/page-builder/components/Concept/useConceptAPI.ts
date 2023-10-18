import { ValueSetControllerService } from '../../generated';

export const useConceptPI = (authorization: string, codeSetNm: string, sort: string) => {
    return ValueSetControllerService.findConceptsByCodeSetNameUsingGet({
        authorization,
        codeSetNm: codeSetNm || 'CONDITION_FAMILY'
    }).then((response: any) => {
        return response || [];
    });
};
