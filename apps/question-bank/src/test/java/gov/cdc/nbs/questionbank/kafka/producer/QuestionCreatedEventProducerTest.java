package gov.cdc.nbs.questionbank.kafka.producer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import gov.cdc.nbs.questionbank.entities.AuditInfo;
import gov.cdc.nbs.questionbank.entities.TextQuestionEntity;
import gov.cdc.nbs.questionbank.kafka.config.RequestProperties;
import gov.cdc.nbs.questionbank.kafka.message.question.QuestionCreatedEvent;
import gov.cdc.nbs.questionbank.questionnaire.EntityMapper;

@ExtendWith(MockitoExtension.class)
class QuestionCreatedEventProducerTest {
    @Mock
    private KafkaTemplate<String, QuestionCreatedEvent> template;

    @Spy
    private EntityMapper entityMapper = new EntityMapper();

    @Spy
    private RequestProperties properties = new RequestProperties(null, null, "test-created-topic");

    @InjectMocks
    private QuestionCreatedEventProducer producer;

    @Test
    void should_set_correct_data_for_question() {
        // given an entity has been created
        TextQuestionEntity entity = textQuestion();

        // when i send a created event
        producer.send(entity);

        // then the right data is posted to kafka
        ArgumentCaptor<QuestionCreatedEvent> captor = ArgumentCaptor.forClass(QuestionCreatedEvent.class);
        verify(template).send(eq(properties.questionCreated()), captor.capture());

        QuestionCreatedEvent actual = captor.getValue();
        assertEquals(entity.getId(), actual.element().id());

        assertEquals(entity.getAudit().getAddTime(), actual.createdAt());
        assertEquals(entity.getAudit().getAddUserId().longValue(), actual.createdBy());
    }


    private TextQuestionEntity textQuestion() {
        TextQuestionEntity entity = new TextQuestionEntity(
                "Label",
                "Tooltip",
                13,
                "placeHolder",
                "default value for text");
        entity.setId(UUID.randomUUID());
        entity.setAudit(audit());
        return entity;
    }

    private AuditInfo audit() {
        Instant now = Instant.now();
        AuditInfo audit = new AuditInfo(
                now,
                123L,
                now,
                321L,
                AuditInfo.Status.ACTIVE,
                now);
        return audit;
    }
}
