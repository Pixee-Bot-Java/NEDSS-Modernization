package gov.cdc.nbs.questionbank.phin.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.cdc.nbs.questionbank.phin.client.PhinVadsClient;
import gov.cdc.nbs.questionbank.question.model.Concept;
import gov.cdc.nbs.questionbank.question.model.Include;
import gov.cdc.nbs.questionbank.question.response.PhinvadsValueSetByIDData;
import gov.cdc.nbs.questionbank.question.response.ValueSetByOIDResults;
import gov.cdc.nbs.questionbank.question.response.ValueSetConcept;

@Service
public class VocabSearchService {

    @Autowired
    PhinVadsClient phinVadsClient;

    public ValueSetByOIDResults fetchValueSetInfoByOID(String oid) {
        ValueSetByOIDResults res = null;
        PhinvadsValueSetByIDData result = phinVadsClient.getValueSetByOID(oid);
        if (null != result) {
            List<ValueSetConcept> conceptList = new ArrayList<>();
            Include include = result.getCompose().getInclude().get(1);
            List<Concept> concepts = include.getConcept();
            if (null != concepts && !concepts.isEmpty()) {
                for (Concept c : concepts) {
                    ValueSetConcept vsc = ValueSetConcept.builder().localCode(c.getCode()).uiDisplayName(c.getDisplay())
                            .conceptCode(c.getCode()).messagingConceptName(c.getDisplay())
                            .codeSystemName(include.getSystem()).conceptCode(c.getCode()).status(result.getStatus())
                            .effectiveFrom(result.getDate()).build();
                    conceptList.add(vsc);
                }
            }
            res = ValueSetByOIDResults.builder().valueSetType("PHIN").valueSetCode(result.getName())
                    .valueSetName(result.getTitle()).status(result.getStatus())
                    .valueSetDesc(result.getDescription().replaceAll("</?[^>]*+>", "")).valueSetConcepts(conceptList)
                    .build();
        }
        return res;
    }

}