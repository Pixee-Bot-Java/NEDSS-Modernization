package gov.cdc.nbs.questionbank.page.content.section;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.EntityManager;

import gov.cdc.nbs.questionbank.page.content.section.exception.DeleteSectionException;
import gov.cdc.nbs.questionbank.page.content.section.exception.UpdateSectionException;
import gov.cdc.nbs.questionbank.page.content.section.request.UpdateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.response.DeleteSectionResponse;
import gov.cdc.nbs.questionbank.page.content.section.response.UpdateSectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import gov.cdc.nbs.questionbank.entity.WaTemplate;
import gov.cdc.nbs.questionbank.entity.WaUiMetadata;
import gov.cdc.nbs.questionbank.page.content.section.exception.AddSectionException;
import gov.cdc.nbs.questionbank.page.content.section.request.CreateSectionRequest;
import gov.cdc.nbs.questionbank.page.content.section.response.CreateSectionResponse;
import gov.cdc.nbs.questionbank.page.content.tab.repository.WaUiMetaDataRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class SectionCreator {

    @Autowired
    private WaUiMetaDataRepository waUiMetaDataRepository;

    private static final String UPDATE_MESSAGE = "Section updated successfully";

    private static final String DELETE_MESSAGE = "Section deleted successfully";

    private static final long TAB = 1010L;
    private static final long SECTION = 1015L;

    @Autowired
    private EntityManager entityManager;

    public CreateSectionResponse createSection(long pageId, Long userId, CreateSectionRequest request) {
        try {
            WaUiMetadata waUiMetadata = createWaUiMetadata(pageId, userId, request);
            log.info("Updating Wa_UI_metadata table by adding new section");
            waUiMetaDataRepository.incrementOrderNumbers(waUiMetadata.getOrderNbr(), waUiMetadata.getId());
            waUiMetaDataRepository.save(waUiMetadata);
            return new CreateSectionResponse(waUiMetadata.getId(), "Section Created Successfully");
        } catch (Exception exception) {
            throw new AddSectionException("Add Section exception");
        }

    }

    public DeleteSectionResponse deleteSection(Long pageNumber, Long sectionId) {
        try {
            log.info("Deleting Section");
            Integer orderNbr = waUiMetaDataRepository.getOrderNumber(sectionId);
            Optional<Long> nbsComponentUidOptional =
                    waUiMetaDataRepository.findNextNbsUiComponentUid(orderNbr+1, pageNumber);
            if (nbsComponentUidOptional.isPresent()) {
                Long nbsComponentUid = nbsComponentUidOptional.get();
                if (nbsComponentUid == TAB ||nbsComponentUid == SECTION
                       || nbsComponentUid == null) {
                    waUiMetaDataRepository.deleteById(sectionId);
                    waUiMetaDataRepository.updateOrderNumberByDecreasing(orderNbr, sectionId);
                    return new DeleteSectionResponse(sectionId, DELETE_MESSAGE);
                } else {
                    throw new DeleteSectionException("Conditions not satisfied");
                }
            } else {
                waUiMetaDataRepository.deleteById(sectionId);
                waUiMetaDataRepository.updateOrderNumberByDecreasing(orderNbr, sectionId);
                return new DeleteSectionResponse(sectionId, DELETE_MESSAGE);
            }
        } catch(Exception exception) {
            throw new DeleteSectionException("Delete Section Exception");
        }

    }
    public UpdateSectionResponse updateSection(Long sectionId, UpdateSectionRequest request) {
        try {
            log.info("Updating section");
            if (request.questionLabel() == null || request.visible() == null) {
                throw new UpdateSectionException("Label and visibility fields are required");
            }
            waUiMetaDataRepository.updateQuestionLabelAndVisibility(request.questionLabel(),
                    request.visible(), sectionId);
            return new UpdateSectionResponse(sectionId, UPDATE_MESSAGE);
        } catch(Exception exception) {
            throw new UpdateSectionException("Update Section Exception");
        }

    }


    private WaUiMetadata createWaUiMetadata(long pageId, Long uid, CreateSectionRequest request) {
        WaTemplate page = entityManager.getReference(WaTemplate.class, pageId);
        WaUiMetadata waUiMetadata = new WaUiMetadata();
        waUiMetadata.setAddUserId(uid);
        waUiMetadata.setNbsUiComponentUid(1015L);
        waUiMetadata.getNbsUiComponentUid();
        waUiMetadata.setWaTemplateUid(page);
        Long nextOrderNumber = waUiMetaDataRepository.findOrderNbr(
                request.tabId(),
                pageId,
                1010L);
        if (nextOrderNumber == null) {
            nextOrderNumber = waUiMetaDataRepository.findOrderNbr_2(
                    request.tabId(),
                    pageId,
                    1010L);
        }
        waUiMetadata.setQuestionLabel(request.name());
        waUiMetadata.setOrderNbr(Math.toIntExact(nextOrderNumber));
        waUiMetadata.setDisplayInd(request.visible() ? "T" : "F");
        waUiMetadata.setPublishIndCd('T');
        waUiMetadata.setEnableInd("T");
        waUiMetadata.setRequiredInd("F");
        waUiMetadata.setCoinfectionIndCd('F');
        waUiMetadata.setFutureDateIndCd('F');
        waUiMetadata.setStandardQuestionIndCd('F');
        waUiMetadata.setStandardNndIndCd('F');
        waUiMetadata.setQuestionLabel(request.name());
        waUiMetadata.setAddTime(Instant.now());
        waUiMetadata.setLastChgTime(Instant.now());
        waUiMetadata.setAddUserId(uid);
        waUiMetadata.setRecordStatusCd("Active");
        waUiMetadata.setLastChgUserId(uid);
        waUiMetadata.setVersionCtrlNbr(1);
        waUiMetadata.setRecordStatusTime(Instant.now());
        waUiMetadata.setQuestionIdentifier("NBS_1_15");
        waUiMetadata.setLocalId("NBS_1_15");

        return waUiMetadata;

    }
}
