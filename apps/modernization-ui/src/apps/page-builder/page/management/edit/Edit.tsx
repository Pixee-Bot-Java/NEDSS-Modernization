import {
    PageHeader,
    PageManagementLayout,
    PageManagementMenu,
    PageManagementProvider,
    useGetPageDetails,
    usePageManagement
} from 'apps/page-builder/page/management';
import { Loading } from 'components/Spinner';
import { NavLinkButton } from 'components/button/nav/NavLinkButton';
import { PageContent } from './content/PageContent';
import { ManageSectionModal } from './section/manage/ManageSectionModal';
import { ModalRef } from '@trussworks/react-uswds';
import { useRef } from 'react';

export const Edit = () => {
    const { page, fetch, refresh } = useGetPageDetails();

    const manageSectionModalRef = useRef<ModalRef>(null);
    const addSectionModalRef = useRef<ModalRef>(null);

    const handleManageSection = () => {
        manageSectionModalRef.current?.toggleModal(undefined, true);
    };

    const handleAddSection = () => {
        addSectionModalRef.current?.toggleModal(undefined, true);
    };

    return (
        <>
            {page ? (
                <PageManagementProvider page={page} fetch={fetch}>
                    <ManageSectionModal
                        refresh={() => page && refresh(page)}
                        addSecModalRef={addSectionModalRef}
                        manageSecModalRef={manageSectionModalRef}
                    />
                    <EditPageContent
                        handleManageSection={handleManageSection}
                        handleAddSection={handleAddSection}
                        refresh={() => page && refresh(page)}
                    />
                </PageManagementProvider>
            ) : (
                <Loading center />
            )}
        </>
    );
};

type EditPageContentProps = {
    handleManageSection?: () => void;
    handleAddSection?: () => void;
    refresh: () => void;
};

const EditPageContent = ({ handleManageSection, handleAddSection, refresh }: EditPageContentProps) => {
    const { page, selected } = usePageManagement();

    return (
        <PageManagementLayout name={page.name} mode={'edit'}>
            <PageHeader page={page} tabs={page.tabs ?? []} selected={selected} onAddTabSuccess={refresh}>
                <PageManagementMenu>
                    <NavLinkButton to={`/page-builder/pages/${page.id}/business-rules`} type="outline">
                        Business rules
                    </NavLinkButton>
                    <NavLinkButton to={'..'}>Preview</NavLinkButton>
                </PageManagementMenu>
            </PageHeader>
            {selected && (
                <PageContent
                    tab={selected}
                    refresh={refresh}
                    handleManageSection={handleManageSection}
                    handleAddSection={handleAddSection}
                />
            )}
        </PageManagementLayout>
    );
};
