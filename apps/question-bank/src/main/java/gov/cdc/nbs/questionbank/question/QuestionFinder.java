package gov.cdc.nbs.questionbank.question;

import java.util.List;


import gov.cdc.nbs.questionbank.question.exception.UniqueQuestionException;
import gov.cdc.nbs.questionbank.question.request.QuestionValidationRequest;
import gov.cdc.nbs.questionbank.question.request.QuestionValidationRequest.FieldName;
import gov.cdc.nbs.questionbank.valueset.ValueSetReader;
import gov.cdc.nbs.questionbank.valueset.response.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import gov.cdc.nbs.questionbank.entity.question.WaQuestion;
import gov.cdc.nbs.questionbank.entity.repository.WaUiMetadataRepository;
import gov.cdc.nbs.questionbank.question.exception.QuestionNotFoundException;
import gov.cdc.nbs.questionbank.question.model.Question;
import gov.cdc.nbs.questionbank.question.repository.WaQuestionRepository;
import gov.cdc.nbs.questionbank.question.request.FindQuestionRequest;
import gov.cdc.nbs.questionbank.question.response.GetQuestionResponse;

@Component
public class QuestionFinder {
    private final WaQuestionRepository questionRepository;
    private final WaUiMetadataRepository uiMetadataRepository;
    private final QuestionMapper questionMapper;
    private final ValueSetReader valueSetReader;

    private static final String SUBGROUP_CODE_SET = "NBS_QUES_SUBGROUP";

    public QuestionFinder(
        final WaQuestionRepository questionRepository,
        final QuestionMapper questionMapper,
        WaUiMetadataRepository uiMetadataRepository,
        ValueSetReader valueSetReader) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.uiMetadataRepository = uiMetadataRepository;
        this.valueSetReader = valueSetReader;
    }

    public GetQuestionResponse find(Long id) {
        Question question = questionRepository.findById(id)
            .map(questionMapper::toQuestion)
            .orElseThrow(() -> new QuestionNotFoundException(id));
        boolean isInUse = checkQuestionInUse(question.uniqueId());
        return new GetQuestionResponse(question, isInUse);
    }

    public Page<Question> find(Pageable pageable) {
        Page<WaQuestion> page = questionRepository.findAll(pageable);
        List<Question> questions = page.get().map(questionMapper::toQuestion).toList();
        return new PageImpl<>(questions, pageable, page.getTotalElements());
    }

    public Page<Question> find(FindQuestionRequest request, Pageable pageable) {
        Page<WaQuestion> page = questionRepository.findAllByNameOrIdentifierOrQuestionTypeOrSubGroup(
            request.search(),
            tryConvert(request.search()),
            request.questionType(),
            pageable);
        List<Question> questions = page.get().map(questionMapper::toQuestion).toList();
        return new PageImpl<>(questions, pageable, page.getTotalElements());
    }

    public boolean checkQuestionInUse(String identifier) {
        return !uiMetadataRepository.findAllByQuestionIdentifier(identifier).isEmpty();
    }

    Long tryConvert(String search) {
        try {
            return Long.valueOf(search);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public boolean checkUnique(QuestionValidationRequest request) {
        if (request.fieldName().equals(FieldName.UNIQUE_ID.getValue()))
            return questionRepository.findIdByQuestionIdentifier(request.value()).isEmpty();
        if (request.fieldName().equals(FieldName.UNIQUE_NAME.getValue()))
            return questionRepository.findIdByQuestionNm(request.value()).isEmpty();
        if (request.fieldName().equals(FieldName.DATA_MART_COLUMN_NAME.getValue()))
            return questionRepository.findIdByUserDefinedColumnNm(request.value()).isEmpty();
        if (request.fieldName().equals(FieldName.RDB_COLUMN_NAME.getValue())) {
            checkSubGroupValidity(request.value());
            return questionRepository.findIdByRdbColumnNm(request.value()).isEmpty();
        }
        throw new UniqueQuestionException("invalid unique field name");
    }

    private void checkSubGroupValidity(String rdbColumnName) {
        String subGroupCode = rdbColumnName.substring(0, rdbColumnName.indexOf("_"));
        boolean isMatch = valueSetReader.findConceptCodes(SUBGROUP_CODE_SET)
            .stream().map(Concept::localCode).anyMatch(code -> code.equals(subGroupCode));
        if (!isMatch)
            throw new UniqueQuestionException("invalid subgroup Code");
    }
}


