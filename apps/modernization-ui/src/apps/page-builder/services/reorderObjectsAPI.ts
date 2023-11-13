import { ReorderControllerService } from '../generated';

export const reorderObjects = (token: string, after: number, component: number, page: number): Promise<any> => {
    return ReorderControllerService.orderComponentAfterUsingPut({
        authorization: token,
        after: after,
        component: component,
        page: page
    })
        .then((response) => {
            return response;
        })
        .catch((error) => {
            console.log('ERR', error);
        });
};
