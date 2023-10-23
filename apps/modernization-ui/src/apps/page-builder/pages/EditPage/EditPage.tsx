import { EditPageHeader } from 'apps/page-builder/components/EditPageHeader/EditPageHeader';
import { EditPageTabs } from 'apps/page-builder/components/EditPageTabs/EditPageTabs';
import { PageBuilder } from '../PageBuilder/PageBuilder';
import { useParams } from 'react-router-dom';
import { useContext, useEffect, useRef, useState } from 'react';
import './EditPage.scss';
import { PagesBreadcrumb } from 'apps/page-builder/components/PagesBreadcrumb/PagesBreadcrumb';
import { EditPageContentComponent } from 'apps/page-builder/components/EditPageContent/EditPageContent';
import { EditPageSidebar } from 'apps/page-builder/components/EditPageSidebar/EditPageSidebar';
import { fetchPageDetails, savePageAsDraft } from 'apps/page-builder/services/pagesAPI';
import { UserContext } from 'user';
import { PagesResponse } from 'apps/page-builder/generated';
import AddSectionModal from 'apps/page-builder/components/AddSection/AddSectionModal';
import { ModalRef } from '@trussworks/react-uswds';
import { Spinner } from 'components/Spinner/Spinner';
import { AlertBanner } from 'apps/page-builder/components/AlertBanner/AlertBanner';
import { ReorderModal } from './ReorderModal/ReorderModal';

export const EditPage = () => {
    const { pageId } = useParams();
    const { state } = useContext(UserContext);
    const token = `Bearer ${state.getToken()}`;
    const [page, setPage] = useState<PagesResponse>();
    const [active, setActive] = useState(0);
    const addSectionModalRef = useRef<ModalRef>(null);
    const reorderModalRef = useRef<ModalRef>(null);
    const [alertType, setAlertType] = useState<string>('');
    const [alertMessage, setAlertMessage] = useState<string | null>(null);

    useEffect(() => {
        if (pageId) {
            fetchPageDetails(token, Number(pageId)).then((data) => {
                setPage(data);
            });
        }
    }, [pageId]);

    const handleAddSuccess = () => {
        if (pageId) {
            fetchPageDetails(token, Number(pageId)).then(setPage);
        }
    };

    const handleSaveDraft = () => {
        savePageAsDraft(token, Number(pageId))
            .then((response) => {
                console.log(response);
                setAlertMessage('Page successfully saved as Draft');
                setAlertType('success');
            })
            .catch((error) => {
                setAlertMessage(error.body.message);
                setAlertType('error');
            });
    };

    return (
        <PageBuilder page="edit-page">
            {page ? (
                <div className="edit-page">
                    <PagesBreadcrumb currentPage={page.name} />
                    <div className="edit-page__header">
                        <EditPageHeader page={page} handleSaveDraft={handleSaveDraft} />
                        {page.tabs ? (
                            <EditPageTabs
                                tabs={page.tabs}
                                active={active}
                                setActive={setActive}
                                onAddSuccess={handleAddSuccess}
                            />
                        ) : null}
                    </div>
                    <div className="edit-page__container">
                        <div className="edit-page__content">
                            {alertMessage ? <AlertBanner type={alertType}>{alertMessage}</AlertBanner> : null}

                            {page.tabs?.[active] ? (
                                <EditPageContentComponent content={page.tabs[active]} onAddSection={handleAddSuccess} />
                            ) : null}

                            <EditPageSidebar
                                addSectionModalRef={addSectionModalRef}
                                reorderModalRef={reorderModalRef}
                            />
                        </div>
                    </div>
                </div>
            ) : (
                <Spinner />
            )}
            {page && pageId && page.tabs?.[active].id ? (
                <AddSectionModal
                    modalRef={addSectionModalRef}
                    pageId={pageId}
                    tabId={page.tabs[active]?.id}
                    onAddSection={handleAddSuccess}
                />
            ) : null}
            {page && page.name && page.tabs?.[active] ? (
                <ReorderModal modalRef={reorderModalRef} pageName={page.name} content={page.tabs[active]} />
            ) : null}
        </PageBuilder>
    );
};
