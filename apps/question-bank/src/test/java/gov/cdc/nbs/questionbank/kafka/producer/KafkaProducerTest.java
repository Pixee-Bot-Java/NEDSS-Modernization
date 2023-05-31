package gov.cdc.nbs.questionbank.kafka.producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;
import gov.cdc.nbs.questionbank.entities.enums.CodeSet;
import gov.cdc.nbs.questionbank.kafka.message.QuestionBankRequest;
import gov.cdc.nbs.questionbank.kafka.message.question.QuestionRequest;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, QuestionBankRequest> kafkaEnvelopeTemplate;

    @InjectMocks
    private KafkaProducer producer;

    public KafkaProducerTest() {
        producer = new KafkaProducer();
    }

    @Test
    void testSendEvent() {
        var message = new QuestionRequest.CreateTextQuestionRequest(
                "requestId",
                13L,
                new QuestionRequest.TextQuestionData(
                        "a label",
                        "a tooltip",
                        11,
                        "a placeholder",
                        "some default text",
                        CodeSet.LOCAL));
        ListenableFuture<SendResult<String, QuestionBankRequest>> future = new SettableListenableFuture<>();
        Mockito.when(kafkaEnvelopeTemplate.send(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(future);

        producer.requestEventEnvelope(message);

        ArgumentCaptor<QuestionRequest.CreateTextQuestionRequest> envelopeEventArgumentCaptor =
                ArgumentCaptor.forClass(QuestionRequest.CreateTextQuestionRequest.class);

        verify(kafkaEnvelopeTemplate).send(nullable(String.class), eq("requestId"),
                envelopeEventArgumentCaptor.capture());

        QuestionRequest.CreateTextQuestionRequest actualRecord = envelopeEventArgumentCaptor.getValue();
        assertThat(actualRecord.requestId()).isEqualTo("requestId");
        assertThat(actualRecord.userId()).isEqualTo(13L);
        assertThat(actualRecord.data().label()).isEqualTo("a label");
        assertThat(actualRecord.data().tooltip()).isEqualTo("a tooltip");
        assertThat(actualRecord.data().maxLength()).isEqualTo(11);
        assertThat(actualRecord.data().placeholder()).isEqualTo("a placeholder");


        verifyNoMoreInteractions(kafkaEnvelopeTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_throw_producer_error() {
        var message = new QuestionRequest.CreateTextQuestionRequest(
                "requestId",
                13L,
                new QuestionRequest.TextQuestionData(
                        "a label",
                        "a tooltip",
                        11,
                        "a placeholder",
                        "default",
                        CodeSet.LOCAL));

        ListenableFutureCallback<SendResult<String, QuestionBankRequest>> callback =
                Mockito.mock(ListenableFutureCallback.class);

        SettableListenableFuture<SendResult<String, QuestionBankRequest>> future = new SettableListenableFuture<>();
        future.setException(new RuntimeException());
        future.addCallback(callback);

        Mockito.when(kafkaEnvelopeTemplate.send(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(future);


        producer.requestEventEnvelope(message);

        verify(callback, times(1)).onFailure(Mockito.any());
        verify(callback, times(0)).onSuccess(Mockito.any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_trigger_success() {
        var message = new QuestionRequest.CreateTextQuestionRequest(
                "requestId",
                13L,
                new QuestionRequest.TextQuestionData(
                        "a label",
                        "a tooltip",
                        11,
                        "a placeholder",
                        "default",
                        CodeSet.LOCAL));

        ListenableFutureCallback<SendResult<String, QuestionBankRequest>> callback =
                Mockito.mock(ListenableFutureCallback.class);

        SettableListenableFuture<SendResult<String, QuestionBankRequest>> future = new SettableListenableFuture<>();
        future.set(null);
        future.addCallback(callback);

        Mockito.when(kafkaEnvelopeTemplate.send(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(future);

        producer.requestEventEnvelope(message);

        verify(callback, times(0)).onFailure(Mockito.any());
        verify(callback, times(1)).onSuccess(Mockito.any());
    }
}
