package gov.cdc.nbs.questionbank.kafka.producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import gov.cdc.nbs.questionbank.kafka.config.RequestProperties;
import gov.cdc.nbs.questionbank.kafka.message.RequestStatus;

@ExtendWith(MockitoExtension.class)
class RequestStatusProducerTest {

    @Mock
    private KafkaTemplate<String, RequestStatus> template;

    @Captor
    private ArgumentCaptor<RequestStatus> statusCaptor;

    private RequestStatusProducer statusProducer;

    @BeforeEach
    void setup() {
        statusProducer = new RequestStatusProducer(template, new RequestProperties("request", "status"));
    }

    @Test
    void sendSuccessTest() {

        statusProducer.successful("RequestId", "Message", 123L);

        verify(template, times(1)).send(eq("status"), statusCaptor.capture());

        RequestStatus actual = statusCaptor.getValue();

        assertThat(actual.isSuccessful()).isTrue();
        assertThat(actual.requestId()).isEqualTo("RequestId");
        assertThat(actual.message()).isEqualTo("Message");
        assertThat(actual.id()).isEqualTo(123L);
    }

    @Test
    void sendFailTest() {
        statusProducer.failure("RequestId", "Message");

        verify(template, times(1)).send(eq("status"), statusCaptor.capture());

        RequestStatus actual = statusCaptor.getValue();

        assertThat(actual.isSuccessful()).isFalse();
        assertThat(actual.requestId()).isEqualTo("RequestId");
        assertThat(actual.message()).isEqualTo("Message");
        assertThat(actual.id()).isNull();
    }

}
