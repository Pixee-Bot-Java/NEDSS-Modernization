package gov.cdc.nbs.questionbank.page.content.tab;

import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import gov.cdc.nbs.questionbank.entity.WaUiMetadata;
import gov.cdc.nbs.questionbank.entity.repository.WaTemplateRepository;
import gov.cdc.nbs.questionbank.page.command.PageContentCommand;
import gov.cdc.nbs.questionbank.page.content.tab.exceptions.UpdateTabException;
import gov.cdc.nbs.questionbank.page.content.tab.repository.WaUiMetaDataRepository;
import gov.cdc.nbs.questionbank.page.content.tab.request.UpdateTabRequest;
import gov.cdc.nbs.questionbank.page.content.tab.response.Tab;

@Component
@Transactional
public class TabUpdater {

    private final WaUiMetaDataRepository repository;
    private final WaTemplateRepository templateRepository;

    public TabUpdater(
            final WaUiMetaDataRepository repository,
            final WaTemplateRepository templateRepository) {
        this.repository = repository;
        this.templateRepository = templateRepository;
    }

    public Tab update(Long page, Long tab, UpdateTabRequest request, long user) {
        if (!templateRepository.isPageDraft(page)) {
            throw new UpdateTabException("Unable to update a published page");
        }
        if (request == null || request.label() == null) {
            throw new UpdateTabException("Label is a required field");
        }

        WaUiMetadata metadata = repository.findById(tab)
                .orElseThrow(() -> new UpdateTabException("Failed to find tab"));

        metadata.update(asCommand(request.label(), request.visible(), user));
        metadata = repository.save(metadata);

        return new Tab(
                metadata.getId(),
                metadata.getQuestionLabel(),
                metadata.getDisplayInd().equals("T"));
    }

    private PageContentCommand.UpdateTab asCommand(String label, boolean visible, long user) {
        return new PageContentCommand.UpdateTab(label, visible, user, Instant.now());
    }
}