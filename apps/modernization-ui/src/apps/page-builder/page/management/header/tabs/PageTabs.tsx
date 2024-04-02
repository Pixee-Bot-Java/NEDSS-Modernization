import { TabGroup } from 'apps/page-builder/components/TabGroup/TabGroup';
import { PagesTab } from 'apps/page-builder/generated';
import { ManageTabs } from '../../edit/tabs/ManageTabs/ManageTabs';
import { usePageManagement } from '../../usePageManagement';
import styles from './page-tabs.module.scss';
import { useEffect, useState } from 'react';

const contactRecordTab: PagesTab = {
    id: -1,
    name: 'Contact Records',
    sections: [],
    visible: true,
    order: -1
};

const supplementalInfoTab: PagesTab = {
    id: -2,
    name: 'Supplemental Info',
    sections: [],
    visible: true,
    order: -1
};

type Props = {
    pageId: number;
    tabs: PagesTab[];
    onAddSuccess?: () => void;
};
export const PageTabs = ({ pageId, tabs, onAddSuccess }: Props) => {
    const { selected, select, selectStaticTab } = usePageManagement();
    const [displayedTabs, setDisplayedTabs] = useState<PagesTab[]>([]);

    useEffect(() => {
        if (!onAddSuccess) {
            setDisplayedTabs([...tabs, contactRecordTab, supplementalInfoTab]);
        } else {
            setDisplayedTabs(tabs);
        }
    }, [JSON.stringify(tabs), onAddSuccess]);

    const handleSelectionChanged = (id: string | number) => {
        const tab = tabs.find((t) => t.id === id);
        if (tab) {
            select(tab);
        } else {
            // Display static tab content
            switch (id) {
                case -1:
                    selectStaticTab('contactRecord');
                    break;
                case -2:
                    selectStaticTab('supplementalInfo');
                    break;
            }
        }
    };

    return (
        <div className={styles.pageTabs}>
            <TabGroup tabs={displayedTabs} onSelected={handleSelectionChanged} initialSelection={selected?.id} />
            {onAddSuccess ? <ManageTabs pageId={pageId} tabs={tabs} onAddSuccess={onAddSuccess} /> : null}
        </div>
    );
};
