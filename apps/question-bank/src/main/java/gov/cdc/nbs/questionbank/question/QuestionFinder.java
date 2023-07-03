package gov.cdc.nbs.questionbank.question;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import gov.cdc.nbs.questionbank.entity.question.WaQuestion;
import gov.cdc.nbs.questionbank.entity.repository.WaUiMetadatumRepository;
import gov.cdc.nbs.questionbank.question.exception.QuestionNotFoundException;
import gov.cdc.nbs.questionbank.question.model.Question;
import gov.cdc.nbs.questionbank.question.repository.WaQuestionRepository;
import gov.cdc.nbs.questionbank.question.request.FindQuestionRequest;
import gov.cdc.nbs.questionbank.question.response.GetQuestionResponse;

@Component
public class QuestionFinder {
    private final WaQuestionRepository questionRepository;
    private final WaUiMetadatumRepository uiMetadataRepository;
    private final QuestionMapper questionMapper;

    public QuestionFinder(
            final WaQuestionRepository questionRepository,
            final QuestionMapper questionMapper,
            WaUiMetadatumRepository uiMetadataRepository) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.uiMetadataRepository = uiMetadataRepository;
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
        Page<WaQuestion> page = questionRepository.findAllByNameOrIdentifier(
                request.search(),
                tryConvert(request.search()),
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

}