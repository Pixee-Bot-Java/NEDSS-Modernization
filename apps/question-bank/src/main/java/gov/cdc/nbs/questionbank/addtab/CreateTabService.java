package gov.cdc.nbs.questionbank.addtab;

import gov.cdc.nbs.questionbank.addtab.exceptions.AddTabException;
import gov.cdc.nbs.questionbank.addtab.repository.WaUiMetaDataRepository;
import gov.cdc.nbs.questionbank.entity.addtab.WaUiMetadata;
import gov.cdc.nbs.questionbank.addtab.model.CreateTabRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class CreateTabService implements CreateTabInterface{

    @Autowired
    private WaUiMetaDataRepository waUiMetaDataRepository;

    private Long getCurrentHighestOrderNumber(Long waTemplateId) {
        Long maxOrderNumber = waUiMetaDataRepository.findMaxOrderNumberByTemplateUid(waTemplateId);
        return (maxOrderNumber != null) ? maxOrderNumber : 0L;
    }

    @Override
    public CreateUiResponse createTab(Long userId, CreateTabRequest request) throws AddTabException {
        try {
            WaUiMetadata waUiMetadata = createWaUiMetadata(userId, request);
            log.info("Saving Rule to DB");
            waUiMetaDataRepository.save(waUiMetadata);
            return new CreateUiResponse(waUiMetadata.getId(), "Tab Created Successfully");
        } catch(Exception exception) {
            throw new AddTabException("Add tab exception", 1010);
        }

    }
    private WaUiMetadata createWaUiMetadata(Long uid, CreateTabRequest request ) {
        WaUiMetadata waUiMetadata = new WaUiMetadata();
        waUiMetadata.setAddUserId(uid);
        waUiMetadata.setNbsUiComponentUid(1010L);
        waUiMetadata.getNbsUiComponentUid();
        waUiMetadata.setWaTemplateUid(request.page());
        Long waTemplateUid = waUiMetadata.getWaTemplateUid();
        Long nextOrderNumber = getCurrentHighestOrderNumber(waTemplateUid) + 1;


        waUiMetadata.setDisplayInd(request.visible());
        waUiMetadata.setOrderNbr(Math.toIntExact(nextOrderNumber));
        waUiMetadata.setRequiredInd("F");
        waUiMetadata.setAddUserId(uid);
        waUiMetadata.setAddTime(Instant.now());
        waUiMetadata.setLastChgTime(Instant.now());
        waUiMetadata.setLastChgUserId(uid);
        waUiMetadata.setRecordStatusCd("Active");
        waUiMetadata.setRecordStatusTime(Instant.now());
        waUiMetadata.setVersionCtrlNbr(1);
        waUiMetadata.setStandardNndIndCd("F");
        waUiMetadata.setLocalId("NBS_1_14");
        waUiMetadata.setQuestionIdentifier("NBS_UI_4");
        waUiMetadata.setCoinfectionIndCd("F");
        waUiMetadata.setFutureDateIndCd("F");
        waUiMetadata.setStandardQuestionIndCd("F");
        waUiMetadata.setPublishIndCd("T");
        waUiMetadata.setQuestionLabel(request.name());
        waUiMetadata.setEnableInd("T");

        return waUiMetadata;

    }
}