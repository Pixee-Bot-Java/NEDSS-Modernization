import { PagesTab } from 'apps/page-builder/generated';
import { Sections } from '../section/Sections';
import { PageSideMenu } from './PageSideMenu';
import styles from './page-content.module.scss';

type Props = {
    tab: PagesTab;
};
export const PageContent = ({ tab }: Props) => {
    const handleAddSubsection = (section: number) => {
        console.log('add subsection not yet implemented', section);
    };

    return (
        <div className={styles.pageContent}>
            <div className={styles.invisible} />
            <Sections sections={tab.sections ?? []} onAddSubsection={handleAddSubsection} />
            <PageSideMenu />
        </div>
    );
};