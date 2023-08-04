package gov.cdc.nbs.questionbank.page.request;

import java.util.Set;

public record PageCreateRequest(
		Long eventTypeId,
		Set<String> conditionIds,
		String name,
		Long templateId,
	    String messageMappingGuide, // check type
		String pageDescription,
		String dataMartName
		
		) {
}
