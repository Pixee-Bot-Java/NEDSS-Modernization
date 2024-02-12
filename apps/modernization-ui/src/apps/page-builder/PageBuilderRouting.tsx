import { FeatureGuard, FeatureLayout } from 'feature';
import { Navigate, RouteObject } from 'react-router-dom';
import PageBuilderContextProvider from './context/PageBuilderContext';
import { PageLibrary } from './page/library/PageLibrary';
import { Edit } from './page/management/edit/Edit';
import { PreviewPage } from './page/management/preview';
import { AddNewPage } from './pages/AddNewPage/AddNewPage';
import { BusinessRulesLibrary } from './pages/BusinessRulesLibrary/BusinessRulesLibrary';
import { ValuesetLibrary } from './pages/ValuesetLibrary/ValuesetLibrary';
import AddBusinessRule from './pages/BusinessRulesLibrary/Add/AddBusinessRule';

const routing: RouteObject[] = [
    {
        path: '/page-builder',
        element: (
            <FeatureGuard guard={(features) => features.pageBuilder.enabled}>
                <PageBuilderContextProvider />
            </FeatureGuard>
        ),
        children: [
            {
                index: true,
                element: <Navigate to="pages" />
            },
            {
                path: 'pages',
                children: [
                    {
                        index: true,
                        element: (
                            <FeatureGuard guard={(features) => features.pageBuilder.page.library.enabled}>
                                <PageLibrary />
                            </FeatureGuard>
                        )
                    },
                    {
                        path: 'add',
                        element: (
                            <FeatureGuard guard={(features) => features.pageBuilder.page.management.create.enabled}>
                                <AddNewPage />
                            </FeatureGuard>
                        )
                    },
                    {
                        path: ':pageId',
                        element: (
                            <FeatureLayout guard={(features) => features.pageBuilder.page.management.edit.enabled} />
                        ),
                        children: [
                            {
                                index: true,
                                element: <PreviewPage />
                            },
                            {
                                path: 'edit',
                                element: <Edit />
                            },
                            {
                                path: 'business-rules',
                                children: [
                                    {
                                        index: true,
                                        element: <BusinessRulesLibrary />
                                    },
                                    {
                                        path: 'add',
                                        element: <AddBusinessRule />
                                    }
                                ]
                            },
                            {
                                path: ':ruleId',
                                element: <AddBusinessRule />
                            }
                        ]
                    }
                ]
            },
            {
                path: 'valueset-library',
                element: <ValuesetLibrary />
            }
        ]
    }
];

export { routing };