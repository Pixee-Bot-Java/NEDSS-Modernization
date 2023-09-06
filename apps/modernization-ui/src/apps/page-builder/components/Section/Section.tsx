import { Icon, ModalRef, ModalToggleButton } from '@trussworks/react-uswds';
import { Counter } from '../Counter/Counter';
import './Section.scss';
import { useRef, useState } from 'react';
import { Subsection as SubsectionProps } from 'apps/page-builder/generated/models/Subsection';
import { SubsectionComponent as Subsection } from '../Subsection/Subsection';
import { Section as SectionProps } from 'apps/page-builder/generated/models/Section';
import AddSectionModal from '../AddSection/AddSectionModal';
import { useParams } from 'react-router-dom';

export const SectionComponent = ({ section, onAddSection }: { section: SectionProps; onAddSection: () => void }) => {
    const [open, setOpen] = useState(true);
    const { pageId } = useParams();
    const addSectionModalRef = useRef<ModalRef>(null);

    return (
        <>
            <div className="section">
                <div className="section__header">
                    <div className="section__header--left">
                        <h2>{section.name}</h2>
                        <Counter count={section.sectionSubSections.length} />
                    </div>
                    <div className="section__header--right">
                        <ModalToggleButton type="button" outline modalRef={addSectionModalRef} opener>
                            Add subsection
                        </ModalToggleButton>
                        <Icon.MoreVert size={4} />
                        {open ? (
                            <Icon.ExpandLess size={4} onClick={() => setOpen(!open)} />
                        ) : (
                            <Icon.ExpandMore size={4} onClick={() => setOpen(!open)} />
                        )}
                    </div>
                </div>
                {open ? (
                    <div className="section__body">
                        {section.sectionSubSections.map((subsection: SubsectionProps, i: number) => {
                            return <Subsection key={i} subsection={subsection} />;
                        })}
                    </div>
                ) : null}
            </div>
            {pageId && section ? (
                <AddSectionModal
                    isSubSection={true}
                    modalRef={addSectionModalRef}
                    pageId={pageId}
                    sectionId={section.id}
                    onAddSection={onAddSection}
                />
            ) : null}
        </>
    );
};
