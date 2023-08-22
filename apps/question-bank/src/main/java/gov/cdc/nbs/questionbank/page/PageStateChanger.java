package gov.cdc.nbs.questionbank.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.cdc.nbs.questionbank.entity.WaTemplate;
import gov.cdc.nbs.questionbank.entity.WaUiMetadata;
import gov.cdc.nbs.questionbank.entity.repository.WaTemplateRepository;
import gov.cdc.nbs.questionbank.entity.repository.WaUiMetadataRepository;
import gov.cdc.nbs.questionbank.page.exception.PageUpdateException;
import gov.cdc.nbs.questionbank.page.response.PageStateResponse;
import gov.cdc.nbs.questionbank.page.util.PageConstants;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PageStateChanger {

    @Autowired
    private WaTemplateRepository templateRepository;

    @Autowired
    private WaUiMetadataRepository waUiMetadataRepository;

    public PageStateResponse savePageAsDraft(Long id) {
        PageStateResponse response = new PageStateResponse();
        try {
            Optional<WaTemplate> result = templateRepository.findById(id);
            if (result.isPresent()) {
                WaTemplate page = result.get();
                WaTemplate draftPage = createDraftCopy(page);
                page.setTemplateType("Published With Draft");
                templateRepository.save(page);
                templateRepository.save(draftPage);
                List<WaUiMetadata> draftMappings = copyWaTemplateUIMetaData(page, draftPage);
                waUiMetadataRepository.saveAll(draftMappings);

                response.setMessage(PageConstants.SAVE_DRAFT_SUCCESS);
                response.setTemplateId(page.getId());
            } else {
                throw new PageUpdateException(PageConstants.SAVE_DRAFT_FAIL);
            }
        } catch (PageUpdateException e) {
            throw e;
        } catch (Exception e) {
            throw new PageUpdateException(PageConstants.SAVE_DRAFT_FAIL);
        }
        return response;
    }

    public List<WaUiMetadata> copyWaTemplateUIMetaData(WaTemplate page, WaTemplate clonePage) {
        List<WaUiMetadata> draftMappings = new ArrayList<>();
        List<WaUiMetadata> pages = waUiMetadataRepository.findAllByWaTemplateUid(page);
        for (WaUiMetadata original : pages) {
            WaUiMetadata clone = original;
            clone.setWaTemplateUid(clonePage);
            draftMappings.add(clone);
        }
        return draftMappings;

    }

    public WaTemplate createDraftCopy(WaTemplate oldPage) {
        if (oldPage.getTemplateType().equals("Draft")) {
            return oldPage;
        }

        WaTemplate draftCopy = new WaTemplate();
        draftCopy.setTemplateNm(oldPage.getTemplateNm());
        draftCopy.setTemplateType("Draft");
        draftCopy.setPublishVersionNbr(0);
        draftCopy.setPublishIndCd('F');
        draftCopy.setAddTime(oldPage.getAddTime());
        draftCopy.setAddUserId(oldPage.getAddUserId());
        draftCopy.setBusObjType(oldPage.getBusObjType());
        draftCopy.setConditionCd(oldPage.getConditionCd());
        draftCopy.setConditionMappings(oldPage.getConditionMappings());
        draftCopy.setDatamartNm(oldPage.getDatamartNm());
        draftCopy.setDescTxt(oldPage.getDescTxt());
        draftCopy.setFormCd(oldPage.getFormCd());
        draftCopy.setLastChgTime(oldPage.getLastChgTime());
        draftCopy.setLastChgUserId(oldPage.getLastChgUserId());
        draftCopy.setLocalId(oldPage.getLocalId());
        draftCopy.setNndEntityIdentifier(oldPage.getNndEntityIdentifier());
        draftCopy.setParentTemplateUid(oldPage.getParentTemplateUid());
        draftCopy.setRecordStatusCd(oldPage.getRecordStatusCd());
        draftCopy.setRecordStatusTime(oldPage.getRecordStatusTime());
        draftCopy.setSourceNm(oldPage.getSourceNm());
        draftCopy.setVersionNote(oldPage.getVersionNote());
        draftCopy.setXmlPayload(oldPage.getXmlPayload());


        return draftCopy;

    }


}